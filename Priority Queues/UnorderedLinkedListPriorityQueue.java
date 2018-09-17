import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnorderedLinkedListPriorityQueue<E extends Comparable <E>> implements PriorityQueue<E> {

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

	public UnorderedLinkedListPriorityQueue() {
		head = null;
		currentSize = 0;
		modCounter = 0;
	}

	public boolean insert(E obj) {
		Node<E> newNode = new Node<E>(obj);
		newNode.next = head;
		head = newNode;
		modCounter++;
		currentSize++;
		return true;
	}

	public E remove() {
		if (isEmpty()) return null;
		Node<E> prevTmp = null, prev = null, 
				currTmp = head, curr = head;
		E tmp = head.data;
		while (curr != null) { 
			if (curr.data.compareTo(tmp) <= 0) {
				tmp = curr.data;
				prevTmp = prev;
				currTmp = curr;
			}
			prev = curr;
			curr = curr.next;
		}
		if (prevTmp == null) head = head.next;
		else {
			prevTmp.next = currTmp.next;
		}
		currentSize--;
		modCounter++;
		return tmp;
	}

	public boolean delete(E obj) {
		boolean bool = false;
		Node<E> prev = null, curr = head;
		while (curr != null && curr.data.compareTo(obj) >= 0) {
			if (obj.compareTo(curr.data) == 0) {
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

	public E peek() {
		if (isEmpty()) return null;
		Node<E> curr = head;
		E tmp = head.data;
		while (curr != null) {
			if (curr.data.compareTo(tmp) <= 0) tmp = curr.data;
			curr = curr.next;
		}
		return tmp;
	}

	public boolean contains(E obj) {
		Node<E> tmp = head;
		while(tmp != null) {
			if(tmp.data == obj) return true;
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


