public class EmptyStackException extends IllegalStateException {
	//private Stack<E> stack;

	/**S
	*constructor for EmptyStackException
	*/
	public EmptyStackException(){
		super("Cannot remove from empty stack."); 
		this.stack = stack; 
	}

/*
	public Stack<E> getStack(){
		return stack; 
	}
	**
}