import java.io.Serializable;

/**
 * Doubly linked list that stored nodes
 * @author PO Bogdanov 218029215 (coded with the assistance of the perscribed text book)
 *
 * @param <E> The type of element that will be stored in the Node
 */
public class DoublyLinkedList<E> implements Serializable {
	/**
	 * Serial ID of the DoublyLinkedList
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The node in the liked list
	 * @author PO Bogdanov 218029215 (coded with the assistance of the perscribed text book)
	 *
	 * @param <E> The type of element that will be stored in the Node
	 */
	private static class Node<E> implements Serializable{
		/**
		 * Serial ID of the Node
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * The element stored in the node
		 */
		private E element;
		/**
		 * The previous node
		 */
		private Node<E> prev;
		/**
		 * The next node
		 */
		private Node<E> next;
		
		/**
		 * Constructor
		 * @param e The element that will be stored in the node
		 * @param p The previous node
		 * @param n The next node
		 */
		public Node(E e, Node<E> p, Node<E> n) {
			//Initiallises attributes
			element=e;
			prev=p;
			next=n;
		}
		/**
		 * Returns the element in the node
		 * @return The element in the node
		 */
		public E getElement() {
			return element;
		}
		/**
		 * Returns the previous node
		 * @return The previous node
		 */
		public Node<E> getPrev() {
			return prev;
		}
		/**
		 * Returns the next node
		 * @return The next node
		 */
		public Node<E> getNext() {
			return next;
		}
		/**
		 * Changes the previous node
		 * @param prev The new previous node
		 */
		public void setPrev(Node<E> prev) {
			this.prev = prev;
		}
		/**
		 * Changes the next node
		 * @param next The new next node
		 */
		public void setNext(Node<E> next) {
			this.next = next;
		}
		
		/**
		 * Returns the string version of the node
		 * @return the string version of the node
		 */
		public String toString() {
			if (element==null) {
				return "<>";
			}
			return "<" +element.toString() +">";
		}
		/**
		 * Change the element of the node
		 * @param element the new element of the node
		 */
		public void setElement(E element) {
			this.element = element;
		}
	}
	/**
	 * The header of the doubly linked list
	 */
	private Node<E> header;
	/**
	 * The trailer of the doubly linked list
	 */
	private Node<E> trailer;
	/**
	 * The size of the doubly linked list
	 */
	private int size=0;
	/**
	 * Constructor
	 */
	public DoublyLinkedList() {
		header=new Node<>(null, null, null);
		trailer=new Node<>(null, header, null);
		header.setNext(trailer);
	}
	/**
	 * Returns the size of the doubly linked list
	 * @return The size of the doubly linked list
	 */
	public int size() {
		return size;
	}
	/**
	 * Check if the list is empty
	 * @return true if the list is empty otherwise false
	 */
	public boolean isEmpty() {
		return size==0;
	}
	/**
	 * Returns the first element in the list
	 * @return The first element in the list
	 */
	public E first() {
		if (isEmpty()) {
			return null;
		}
		return header.getNext().getElement();
	}
	/**
	 * Returns the last element in the list
	 * @return The last element in the list
	 */
	public E last() {
		if (isEmpty()) {
			return null;
		}
		return trailer.getPrev().getElement();
	}
	
	/**
	 * Returns the previous node of specified parameter
	 * @param node
	 * @return the previous node of specified parameter
	 */
	public Node<E> prev(Node<E> node) {
		return node.getPrev();
	}
	/**
	 * Returns the next node of specified parameter
	 * @param node
	 * @return the next node of specified parameter
	 */
	public Node<E> next(Node<E> node) {
		return node.getNext();
	}
	/**
	 * Insert a node with element e between the predecessor and successor
	 * @param e the element of the node that will be inserted 
	 * @param predecessor the predecessor of the of the node that will be added
	 * @param successor the successor of the of the node that will be added
	 */
	private void addBetween(E e, Node<E> predecessor, Node<E> successor) {
		Node<E> newest=new Node<>(e,predecessor,successor);
		predecessor.setNext(newest);
		successor.setPrev(newest);
		size++;
	}
	/**
	 * Remove a specific node from the list
	 * @param node The node that will be removed
	 * @return The element of the node that will be removed
	 */
	private E remove(Node<E> node) {
		Node<E> predecessor=node.getPrev();
		Node<E> successor=node.getNext();
		predecessor.setNext(successor);
		successor.setPrev(predecessor);
		size--;
		return node.getElement();
	}
	/**
	 * Adds a node at the beginning of the list
	 * @param e The element of the node that will be added
	 */
	public void addFirst(E e) {
		addBetween(e, header, header.getNext());
	}
	/**
	 * Adds a node at the end of the list
	 * @param e The element of the node that will be added
	 */
	public void addLast(E e) {
		addBetween(e, trailer.getPrev(), trailer);
	}
	
	/**
	 * Inserts a node after a specified node
	 * @param node The new node will be insert after this node (parameter)
	 * @param e The element of the node that will be added
	 * @return
	 */
	public Node<E> insertAfter(Node<E> node,E e) {
		Node<E> newest=new Node<>(e, node, node.getNext());
		node.setNext(newest);
		node.getNext().setPrev(newest);
		return newest;
	}
	
	/**
	 * Inserts a node before a specified node
	 * @param node The new node will be before after this node (parameter)
	 * @param e The element of the node that will be added
	 * @return
	 */
	public Node<E> insertBefore(Node<E> node,E e) {
		Node<E> newest=new Node<>(e, node.getPrev(), node);
		node.getPrev().setNext(newest);
		node.setNext(newest);
		return newest;
	}
	
	/**
	 * Removes the first node in the list
	 * @return The element of the removed node
	 */
	public E removeFirst() {
		if (isEmpty()) {
			return null;
		}
		return remove(header.getNext());
	}
	/**
	 * Removes the last node in the list
	 * @return The element of the removed node
	 */
	public E removeLast() {
		if (isEmpty()) {
			return null;
		}
		return remove(trailer.getPrev());
	}
	/**
	 * Replaces a specified node's element with a specified element
	 * @param node The node whose element will be replaced
	 * @param e The new element of the node
	 * @return The previous replaced element of the node
	 */
	public E replace(Node<E> node, E e) {
		node.setElement(e);
		return e;
	}
	/**
	 * Returns the string version of the doubly linked list
	 * @return the string version of the doubly linked list
	 */
	public String toString() {
		String result="";
		Node<E> walk=header;
		while (walk!=trailer) {
			result+= walk.toString()+"<->";
			walk=walk.getNext();
		}
		result+="<>";
		return result;
	}
}
