import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnorderedArrayPriorityQueue<E extends Comparable <E>> implements PriorityQueue<E> {
	private E[] storage;
	private int maxSize,currentSize;

	public UnorderedArrayPriorityQueue(int max) {
		maxSize = max;
		currentSize = 0;
		storage = (E[]) new Comparable[maxSize];
	}
	
	public UnorderedArrayPriorityQueue() {
		this(DEFAULT_MAX_CAPACITY);
	}

	public boolean insert(E obj) {
		if (isFull()) return false;
		storage[currentSize++] = obj;
		return true;
	}

	public E remove() {
		if(isEmpty()) return null;
		int priorityIndex = 0;
		E high = storage[priorityIndex];
		
		//finds highest priority object and removes it
		for(int i = 0; i < currentSize; i++) {
			if(storage[i].compareTo(storage[priorityIndex]) < 0) {
				priorityIndex = i;
				high = storage[priorityIndex];
			}
		}
		currentSize--;
		
		//shift array to the left from the removed object
		for(int i = priorityIndex; i < currentSize; i++) {
			storage[i] = storage[i+1];
		}
		return high;
	}

	public boolean delete(E obj) {
		//bool = if obj is found or not
		boolean bool = false;
		if(isEmpty()) return false;
		//for loop deletes all instances of obj
		for(int i = 0; i < currentSize; i++) {		
			if(obj.compareTo(storage[i]) == 0) {
				for( int j = i ; j < currentSize ; j++) {
					storage[j] = storage[j+1];
				}
				i--;
				currentSize--;
				bool = true;
			}
		}
		return bool;
	}

	public E peek() {
		if(isEmpty()) return null;
		E high = storage[0];
		for(int i = 1; i < currentSize; i++) {
			if(storage[i].compareTo(high) < 0)
				high = storage[i];
		}
		return high;
	}

	public boolean contains(E obj) {
		if(isEmpty()) return false;
		for(int i = 0; i < currentSize; i++) {
			if(obj.compareTo(storage[i]) == 0) return true;
		}
		return false;
	}

	public int size() {
		return currentSize;
	}

	public void clear() {
		currentSize = 0;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public boolean isFull() {
		return currentSize == maxSize;
	}

	public Iterator<E> iterator() {
		return new IteratorHelper();
	}

	class IteratorHelper implements Iterator<E> {
		int index;

		public IteratorHelper() {
			index = 0;
		}

		public boolean hasNext() {
			return index < currentSize;
		}

		public E next() {
			if(!hasNext()) throw new NoSuchElementException();
			return storage[index++];
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}

