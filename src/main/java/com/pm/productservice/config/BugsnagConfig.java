package com.pm.productservice.config;

import com.bugsnag.Bugsnag;
import com.bugsnag.BugsnagSpringConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BugsnagSpringConfiguration.class)
public class BugsnagConfig {

    @Bean
    public Bugsnag bugsnag(
            @Value("${bugsnag.api-key}") String apiKey
    ) {
        Bugsnag bugsnag = new Bugsnag(apiKey);

        bugsnag.setReleaseStage("development");
        bugsnag.setProjectPackages("com.pm.productservice");

        return bugsnag;
    }
}