package ges.simulator.diary.actions;

import java.util.HashMap;

public class HashMapValueChange<K, V> implements Action {
	private final HashMap<K, V> map;
	private final K key;
	private final V oldValue;

	public HashMapValueChange(HashMap<K, V> map, K key, V oldValue) {
		this.map = map;
		this.key = key;
		this.oldValue = oldValue;
	}

	@Override
	public void revert() {
		map.replace(key, oldValue);
	}
}
