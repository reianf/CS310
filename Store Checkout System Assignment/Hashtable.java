import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class Hashtable<K extends Comparable<K>, V> implements DictionaryADT<K,V> {

	private LinkedList<Wrapper<K, V>>[] list;
	private int currentSize, maxSize, tableSize;
	private long modCounter;

	public Hashtable(int max) {
		currentSize = 0;
		maxSize = max;
		tableSize = (int) (maxSize * 1.3f);
		list = new LinkedList[tableSize];
		modCounter = 0;
		for (int i = 0; i < tableSize; i++) {
			list[i] = new LinkedList<Wrapper<K, V>>();
		}
	}

	public boolean contains(K key) {
		return list[getIndex(key)].contains(new Wrapper<K, V>(key, null));
	}

	public boolean add(K key, V value) {
		if (isFull()) return false;
		if (contains(key)) return false;
		list[getIndex(key)].addFirst(new Wrapper<K, V>(key, value));
		currentSize++;
		modCounter++;
		return true;
	}

	public boolean delete(K key) {
		if (list[getIndex(key)].delete(new Wrapper<K,V>(key,null))){
			currentSize--;
			modCounter++;
			return true;
		}
		return false;
	}

	public V getValue(K key) {
		Wrapper<K, V> tmp = list[getIndex(key)].find(new Wrapper<K, V>(key, null));
		if (tmp == null)
			return null;
		return tmp.value;
	}

	public K getKey(V value) {
		for (int i = 0; i < tableSize; i++)
			for (Wrapper<K, V> w : list[i])
				if (((Comparable<V>) value).compareTo(w.value) == 0)
					return w.key;
		return null;
	}

	public int size() {
		return currentSize;
	}

	public boolean isFull() {
		return currentSize == maxSize;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public void clear() {
		currentSize = 0;
		modCounter = 0;
		for (int i = 0; i < tableSize; i++)
			list[i].clear();
	}

	public Iterator keys() {
		if (new KeyIteratorHelper() == null) return null;
		return new KeyIteratorHelper();
	}

	public Iterator values() {
		if (new ValueIteratorHelper() == null) return null;
		return new ValueIteratorHelper();
	}

	// helper method to call hashCode()
	private int getIndex(K key) {
		return (key.hashCode() & 0x7FFFFFFF) % tableSize;
	}

	// inner classes (LinkedList, Wrapper, KeyIteratorHelper,ValueIteratorHelper)

	class LinkedList<E> implements Iterable<E> {
		private Node<E> head, tail;
		private int currentSize;
		private long modCounter;

		class Node<E> {
			E data;
			Node<E> next;

			public Node(E obj) {
				data = obj;
				next = null;
			}
		}

		public boolean addFirst(E obj) {
			Node<E> tmp = new Node<E>(obj);
			if (head == null) head = tail = tmp;
			else {
				tmp.next = head;
				head = tmp;
			}
			currentSize++;
			modCounter++;
			return true;
		}
		
		public boolean delete(E obj) {
			boolean bool = false;
			Node<E> prev = null, curr = head;
			while (curr != null && ((Comparable<E>) curr.data).compareTo(obj) >= 0) {
				if (((Comparable<E>) obj).compareTo(curr.data) == 0) {
					if (curr == head) head = head.next;
					else { 
						prev.next = curr.next;
						curr = prev;
					}
					currentSize--;
					modCounter++;
					bool = true;
				}
				prev = curr;
				curr = curr.next;
			}
			return bool;
		}
		
		public E find(E obj) {
			Node<E> tmp = head;
			while (tmp != null){
				if (((Comparable<E>)obj).compareTo(tmp.data) == 0) return tmp.data;
				tmp = tmp.next;
			}
			return null;
		}
		
		public boolean contains(E obj) {
			Node<E> tmp = head;
			while(tmp != null && ((Comparable <E>)obj).compareTo(tmp.data) != 0 ){
				tmp = tmp.next;		
			}	
			if(tmp == null) return false;	
			return true;
		}
		
		public int size() {		
			return currentSize;
		}
		
		public void clear() {
			head = null;
			currentSize = 0;
			modCounter++;
		}
		
		public Iterator<E> iterator() {
			return new IteratorHelper();
		}
		
		class IteratorHelper implements Iterator<E> {
			private Node<E> iterator;
			private long modCheck;

			public IteratorHelper() {
				modCheck = modCounter;
				iterator = head;
			}

			public boolean hasNext() {
				if (modCheck != modCounter)
					throw new ConcurrentModificationException();
				return iterator != null;
			}

			public E next() {
				if (!hasNext()) throw new NoSuchElementException();
				E tmp = iterator.data;
				iterator = iterator.next;
				return tmp;
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}

	}

	class Wrapper<K, V> implements Comparable<Wrapper<K, V>> {
		K key;
		V value;

		public Wrapper(K k, V v) {
			key = k;
			value = v;
		}

		public int compareTo(Wrapper<K, V> node) {
			return (((Comparable<K>) key).compareTo((K) node.key));
		}
	}

	class KeyIteratorHelper<K> implements Iterator<K>{
		private Wrapper<K,V>[] nodes;
		private int index;
		private long modCheck;

		public KeyIteratorHelper(){
			nodes = new Wrapper[currentSize];
			index = 0;
			modCheck = modCounter;
			int j = 0;
			for (int i = 0; i <tableSize; i++)
				for (Wrapper n: list[i])
					nodes[j++] = n;
			nodes = shellSort(nodes);
		}

		public boolean hasNext(){
			if(modCheck != modCounter) throw new ConcurrentModificationException();
			return index < currentSize;
		}

		public K next(){
			if(!hasNext()) throw new NoSuchElementException();
			return nodes[index++].key;
		}

		public void remove(){
			throw new UnsupportedOperationException();
		}
		
		private Wrapper[] shellSort(Wrapper[] array){
			Wrapper[] n = array;
			int in, out, h = 1;
			Wrapper temp;
			int size = n.length;

			while(h <= size/3)
				h = h*3+1;

			while(h > 0){
				for(out = h; out < size; out++){
					temp = n[out];
					in = out;
					while(in > h-1 && ((Comparable<Wrapper>)n[in-h]).compareTo(temp) >= 0){
						n[in] = n[in-h];
						in -= h;
					}
					n[in] = temp;
				}
				h = (h-1)/3;
			}
			return n;
		}
	}

	class ValueIteratorHelper<V> implements Iterator<V>{
		Iterator<K> iterator;

		public ValueIteratorHelper(){
			iterator = keys();
		}

		public boolean hasNext(){
			return iterator.hasNext();
		}

		public V next(){
			return (V)getValue(iterator.next());
		}

		public void remove(){
			iterator.remove();
		}
	}
}