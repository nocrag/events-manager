package nbcc.springresto.config;

import nbcc.springresto.interceptors.LoginInterceptor;
import nbcc.springresto.interceptors.UserActivityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;
    private final UserActivityInterceptor userActivityInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor, UserActivityInterceptor userActivityInterceptor) {
        this.loginInterceptor = loginInterceptor;
        this.userActivityInterceptor = userActivityInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns(
                "/event/create/**",
                "/event/delete/**",
                "/event/**/edit"
        );

        registry.addInterceptor(loginInterceptor).addPathPatterns(
                "/event/**/seatings/new",
                "/event/**/seatings/edit/**",
                "/event/**/seatings/delete/**"
        );

        registry.addInterceptor(loginInterceptor).addPathPatterns(
                "/layout/create/**",
                "/layout/edit/**",
                "/layout/delete/**",
                "/layouts/archived"
        );

        registry.addInterceptor(loginInterceptor).addPathPatterns(
                "/dining-table/**",
                "/dining-tables/**"
        );

        registry.addInterceptor(loginInterceptor).addPathPatterns(
                "/menu/create/**",
                "/menu/edit/**",
                "/menu/delete/**",
                "/menu/**/items/edit/**",
                "/menu/**/items/delete/**"
        );

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/reservation/**")
                .excludePathPatterns("/reservation/create");

        registry.addInterceptor(userActivityInterceptor);
    }
}