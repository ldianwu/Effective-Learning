package com.lidianwu.effective.commons.concurrent;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashSet
 *
 * @author Created by ldianwu on 2019/9/21
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E> {

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();

    private transient ConcurrentHashMap<E, Object> map;

    public ConcurrentHashSet() {
        map = new ConcurrentHashMap<E, Object>();
    }

    public ConcurrentHashSet(Collection<? extends E> c) {
        map = new ConcurrentHashMap<E, Object>(Math.max((int) (c.size() / .75f) + 1, 16));
        addAll(c);
    }

    public ConcurrentHashSet(int initialCapacity, float loadFactor) {
        map = new ConcurrentHashMap<E, Object>(initialCapacity, loadFactor);
    }

    public ConcurrentHashSet(int initialCapacity) {
        map = new ConcurrentHashMap<E, Object>(initialCapacity);
    }

    ConcurrentHashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new ConcurrentHashMap<E, Object>(initialCapacity, loadFactor);
    }

    public Set<E> list() {
        return map.keySet();
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    @Override
    public void clear() {
        map.clear();
    }

}
