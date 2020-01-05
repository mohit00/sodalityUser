package com.radius.sodalityUser;
 import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableConfigurationProperties()

public class SodalityUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(SodalityUserApplication.class, args);
	}
@SuppressWarnings("deprecation")
@Bean
WebMvcConfigurer webMvcConfigurer() {
	return new WebMvcConfigurerAdapter() {

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			// TODO Auto-generated method stub
			registry.addResourceHandler("/images/**")
	        .addResourceLocations("classpath:/images/");

		}
		
	};
}
}
