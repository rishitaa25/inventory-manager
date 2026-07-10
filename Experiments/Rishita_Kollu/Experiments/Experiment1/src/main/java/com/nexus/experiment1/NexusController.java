package com.nexus.experiment1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NexusController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Nexus: Simple Web Scraper App";
    }

    @GetMapping("/idea")
    public String idea() {
        return "Nexus helps users search multiple websites and extract targeted information efficiently!!!";
    }

    @GetMapping("/hello/{name}")
    public String greet(@PathVariable String name) {
        return "Hello " + name + ", welcome to Nexus!";
    }
}
