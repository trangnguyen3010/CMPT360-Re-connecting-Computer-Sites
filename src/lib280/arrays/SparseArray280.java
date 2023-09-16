package lib280.arrays;
import lib280.list.LinkedList280;
import lib280.list.LinkedIterator280;
import lib280.base.Pair280;

public class SparseArray280<I> {

	/**
	 * A linked list of IndexedItem280 objects provides the data structure for this sparse array ADT.
	 */
	protected LinkedList280<Pair280<I,Integer>> sparseItems;
	
	
	/**
	 * Default constructor.
	 */
	SparseArray280 () {
		this.sparseItems = new LinkedList280<Pair280<I, Integer>>();
	}
	
	
	/**
	 * Locate an array position.
	 * @param idx The position to be located
	 * @return The IndexedItem280<I> element from this.sparseItems whose index equals 'idx', or null if no such object (there is no element at index 'idx').
	 */
	private Pair280<I, Integer> findIndexedItem(int idx) {
		
		// Handle special case when list is empty
		if( sparseItems.isEmpty() ) return null;
		
		// A new iterator to iterate over IndexedItem280<I> objects in our sparse array.
		LinkedIterator280<Pair280<I, Integer>> currentItem = new LinkedIterator280<Pair280<I, Integer>>(sparseItems);
		
		// Iterate over all IndexedItem280 elements in 'sparseItems' ... look for one whose index is 'idx'.
		currentItem.goFirst();
		while(currentItem.itemExists()) {
			
			// If this list element represents position 'idx' in the sparse array...
			if( currentItem.item().secondItem() == idx)  {
				// Return this list element.
				return currentItem.item();
			}

			// Otherwise, try the next one.
			currentItem.goForth();
		}
		
		return null;
	}
	
	
	/**
	 * Change the item at a given array index.
	 * 
	 * @param item The new element to store at index 'idx' in the array.
	 * @param idx The index at which 'item' is to be stored.
	 */
	public void setItemAtIndex(I item, int idx) {
		// Handle special case when sparseItems is empty.
		if( sparseItems.isEmpty() ) {
			sparseItems.insert(new Pair280<I, Integer>(item, idx));
			return;
		}
		
		// Search to see if there is already an item at index 'idx'...
		Pair280<I, Integer> position = findIndexedItem(idx);		
		
		// If there isn't...
		if( position == null ) {
			// We have to add a new IndexedItem280 into the list at the position idx.
			LinkedIterator280<Pair280<I, Integer>> currentItem = new LinkedIterator280<Pair280<I, Integer>>(sparseItems);
			
			// Find the position that our new element should immediately precede.
			currentItem.goFirst();
			while(currentItem.itemExists()) {
				if( currentItem.item().secondItem() >= idx ) {
					break;
				}
				
				currentItem.goForth();
			}
			
			// Put the cursor of sparseItems at the item before which we need to insert our new item.
			sparseItems.goPosition(currentItem);	
			sparseItems.insertBefore(new Pair280<I, Integer>(item, idx));
		}
		else {
			// Otherwise, there was already a non-zero element at position idx, so just update it.
			position.setFirstItem(item);
		}
	}
	
	
	/**
	 * Obtain the item in the array at a given index.
	 * @param idx The array index whose data item is to be retrieved.
	 * @return The item stored at index 'idx' in the array.
	 */
	public I getItemAtIndex(int idx) {
		// Search to see if there is an item at position 'idx'.
		Pair280<I, Integer> position = findIndexedItem(idx);		
		
		// If there isn't, return null
		if( position == null ) return null;
		
		// If there is, return the item.
		return position.firstItem();
				
	}
	
	
	/**
	 * A toString() method that prints out the non-zero elements of the array in index-order, in the following format:  index:item
	 * Thus the array [0 5 0 2 0 9] would print as:
	 * 
	 * 1:5, 3:2, 5:9, 
	 * 
	 * @return string reprsentation of the sparse array, not including null-entries.
	 */
	public String toString() {
		// Handle special case when list is empty
		if( sparseItems.isEmpty() ) return "empty";
		
		// A new iterator to iterate over IndexedItem280<I> objects in our sparse array.
		LinkedIterator280<Pair280<I, Integer>> currentItem = new LinkedIterator280<Pair280<I, Integer>>(sparseItems);
		
		// Iterate over all IndexedItem280 elements in 'sparseItems' ... look for one whose index is 'idx'.
		String result = "";
		currentItem.goFirst();		
		while(currentItem.itemExists()) {
			result += currentItem.item().secondItem() + ":" + currentItem.item().firstItem() + ", ";
			currentItem.goForth();
		}
		
		return result + "\n";
	}
	
	/**
	 * Unit test for Sparse Array.
	 * 
	 * @param args none
	 */
	public static void main(String args[]) {
		SparseArray280<Integer> A = new SparseArray280<Integer>();
		
		// Test getting an index when the array is empty.
		if( A.getItemAtIndex(0) != null ) {
			System.out.println("Error!  Got an element at index 0 when the array is empty!");
		}

		// Let's populate it with some data.
		A.setItemAtIndex(1, 1);
		A.setItemAtIndex(5, 5);
		A.setItemAtIndex(10, 10);
		A.setItemAtIndex(15, 15);

		System.out.println("Expected Array: 1:1, 5:5, 10:10, 15:15, ");
		System.out.print("  Actual Array: " + A.toString());
		
		// Test new element at beginning
		A.setItemAtIndex(0,0);
		System.out.println("Expected Array: 0:0, 1:1, 5:5, 10:10, 15:15, ");
		System.out.print("  Actual Array: " + A.toString());
		
		// Test new element at the end
		A.setItemAtIndex(25, 25);
		System.out.println("Expected Array: 0:0, 1:1, 5:5, 10:10, 15:15, 25:25, ");
		System.out.print("  Actual Array: " + A.toString());
		
		
		// Test new element in middle
		A.setItemAtIndex(7, 7);
		System.out.println("Expected Array: 0:0, 1:1, 5:5, 7:7, 10:10, 15:15, 25:25, ");
		System.out.print("  Actual Array: " + A.toString());
		
		// Test Modify first element
		A.setItemAtIndex(555,0);
		System.out.println("Expected Array: 0:555, 1:1, 5:5, 7:7, 10:10, 15:15, 25:25, ");
		System.out.print("  Actual Array: " + A.toString());
		
		// Test modify last element
		A.setItemAtIndex(555,25);
		System.out.println("Expected Array: 0:555, 1:1, 5:5, 7:7, 10:10, 15:15, 25:555, ");
		System.out.print("  Actual Array: " + A.toString());
		
		// Test modify middle element
		A.setItemAtIndex(555,7);
		System.out.println("Expected Array: 0:555, 1:1, 5:5, 7:555, 10:10, 15:15, 25:555, ");
		System.out.print("  Actual Array: " + A.toString());
		
		// Test obtain first element
		Integer result = A.getItemAtIndex(0);
		if( result != 555 )
			System.out.println("Error: element at index 0 was expeted to be 555 but it was: " + result);
		
		// Test obtain last element
		result = A.getItemAtIndex(25);
		if( result != 555 )
			System.out.println("Error: element at index 25 was expeted to be 555 but it was: " + result);
		
		// Test obtain middle element
		result = A.getItemAtIndex(5);
		if( result != 5 )
			System.out.println("Error: element at index 5 was expeted to be 5 but it was: " + result);
		
		
	}
	
}
