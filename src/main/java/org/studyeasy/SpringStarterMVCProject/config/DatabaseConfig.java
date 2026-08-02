package org.studyeasy.SpringStarterMVCProject.config;

import java.net.URI;
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

        // If DATABASE_URL is provided in standard cloud URI format (postgres:// or postgresql:// without jdbc:)
        if (dbUrl != null && (dbUrl.startsWith("postgres://") || dbUrl.startsWith("postgresql://"))) {
            try {
                logger.info("Detected Cloud PostgreSQL URI format in DATABASE_URL, converting for JDBC...");
                URI uri = new URI(dbUrl);
                String userInfo = uri.getUserInfo();
                String username = datasourceUsername;
                String password = datasourcePassword;

                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    username = parts[0];
                    password = parts[1];
                }

                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath(); // includes leading '/'
                String dbName = (path != null && path.length() > 1) ? path.substring(1) : "blogverse";

                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
                if (uri.getQuery() != null && !uri.getQuery().isEmpty()) {
                    jdbcUrl += "?" + uri.getQuery();
                }

                logger.info("Configured JDBC URL: jdbc:postgresql://{}:{}/{}", host, port, dbName);

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(jdbcUrl);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("org.postgresql.Driver");
                config.setMaximumPoolSize(5);
                config.setMinimumIdle(1);
                config.setIdleTimeout(30000);
                config.setConnectionTimeout(30000);
                config.setMaxLifetime(600000);

                return new HikariDataSource(config);
            } catch (Exception e) {
                logger.error("Error parsing DATABASE_URL URI: {}", e.getMessage(), e);
            }
        }

        // Fallback to standard datasource configuration (H2 or standard JDBC url)
        HikariConfig config = new HikariConfig();
        String finalUrl = (dbUrl != null && !dbUrl.trim().isEmpty()) ? dbUrl : "jdbc:h2:file:./db/blogdb";
        config.setJdbcUrl(finalUrl);
        config.setUsername((datasourceUsername != null && !datasourceUsername.trim().isEmpty()) ? datasourceUsername : "admin");
        config.setPassword((datasourcePassword != null && !datasourcePassword.trim().isEmpty()) ? datasourcePassword : "password");
        if (driverClassName != null && !driverClassName.trim().isEmpty()) {
            config.setDriverClassName(driverClassName);
        }

        return new HikariDataSource(config);
    }
}
