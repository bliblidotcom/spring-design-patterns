package com.blibli.belajar.design.patterns.singleton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class SingletonApplication {

  @SpringBootApplication
  public static class Configuration {

    @Bean
    public Contoh contoh(){
      return new Contoh();
    }

  }

  public static class Contoh {
  }

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(Configuration.class);

    Contoh contoh1 = context.getBean(Contoh.class);
    Contoh contoh2 = context.getBean(Contoh.class);

    System.out.println(contoh1 == contoh2); // true
  }

}
