package com.customer.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public controller used for integration test
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Operation(summary = "Generates hello world for integration testing")
    @GetMapping("/hello")
    public String hello() {
        return "Hoi world!";
    }

    @Operation(summary = "Used for integration testing of the authenticated scenario")
    @GetMapping("/private")
    public String getPrivate() {
        return "User is valid.";
    }
}
