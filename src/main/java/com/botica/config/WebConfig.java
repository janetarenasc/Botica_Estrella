package com.botica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ActivityInterceptor activityInterceptor;

    public WebConfig(ActivityInterceptor activityInterceptor) {
        this.activityInterceptor = activityInterceptor;
    }

    @Override
    public void addInterceptors(
            InterceptorRegistry registry) {

        registry.addInterceptor(activityInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/botica-uploads/");
    }

}