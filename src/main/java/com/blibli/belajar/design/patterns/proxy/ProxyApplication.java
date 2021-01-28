package com.blibli.belajar.design.patterns.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class ProxyApplication {

  public static interface CustomerService {

    void save(String id, String name);

    void update(String id, String name);

  }

  public static class CustomerServiceImpl implements CustomerService {

    @Override
    public void save(String id, String name) {
      System.out.println("Save ke database");
    }

    @Override
    public void update(String id, String name) {
      System.out.println("Update ke database");
    }
  }

  public static class LogInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      try {
        System.out.println("Manggil method " + invocation.getMethod());
        return invocation.proceed();
      } finally {
        System.out.println("Selesai method " + invocation.getMethod());
      }
    }
  }

  public static class ValidationInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

      Object[] arguments = invocation.getArguments();
      for (Object argument : arguments) {
        if(argument instanceof String){
          String value = (String) argument;
          if(value.equals("")){
            throw new RuntimeException("argument tidak boleh string kosong");
          }
        }
      }

      return invocation.proceed();
    }
  }

  @SpringBootApplication
  public static class Application {

    @Bean
    public ProxyFactoryBean customerService() {
      //return new CustomerServiceImpl();
      ProxyFactoryBean factoryBean = new ProxyFactoryBean();
      factoryBean.setInterfaces(CustomerService.class);
      factoryBean.setTarget(new CustomerServiceImpl());
      factoryBean.setInterceptorNames("logInterceptor", "validationInterceptor");
      return factoryBean;
    }

    @Bean("logInterceptor")
    public LogInterceptor logInterceptor() {
      return new LogInterceptor();
    }

    @Bean("validationInterceptor")
    public ValidationInterceptor validationInterceptor(){
      return new ValidationInterceptor();
    }

  }

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(Application.class);
    CustomerService customerService = context.getBean(CustomerService.class);
    customerService.save("1", "Eko");
    customerService.update("1", "Eko");
    customerService.save("", "");

  }

}
