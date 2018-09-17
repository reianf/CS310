import java.util.Iterator;
import java.util.TreeMap;

public class BalancedTree<K extends Comparable<K>, V> implements DictionaryADT<K, V> {
	private TreeMap<K, V> balancedTree;

	public BalancedTree() {
		balancedTree = new TreeMap<K, V>();
	}

	public boolean contains(K key) {
		return balancedTree.containsKey(key);
	}

	public boolean add(K key, V value) {
		if (contains(key)) return false;
		balancedTree.put(key, value);
		return true;
	}

	public boolean delete(K key) {
		if (contains(key)){
			balancedTree.remove(key);
			return true;
		}
		return false;
	}

	public V getValue(K key) {
		return balancedTree.get(key);
	}

	public K getKey(V value) {
		K key = null;
		Iterator<K> keys = keys();
		Iterator<V> values = values();
		while (values.hasNext()) {
			if (((Comparable<V>) values.next()).compareTo(value) == 0) {
				key = keys.next();
				break;
			}
			keys.next();
		}
		return key;
	}

	public int size() {
		return balancedTree.size();
	}

	public boolean isFull() {
		return false;
	}

	public boolean isEmpty() {
		return balancedTree.isEmpty();
	}

	public void clear() {
		balancedTree.clear();
	}

	public Iterator keys() {
		return balancedTree.keySet().iterator();
	}

	public Iterator values() {
		return balancedTree.values().iterator();
	}
}
