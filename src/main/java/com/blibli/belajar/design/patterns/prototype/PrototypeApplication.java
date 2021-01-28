package com.blibli.belajar.design.patterns.prototype;

import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

public class PrototypeApplication {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Customer {

    private String id;
    private String name;
    private String category;
    private Integer discount;

  }

  @SpringBootApplication
  public static class Configuration {

    @Bean("standardCustomer")
    @Scope("prototype")
    public Customer standardCustomer(){
      return Customer.builder()
          .category("STANDARD")
          .discount(10)
          .build();
    }

    @Bean("premiumCustomer")
    @Scope("prototype")
    public Customer premiumCustomer(){
      return Customer.builder()
          .category("PREMIUM")
          .discount(50)
          .build();
    }

  }

  public static void main(String[] args) {

    ApplicationContext context = SpringApplication.run(Configuration.class);
    Customer customerStandard1 = context.getBean("standardCustomer", Customer.class);
    Customer customerStandard2 = context.getBean("standardCustomer", Customer.class);
    Customer customerStandard3 = context.getBean("standardCustomer", Customer.class);

    Customer premiumCustomer1 = context.getBean("premiumCustomer", Customer.class);
    Customer premiumCustomer2 = context.getBean("premiumCustomer", Customer.class);
    Customer premiumCustomer3 = context.getBean("premiumCustomer", Customer.class);

    System.out.println(customerStandard1 == customerStandard2);
    System.out.println(customerStandard2 == customerStandard3);

    System.out.println(customerStandard1);
    System.out.println(customerStandard2);
    System.out.println(customerStandard3);

    System.out.println(premiumCustomer1);
    System.out.println(premiumCustomer2);
    System.out.println(premiumCustomer3);

//    Customer customerStandard1 = Customer.builder().id("1").name("Eko").
//        category("STANDARD").discount(10).build();
//
//    Customer customerStandard2 = new Customer();
//    BeanUtils.copyProperties(customerStandard1, customerStandard2);
//
//    Customer customerStandard3 = Customer.builder().id("1").name("Eko").
//        category(customerStandard1.getCategory()).discount(customerStandard1.getDiscount()).build();
//
//    Customer customerStandard4 = Customer.builder().id("1").name("Eko").
//        category(customerStandard1.getCategory()).discount(customerStandard1.getDiscount()).build();

  }

}
