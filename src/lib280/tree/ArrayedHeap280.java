package lib280.tree;


import lib280.tree.ArrayedBinaryTree280;

import lib280.exception.InvalidState280Exception;

public class ArrayedHeap280<I extends Comparable<? super I>> extends ArrayedBinaryTree280<I> {

	@SuppressWarnings("unchecked")
	public ArrayedHeap280(int cap) {
		super(cap);
		items = (I[]) new Comparable[capacity+1];  // Override the constructor's implementation of items
	}
	
	
	/**
	 * Insert a new item into the heap.
	 * 
	 * @param item The item to be inserted.
	 * 
	 */
	public void insert(I item) throws InvalidState280Exception {
		// Insert normally
		super.insert(item);
		
		if( count == 1 ) return;
		
		// Then propagate up the tree.
		int n = count;
				
		while(n > 1 && items[n].compareTo(items[findParent(n)]) > 0)  {
			int p = findParent(n);
			I temp = items[p];
			items[p] = items[n];
			items[n] = temp;
			n = n / 2;
		}
	}
	
	/**
	 * Removes the item at the top of the heap.
	 * 
	 * 
	 */
	
	public void removeItem() {
		// Delete the root.  This swaps the root with the last item.
		root();
		super.removeItem();
		
		// Propogate the new root down.
		int n = 1;
		while( findLeftChild(n) <= count ) {  // While n has a left child...
			// Select the left child.
			int child = findLeftChild(n);
			
			// If the right child exists and is larger, select it instead.
			if( child + 1 <= count && items[child].compareTo(items[child+1]) < 0 ) 
				child++;
			
			// If the parent is smaller than the root...
			if( items[n].compareTo(items[child]) < 0 ) {
				// Swap them.
				I temp = items[n];
				items[n] = items[child];
				items[child] = temp;
				n = child;
			}
			else return;
			
		}
	}
	
	
	/** 
	 * Helper for the regression test.  Verifies the heap property for all nodes.
	 */
	private boolean hasHeapProperty() {	
		for(int i=1; i <= count; i++) {
			if( findRightChild(i) <= count ) {  // if i Has two children...
				// ... and i is smaller than either of them, , then the heap property is violated.
				if( items[i].compareTo(items[findRightChild(i)]) < 0 ) return false;
				if( items[i].compareTo(items[findLeftChild(i)]) < 0 ) return false;
			}
			else if( findLeftChild(i) <= count ) {  // if n has one child...
				// ... and i is smaller than it, then the heap property is violated.
				if( items[i].compareTo(items[findLeftChild(i)]) < 0 ) return false;
			}
			else break;  // Neither child exists.  So we're done.	
		}
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayedHeap280<Integer> H = new ArrayedHeap280<Integer>(10);
		
		// Empty heap should have the heap property.
		if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");

		for(int i = 1; i <= 10; i++) {
			H.insert(i);
			H.root();
			if(H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
			if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
		}
			
		for(int i = 10; i >= 1; i--) {
			// Remove the element i.
			H.removeItem();
			// If we've removed item 1, the heap should be empty.
			if(i==1) { 
				if( !H.isEmpty() ) System.out.println("Expected the heap to be empty, but it wasn't.");
			}
			else {
				// Otherwise, the item left at the top of the heap should be equal to i-1.
				H.root();			
				if(H.item() != i-1) System.out.println("Expected current item to be " + i + ", got " + H.item());
				if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
			}
		}
		
		System.out.println("Regression Test Complete.");
	}
	
	

}
