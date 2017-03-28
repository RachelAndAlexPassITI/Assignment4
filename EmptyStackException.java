public class EmptyStackException extends IllegalStateException {
	//private Stack<E> stack;

	/**S
	*constructor for EmptyStackException
	*/
	public EmptyStackException(){
		super("Cannot remove from empty stack."); 
	}

/*
	public Stack<E> getStack(){
		return stack; 
	}
	**/
}