import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class BinarySearchTree<K extends Comparable<K>, V> implements DictionaryADT<K, V> {

	private Node<K, V> root;
	private int currentSize;
	private long modCounter;

	public BinarySearchTree() {
		root = null;
		currentSize = 0;
		modCounter = 0;
	}

	public boolean contains(K key) {
		Node<K, V> tmp = root;
		if (root == null) return false;
		while (tmp != null) {
			if (tmp.key.compareTo(key) == 0) return true;
			if (key.compareTo(tmp.key) < 0) tmp = tmp.leftChild;
			else {
				tmp = tmp.rightChild;
			}
		}
		return false;
	}

	public boolean add(K key, V value) {
		if (contains(key)) return false;
		if (root == null) root = new Node<K, V>(key, value);
		else {
			insert(key, value, root, null, false);
		}
		currentSize++;
		modCounter++;
		return true;
	}

	public boolean delete(K key) {
		root = remove(key, root);
		if (removeNode) {
			modCounter++;
			currentSize--;
			return true;
		}
		return false;
	}

	public V getValue(K key) {
		return findValue(key, root);
	}

	public K getKey(V value) {
		findKey(root, value);
		return foundKey;
	}

	public int size() {
		return currentSize;
	}

	public boolean isFull() {
		return false;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public void clear() {
		currentSize = 0;
		modCounter++;
		root = null;
	}
	
	public Iterator keys() {
		return new KeyIteratorHelper();
	}

	public Iterator values() {
		return new ValueIteratorHelper();
	}
	// helper methods (insert, getSuccessor, remove, findValue, findKey)

	// for add
	private void insert(K key, V value, Node<K, V> node, Node<K, V> parent, boolean bool) {
		if (node == null) {
			if (bool) parent.leftChild = new Node<K, V>(key, value);
			else {
				parent.rightChild = new Node<K, V>(key, value);
			}
		} else if (key.compareTo(node.key) < 0)
			insert(key, value, node.leftChild, node, true);
		else {
			insert(key, value, node.rightChild, node, false);
		}
	}

	// for delete
	private boolean removeNode;

	private Node<K, V> getSuccessor(Node<K, V> node) {
		if (node.leftChild == null) return node;
		return getSuccessor(node.leftChild);
	}

	private Node<K, V> remove(K key, Node<K, V> current) {
		if (current == null) {
			removeNode = false;
			return current;
		}
		if (key.compareTo(current.key) < 0)
			current.leftChild = remove(key, current.leftChild);
		else if (key.compareTo(current.key) > 0)
			current.rightChild = remove(key, current.rightChild);
		else if (current.leftChild != null && current.rightChild != null) {
			current.key = getSuccessor(current.rightChild).key;
			current.rightChild = remove(current.key, current.rightChild);
		} 
		else {
			if (current.leftChild != null) current = current.leftChild;
			else
				current = current.rightChild;
			removeNode = true;
		}
		return current;
	}
	
	// for getValue
	private V findValue(K key, Node<K, V> current) {
		if (current == null) return null;
		if (key.compareTo(current.key) < 0)
			return findValue(key, current.leftChild);
		if (key.compareTo(current.key) > 0)
			return findValue(key, current.rightChild);
		return current.value;
	}
	
	// for getKey
	private K foundKey;

	private void findKey(Node<K,V> current, V value) {
		if (current == null) return;
		if (((Comparable<V>) value).compareTo(current.value) == 0) {
			foundKey = current.key;
			return;
		}
		findKey(current.leftChild, value);
		findKey(current.rightChild, value);
	}

	// inner classes (Node, KeyIteratorHelper, ValueIteratorHelper)

	private class Node<K, V> {
		private K key;
		private V value;
		private Node<K, V> leftChild;
		private Node<K, V> rightChild;

		public Node(K k, V v) {
			key = k;
			value = v;
			leftChild = rightChild = null;
		}
	}

	class KeyIteratorHelper<K> implements Iterator<K> {
		private Node<K, V>[] nodes;
		protected int index;
		protected long modCheck;

		public KeyIteratorHelper() {
			nodes = new Node[currentSize];
			modCheck = modCounter;
			orderedArray((Node<K, V>) root);
			index = 0;
		}

		public boolean hasNext() {
			if (modCheck != modCounter)
				throw new ConcurrentModificationException();
			return index < currentSize;
		}

		public K next() {
			if (!hasNext()) throw new NoSuchElementException();
			return nodes[index++].key;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		private void orderedArray(Node<K,V> node) {
			if(node == null) return;
			orderedArray(node.leftChild);
			nodes[index++] = node;
			orderedArray(node.rightChild);
		}
	}

	class ValueIteratorHelper<V> implements Iterator<V> {
		Iterator<K> iterator;

		public ValueIteratorHelper() {
			iterator = keys();
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public V next() {
			return (V) getValue(iterator.next());
		}

		public void remove() {
			iterator.remove();
		}
	}
}
