package com.vicky.product_service.Seeder;

import com.vicky.product_service.Entity.CategoryEntity;
import com.vicky.product_service.Entity.ProductEntity;
import com.vicky.product_service.Repository.CategoryRepository;
import com.vicky.product_service.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@DependsOn("categorySeeder")
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private static final int RESTOCK_QUANTITY = 10;

    @Override
    public void run(String... args) {
        CategoryEntity electronics = categoryRepository.findByName(CategorySeeder.ELECTRONICS);
        CategoryEntity clothing = categoryRepository.findByName(CategorySeeder.CLOTHING);
        CategoryEntity books = categoryRepository.findByName(CategorySeeder.BOOKS);

        List<Object[]> catalog = List.of(
                new Object[]{"iPhone 15", "Apple smartphone", "ELEC-001", 999.99, electronics},
                new Object[]{"Samsung TV 55", "4K Smart TV", "ELEC-002", 799.99, electronics},
                new Object[]{"Nike T-Shirt", "Cotton crew neck", "CLTH-001", 29.99, clothing},
                new Object[]{"Levi's Jeans", "Slim fit denim", "CLTH-002", 59.99, clothing},
                new Object[]{"Clean Code", "By Robert C. Martin", "BOOK-001", 39.99, books}
        );

        for (Object[] row : catalog) {
            String sku = (String) row[2];

            if (!productRepository.existsBySku(sku)) {
                ProductEntity p = new ProductEntity();
                p.setName((String) row[0]);
                p.setDescription((String) row[1]);
                p.setSku(sku);
                p.setPrice((Double) row[3]);
                p.setQuantity(RESTOCK_QUANTITY);
                p.setActive(true);
                p.setCreatedAt(LocalDateTime.now());
                p.setUpdatedAt(LocalDateTime.now());
                p.setCategoryEntity((CategoryEntity) row[4]);
                productRepository.save(p);
                System.out.println("Product created: " + p.getName());
            } else {
                ProductEntity product = productRepository.findBySku(sku)
                        .orElseThrow();
                if (product.getCategoryEntity() == null) {
                    product.setCategoryEntity((CategoryEntity) row[4]);
                }
                product.setQuantity(RESTOCK_QUANTITY);
                productRepository.save(product);
            }
        }
    }
}