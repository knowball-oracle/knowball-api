package br.com.fiap.knowball.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
@EnableConfigurationProperties(RsaKeyProperties.class)
public class RsaKeyDevConfig {
}
