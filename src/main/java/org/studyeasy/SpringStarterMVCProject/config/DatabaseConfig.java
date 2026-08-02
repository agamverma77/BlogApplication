package org.studyeasy.SpringStarterMVCProject.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url:}")
    private String rawDatasourceUrl;

    @Value("${spring.datasource.username:}")
    private String datasourceUsername;

    @Value("${spring.datasource.password:}")
    private String datasourcePassword;

    @Value("${spring.datasource.driver-class-name:}")
    private String driverClassName;

    @Bean
    @Primary
    public DataSource dataSource() {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl == null || dbUrl.trim().isEmpty()) {
            dbUrl = System.getenv("SPRING_DATASOURCE_URL");
        }
        if (dbUrl == null || dbUrl.trim().isEmpty()) {
            dbUrl = rawDatasourceUrl;
        }

        if (dbUrl != null) {
            dbUrl = dbUrl.trim();
        }

        // Case 1: Cloud PostgreSQL URI (postgres:// or postgresql://)
        if (dbUrl != null && (dbUrl.startsWith("postgres://") || dbUrl.startsWith("postgresql://"))) {
            try {
                logger.info("Detected Cloud PostgreSQL URI format in DATABASE_URL, parsing...");
                
                // Clean scheme if needed for standard URI parser
                String uriString = dbUrl;
                if (uriString.startsWith("postgresql://")) {
                    uriString = "postgres://" + uriString.substring("postgresql://".length());
                }
                
                URI uri = new URI(uriString);
                String userInfo = uri.getUserInfo();
                String username = datasourceUsername;
                String password = datasourcePassword;

                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    username = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                    password = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                } else if (userInfo != null) {
                    username = URLDecoder.decode(userInfo, StandardCharsets.UTF_8);
                }

                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath(); // includes leading '/'
                String dbName = (path != null && path.length() > 1) ? path.substring(1) : "blogverse";

                StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://")
                        .append(host)
                        .append(":")
                        .append(port)
                        .append("/")
                        .append(dbName);

                boolean hasQuery = (uri.getQuery() != null && !uri.getQuery().isEmpty());
                if (hasQuery) {
                    jdbcUrl.append("?").append(uri.getQuery());
                }

                // If remote host and no sslmode parameter is present, enforce sslmode=require
                if (host != null && !host.equals("localhost") && !host.equals("127.0.0.1") && !jdbcUrl.toString().contains("sslmode")) {
                    jdbcUrl.append(hasQuery ? "&" : "?").append("sslmode=require");
                }

                logger.info("Configured PostgreSQL JDBC URL for host: {}, db: {}", host, dbName);

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(jdbcUrl.toString());
                if (username != null && !username.isEmpty()) {
                    config.setUsername(username);
                }
                if (password != null && !password.isEmpty()) {
                    config.setPassword(password);
                }
                config.setDriverClassName("org.postgresql.Driver");
                config.setMaximumPoolSize(5);
                config.setMinimumIdle(1);
                config.setIdleTimeout(30000);
                config.setConnectionTimeout(30000);
                config.setMaxLifetime(600000);

                return new HikariDataSource(config);
            } catch (Exception e) {
                logger.error("Error parsing PostgreSQL URI ({}), falling back to direct configuration: {}", dbUrl, e.getMessage());
            }
        }

        // Case 2: Standard JDBC URL (jdbc:postgresql://, jdbc:h2:, etc.) or local fallback
        HikariConfig config = new HikariConfig();
        String finalUrl = (dbUrl != null && !dbUrl.trim().isEmpty()) ? dbUrl : "jdbc:h2:file:./db/blogdb";
        config.setJdbcUrl(finalUrl);

        if (datasourceUsername != null && !datasourceUsername.trim().isEmpty()) {
            config.setUsername(datasourceUsername);
        } else if (finalUrl.startsWith("jdbc:h2:")) {
            config.setUsername("admin");
        }

        if (datasourcePassword != null && !datasourcePassword.trim().isEmpty()) {
            config.setPassword(datasourcePassword);
        } else if (finalUrl.startsWith("jdbc:h2:")) {
            config.setPassword("password");
        }

        if (driverClassName != null && !driverClassName.trim().isEmpty()) {
            config.setDriverClassName(driverClassName);
        }

        logger.info("Initializing DataSource with URL: {}", finalUrl);
        return new HikariDataSource(config);
    }
}
