package lib280.list;

import lib280.base.LinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class ArrayedListIterator280<I extends Comparable<? super I>> implements LinearIterator280<I> {

	/**
	 * Array where the elements are stored.
	 */
	protected I[] listElements;
	
	/**
	 * Indices of the beginning and end of the list.
	 * List is empty when head = tail.  List is full when
	 * ((this.tail - 1) % capacity) == this.head 
	 * 
	 */
	protected int head, tail;
	
	/**
	 * Size of the listElements array, maximum number of elements in the list.
	 */
	protected int capacity;
	
	/**
	 * Constant denoting the "after" position.
	 */
	protected static int afterPos = -1;
	
	/**
	 * Constant denoting the "before" position.
	 */
	protected static int beforePos = -2;
	
	/**
	 * Number of Elements in the list
	 */
	protected int numEl;
	
	/**
	 * Index of the position of the cursor.
	 */
	protected int position;
	
	/**
	 * Initialize an iterator.
	 * @param elements The elements in the list.
	 * @param head The index of the current head of the list.
	 * @param tail The index of the current tail of the list.
	 */
	public ArrayedListIterator280(I[] elements, int head, int tail, int numEl) {
		this.listElements = elements;
		this.head = head;
		this.tail = tail;
		this.capacity = elements.length;
		this.numEl = numEl;
		this.position = head;
	}
	

	/**
	 * Sets the current position of the iterator.
	 * @param pos Index of the position to set the iterator to.
	 * @precond pos must be a position that corresponds to a valid item position
	 * @precond The list is not empty.
	 * @throws IllegalArgumentException when pos does not correspond to an valid item.
	 * @throws ContainerEmpty280Exception
	 */
	 void setPosition(int pos) throws IllegalArgumentException, ContainerEmpty280Exception {
		if ( this.numEl == 0 ) throw new ContainerEmpty280Exception();
		else if( pos >= capacity ) throw new IllegalArgumentException();
		else if( pos == beforePos ) this.position = beforePos;
		else if ( pos == afterPos ) this.position = afterPos;
		else if( this.head > this.tail && pos <= this.head && pos >= this.tail )
				this.position = pos;
		else if( this.head < this.tail && (pos >= this.tail || pos <= this.head) )
				this.position = pos;
		else if( this.head == this.tail )
				this.position = pos;
		else throw new IllegalArgumentException("" + pos + " is not a legal position when count = " + this.numEl + " head = " + this.head + " tail = " + this.tail); 
			
	}
	
	
	/**
	 * Returns the current item.
	 */
	public I item() throws NoCurrentItem280Exception {
		if( !itemExists() ) throw new NoCurrentItem280Exception();
		
		return this.listElements[this.position];
	}

	@Override
	public boolean itemExists() {
		if( this.position != ArrayedListIterator280.beforePos &&
			this.position != ArrayedListIterator280.afterPos )
			return true;
		else return false;
	}

	@Override
	public boolean before() {
		return this.position == ArrayedListIterator280.beforePos;
	}

	@Override
	public boolean after() {
		return this.position == ArrayedListIterator280.afterPos;
	}

	
	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if( this.position == ArrayedListIterator280.beforePos ) this.position = head;
		else if( this.position == this.tail ) this.position = ArrayedListIterator280.afterPos;
		else this.position = (this.position - 1) % capacity;
	}

	
	@Override
	public void goFirst() {
		this.position = this.head;
	}

	@Override
	public void goBefore() {
		this.position = ArrayedListIterator280.beforePos;
	}

	@Override
	public void goAfter() {
		this.position = ArrayedListIterator280.afterPos;
	}

}
