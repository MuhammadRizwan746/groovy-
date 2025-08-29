package com.example.supabaseauth.controller;



import com.example.supabaseauth.model.Product;
import com.example.supabaseauth.service.ProductService;
import com.example.supabaseauth.service.SupabaseAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final SupabaseAuthService authService;

    public ProductController(ProductService productService, SupabaseAuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> all(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if (auth == null || !auth.startsWith("Bearer ") || !authService.validateToken(auth)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> byId(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,
                                        @PathVariable Long id) {
        if (auth == null || !auth.startsWith("Bearer ") || !authService.validateToken(auth)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,
                                          @RequestBody Product p) {
        if (auth == null || !auth.startsWith("Bearer ") || !authService.validateToken(auth)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(productService.create(p));
    }
}