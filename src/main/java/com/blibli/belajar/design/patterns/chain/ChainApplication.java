package com.blibli.belajar.design.patterns.chain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChainApplication {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Contoh {
    private String name;
  }

  @SpringBootApplication
  public static class Application {

    @RestController
    public static class HelloController {

      @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
      public Contoh helloWorld() {
        // pasti udah LOGIN
        return Contoh.builder().name("Hello World").build();
      }

    }

    public static class AuthInterceptor implements HandlerInterceptor {

      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String xApi = request.getHeader("X-API");
        if (StringUtils.hasText(xApi)) {
          return true;
        } else {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return false;
        }
      }
    }

    @Component
    public static class WebConfiguration implements WebMvcConfigurer {

      @Override
      public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
      }
    }

  }


  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

}
