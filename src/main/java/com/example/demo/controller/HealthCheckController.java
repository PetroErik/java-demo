package com.example.demo.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HealthCheckController {
  @GetMapping(path="/healthcheck")
  public ResponseEntity<Map<String, Boolean>> healthCheck() {
    return ResponseEntity.ok(Map.of("success", true));
  }
}
