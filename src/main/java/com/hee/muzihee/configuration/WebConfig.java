package com.hee.muzihee.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Value("${image.add-resource-locations}") // yml 파일에 설정해둔 경로를 읽어옴
    private String ADD_RESOURCE_LOCATION;

    @Value("${image.add-resource-handler}") // yml 파일에 설정해둔 경로를 읽어옴
    private String ADD_RESOURCE_HANDLER;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(ADD_RESOURCE_HANDLER)
                .addResourceLocations("file://" +ADD_RESOURCE_LOCATION);


    }

}
