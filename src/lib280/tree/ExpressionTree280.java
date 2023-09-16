package lib280.tree;
import java.util.StringTokenizer;

import lib280.dispenser.LinkedStack280;
import lib280.exception.InvalidState280Exception;
import lib280.list.LinkedList280;
import lib280.tree.BinaryNode280;
import lib280.tree.LinkedSimpleTree280;


public class ExpressionTree280 extends LinkedSimpleTree280<String> {

	/**
	 * Determine the priority of a token
	 * @param str A token from an expression
	 * @return Priority of the token.  ^ has priority 3, * and / have priority 2, + and - have priority 1.  All other tokens have priority 0.
	 */
	protected int priority( String str ) {
		char x = str.charAt(0);
		if( x == '^' ) return 3;
		if( x == '*' || x == '/' ) return 2;
		if( x == '+' || x == '-' ) return 1 ;
		return 0;
	}
	
	/**
	 * Determine if a string is an expression operand token
	 * @param str String to query
	 * @return true if 'str' is an operand (contains only digits and decimal points), false otherwise.
	 */
	protected boolean isOperand(String str) {
		
		// An operand can only consist of positive floating point values, so all charaters must be 
		// either digits between 0 and 1 or '.'
		for(int i=0; i < str.length(); i++)
			if( !Character.isDigit(str.charAt(i)) && str.charAt(i) != '.' ) return false;
		
		return true;
	}
	
	/**
	 * Determine if a string is an expression operator token
	 * @param str String to query
	 * @return true if 'str' is one of the single characters +,-,*,/ or ^, false otherwise.
	 */
	protected boolean isOperator(String str) {	
		// An operator is only one character long and is one of the characters +, -, /, *, or ^.
		char x = str.charAt(0);
		return ( x == '+' || x == '-' || x == '*' || x == '/' || x == '^') && str.length() == 1;
	}
	
	/**
	 * Determine if a string is an opening parenthesis expression token.
	 * @param str String to query
	 * @return true if 'str' is equal to "(".  False otherwise.
	 */
	protected boolean isOpenParen(String str) {
		char x = str.charAt(0);
		return x == '(' && str.length() == 1;
	}
	
	/**
	 * Determine if a string is an closing parenthesis expression token.
	 * @param str String to query
	 * @return true if 'str' is equal to ")".  False otherwise.
	 */
	protected boolean isCloseParen(String str) {
		char x = str.charAt(0);
		return x == ')' && str.length() == 1;
	}
	
	
	/**
	 * Construct a new expression tree.
	 * @param expr Infix expression from which to construct an expression tree.  All operators, operands and parentheses are assumed to be separated by one or more spaces.
	 * @throws InvalidState280Exception when an invalid expression is detected.
	 */
	public ExpressionTree280( String expr )  throws InvalidState280Exception {
		// Initialize superclass
		super();
		
		// Convert infix expression to expression tree. 
		
		
		LinkedStack280< String > tokenStack = new LinkedStack280<String>();   // Stack to hold infix expression tokens awaiting processing
		LinkedList280<String> postFix = new LinkedList280<String>();          // List to hold postfix expression tokens as they are output.
		
		StringTokenizer st = new StringTokenizer(expr, " ");
		
		// First convert infix to postfix.
		
		while(st.hasMoreTokens()) {
			String curToken = st.nextToken();
			//System.out.println(S);
			//System.out.println(postFix);
			//System.out.println("Processing token: " + curToken);
			
			// If token is an operand, it goes on the stack to await processing by an operator.
			if( this.isOperand(curToken) ) {
				postFix.insertLast(curToken);
			}
			
			// If the token is an opening parenthesis, it gets pushed.
			else if(this.isOpenParen(curToken) ) {
				tokenStack.insert(curToken);
			}
			
			// If the token is a closing parenthesis, 
			else if( this.isCloseParen(curToken) ) {
				
				// We pop everything from the stack until we encounter the matching opening parenthesis.  As we pop tokens, add them to the
				// postfix expression output.
				// If the stack becomes empty before we end this process, then there are too many )'s.
				
				// If the stack is already empty, then the parentheses are unbalanced.
				if( tokenStack.isEmpty() ) throw new InvalidState280Exception("Experssion error:  to many )'s.");
				
				// Pop items and append them to the postfix expression until we find a '(' on the stack.
				while(!this.isOpenParen(tokenStack.item()) ) {
					postFix.insertLast(tokenStack.item());  // Add the top token to the output postfix string.
					tokenStack.deleteItem();                // pop the token.
					
					// If the stack is now empty, then the parentheses are unbalanced.
					if( tokenStack.isEmpty() ) throw new InvalidState280Exception("Experssion error:  to many )'s.");
				}
				tokenStack.deleteItem();  // pop off the matching paren
			}
			
			// If the token is an operator
			else if( this.isOperator(curToken) ) {
				if(tokenStack.isEmpty() ) tokenStack.insert(curToken);
				else {
					// As long the item on top of the stack is of higher priority than the current token,
					// pop it and add it to the postfix expression.
					while( tokenStack.itemExists() && this.priority(tokenStack.item()) >= this.priority(curToken)) {
						postFix.insertLast(tokenStack.item());
						tokenStack.deleteItem();
					}
					
					// Push the current token to the top of the stack.
					tokenStack.insert(curToken);
				}
			}
			
			// Otherwise, the token is unrecognized, and illegal.
			else throw new InvalidState280Exception("Invalid token: " + curToken);
			
		}
		
		// We've now reached the end of the infix expression.  Pop each item off the stack and add it to the postfix expression in turn.
		while( !tokenStack.isEmpty() ) {
			// If we find an opening parenthesis during this process, then the parentheses are unbalanced.
			if( this.isOpenParen(tokenStack.item()) ) throw new InvalidState280Exception("Expression error:  too may ('s. ");
			postFix.insertLast(tokenStack.item());
			tokenStack.deleteItem();
		}
		

		// Now, we are going to construct the expression tree from the postfix expression.
		// Move the cursor to the start of the postfix token list and start iterating.
		// During this process, we use a different stack, called a token stack.
		postFix.goFirst();
		
		LinkedStack280<BinaryNode280<String>> nodeStack = new LinkedStack280<BinaryNode280<String>>();
		
		while( postFix.itemExists() ) {
			String curToken = postFix.item();
			
			// If the next postfix token is an operand, stick it in a node and put it on the stack
			if( this.isOperand(curToken) ) {
				BinaryNode280<String> operandNode = new BinaryNode280<String>(curToken);
				
				// Operands are always leaf nodes.
				operandNode.setLeftNode(null);
				operandNode.setRightNode(null);
				
				// Push to stack.
				nodeStack.insert(operandNode);
				
			}
			else {
				// Otherwise, it's an operator, and we need to make a new node containing the operator,
				// and make it's children the top 2 nodes on the stack.
				// If our initial infix expression was legal we should not have any stack underflow problems here.
				
				// Pop two operands
				BinaryNode280<String> operand2 = nodeStack.item();
				nodeStack.deleteItem();
				BinaryNode280<String> operand1 = nodeStack.item();
				nodeStack.deleteItem();
				
				// Make a new node that contains the current token, with the two popped nodes its children.
				BinaryNode280<String> operatorNode = new BinaryNode280<String>(curToken);
				operatorNode.setLeftNode(operand1);
				operatorNode.setRightNode(operand2);
				
				// Put the new node back on the stack.
				nodeStack.insert(operatorNode);
			}
			
			// Advance to process the next token in the postfix expression.
			postFix.goForth();
		}
		
		// Top node on the stack is now the root of the expression tree, so make it the root node for this expression tree.
		this.rootNode = nodeStack.item();
	}
	
	
	protected double evaluate(BinaryNode280<String> root) throws InvalidState280Exception {
		
		if( root == null ) throw new InvalidState280Exception("Error: invalid tree.  There is a leaf node that contains somethign other than an operand.");
		
		// If this is a leaf (an operand) return its value. 
		if( this.isOperand(root.item()) ) return Double.valueOf(root.item()).doubleValue();
				
		// Otherwise, find the values of the subtrees and perform the operator at this node.
		if( !this.isOperator(root.item()) ) throw new InvalidState280Exception("Error: invalid tree.  There is an internal node that contains something other than an operand.");  
		
		// Process left, process right...
		double leftOperand = evaluate(root.leftNode());
		double rightOperand = evaluate(root.rightNode());
		
		// Process root.
		switch(root.item().charAt(0)) {
		case '+':
			return leftOperand + rightOperand;
		case '-':
			return leftOperand - rightOperand;			
		case '/':
			return leftOperand / rightOperand;
		case '*':
			return leftOperand * rightOperand;
		case '^':
			return Math.pow(leftOperand, rightOperand);
		
		default:
			throw new InvalidState280Exception("Error: invalid operand.  This should never happen because we already checked if root.item() was a valid operand.");
		}
		
	}
	
	
	public double evaluate() {
		// Do a postorder traversal to evaluate the tree.		
		return this.evaluate(this.rootNode);		
	}
	
	
	protected String toInfixExpression(BinaryNode280<String> root) {
		String result = "";
		
		// Process left subtree
		if( root.leftNode() != null ) result += " ( " + toInfixExpression(root.leftNode());
		
		// Process root
		result += " " + root.item() + " ";
		
		// Process right subtree
		if( root.rightNode() != null ) result += toInfixExpression(root.rightNode()) + " ) ";
				
		return result;
	}
	
	public String toInfixExpression() {
		// do an inorder traversal to print infix expression
		return this.toInfixExpression(this.rootNode);
	}
	
	
	public String toPrefixExpression(BinaryNode280<String> root) {
		String result = "";

		// Process root
		result += " " + root.item() + " ";
		
		// Process left subtree
		if( root.leftNode() != null ) result += toPrefixExpression(root.leftNode());
		
		// Process right subtree
		if( root.rightNode() != null ) result += toPrefixExpression(root.rightNode());
				
		return result;
	}
	
	public String toPrefixExpression() {
		// do an preorder traversal to print prefix expression
		return this.toPrefixExpression(this.rootNode);
	}
	
	
	public String toPostfixExpression(BinaryNode280<String> root) {
		String result = "";

		// Process left subtree
		if( root.leftNode() != null ) result += toPostfixExpression(root.leftNode());
		
		// Process right subtree
		if( root.rightNode() != null ) result += toPostfixExpression(root.rightNode());

		// Process root
		result += " " + root.item() + " ";
		

		return result;
	}
	
	public String toPostfixExpression() {
		// do an preorder traversal to print prefix expression
		return this.toPostfixExpression(this.rootNode);
	}
	
	

	
	public static void main(String args[]) {
		ExpressionTree280 E = new ExpressionTree280("1 + 2 + 3");
		
//		System.out.println("-----------------------------------------------");
//
//		System.out.println("Original expression: 1 + 2 + 3");
//		System.out.println("Infix from tree:    " + E.toInfixExpression());
//		System.out.println("Prefix from tree:   " + E.toPrefixExpression());
//		System.out.println("Postfix from tree:  " + E.toPostfixExpression());
//		System.out.printf("Expression value = %f\n", E.evaluate());
//		System.out.println("The tree: \n");
//		System.out.println(E.toStringByLevel());
//		System.out.println("-----------------------------------------------");
//		
//
//		E = new ExpressionTree280("1 + ( 2 + 3 )");
//		System.out.println("Original expression: 1 + ( 2 + 3 )");
//		System.out.println("Infix from tree:    " + E.toInfixExpression());
//		System.out.println("Prefix from tree:   " + E.toPrefixExpression());
//		System.out.println("Postfix from tree:  " + E.toPostfixExpression());
//		System.out.printf("Expression value = %f\n", E.evaluate());
//		System.out.println("The tree: \n");
//		System.out.println(E.toStringByLevel());
//		System.out.println("-----------------------------------------------");
//
//
//		E = new ExpressionTree280("1 * 2 + 3");
//		System.out.println("Original expression: 1 * 2 + 3");
//		System.out.println("Infix from tree:    " + E.toInfixExpression());
//		System.out.println("Prefix from tree:   " + E.toPrefixExpression());
//		System.out.println("Postfix from tree:  " + E.toPostfixExpression());
//		System.out.printf("Expression value = %f\n", E.evaluate());
//		System.out.println("The tree: \n");
//		System.out.println(E.toStringByLevel());
//		System.out.println("-----------------------------------------------");
//
//		
//		E = new ExpressionTree280("1 * ( 2 + 3 )");
//		System.out.println("Original expression: 1 * ( 2 + 3 )");
//		System.out.println("Infix from tree:    " + E.toInfixExpression());
//		System.out.println("Prefix from tree:   " + E.toPrefixExpression());
//		System.out.println("Postfix from tree:  " + E.toPostfixExpression());
//		System.out.printf("Expression value = %f\n", E.evaluate());
//		System.out.println("The tree: \n");
//		System.out.println(E.toStringByLevel());
//		System.out.println("-----------------------------------------------");
//
//		
//		
//		E = new ExpressionTree280("1.5 * 2 + 3 ^ 7");
//		System.out.println("Original expression: 1.5 * 2 + 3 ^ 7");
//		System.out.println("Infix from tree:    " + E.toInfixExpression());
//		System.out.println("Prefix from tree:   " + E.toPrefixExpression());
//		System.out.println("Postfix from tree:  " + E.toPostfixExpression());
//		System.out.printf("Expression value = %f\n", E.evaluate());
//		System.out.println("The tree: \n");
//		System.out.println(E.toStringByLevel());
//		System.out.println("-----------------------------------------------");
//
//
//		
//		E = new ExpressionTree280("1.5 / 2 - 3 ^ 3");
//		System.out.println("Original expression: 1.5 / 2 - 3 ^ 3");
//		System.out.println("Infix from tree:    " + E.toInfixExpression());
//		System.out.println("Prefix from tree:   " + E.toPrefixExpression());
//		System.out.println("Postfix from tree:  " + E.toPostfixExpression());
//		System.out.printf("Expression value = %f\n", E.evaluate());
//		System.out.println("The tree: \n");
//		System.out.println(E.toStringByLevel());
//		System.out.println("-----------------------------------------------");
//		
//		E = new ExpressionTree280("1.5 / ( 2 - 3 ) ^ 3");
//		System.out.println("Original expression: 1.5 / ( 2 - 3 ) ^ 3");
//		System.out.println("Infix from tree:    " + E.toInfixExpression());
//		System.out.println("Prefix from tree:   " + E.toPrefixExpression());
//		System.out.println("Postfix from tree:  " + E.toPostfixExpression());
//		System.out.printf("Expression value = %f\n", E.evaluate());
//		System.out.println("The tree: \n");
//		System.out.println(E.toStringByLevel());
//		System.out.println("-----------------------------------------------");
//				
//		
		E = new ExpressionTree280("3 + 5 * 8");
		System.out.println("Original expression: 3 + 5 * 8");
		System.out.println("Infix from tree:    " + E.toInfixExpression());
		System.out.println("Prefix from tree:   " + E.toPrefixExpression());
		System.out.println("Postfix from tree:  " + E.toPostfixExpression());
		System.out.printf("Expression value = %f\n", E.evaluate());
		System.out.println("The tree: \n");
		System.out.println(E.toStringByLevel());
		System.out.println("-----------------------------------------------");
					

		}
	
	

}
