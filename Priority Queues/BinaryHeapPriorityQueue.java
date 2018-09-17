import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinaryHeapPriorityQueue<E extends Comparable <E>> implements PriorityQueue<E> {
	class Wrapper<E> implements Comparable<Wrapper<E>> {
		E data;
		long seqNumber;

		public Wrapper(E obj) {
			data = obj;
			seqNumber = modCounter++;
		}

		public int compareTo(Wrapper<E> w) {
			int comp = ((Comparable<E>) data).compareTo(w.data);
			if (comp == 0) 
				return (int) (seqNumber - w.seqNumber);
			return comp;
		}

		public String toString() {
			return "" + data;
		}
	}
	private Wrapper<E>[] storage;
	private int currentSize;
	private long modCounter;

	public BinaryHeapPriorityQueue() {
		this(DEFAULT_MAX_CAPACITY);
	}

	public BinaryHeapPriorityQueue(int size) {
		modCounter = 0;
		currentSize = 0;
		storage = new Wrapper[size];
	}

	public boolean insert(E obj) {
		if (isFull()) return false;
		Wrapper<E> newValue = new Wrapper(obj);
		int newIndex = currentSize++;
		while(newIndex > 0 && storage[(newIndex-1)/2].compareTo(newValue) > 0) {
			int parentIndex = (newIndex-1)/2;
			storage[newIndex] = storage[parentIndex];
			newIndex = parentIndex;
		}
		storage[newIndex] = newValue;
		return true;
	}

	public E remove() {
		if (isEmpty()) return null;
		E tmp = storage[0].data;
		Wrapper<E> element = storage[--currentSize];
		int current = 0;
		int left = 1;
		int right = 2;
		while(left < currentSize) {
			if(right < currentSize) {
				if(storage[left].compareTo(storage[right]) < 0) {
					if(element.compareTo(storage[left]) > 0) {
						storage[current] = storage[left];
						current = left;
					}
					else break;
				}
				else {
					if(element.compareTo(storage[right]) > 0) {
						storage[current] = storage[right];
						current = right;
					}
					else break;
				}
			}
			else {
				if(element.compareTo(storage[left]) > 0) {
					storage[current] = storage[left];
					current = left;
				}
				else break;
			}
			left = current * 2 + 1;
			right = left + 1;
		}
		storage[current] = element;
		modCounter++;
		return tmp;
	}
	public boolean delete(E obj) {
		BinaryHeapPriorityQueue temp = new BinaryHeapPriorityQueue(currentSize);
		int tempSize = currentSize;
		while(tempSize != 0) {
			E x = remove();
			if (x.compareTo(obj) != 0) temp.insert(x);
			tempSize--;
		}
		this.currentSize = temp.currentSize;
		this.storage = temp.storage;
		return true;
	}

	public E peek() {
		return storage[0].data;
	}

	public boolean contains(E object) {
		for(Wrapper<E> w : storage)
			if(((Comparable<E>)object).compareTo(w.data) == 0)
				return true;
		return false;
	}

	public int size() {
		return currentSize;
	}

	public void clear() {
		currentSize = 0;
		modCounter = 0;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public boolean isFull() {
		return currentSize >= storage.length;
	}

	public Iterator<E> iterator() {
		return new IteratorHelper();
	}

	class IteratorHelper implements Iterator<E> {
		private int current;
		private long modCheck;

		public IteratorHelper() {
			modCheck = modCounter;
			current = 0;
		}

		public boolean hasNext() {
			if (modCheck != modCounter) 
				throw new ConcurrentModificationException();
			return current < currentSize;
		}

		public E next() {
			if (!hasNext()) 
				throw new NoSuchElementException();
			return storage[current++].data;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}