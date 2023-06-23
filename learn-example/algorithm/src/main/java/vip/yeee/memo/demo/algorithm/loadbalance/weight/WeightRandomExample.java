package vip.yeee.memo.demo.algorithm.loadbalance.weight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/6/1 17:26
 */
public class WeightRandomExample {

    public static void main(String[] args) {
        List<WeightRandom.WeightObj<String>> weightObjs = new ArrayList<WeightRandom.WeightObj<String>>() {{
            add(new WeightRandom.WeightObj<>("1", 30));
            add(new WeightRandom.WeightObj<>("2", 30));
            add(new WeightRandom.WeightObj<>("3", 30));
            add(new WeightRandom.WeightObj<>("4", 10));
        }};
        WeightRandom<String> weightRandom = new WeightRandom<>(weightObjs);
        for (int i = 0; i < 10; i++) {
            System.out.println("next = " + weightRandom.next());
        }
    }
}

class WeightRandom<T> {

    private final List<WeightObj<T>> weightObjs;

    public WeightRandom(List<WeightObj<T>> weightObjs) {
        this.weightObjs = weightObjs;
    }

    public T next() {
        if (weightObjs.isEmpty()) {
            return null;
        }
        Integer tmp = 0, sum = 0;
        for (WeightObj<T> weightObj : this.weightObjs) {
            sum += weightObj.getWeight();
        }
        List<Double> region = new ArrayList<>();
        for (WeightObj<T> weightObj : this.weightObjs) {
            tmp += weightObj.getWeight();
            region.add((tmp * 1.0) / sum);
        }
        double random = Math.random();
        region.add(random);
        Collections.sort(region);
        return weightObjs.get(region.indexOf(random)).getObj();
    }

    static class WeightObj<T> {
        private final T obj;
        private final Integer weight;

        public WeightObj(T obj, Integer weight) {
            this.obj = obj;
            this.weight = weight;
        }

        public T getObj() {
            return obj;
        }

        public Integer getWeight() {
            return weight;
        }
    }
}
