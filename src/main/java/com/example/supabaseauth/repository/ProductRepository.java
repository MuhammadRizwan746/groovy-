package com.example.supabaseauth.repository;



import com.example.supabaseauth.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}