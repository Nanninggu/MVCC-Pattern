package com.example.MultiVersion.Concurrency.Control_Pattern.service;

import com.example.MultiVersion.Concurrency.Control_Pattern.dto.ProductEntity;
import com.example.MultiVersion.Concurrency.Control_Pattern.mapper.ProductMapper;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ProductEntity getProduct(int id) {
        return productMapper.selectById(id);
    }

    @Transactional
    public void updateProduct(int id, String newName, int newPrice) {
        ProductEntity product = productMapper.selectById(id);
        product.setName(newName);
        product.setPrice(newPrice);
        int updatedRows = productMapper.updateIfVersionMatches(product);
        if (updatedRows == 0) {
            throw new OptimisticLockingFailureException("Version conflict");
        }
    }

    public void generateConflict(int productId, String newName, int newPrice) {
        Thread thread1 = new Thread(() -> {
            try {
                updateProduct(productId, newName, newPrice);
            } catch (OptimisticLockingFailureException e) {
                System.out.println("Thread 1: " + e.getMessage());
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                updateProduct(productId, newName, newPrice);
            } catch (OptimisticLockingFailureException e) {
                System.out.println("Thread 2: " + e.getMessage());
            }
        });

        thread1.start();
        thread2.start();

        // Wait for threads to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
