package com.king.bishe.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author king
 * @date 2020/11/12 15:43
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("POST", "GET", "PUT", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600)
                .allowedHeaders(
                        "Access-Control-Allow-Headers",
                        "access-control-allow-origin, authority, content-type, version-info, X-Requested-With"

                );
    }

}
