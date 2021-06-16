package net.juanlopes.javabahia;

import com.harium.storage.kdtree.KDTree;

import java.util.List;

public class V4_KDTree<T> implements KNearest<T> {
    private final KDTree<T> tree = new KDTree<>(2);

    @Override
    public void add(double x, double y, T data) {
        this.tree.insert(new double[]{x, y}, data);
    }

    @Override
    public List<T> query(double x, double y, int limit) {
        return tree.nearest(new double[]{x, y}, limit);
    }
}
