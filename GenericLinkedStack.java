import java.io.*;

public class GenericLinkedStack<E> implements Stack<E>, Serializable {

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

    private static class Elem<E> implements Serializable{
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

    /**
     * Method to check whether stack is empty. 
     * 
     * @return true or false
     */ 
    public boolean isEmpty() {
        return (top==null);
    }

    /**
     * Pushes an element onto the stack
     * 
     */ 
    public void push( E elem ) {
        if (elem==null){
            throw new NullPointerException("Cannot stack null value.");
        }
        size++; 
        top = new Elem<E> (elem, top); // right side is executed before left
    }

    /**
     * Takes element from stack. 
     * 
     * @return saved
     */  
    public E pop() {
        //shouldn't call pop() if object state is empty 
        //-> use an illegal state exception
        //this will activate anytime someone tries to pop() an empty stack
        
        if (isEmpty()){
            throw new EmptyStackException(); 
        }

        E saved = top.value; //store value so we can return it later 
        top = top.next;
        size--; 
    	return saved;
    }

    /**
     * Peek the top element on the stack
     * 
     * @return top.value
     */ 
    public E peek() {
        if (top==null){
            throw new NullPointerException("Cannot peek null value.");
        }
        return top.value; 
    }

    /**
     * Getter method for the size of the stack
     * 
     * @return size
     */ 
    public int getSize(){
        return size; 
    }

}
