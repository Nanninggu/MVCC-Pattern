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

    /**
     * 업데이트 row 0이면 OptimisticLockingFailureException 던진다.
     * 업데이트 row 1이면 업데이트 수행한다.
     * <p>
     * synchronized 키워드는 한 번에 하나의 스레드만 메소드를 실행할 수 있도록 한다.
     * 그러나 이 방법은 성능에 부정적인 영향을 미칠 수 있기때문에 생각 후 적용 해야한다.
     * (ex: public synchronized void updateProduct(int id, String newName, int newPrice))
     *
     * @param id
     * @param newName
     * @param newPrice
     */
    @Transactional
    public void updateProduct(int id, String newName, int newPrice) {
        ProductEntity product = productMapper.selectById(id);
        product.setName(newName);
        product.setPrice(newPrice);
        System.out.println("Product version: " + product.getVersion());
        System.out.println("Product name: " + product.getName());
        System.out.println("Product price: " + product.getPrice());
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
