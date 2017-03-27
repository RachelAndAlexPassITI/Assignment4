public class GenericLinkedStack<E> implements Stack<E> {

    // E is the type of the elements of this stack. The specific type
    // will specified when a reference is declared and a stack is
    // actually created. E.g.:
    //
    // Stack<Integer> nums;
    // nums = new GenericArrayStack<Integer>( 10 );

    // Instance variables
    private Elem<E> top; // keep track of top element
    private int size; 
    
    @SuppressWarnings( "unchecked" )

    private static class Elem<E>{
        private E value; //contains dot object or game model
        private Elem<E> next; //points to following game model version

        private Elem(E value, Elem<E> next){
            this.value = value;
            this.next = next; 
        }
    }

    // Constructor
    public GenericLinkedStack() {
	   top = null;
       size=0;
    }

    // Returns true if this ArrayStack is empty
    public boolean isEmpty() {
        return (top==null);
    }

    public void push( E elem ) {
        if (elem==null){
            throw new NullPointerException("Cannot stack null value.");
        }
        size++; 
        top = new Elem<E> (elem, top); // right side is executed before left
    }

    public E pop() {
        //shouldn't call pop() if object state is empty 
        //-> use an illegal state exception
        //this will activate anytime someone tries to pop() an empty stack
        /*
        if (isEmpty()){
            throw new EmptyStackException(stack); 
        }
        */

        E saved = top.value; //store value so we can return it later 
        top = top.next;
        size--; 
    	return saved;
    }

    public E peek() {
        if (top==null){
            throw new NullPointerException("Cannot peek null value.");
        }
        return top.value; 
    }

    public int getSize(){
        return size; 
    }

}
