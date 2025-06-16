package nbcc.springresto.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentConfig implements EmailSenderConfig {

    private final Environment environment;

    public EnvironmentConfig(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String getDefaultFrom() {
        return environment.getProperty("Default.From.Email");
    }
}
