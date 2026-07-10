package org.example.inventory_manager_beta1.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * The web socket configuration class for setting up web socket notification
 * used throughout the application
 */
@Configuration
public class WebSocketConfiguration {
    /**
     * INIT: The constructor for initializing the ServerEndpointExporter used
     * for web socket based notifications
     * @return
     *  The ServerEndpointExporter object
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {return new ServerEndpointExporter();}}
