package com.uce.notes.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HellowController {

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello");
    }
    @GetMapping("/hi")
    public ResponseEntity<String> sayHi(){
        return ResponseEntity.ok("Hi");
    }
}
