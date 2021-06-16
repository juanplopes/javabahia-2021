package net.juanlopes.javabahia;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TimePlayground {
    public static void main(String[] args) {
//        test(5000, 10, V1_Naive::new,
//                V2_QuickSelect::new,
//                V3_RTree::new,
//                V4_KDTree::new,
//                V5_CustomKDTree::new);

        test(20000, 10,
                V2_QuickSelect::new);

        test(100000, 10,
                V4_KDTree::new,
                V5_CustomKDTree::new);
    }

    private static void test(int limit, int repeats, Supplier<KNearest<Object>>... suppliers) {
        var random = new Random(0);

        int step = limit / 20;
        for (int points = step; points <= limit; points += step) {
            List<KNearest<Object>> structures = Arrays.stream(suppliers)
                    .map(Supplier::get)
                    .collect(Collectors.toList());

            for (int i = 0; i < points; i++) {
                double x = random.nextDouble();
                double y = random.nextDouble();
                structures.forEach(s -> s.add(x, y, random));
            }

            long seed = random.nextLong();

            System.out.print(points);
            for (KNearest<Object> structure : structures) {
                Random queryRandom = new Random(seed);

                long total = 0;
                for (int repeat = 0; repeat < repeats; repeat++) {
                    long start = System.nanoTime();
                    for (int i = 0; i < points; i++) {
                        structure.query(queryRandom.nextDouble(), queryRandom.nextDouble(), 100);
                    }
                    total += System.nanoTime() - start;
                }
                System.out.print("\t" + total / 1e9 / repeats);
            }
            System.out.println();
        }

    }
}
