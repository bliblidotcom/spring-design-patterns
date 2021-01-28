package com.blibli.belajar.design.patterns.observer;

import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.*;
import org.springframework.context.annotation.Bean;

public class ObserverApplication {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Product {

    private String id;
    private String name;

  }

  public static class ProductEvent extends ApplicationEvent {

    public ProductEvent(Product source) {
      super(source);
    }
  }

  public static class ProductRepository implements ApplicationEventPublisherAware {

    @Setter
    private ApplicationEventPublisher applicationEventPublisher;

    public void save(Product product){
      System.out.println("Done save to database");
      applicationEventPublisher.publishEvent(new ProductEvent(product));
    }

  }

  public static class MessageBrokerObserver implements ApplicationListener<ProductEvent>{

    @Override
    public void onApplicationEvent(ProductEvent event) {
      System.out.println("Kirim ke message broker");
    }
  }

  public static class RedisObserver implements ApplicationListener<ApplicationEvent>{

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
      System.out.println("Kirim ke redis server");
    }
  }

  public static class LogObserver implements ApplicationListener<ProductEvent>{

    @Override
    public void onApplicationEvent(ProductEvent event) {
      System.out.println("Kirim ke log server");
    }
  }

  @SpringBootApplication
  public static class Application {

    @Bean
    public ProductRepository productRepository(){
      return new ProductRepository();
    }

    @Bean
    public MessageBrokerObserver messageBrokerObserver(){
      return new MessageBrokerObserver();
    }

    public RedisObserver redisObserver(){
      return new RedisObserver();
    }

    @Bean
    public LogObserver logObserver(){
      return new LogObserver();
    }

  }

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(Application.class);
    ProductRepository productRepository = context.getBean(ProductRepository.class);

    productRepository.save(new Product("1", "ipon"));
  }

}
