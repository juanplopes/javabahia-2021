package net.juanlopes.javabahia;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;
import gnu.trove.TIntProcedure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class V3_RTree<T> implements KNearest<T> {
    private final RTree tree = new RTree();
    private final List<T> data = new ArrayList<>();

    public V3_RTree() {
        tree.init(null);
    }

    @Override
    public void add(double x, double y, T data) {
        this.tree.add(new Rectangle((float) x, (float) y, (float) x, (float) y), this.data.size());
        this.data.add(data);
    }

    @Override
    public List<T> query(double x, double y, int limit) {
        List<T> answer = new ArrayList<>();
        tree.nearestN(new com.infomatiq.jsi.Point((float) x, (float) y), index -> {
            answer.add(data.get(index));
            return true;
        }, limit, Float.POSITIVE_INFINITY);
        return answer;
    }
}
