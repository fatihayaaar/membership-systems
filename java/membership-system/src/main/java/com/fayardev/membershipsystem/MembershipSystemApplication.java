package com.fayardev.membershipsystem;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MembershipSystemApplication extends SpringBootServletInitializer {

    private static final SessionFactory SESSION_FACTORY = new Configuration()
            .configure("hibernate.cfg.xml")
            .buildSessionFactory();
    private static final Class<MembershipSystemApplication> applicationClass = MembershipSystemApplication.class;
    private static Session session = SESSION_FACTORY.openSession();

    public static void main(String[] args) {
        SpringApplication.run(MembershipSystemApplication.class, args);

        getSession();
        closeSession();
    }

    public static Session getSession() {
        if (!session.isOpen()) {
            session = SESSION_FACTORY.openSession();
        }
        return session;
    }

    public static void closeSession() {
        try {
            getSession().close();
        } catch (Exception ignored) {
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
