package com.blibli.belajar.design.patterns.resolver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class ResolverApplication {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Member {
    private String id;
    private String name;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MandatoryParameter {
    private String requestId;
    private String userId;
    private String serviceId;
    private Long timestamp;
  }

  @SpringBootApplication
  public static class Application {

    @Component
    public static class WebConfiguration implements WebMvcConfigurer {

      @Override
      public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new MandatoryParameterArgumentResolver());
      }
    }

    public static class MandatoryParameterArgumentResolver implements HandlerMethodArgumentResolver {

      @Override
      public boolean supportsParameter(MethodParameter parameter) {
        return MandatoryParameter.class.equals(parameter.getParameterType());
      }

      @Override
      public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        System.out.println("Manggil Mandatory Parameter Resolver");
        return MandatoryParameter.builder()
            .userId(webRequest.getHeader("X-User-Id"))
            .serviceId(webRequest.getHeader("X-Service-Id"))
            .requestId(webRequest.getHeader("X-Request-Id"))
            .timestamp(Long.valueOf(webRequest.getHeader("X-Timestamp")))
            .build();
      }
    }

    @RestController
    public static class SampleController {

      @GetMapping(value = "/api/members/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
      public Member getMember(@PathVariable("id") String memberId,
                              MandatoryParameter mandatoryParameter){

        System.out.println(mandatoryParameter);

        return Member.builder().id(memberId).name("Eko").build();
      }

      @GetMapping(value = "/api/members2/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
      public Member getMember2(@PathVariable("id") String memberId){

        return Member.builder().id(memberId).name("Eko").build();
      }

    }

  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }

}
