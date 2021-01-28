package com.blibli.belajar.design.patterns.iterator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Map;

public class IteratorApplication {

  public enum PaymentType {
    CREDIT_CARD, BCA, MANDIRI, BRI
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Payment {
    private PaymentType paymentType;
    private Long amount;
  }

  public static interface PaymentService {

    boolean isSupport(PaymentType paymentType);

    void pay(Payment payment);

  }

  public static class CreditCardPaymentService implements PaymentService {

    @Override
    public boolean isSupport(PaymentType paymentType) {
      return paymentType.equals(PaymentType.CREDIT_CARD);
    }

    @Override
    public void pay(Payment payment) {
      System.out.println("Bayar Credit Card");
    }
  }

  public static class MandiriPaymentService implements PaymentService {

    @Override
    public boolean isSupport(PaymentType paymentType) {
      return paymentType.equals(PaymentType.MANDIRI);
    }

    @Override
    public void pay(Payment payment) {
      System.out.println("Bayar Mandiri");
    }
  }

  public static class BCAPaymentService implements PaymentService {

    @Override
    public boolean isSupport(PaymentType paymentType) {
      return paymentType.equals(PaymentType.BCA);
    }

    @Override
    public void pay(Payment payment) {
      System.out.println("Bayar BCA");
    }
  }

  public static class BRIPaymentService implements PaymentService {

    @Override
    public boolean isSupport(PaymentType paymentType) {
      return paymentType.equals(PaymentType.BRI);
    }

    @Override
    public void pay(Payment payment) {
      System.out.println("Bayar BRI");
    }
  }

  @SpringBootApplication
  public static class Application {

    @Bean
    public CreditCardPaymentService creditCardPaymentService(){
      return new CreditCardPaymentService();
    }

    @Bean
    public MandiriPaymentService mandiriPaymentService(){
      return new MandiriPaymentService();
    }

    @Bean
    public BCAPaymentService bcaPaymentService(){
      return new BCAPaymentService();
    }

    @Bean
    public BRIPaymentService briPaymentService(){
      return new BRIPaymentService();
    }

  }

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(Application.class);

    Payment payment = Payment.builder().paymentType(PaymentType.BRI).amount(1000000L).build();

    context.getBeansOfType(PaymentService.class).values()
        .stream()
        .filter(paymentService -> paymentService.isSupport(payment.getPaymentType()))
        .findFirst()
        .ifPresent(paymentService -> paymentService.pay(payment));

//    if(payment.getPaymentType() == PaymentType.CREDIT_CARD){
//      PaymentService paymentService = context.getBean("creditCardPaymentService", PaymentService.class);
//      paymentService.pay(payment);
//    }else if(payment.getPaymentType() == PaymentType.MANDIRI){
//      PaymentService paymentService = context.getBean("mandiriPaymentService", PaymentService.class);
//      paymentService.pay(payment);
//    }else if(payment.getPaymentType() == PaymentType.BCA){
//      PaymentService paymentService = context.getBean("bcaPaymentService", PaymentService.class);
//      paymentService.pay(payment);
//    }

  }

}
