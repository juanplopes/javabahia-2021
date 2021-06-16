package net.juanlopes.javabahia;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class V1_Naive<T> implements KNearest<T> {
    private final List<Point<T>> list = new ArrayList<>();

    @Override
    public void add(double x, double y, T data) {
        list.add(new Point<>(x, y, data));
    }

    @Override
    public List<T> query(double x, double y, int limit) {
        return list.stream()
                .sorted(Comparator.comparing(p ->
                        Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2)))
                .limit(limit)
                .map(p -> p.data)
                .collect(Collectors.toList());
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
