package com.king.graduation.backend_system.Config;

import com.king.graduation.backend_system.Utils.VideoUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author king
 * @date 2021/1/2 - 21:04
 */
@Configuration
public class MyWebAppConfig implements WebMvcConfigurer {
    public static final String AVATAR_PATH = VideoUtil.Base_Path+"\\images\\avatar\\";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/video/**").
                addResourceLocations("file:" + VideoUtil.VIDEO_BASE_PATH);

        registry.addResourceHandler("/avatar/**").
                addResourceLocations("file:" + AVATAR_PATH);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowCredentials(true)
                .maxAge(3600);
    }


}
