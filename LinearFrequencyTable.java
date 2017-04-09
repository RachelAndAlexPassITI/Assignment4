import java.util.NoSuchElementException;

/** Implements the interface <code>FrequencyTable</code> using linked
 *  elements. The linked structure is circular and uses a dummy node.
 *
 * @author Marcel Turcotte (turcott@eecs.uottawa.ca)
 */

public class LinearFrequencyTable implements FrequencyTable {

    // Linked elements

    private static class Elem {

    private String key; //string of ACGT
    private long count; //count associated with each key
    private Elem previous; //key is linked to previous element
    private Elem next; //key is linked to next element

    private Elem(String key, Elem previous, Elem next) {
        this.key = key;
        this.count = 0;
        this.previous = previous;
        this.next = next;
    }

    }

    private Elem head; //keep track of first element
    private int size; //keep track of size of table

    /** Constructs an empty <strong>FrequencyTable</strong>.
     */

    public LinearFrequencyTable() {
	    head = new Elem(null, null, null); // dummy node
	    head.previous = head; // making the dummy node circular
	    head.next = head; // making the dummy node circular
	    size = 0; //initially set list size to 0 
    }

    /** The size of the frequency table.
     *
     * @return the size of the frequency table
     */

    public int size() {
    	return size;
    }
  
    /** Returns the frequency value (elem.count) associated with this key.
     *
     *  @param key key whose frequency value is to be returned
     *  @return the frequency associated with this key
     *  @throws NoSuchElementException if the key is not found
     */

    public long get(String key) {
    	Elem iterate = head;
    	while(iterate.next!=head){
    		if(iterate.next.key.compareTo(key)==0){
    			return iterate.count; 
    		}
    		iterate=iterate.next;
    	}
    	throw new NoSuchElementException("key not in table"); 
    }

    /** Creates an entry in the frequency table and initializes its
     *  count to zero. The keys are kept in order (according to their
     *  method <strong>compareTo</strong>).
     *
     *  @param key key with which the specified value is to be associated
     *  @throws IllegalArgumentException if the key was already present
     */

    public void init(String key) { //basically an 'addLast' method
    	if (key==null)
    		throw new IllegalArgumentException("null in init");

    	//dummy proof for key already in table
    	Elem goThru = head; //point to dummy element
    	
    	while(goThru.next!=head){
    		if(goThru.next.key.compareTo(key)==0)
    			throw new IllegalArgumentException("already in table");
    		goThru=goThru.next;
    	}

    	if (head.next == head){ //only the dummy node is in the list; points to itself
			head.next = new Elem(key, head, head.next); //head.next refers to head rn
			head.previous = head.next; //circular; head.prev points to newly made Elem
    	}
    	else{
    		Elem iterate = head; //point to dummy element
    		while(iterate.next != head && iterate.next.key.compareTo(key) < 0){  //haven't circled around AND next value is less than key
    			iterate=iterate.next; //go through element by element
    		}

    		Elem following = iterate.next; //node that follows

    		iterate.next = new Elem(key, iterate, following); 
    		following.previous = iterate.next;
    	}

    	size++; //increment size 
    	
    	//throw new UnsupportedOperationException("IMPLEMENT THIS METHOD");

    }

    /** The method updates the frequency associated with the key by one.
     *
     *  @param key key with which the specified value is to be associated
     *  @throws NoSuchElementException if the key is not found
     */

    public void update(String key) {
    
    	Elem iterate = head; //point to dummy element
		while(iterate.next != head && iterate.next.key.compareTo(key)!= 0){  //haven't circled around AND value isn't equal to key
			iterate=iterate.next; //go through element by element
		}

		if (iterate.next==head)
			throw new NoSuchElementException("element not in list"); 
		else
			iterate.next.count++; 


   		// throw new UnsupportedOperationException("IMPLEMENT THIS METHOD");

    }

    /** Returns the list of keys in order, according to the method
     *  <strong>compareTo</strong> of the key objects.
     *
     *  @return the list of keys in order
     */

    public LinkedList<String> keys() {
    	LinkedList<String> keysList = new LinkedList<String>(); 
	  
	    Elem iterate = head; //point to dummy element
    		while(iterate.next != head /*&& iterate.next.key.compareTo(key) < 0*/){  //haven't circled around AND next value is less than key
    			keysList.addLast(iterate.next.key); //add each count to list (should already be in order)
    			iterate=iterate.next; //go through element by element
    		}
    	return keysList;
    }

    /** Returns an array containing the frequencies of the keys in the
     *  order specified by the method <strong>compareTo</strong> of
     *  the key objects.
     *
     *  @return an array of frequency counts
     */

    public long[] values() {
    	long[] counts = new long[size]; 
    	int counter=0;
    	
    	Elem iterate = head; //point to dummy element
    		while(iterate.next != head){  //haven't circled entire way around 
    			counts[counter]=iterate.next.count; //add each count to list (should already be in order)
    			iterate=iterate.next; //go through element by element
    			counter++; 
    		}
    	return counts;
    	
    	//throw new UnsupportedOperationException("IMPLEMENT THIS METHOD");

    }

    /** Returns a <code>String</code> representations of the elements
     * of the frequency table.
     *  
     *  @return the string representation
     */

    public String toString() {

    StringBuffer str = new StringBuffer("{");
    Elem p = head.next;

    while (p != head) {
        str.append("{key="+p.key+", count="+p.count+"}");
        if (p.next != head) {
        str.append(",");
        }
        p = p.next;
    }
    str.append("}");
    return str.toString();
    }

}
