package lib280.tree;
import lib280.base.Container280;
import lib280.base.Cursor280;

public interface ArrayedTree280<I> extends Cursor280<I>, Container280  {
	/**
	 * Add an item to the tree.
	 * @param item The item to be added.
	 * @precond The tree must not be full.
	 */
	public void insert(I item);
	
	/**
	 * Set the cursor position to the parent of the current item.
	 * @precond Tree cannot be empty and the current node must not be the root.
	 */
	public void parent();						// Set cursor position to parent of current node.
	
	/**
	 * Set the cursor position to the left child of the current item.
  	 * @precond The tree must not be empty and the current node must have a left child.
	 * 
	 */
	public void goLeftChild();					// Set cursor position to left child of current node.
	
	/**
	 * set the cursor position to the right child of the current item.
	 * @precond The tree must not be empty and the current node must have a right child.
	 */
	public void goRightChild();					// Set cursor position to right child of current node.
	
	/** 
	 * Set the cursor position to the sibling of the current item. 
	 * @precond The current node must have a sibling.  The tree must not be empty.
	 */
	public void goSibling();
	
	/**
	 * Set the cursor position to the root item.
	 * @precond The tree must not be empty.
	 */
	public void root();							// Set cursor position to root node.
	
	/**
	 * Remove the item at the cursor from the tree.
	 * @precond There must be an item at the cursor.  The tree must not be empty.
	 */
	public void removeItem();					// Remove the item in the current node.
}
