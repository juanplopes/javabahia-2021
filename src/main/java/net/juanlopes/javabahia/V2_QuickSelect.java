package net.juanlopes.javabahia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class V2_QuickSelect<T> implements KNearest<T> {
    private final List<Point<T>> list = new ArrayList<>();

    @Override
    public void add(double x, double y, T data) {
        list.add(new Point<>(x, y, data));
    }

    @Override
    public List<T> query(double x, double y, int limit) {
        int index = quickSelect(list, limit, Comparator.comparingDouble(p ->
                Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2)));

        return list.subList(0, index)
                .stream()
                .map(p -> p.data)
                .collect(Collectors.toList());
    }


    public static <T> int quickSelect(List<T> list, int index, Comparator<T> comparator) {
        int begin = 0;
        int end = list.size();
        while (end - begin > 1) {
            int pivot = partition(list, begin, end, comparator);
            if (index <= pivot) {
                end = pivot;
            } else {
                begin = pivot + 1;
            }
        }
        return Math.min(index, end);
    }

    public static <T> int partition(List<T> list, int begin, int end, Comparator<T> comparator) {
        Collections.swap(list, (begin + end) / 2, end - 1);
        T pivotValue = list.get(end - 1);

        int i = begin;
        for (int j = begin; j < end; j++) {
            if (comparator.compare(list.get(j), pivotValue) < 0)
                Collections.swap(list, i++, j);
        }
        Collections.swap(list, i, end - 1);
        return i;
    }

    private static class Point<T> {
        private final double x;
        private final double y;
        private final T data;

        public Point(double x, double y, T data) {
            this.x = x;
            this.y = y;
            this.data = data;
        }
    }
}
