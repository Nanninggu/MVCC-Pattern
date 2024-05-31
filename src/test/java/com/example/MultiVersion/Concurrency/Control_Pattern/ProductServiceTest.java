package com.example.MultiVersion.Concurrency.Control_Pattern;

import com.example.MultiVersion.Concurrency.Control_Pattern.dto.ProductEntity;
import com.example.MultiVersion.Concurrency.Control_Pattern.mapper.ProductMapper;
import com.example.MultiVersion.Concurrency.Control_Pattern.service.ProductService;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * ProductServiceTest 클래스를 실행할 때 동시성 문제가 발생하는 이유는 updateProduct 메소드가 스레드에 안전하지 않기 때문입니다.
 * 이 클래스에서는 두 개의 스레드가 동시에 같은 제품을 업데이트하려고 시도합니다.
 * 각 스레드는 updateProduct 메소드를 호출하여 제품을 업데이트하려고 시도합니다.
 * 그러나 updateProduct 메소드는 동기화되지 않았으므로 여러 스레드가 동시에 이 메소드를 실행할 수 있습니다.
 * 이로 인해 레이스 컨디션(race condition)이 발생할 수 있습니다.
 * (레이스 컨디션은 두 개 이상의 스레드가 데이터를 동시에 변경하려고 할 때 발생하는 문제입니다.)
 * 이 경우, 두 스레드가 동시에 같은 제품을 업데이트하려고 하면, 한 스레드가 제품을 업데이트하는 동안 다른 스레드도 동일한 제품을 업데이트하려고 시도할 수 있습니다.
 * 이로 인해 예상치 못한 결과가 발생할 수 있습니다.
 * 이 문제를 해결하려면 updateProduct 메소드를 동기화하거나 다른 동시성 제어 메커니즘을 사용해야 합니다.
 * 이렇게 하면 한 번에 하나의 스레드만 제품을 업데이트할 수 있으므로 레이스 컨디션을 방지할 수 있습니다.
 */

public class ProductServiceTest {
    public static void main(String[] args) throws Exception {
        ProductService productService = new ProductService(new ProductMapper() {
            @Override
            public ProductEntity selectById(int id) {
                // Create and return a new ProductEntity instance instead of null
                ProductEntity product = new ProductEntity();
                product.setId(id);
                product.setName("Existing product name");
                product.setPrice(5000);
                return product;
            }

            @Override
            public int updateIfVersionMatches(ProductEntity product) {
                return 0;
            }
        });
        int productId = 1; // Assuming a product with id 1 exists
        String newName = "TEST1";
        int newPrice = 10000;

        Thread thread1 = new Thread(() -> {
            try {
                productService.updateProduct(productId, newName, newPrice);
            } catch (OptimisticLockingFailureException e) {
                System.out.println("Thread 1: " + e.getMessage());
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                productService.updateProduct(productId, newName, newPrice);
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
