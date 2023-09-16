package lib280.dispenser;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.tree.ArrayedHeap280;

public class PriorityQueue<I extends Comparable<? super I>> {
	
	protected ArrayedHeap280<I> items;
	
	
	public PriorityQueue(int cap) {
		items = new ArrayedHeap280<I>(cap);
	}
	
	/**
	 * Enqueues an item into the queue in priority order.
	 * 
	 * @param item The item to be inserted.
	 * @precond The queue must not be full.
	 */
	public void insert(I item) {
		if( items.isFull() )
			throw new ContainerFull280Exception();
		items.insert(item);
	}
	
	
	/** 
	 * Determine if the queue is full.
	 * @return true if the queue is full, false otherwise.
	 * 
	 */
	public boolean isFull() {
		return items.isFull();
	}
	
	
	/** Determine if the queue is empty.
	 * 
	 * @return true if the queue is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	/**
	 * Determine the number of items in the queue.
	 *
	 * @return the number of items in the priority queue.
	 */
	public int count() {
		return items.count();
	}
	
	/**
	 * Obtain the largest item in the queue.
	 * @return A reference to the largest item in the priority queue.
	 * @precond The queue must not be empty.
	 */
	public I item() throws ContainerEmpty280Exception {
		if( items.isEmpty() )
			throw new ContainerEmpty280Exception();
		items.root();
		return items.item();
	}
	
	
	
	/** 
	 * Delete the largest item in the queue (ie - dequeue).
	 * @precond The queue must not be empty.
	 */
	public void deleteItem() throws ContainerEmpty280Exception {
		if( items.isEmpty() )
			throw new ContainerEmpty280Exception();
		items.removeItem();
	}

	public String toString() {
		return items.toString();	
	}
	
	
	
	// Regression test for PriorityQueue.
	public static void main(String [] args) {
		
		PriorityQueue<Integer> Q = new PriorityQueue<Integer>(5);
		
		// Test maxItem() on empty queue.
		Exception ex = null;
		try {Q.item();}
		catch(ContainerEmpty280Exception e) { ex = e; }
		
		if( ex == null ) 
			System.out.printf("maxItem() on an empty queue failed to produce an excption.");
		
		// Test deleteMax() on empty queue.
		ex = null;
		try {Q.deleteItem();}
		catch(ContainerEmpty280Exception e) { ex = e; }
		
		if( ex == null ) 
			System.out.printf("deleteMax() on an empty queue failed to produce an excption.");
		
		// Insert 5 items to make the queue full.
		for(int i=0; i < 5; i++) Q.insert(i);
		
		// Test insert on full queue.
		ex = null;
		try {Q.insert(5);}
		catch(ContainerFull280Exception e) { ex = e; }
		
		if( ex == null ) 
			System.out.printf("insert() on an full queue failed to produce an excption.");
		
		// Check the contents of the queue:
		System.out.println("Queue Contents: " + Q);
		System.out.println("Expected:       4, 3, 1, 0, 2,");
		
		// Check that we get the items off the queue in the proper order:
		for(int i=4; i >=0; i--) {
			Integer front = Q.item();
			Q.deleteItem();
			if( front != i ) 
				System.out.println("maxItem() failed: expected " + i + ", got " + "front.");
			
		}
	}
	
}
