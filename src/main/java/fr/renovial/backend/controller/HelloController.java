package fr.renovial.backend.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/hello")
    public String sayHelloAdmin() {
        return "Hello, Admin!";
    }
}
