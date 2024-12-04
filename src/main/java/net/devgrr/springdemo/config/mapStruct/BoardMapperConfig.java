package net.devgrr.springdemo.config.mapStruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoardMapperConfig {
  @Bean
  public BoardMapper boardMapper() {
    return new BoardMapperImpl();
  }
}
