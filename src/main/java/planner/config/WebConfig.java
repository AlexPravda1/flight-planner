package planner.config;

import static planner.config.template.WebJspConfig.PAGE_SUFFIX;
import static planner.config.template.WebJspConfig.WEB_INF;
import static planner.config.template.WebSecurityConfig.ANT_RESOURCES;
import static planner.config.template.WebSecurityConfig.PATH_RESOURCES;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "planner.controller")
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp(WEB_INF.value(), PAGE_SUFFIX.value());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(ANT_RESOURCES.value())
                .addResourceLocations(PATH_RESOURCES.value())
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic())
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        /* Other possible location examples:
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/statics/", "D:/statics/", "file:/Users/Me/")
        */
    }
}
