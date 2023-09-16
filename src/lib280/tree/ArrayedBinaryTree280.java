package lib280.tree;



import lib280.exception.NoCurrentItem280Exception;
import lib280.exception.InvalidState280Exception;
import lib280.exception.ContainerEmpty280Exception;

/**
 * @author eramian
 *
 */
public class ArrayedBinaryTree280<I> implements ArrayedTree280<I> {

	protected int currentNode;		// Index of the node corresponding to the current cursor position.
	protected int capacity;			// Maximum number of elements in the tree.
	protected int count;			// Current number of elements in the tree.
	
	protected I items[];
	
	@SuppressWarnings("unchecked")
	/**
	 * Constructor.
	 * 
	 * @param cap Maximum number of elements that can be in the tree.
	 */
	public ArrayedBinaryTree280(int cap) {
		capacity = cap;
		currentNode = 0;
		count = 0;
		items = (I[]) new Object[capacity+1];
	}
	
	/**
	 * Gets the index of the left child of the node at index 'node'.
	 */
	protected int findLeftChild(int node) {
		return node * 2;
	}
	
	/**
	 * Gets the index of the right child of the node at index 'node'.
	 */
	protected int findRightChild(int node) {
		return node * 2 + 1;
	}
	
	/**
	 * Gets the index of the parent of the node at index 'node'.
	 */
	protected int findParent(int node) {
		return node / 2;
	}

	/**
	 * Add an item to the tree.
	 * 
	 * @param item The item to be added.
	 * @precond The tree must not be full.
	 */
	public void insert(I item) throws InvalidState280Exception {  
		
		if( isFull() ) throw new InvalidState280Exception("Attempt to insert into a full tree.");
		else {
			count ++;
			items[count] = item;
		}
	}
	
	/**
	 * Move the cursor to the parent of the current node.
	 * 
	 * @precond Tree cannot be empty and the current node must not be the root.
	 */
	public void parent() throws InvalidState280Exception {
		if( currentNode <= 1) throw new InvalidState280Exception("Cannot move to the parent of the root.");
		
		else currentNode = findParent(currentNode);
	}
	
	/**
	 * Move the cursor to the left child of the current node.
	 * 
	 * @precond The tree must not be empty and the current node must have a left child.
	 */
	public void goLeftChild()  throws InvalidState280Exception, ContainerEmpty280Exception {
		if( isEmpty() ) throw new ContainerEmpty280Exception("Cannot position cursor in an empty tree.");

		int leftChild = findLeftChild(currentNode);
		if( leftChild > count) throw new InvalidState280Exception("Current node has no left child.");
		else currentNode = leftChild;
	}	
	
	/**
	 * Move the cursor to the right child of the current node.
	 * 
	 * @precond The tree must not be empty and the current node must have a right child.
	 */
	public void goRightChild() throws InvalidState280Exception, ContainerEmpty280Exception {
		if( isEmpty() ) throw new ContainerEmpty280Exception("Cannot position cursor in an empty tree.");

		int rightChild = findRightChild(currentNode);
		if( rightChild > count) throw new InvalidState280Exception("Current node has no right child.");
		else currentNode = rightChild;
	}	
	
	/**
	 * Move the cursor to the sibling of the current node.
	 * 
	 * @precond The current node must have a sibling.  The tree must not be empty.
	 */	
	public void goSibling() throws InvalidState280Exception, ContainerEmpty280Exception {
		if( isEmpty() ) throw new ContainerEmpty280Exception("Cannot position cursor in an empty tree.");
		
		if( currentNode % 2  == 0)  {
			// This is the left child of it's parent, go to right sibling
			if( currentNode < count ) currentNode++;
			else throw new InvalidState280Exception("Current node has no right sibling.");
		}
		else { // This is the right child of it's parent, so it must have a left sibling, 
			   // unless it is the root, or the tree is empty.
			if( currentNode <= 1) throw new InvalidState280Exception("Current node has no left sibling.");
			else currentNode--;
		}
			
	}
	
	/**
	 * Move the cursor to the root of the tree.
	 * 
	 * @precond The tree must not be empty.
	 */
	public void root() throws ContainerEmpty280Exception {
		if( isEmpty() ) throw new ContainerEmpty280Exception("Empty tree has no root.");
		else currentNode = 1;
	}	
	
	
	/** 
	 * Remove the current item from the tree.
	 * 
	 * @precond There must be an item at the cursor.  The tree must not be empty.
	 */
	public void removeItem() throws NoCurrentItem280Exception {
		
		if(!itemExists() ) throw new NoCurrentItem280Exception();
		
		// If there was more than one item, and we aren't deleting the last item,
		// copy the last item in the array to the current position.
		if( count > 1 && currentNode < count ) {
			items[currentNode] = items[count];
		}
		
		count--;
		
		// If we deleted the last item, make the previous item the new position.
		if( currentNode == count+1 ) currentNode--;
	}	
	
	
	/**
	 * Get the item at the cursor.
	 * 
	 * @precond The tree must not be empty.  The cursor position must be valid.
	 * @return The item at the cursor.
	 */
	public I item() throws NoCurrentItem280Exception{
		if(!itemExists() ) throw new NoCurrentItem280Exception();
		else return items[currentNode];
	}
	
	/**
	 * Determines if an item exists.
	 * 
	 * @return true if there is an item at the cursor, false otherwise.
	 */
	public boolean itemExists(){
		return count > 0 && (currentNode >= 0 || currentNode < count);
	}
	
	
	/**
	 * Get the maximum capacity of the tree.
	 * 
	 * @return The maximum number of items the tree can store.
	 */
	public int capacity() {
		return capacity;
	}
	
	/** 
	 * Get the number of items in the tree.
	 * 
	 * @return The number of items in the tree.
	 * 
	 */
	public int count() {
		return count;
	}
	
	/** Empty the tree
	 * 
	 *  Remove all elements from the tree.
	 */
	public void clear() {
		count = 0;
		currentNode = 0;
	}
	
	/**
	 * Returns a shallow clone of the tree.  The new tree contains
	 * copies of item references, not new items.
	 * 
	 * @return A reference to the new copy.
	 */
	@SuppressWarnings("unchecked")
	public ArrayedBinaryTree280<I> clone() {
		ArrayedBinaryTree280<I> temp;
		try { 
			temp = (ArrayedBinaryTree280<I>) super.clone();
		}
		catch (CloneNotSupportedException e) {
			temp = null;
		}
		return temp;
	}
	
	
	/**
	 * Determine if the tree is empty.
	 * 
	 * @return true if the tree is empty.  false otherwise.
	 */
	public boolean isEmpty() {
		return count == 0 && currentNode == 0;
	}
	
	/**
	 * Determine if the tree is full.
	 * 
	 * @return true if the tree is full.  false otherwise.
	 */
	public boolean isFull() {
		return count == capacity;
	}
	

	public String toString() {
		String out = "";
		for(int i=1; i <= count; i++) {
			out += items[i] + ", ";
		}
		
		return out;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayedBinaryTree280<Integer> T = new ArrayedBinaryTree280<Integer>(10);
		
		// IsEmpty on empty tree.
		if(!T.isEmpty()) System.out.println("Test of isEmpty() on empty tree failed.");
		
		// Test root() on empty tree.
		Exception x = null;
		try {
			T.root();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to root of epty tree.  Got none.");
		}
		
		// Test goLeftChild() on empty tree.
		x = null;
		try {
			T.goLeftChild();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to left child in empty tree.  Got none.");
		}
		
		// Test goLeftChild() on empty tree.
		x = null;
		try {
			T.goRightChild();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to right child in empty tree.  Got none.");
		}
		
		
		// Check itemExists on empty tree
		if(T.itemExists() ) System.out.println("itemExists() returned true on an empty tree.");
		
		// Insert on empty tree.
		T.insert(1);
		
		// Check ItemExists on tree with one element.
		T.root();
		if(!T.itemExists() ) System.out.println("itemExists() returned false on a tree with one element with cursor at the root.");
		
		// isEmpty on tree with 1 element.
		if(T.isEmpty()) System.out.println("Test of isEmpty() on non-empty tree failed.");

		// Insert on tree with 1 element
		T.insert(2);
		
		// Insert some more elements
		for(int i=3; i <= 10; i++) T.insert(i);

		if(T.count() != 10  ) System.out.println("Expected tree count to be 10, got "+ T.count());

		
		// Test for isFull on a full tree.
		if(!T.isFull()) System.out.println("Test of isFull() on a full tree failed.");
		
		// Test insert on a full tree
		x = null;
		try {
			T.insert(11);
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception inserting into a full tree.  Got none.");
		}
		
		

		
		// Test positioning methods
		
		// Test root()
		T.root();
		if( T.item() != 1 ) System.out.println("Expected item at root to be 1, got " + T.item());
		
		T.goLeftChild();
		
		if( T.item() != 2 ) System.out.println("Expected current item to be 2, got " + T.item());
		
		T.goRightChild();
		if( T.item() != 5 ) System.out.println("Expected current item to be 5, got " + T.item());

		
		T.goLeftChild();
		if( T.item() != 10 ) System.out.println("Expected current item to be 10,  got " + T.item());
		
		// Current node now has no children.
		x = null;
		try {
			T.goLeftChild();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to left child of a leaf.  Got none.");
		}
		
		x = null;
		try {
			T.goRightChild();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to right child of a leaf.  Got none.");
		}
		
		
		
		// Remove the last item ( a leaf)
		T.removeItem();
		if( T.item() != 9 ) System.out.println("Expected current item to be 9, got " + T.item());

		T.parent();
		
		
		
		// Remove a node with 2 children.  The right child 9 gets promoted.
		T.removeItem();
		if( T.item() != 9 ) System.out.println("Expected current item to be 9, got " + T.item());
		
		
		// Remove a node with 1 child.  The left child 8 gets promoted.
		T.removeItem();
		if( T.item() != 8 ) System.out.println("Expected current item to be 8, got " + T.item());
		
		// Remove the root successively.  There are 7 items left.
		T.root();
		T.removeItem();
		if( T.item() != 7 ) System.out.println("Expected root to be 7, got " + T.item());

		T.removeItem();
		if( T.item() != 6 ) System.out.println("Expected root to be 6, got " + T.item());

		T.removeItem();
		if( T.item() != 5 ) System.out.println("Expected root to be 5, got " + T.item());
		
		T.removeItem();
		if( T.item() != 8 ) System.out.println("Expected root to be 8, got " + T.item());

		T.removeItem();
		if( T.item() != 3 ) System.out.println("Expected root to be 3, got " + T.item());

		T.removeItem();
		if( T.item() != 2 ) System.out.println("Expected root to be 2, got " + T.item());

		// Tree has one item.  Try parent() on one item.
		x = null;
		try {
			T.parent();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to parent of root.  Got none.");
		}
		
		
		// Try to go to the sibling
		x = null;
		try {
			T.goSibling();
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling when at the root.  Got none.");
		}
		
		
		
		T.removeItem(); 
		
		
		// Tree should now be empty
		if(!T.isEmpty()) System.out.println("Expected empty tree.  isEmpty() returned false.");

		if(T.capacity() != 10) System.out.println("Expected capacity to be 10, got "+ T.capacity());
		
		if(T.count() != 0  ) System.out.println("Expected tree count to be 0, got "+ T.count());
		
		// Remove from empty tree.
		x = null;
		try {
			T.removeItem();
		}
		catch(NoCurrentItem280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception deleting from an empty tree.  Got none.");
		}
		
		
		
		// Try to go to the sibling
		x = null;
		try {
			T.goSibling();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling in empty tree tree.  Got none.");
		}
		
		
		T.insert(1);
		T.root();
		
		// Try to go to the sibling when there is no child.
		x = null;
		try {
			T.goSibling();
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling of node with no sibling.  Got none.");
		}
		
		System.out.println("Regression test complete.");
	}

}
