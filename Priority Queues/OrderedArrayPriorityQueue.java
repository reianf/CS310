import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedArrayPriorityQueue<E extends Comparable <E>> implements PriorityQueue<E> {
	private E[] storage;
	private int maxSize,currentSize;

	public OrderedArrayPriorityQueue(int max) {
		maxSize = max;
		currentSize = 0;
		storage = (E[]) new Comparable[maxSize];
	}

	public OrderedArrayPriorityQueue() {
		this(DEFAULT_MAX_CAPACITY);
	}

	public boolean insert(E obj) {
		if(isFull()) return false;
		int element = binarySearch(obj, 0, currentSize - 1);
    	currentSize++;
    	
    	for(int i= currentSize - 1; i > element; i--) {
    		storage[i] = storage[i-1];
    	}	
    	storage[element] = obj;
    	return true;
    }

	public E remove() {
		if (isEmpty()) return null;
		return storage[--currentSize];
	}

	public boolean delete(E obj) {
		//bool = if obj is found or not
		boolean bool = false;
		if(isEmpty()) return false;
		int indexPosition = binarySearch(obj, 0, currentSize - 1);

		while(obj.compareTo(storage[indexPosition]) == 0) {
			for( int i = indexPosition; i<currentSize - 1 ; i++) {
				storage[i] = storage[i+1];
			}
			currentSize--;
			bool = true;
		}
		return bool; 
	}

	public E peek() {
		if(isEmpty()) return null;
		return storage[currentSize - 1];
	}

	public boolean contains(E obj) {
		if(isEmpty()) return false;
		int element = binarySearch(obj, 0, currentSize - 1);
		if(obj.compareTo(storage[element]) == 0) return true;
		return true;
	}

	//Helper method for the insert, delete, and contain methods
	private int binarySearch(E obj, int lo, int hi) {
		if(hi < lo) return lo;
		int mid = (lo + hi)/2;
		if(obj.compareTo(storage[mid]) >= 0)
			return binarySearch(obj, lo, mid - 1);
		return binarySearch(obj, mid + 1, hi);
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
