package net.juanlopes.javabahia;

import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class EquivalenceTest {
    @Test
    public void testRandom() {
        test(new V1_Naive<>(),
                new V2_QuickSelect<>(),
                new V3_RTree<>(),
                new V4_KDTree<>(),
                new V5_CustomKDTree<>());
    }

    private void test(KNearest<Integer>... ts) {
        for (int seed = 0; seed < 10; seed++) {
            Random random = new Random(seed);
            for (int i = 0; i < 10000; i++) {
                double x = random.nextDouble();
                double y = random.nextDouble();

                for (KNearest<Integer> tree : ts) {
                    tree.add(x, y, i);
                }
            }

            List<Integer> list = ts[0].query(0, 0, 100);

            for (int i = 1; i < ts.length; i++) {
                assertThat(ts[i].query(0, 0, 100)).hasSameElementsAs(list);
            }
        }

    }

}
