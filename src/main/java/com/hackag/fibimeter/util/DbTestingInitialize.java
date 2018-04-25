package com.hackag.fibimeter.util;

import com.hackag.fibimeter.FibimeterApplication;
import com.hackag.fibimeter.mail.MailHelper;
import org.springframework.boot.SpringApplication;

import java.io.IOException;

public class DbTestingInitialize {

    public static void main(String[] args) throws IOException {
        SpringApplication springApplication = new SpringApplication(FibimeterApplication.class);
        springApplication.setAdditionalProfiles("create-db");
        springApplication.run();

        MailHelper.EMAILS_DISABLED = true;
        StaticContextAccessor.getBean(JsonToDatabaseParser.class).parse(null);
        StaticContextAccessor.getBean(RandomFeedbackPopulator.class).populate(1L);
        System.out.println("DB for testing successfully initialized!");
        MailHelper.EMAILS_DISABLED = false;
    }
}
