package nbcc.springresto.config;

import jakarta.servlet.ServletContext;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig implements ServletContextInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        servletContext.setSessionTimeout(30);
    }
}
