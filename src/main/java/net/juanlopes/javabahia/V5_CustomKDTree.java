package net.juanlopes.javabahia;

import net.juanlopes.javabahia.kdtree.KDTree;

import java.util.ArrayList;
import java.util.List;

public class V5_CustomKDTree<T> implements KNearest<T> {
    private final KDTree tree = new KDTree();
    private final List<T> data = new ArrayList<>();
    private final KDTree.Query query = tree.newQuery();

    @Override
    public void add(double x, double y, T data) {
        this.tree.add(x, y, this.data.size());
        this.data.add(data);
    }

    @Override
    public List<T> query(double x, double y, int limit) {
        query.execute(x, y, limit, Double.POSITIVE_INFINITY);

        List<T> answer = new ArrayList<>();
        for (int i = 0; i < query.size(); i++) {
            answer.add(data.get(query.get(i)));
        }
        return answer;
    }
}
