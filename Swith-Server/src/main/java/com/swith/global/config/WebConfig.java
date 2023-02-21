package com.swith.global.config;

import com.swith.global.resolver.authinfo.UserInfoArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public UserInfoArgumentResolver userInfoArgumentResolver() {
        return new UserInfoArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userInfoArgumentResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}

