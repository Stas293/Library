package ua.org.training.library.utility;

import lombok.EqualsAndHashCode;

import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@EqualsAndHashCode(callSuper = true)
public class WeakConcurrentHashMap<K, V> extends AbstractMap<K, V> {

    private final ConcurrentMap<K, WeakReference<V>> cache;

    public WeakConcurrentHashMap() {
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public V get(Object key) {
        V query = null;
        WeakReference<V> reference = cache.get(key);
        if (reference != null) {
            query = reference.get();
        }
        return query;
    }

    @Override
    public V put(K key, V value) {
        WeakReference<V> reference = new WeakReference<>(value);
        WeakReference<V> oldReference = cache.put(key, reference);
        return oldReference != null ? oldReference.get() : null;
    }

    @Override
    public V remove(Object key) {
        WeakReference<V> oldReference = cache.remove(key);
        return oldReference != null ? oldReference.get() : null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<>();
        for (Entry<K, WeakReference<V>> entry : cache.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue().get();
            if (value != null) {
                entrySet.add(new SimpleEntry<>(key, value));
            }
        }
        return Collections.unmodifiableSet(entrySet);
    }
}


