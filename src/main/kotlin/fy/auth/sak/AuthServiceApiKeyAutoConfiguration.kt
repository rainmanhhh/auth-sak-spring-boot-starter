package fy.auth.sak

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@ConfigurationProperties("fy.auth")
@Configuration
class AuthServiceApiKeyAutoConfiguration {
  @NestedConfigurationProperty
  var serviceApiKey = ServiceApiKey()

  @Bean
  fun serviceApiKey() = serviceApiKey
}