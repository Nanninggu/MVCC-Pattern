package com.example.MultiVersion.Concurrency.Control_Pattern;

import com.example.MultiVersion.Concurrency.Control_Pattern.service.ConcurrencyService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * 이 코드에서는 10개의 스레드로 구성된 고정 스레드 풀이 있는 ExecutorService를 만듭니다.
 * 그런 다음 이 실행기 서비스에 1000개의 작업을 실행하며, 각 작업은 incrementCounter 메서드를 호출합니다.
 * submit 메소드에 의해 반환된 Future 객체는 나중에 결과를 검색할 수 있도록 목록에 저장됩니다.
 * 모든 작업이 제출된 후 증가된 값의 합계를 계산하여 최종 카운터 값과 함께 출력합니다.
 * 마지막으로 실행자 서비스를 종료합니다.
 * ConcurrencyService의 incrementCounter 메서드는 스레드로부터 안전하지 않기 때문에 이 코드는 경쟁 조건을 발생시킵니다.
 * 이 코드를 실행하면 증가된 값의 합이 예상 값(1000)보다 작고 최종 카운터 값이 작업 수(1000)보다 작은 것을 볼 수 있습니다.
 * 이는 여러 스레드가 동시에 카운터를 증가시켜 일부 증가분이 손실되기 때문입니다.
 *
 */

public class ConcurrencyTest {
    public static void main(String[] args) throws Exception {
        ConcurrencyService concurrencyService = new ConcurrencyService();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // Create a list to hold the Future object associated with Callable
        List<Future<Integer>> futureList = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Future<Integer> future = executorService.submit(concurrencyService::incrementCounter);
            futureList.add(future);
        }

        // Calculate sum of incremented values
        int sum = 0;
        for (Future<Integer> future : futureList) {
            sum += future.get();
        }

        System.out.println("Final counter value: " + concurrencyService.incrementCounter());
        System.out.println("Sum of incremented values: " + sum);

        // Shut down the executor service
        executorService.shutdown();
    }
}
