package com.lidianwu.effective.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * TopN
 *
 * @author Created by ldianwu on 2019/9/18
 */
public class TopN<K, V> implements Serializable {

    private final int topSize;
    private final TreeMap<K, V> treeMap;
    private final Comparator<? super K> comparator;

    public TopN(int n) {
        this(n, null);
    }

    public TopN(int topSize, Comparator<? super K> comparator) {
        this.topSize = topSize;
        this.treeMap = new TreeMap<>(comparator);
        this.comparator = comparator;
    }

    public void put(K key, V value) {
        // 如果满足top的数量
        if (treeMap.size() >= topSize) {
            // 取第一个
            K _k = this.treeMap.firstKey();
            // 如果比first的值小，则丢弃
            if (this.comparator.compare(key, _k) <= 0) {
                return;
            }
            this.treeMap.put(key, value);
            // 否则丢弃第一个
            treeMap.pollFirstEntry();
        } else {
            this.treeMap.put(key, value);
        }

        // 等同于下面代码，区别在于：
        // 上面的代码会判断，如果新加入的值比first的值还小，则丢弃，这样不用进行红黑树的操作，提高put性能

//        this.treeMap.put(key, value);
//        // 满足则丢弃
//        if (this.treeMap.size() > topSize) {
//            this.treeMap.pollFirstEntry();
//        }
    }

    public TreeMap<K, V> getTreeMap() {
        return treeMap;
    }
}