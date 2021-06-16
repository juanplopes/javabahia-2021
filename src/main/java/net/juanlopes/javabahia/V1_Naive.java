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
                .sorted(Comparator.comparingDouble(p ->
                        Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2)))
                .limit(limit)
                .map(Point::getData)
                .collect(Collectors.toList());
    }

}
