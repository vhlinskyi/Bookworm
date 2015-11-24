package com.maxclay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.maxclay.config.BookPicturesUploadProperties;

@SpringBootApplication
@EnableConfigurationProperties({BookPicturesUploadProperties.class})
public class BookwormApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(BookwormApplication.class, args);
    }
}
