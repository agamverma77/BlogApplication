package org.studyeasy.SpringStarterMVCProject.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.studyeasy.SpringStarterMVCProject.models.Account;
import org.studyeasy.SpringStarterMVCProject.models.Authority;
import org.studyeasy.SpringStarterMVCProject.models.Post;
import org.studyeasy.SpringStarterMVCProject.services.AccountService;
import org.studyeasy.SpringStarterMVCProject.services.AuthorityService;
import org.studyeasy.SpringStarterMVCProject.services.PostService;
import org.studyeasy.SpringStarterMVCProject.util.constants.Privilages;
import org.studyeasy.SpringStarterMVCProject.util.constants.Roles;

@Component
public class SeedData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SeedData.class);

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) {
        try {
            logger.info("Initializing SeedData...");

            // 1. Seed Authorities
            for (Privilages auth : Privilages.values()) {
                try {
                    Optional<Authority> existingAuth = authorityService.findbyId(auth.getAuthorityId());
                    if (existingAuth.isEmpty()) {
                        Authority authority = new Authority();
                        authority.setId(auth.getAuthorityId());
                        authority.setName(auth.getAuthorityString());
                        authorityService.save(authority);
                    }
                } catch (Exception e) {
                    logger.warn("Could not save authority {}: {}", auth.getAuthorityString(), e.getMessage());
                }
            }

            // 2. Seed Accounts (Check before saving to prevent duplicate key constraint violations)
            Account account01 = accountService.findOneByEmail("user@user.com").orElse(null);
            if (account01 == null) {
                account01 = new Account();
                account01.setEmail("user@user.com");
                account01.setPassword("pass987");
                account01.setFirstname("user");
                account01.setLastname("lastname");
                account01.setRole(Roles.USER.getRole());
                account01.setAge(25);
                account01.setDate_of_birth(LocalDate.parse("1990-01-01"));
                account01.setGender("Male");
                account01 = accountService.save(account01);
                logger.info("Created seed account: user@user.com");
            }

            Account account02 = accountService.findOneByEmail("admin@admin.com").orElse(null);
            if (account02 == null) {
                account02 = new Account();
                account02.setEmail("admin@admin.com");
                account02.setPassword("pass987");
                account02.setFirstname("admin");
                account02.setLastname("lastname");
                account02.setRole(Roles.ADMIN.getRole());
                account02.setAge(30);
                account02.setDate_of_birth(LocalDate.parse("1985-01-01"));
                account02.setGender("Male");
                account02 = accountService.save(account02);
                logger.info("Created seed account: admin@admin.com");
            }

            Account account03 = accountService.findOneByEmail("editor@editor.com").orElse(null);
            if (account03 == null) {
                account03 = new Account();
                account03.setEmail("editor@editor.com");
                account03.setPassword("pass987");
                account03.setFirstname("editor");
                account03.setLastname("lastname");
                account03.setRole(Roles.EDITOR.getRole());
                account03.setAge(25);
                account03.setDate_of_birth(LocalDate.parse("1985-01-01"));
                account03.setGender("Female");
                account03 = accountService.save(account03);
                logger.info("Created seed account: editor@editor.com");
            }

            Account account04 = accountService.findOneByEmail("super_editor@editor.com").orElse(null);
            if (account04 == null) {
                account04 = new Account();
                account04.setEmail("super_editor@editor.com");
                account04.setPassword("pass987");
                account04.setFirstname("super_editor");
                account04.setLastname("lastname");
                account04.setRole(Roles.EDITOR.getRole());
                account04.setAge(35);
                account04.setDate_of_birth(LocalDate.parse("1980-01-01"));
                account04.setGender("Male");

                Set<Authority> authorities = new HashSet<>();
                authorityService.findbyId(Privilages.RESET_ANY_USER_PASSWORD.getAuthorityId()).ifPresent(authorities::add);
                authorityService.findbyId(Privilages.ACCESS_ADMIN_PANEL.getAuthorityId()).ifPresent(authorities::add);
                account04.setAuthorities(authorities);

                account04 = accountService.save(account04);
                logger.info("Created seed account: super_editor@editor.com");
            }

            // 3. Seed Posts if none exist
            List<Post> posts = postService.getAll();
            if (posts.isEmpty()) {
                logger.info("No posts found in database. Adding seed posts...");
                
                Post post01 = new Post();
                post01.setTitle("About Spring Boot");
                post01.setBody("Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can 'just run'. It takes an opinionated view of the Spring platform and third-party libraries so you can get started with minimum fuss.\n\nKey features include embedded Tomcat/Jetty server support, automatic starter dependencies, opinionated auto-configuration, and production-ready metrics and health checks.");
                post01.setAccount(account01);
                postService.save(post01);

                Post post02 = new Post();
                post02.setTitle("Data Structures and Algorithms");
                post02.setBody("In computer science, a data structure is a data organization, management, and storage format that enables efficient access and modification. Common data structures include arrays, linked lists, stacks, queues, hash tables, trees, and graphs.\n\nChoosing the right data structure is fundamental to developing performant, scalable software applications.");
                post02.setAccount(account02);
                postService.save(post02);
                
                logger.info("Seed posts added successfully.");
            }

            logger.info("SeedData initialization completed successfully.");
        } catch (Exception e) {
            logger.error("Error during SeedData execution: {}", e.getMessage(), e);
        }
    }
}
