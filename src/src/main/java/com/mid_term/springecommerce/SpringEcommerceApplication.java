package com.mid_term.springecommerce;

import com.mid_term.springecommerce.Services.ProductService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@SpringBootApplication
public class SpringEcommerceApplication {
    public static void main(String[] args) {

        SpringApplication.run(SpringEcommerceApplication.class, args);
    }
}
