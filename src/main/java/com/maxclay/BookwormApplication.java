package com.maxclay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.maxclay.config.BookPicturesUploadProperties;
import com.maxclay.config.UserPicturesUploadProperties;

@SpringBootApplication
@EnableConfigurationProperties({BookPicturesUploadProperties.class, UserPicturesUploadProperties.class})
public class BookwormApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(BookwormApplication.class, args);
    }
}
