import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedLinkedListPriorityQueue<E extends Comparable <E>> implements PriorityQueue<E> {
	
	private class Node<T> {
		T data;
		Node<T> next;

		public Node (T d) {
			data = d;
			next = null;
		}
	}

	private Node<E> head;
	private int currentSize;
	private long modCounter;

	public OrderedLinkedListPriorityQueue() {
		head = null;
		currentSize = 0;
		modCounter = 0;
	}

	public boolean insert(E obj) {
		Node<E> newNode = new Node<E>(obj);
		Node<E> previous = null, current = head;

		while (current != null && obj.compareTo(current.data) >= 0) {
			previous = current;
			current = current.next;
		}
		if (previous == null) {
			newNode.next = head;
			head = newNode;
		} else if (current == null) {
			previous.next = newNode;

		} else {
			previous.next = newNode;
			newNode.next = current;
		}
		modCounter++;
		currentSize++;
		return true;
	}

	public E remove() {
		if(isEmpty()) return null;
		E tmp = head.data;
		head = head.next;
		modCounter++;
		currentSize--;
		return tmp;
	}
	
	public boolean delete(E obj) {
		boolean bool = false;
		Node<E> prev = null, curr = head;
		while(curr!=null) {
		    if(curr.data.compareTo(obj) == 0) {
		    	if(curr == head) head = head.next;
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

	public E peek() {
		if (isEmpty()) return null;
		return head.data;
	}

	public boolean contains(E obj) {
		Node<E> tmp = head;
		while (tmp != null) {
			if (obj.compareTo(tmp.data) == 0) return true;
			tmp = tmp.next;
		}
		return false;
	}

	public int size() {
		return currentSize;
	}

	public void clear() {
		head = null;
		currentSize = 0;
		modCounter++;
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public boolean isFull() {
		return false;
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