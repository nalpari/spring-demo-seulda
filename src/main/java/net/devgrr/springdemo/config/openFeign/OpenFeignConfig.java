package net.devgrr.springdemo.config.openFeign;

import feign.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignConfig {

  @Bean
  public Client feignClient() {
    return new Client.Default(null, null);
  }
}
