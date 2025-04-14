package uk.ac.bradford.projecttwo.webinterface.security;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to customize the embedded Tomcat server settings.
 * Increases the maximum size for POST requests to allow larger file uploads.
 */
@Configuration
public class TomcatConfig {

    /**
     * Customizes the embedded Tomcat container to increase the maximum POST size.
     *
     * @return A WebServerFactoryCustomizer that modifies the max post size and save post size.
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            // Set max POST size to 50 MB (default is usually 2 MB)
            connector.setMaxPostSize(52428800); // 50MB
            // Optionally increase the maxSavePostSize as well for large uploads
            connector.setMaxSavePostSize(52428800); // 50MB
        });
    }
}
