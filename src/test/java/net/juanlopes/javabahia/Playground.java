package net.juanlopes.javabahia;

import com.sun.management.ThreadMXBean;

import java.lang.management.ManagementFactory;
import java.util.Random;

public class Playground {
    public static void main(String[] args) {
        test(new V1_Naive<>());
    }

    private static void test(KNearest<Object> structure) {
        var random = new Random(123456);
        for (int i = 0; i < 5000; i++) {
            structure.add(random.nextDouble(), random.nextDouble(), random);
        }
        for (int i = 0; i < 1000; i++) {
            structure.query(random.nextDouble(), random.nextDouble(), 100);
        }

        System.out.print(structure.getClass().getSimpleName());
        time(() -> {
            for (int i = 0; i < 5000; i++) {
                structure.query(random.nextDouble(), random.nextDouble(), 100);
            }
        });
    }

    private static void time(Runnable runnable) {
        var thread = (ThreadMXBean) ManagementFactory.getThreadMXBean();

        long startTime = System.nanoTime();
        long startAllocations = thread.getThreadAllocatedBytes(Thread.currentThread().getId());
        runnable.run();
        System.out.printf(" time=%s  alloc=%.2fMB%n",
                (System.nanoTime() - startTime) / 1e9,
                (thread.getThreadAllocatedBytes(Thread.currentThread().getId()) - startAllocations) / (double) (1 << 20));
    }

}
