package com.interiordesign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Main Spring Boot Application for Interior Design API
 * AI-powered room layout planner with rule-based furniture placement
 */
@SpringBootApplication
public class InteriorDesignApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(InteriorDesignApplication.class);
    
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(InteriorDesignApplication.class, args);
        
        // Log application startup information
        logApplicationStartup(context.getEnvironment());
    }
    
    /**
     * Log application startup information with URLs
     */
    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        String serverPort = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "/");
        String hostAddress = "localhost";
        
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.warn("Unable to determine host address");
        }
        
        logger.info("\n----------------------------------------------------------\n" +
                "Application '{}' is running!\n" +
                "Access URLs:\n" +
                "  Local:      {}://localhost:{}{}\n" +
                "  External:   {}://{}:{}{}\n" +
                "  H2 Console: {}://localhost:{}/h2-console\n" +
                "API Endpoints:\n" +
                "  Health:     {}://localhost:{}/api/health\n" +
                "  Furniture:  {}://localhost:{}/api/furniture\n" +
                "  Layout:     {}://localhost:{}/api/layout (POST)\n" +
                "Profile(s):   {}\n" +
                "----------------------------------------------------------",
                env.getProperty("spring.application.name", "Interior Design API"),
                protocol, serverPort, contextPath,
                protocol, hostAddress, serverPort, contextPath,
                protocol, serverPort,
                protocol, serverPort,
                protocol, serverPort,
                protocol, serverPort,
                env.getActiveProfiles().length > 0 ? 
                    String.join(", ", env.getActiveProfiles()) : "default"
        );
        
        // Check API key configuration
        String apiKey = env.getProperty("openrouter.api.key");
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here")) {
            logger.warn("\n" +
                    "╔════════════════════════════════════════════════════════════╗\n" +
                    "║  WARNING: OpenRouter API Key Not Configured!              ║\n" +
                    "║                                                            ║\n" +
                    "║  Set OPENROUTER_API_KEY environment variable to use AI.   ║\n" +
                    "║  Example: export OPENROUTER_API_KEY=your_key_here         ║\n" +
                    "╚════════════════════════════════════════════════════════════╝"
            );
        } else {
            logger.info("✓ OpenRouter API Key configured");
        }
    }
}
