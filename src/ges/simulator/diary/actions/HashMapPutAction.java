package ges.simulator.diary.actions;

import java.util.HashMap;

public class HashMapPutAction<K, V> implements Action {
	private final K key;
	private final V value;
	private final HashMap<K, V> map;

	public HashMapPutAction(HashMap<K, V> map, K key, V value) {
		this.key = key;
		this.value = value;
		this.map = map;
	}

	@Override
	public void revert() {
		map.remove(key, value);
	}
}
