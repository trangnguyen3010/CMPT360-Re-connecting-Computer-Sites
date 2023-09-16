package lib280.list;

 


import lib280.exception.*;
import lib280.base.*;

/**	A LinkedIterator which has functions to move forward and back, 
	and to the first and last items of the list.  It keeps track of 
	the current item, and also has functions to determine if it is 
	before the start or after the end of the list. */
public class BilinkedIterator280<I> extends LinkedIterator280<I> implements BilinearIterator280<I>
{

	/**	Constructor creates a new iterator for list 'list'. <br>
		Analysis : Time = O(1) 
		@param list list to be iterated */
	public BilinkedIterator280(BilinkedList280<I> list)
	{
		super(list);
	}

	/**	Create a new iterator at a specific position in the newList. <br>
		Analysis : Time = O(1)
		@param newList list to be iterated
		@param initialPrev the previous node for the initial position
		@param initialCur the current node for the initial position */
	public BilinkedIterator280(BilinkedList280<I> newList, 
			LinkedNode280<I> initialPrev, LinkedNode280<I> initialCur)
	{
		super(newList, initialPrev, initialCur);
	}
    
	/**	Go to the last item of the data structure. <br> 
		Analysis: Time = O(1) */
	public void  goLast()
	{
		cur = ((BilinkedList280<I>) list).lastNode();
		if (cur==null)
			prev = null;
		else
			prev = ((BilinkedNode280<I>) cur).previousNode();
	}

	/**	Move back one item in the data structure. <br> 
		Analysis: Time = O(1) <br>
		PRECONDITION:  <br>
		<ul>
			!before() 
		</ul>  */
	public void goBack() throws BeforeTheStart280Exception
	{
		if (before())
			throw new BeforeTheStart280Exception("Cannot move back since already before().");
		
		if (after())
			goLast();
		else
		{
			cur = ((BilinkedNode280<I>) cur).previousNode();
			if (cur!=null)
				prev = ((BilinkedNode280<I>) cur).previousNode();
		}
	 }

	/**	A shallow clone of this object. <br> 
	Analysis: Time = O(1) */
	public BilinkedIterator280<I> clone()
	{
		return (BilinkedIterator280<I>) super.clone();
	}

	/**	A unique class identifier for serializing and deserializing. */
	static final long serialVersionUID = 1l;
	  
} 
