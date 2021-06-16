package net.juanlopes.javabahia;

import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class V2_QuickSelectTest {
    @Test
    public void testPartition() {
        for (int k = 0; k < 1000; k++) {
            int size = 10;
            Random r = new Random(k);
            List<Integer> list = IntStream.range(0, size).boxed().collect(Collectors.toList());
            Collections.shuffle(list, r);

            int idx = V2_QuickSelect.partition(list, 0, 10, Comparator.naturalOrder());
            int value = list.get(idx);

            for (int i = 0; i < idx; i++) {
                assertThat(list.get(i)).isLessThan(value);
            }
            assertThat(value).isEqualTo(idx);
            for (int i = idx + 1; i < size; i++) {
                assertThat(list.get(i)).isGreaterThan(value);
            }
        }

    }
}