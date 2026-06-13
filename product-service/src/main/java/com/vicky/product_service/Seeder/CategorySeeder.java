package com.vicky.product_service.Seeder;

import com.vicky.product_service.Entity.CategoryEntity;
import com.vicky.product_service.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public static final String ELECTRONICS = "Electronics";
    public static final String CLOTHING = "Clothing";
    public static final String BOOKS = "Books";

    @Override
    public void run(String... args) {
        seedIfAbsent(ELECTRONICS);
        seedIfAbsent(CLOTHING);
        seedIfAbsent(BOOKS);
    }

    private void seedIfAbsent(String name) {
        if (categoryRepository.existsByName(name)) return;

        CategoryEntity c = new CategoryEntity();
        c.setName(name);
        c.setActive(true);
        c.setCreatedAt(LocalDateTime.now());
        categoryRepository.save(c);
        System.out.println("Category created: " + name);
    }
}