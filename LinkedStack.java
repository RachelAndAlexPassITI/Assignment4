/** Implements the interface <code>Stack</code> using linked elements.
 *
 *
 * @author  Marcel Turcotte (turcotte@eecs.uottawa.ca)
 */

public class LinkedStack<E> implements Stack<E> {

    // Objects of the class Elem are used to store the elements of the
    // stack.
    
    private static class Elem<T> {
        private T value;
        private Elem<T> next;

        private Elem(T value, Elem<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    // Reference to the top element
    
    private Elem<E> top;

    /** 
    *Returns <code>true</code> if this stack is empty, and
     * <code>false</code> otherwise.
     *
     * @return <code>true</code> if this stack is empty, and
     * <code>false</code> otherwise.
     */

    public boolean isEmpty() {
        return top == null;
    }

    /** 
    *Inserts an element onto the stack.
     *
     * @param value the element to be inserted
     */

    public void push(E value) {

	if (value == null) {
	    throw new NullPointerException();
	}
	
        top = new Elem<E>(value, top);
    }

    /** Returns the top element, without removing it.
     *
     * @return the top element
     */

    public E peek() {

	// pre-condition: the stack is not empty
	
        return top.value;
    }

    /** 
    *Removes and returns the top element.
     *
     * @return the top element
     */

    public E pop() {

	// pre-condition: the stack is not empty
	
        E saved = top.value;
        top = top.next;
        return saved;

    }

    /** Removes the top element of the stack. The element inserted at
     * the bottom of the stack.
     */

    public void roll() 
    {
        if(!isEmpty())
        {
            E temp1=pop(); //to save current state for use
            rollHelper(temp1);
        }
    }

    /** 
    *helper method for roll()
    * implements the recursive function
    * @param temp1 current element
    */
    private void rollHelper(E temp1)
    {   
        if(isEmpty())
        {
            push(temp1);
        }
        else
        {
            E first=pop();
            rollHelper(temp1);
            push(first);
        }
    }

    
    /** Removes the botttom element. The element is inserted on the
     * top of the stack.
     */

    E lastTail;
    public void unroll() 
    {
        lastTail=null;
        if(!isEmpty())
        {
            E temp=pop();
            if(!isEmpty())
            {
                push(temp);
                unrollHelper();
                push(lastTail);
            }
            else
            {
                push(temp);
            }

        }
	
    }

    /** 
    *helper method for unroll()
    * implements the recursive function
    */
    private void unrollHelper()
    {
        E current=pop();
        E temp=pop();
        if(isEmpty())
        {
            lastTail=temp;
            push(current);
        }
        else
        {
            push(temp);
            unrollHelper();
            push(current);
        }
        
    }

    /** 
    *Returns a string representation of the stack.
     *
     * @return a string representation
     */

    @Override public String toString() {
	StringBuffer stackStr = new StringBuffer("{");

	Elem<E> current = top;
	
	while (current != null) {
	    stackStr.append(current.value);
	    if (current.next != null) {
		stackStr.append(",");
	    }
	    current = current.next;
	}
	stackStr.append("}");

	return stackStr.toString();
    }
    
}
