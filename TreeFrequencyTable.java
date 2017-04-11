import java.util.NoSuchElementException;

/** Implements the interface <code>FrequencyTable</code> using a
 *  binary search tree.
 *
 * @author Marcel Turcotte (turcott@eecs.uottawa.ca)
 */

public class TreeFrequencyTable implements FrequencyTable {

    // Stores the elements of this binary search tree (frequency
    // table)
    
    private static class Elem {
    
        private String key;
        private long count;
    
        private Elem left;
        private Elem right;
    
        private Elem(String key) {
            this.key = key;
            this.count = 0;
            left = null;
            right = null;
        }
    }

    private Elem root = null; // A reference to the root element
    private int size = 0; // The size of the tree
    private LinkedList<String> keysList; //stores all the strings
    private long[] countList; //stores all the numbers 

    /** The size of the frequency table.
     *
     * @return the size of the frequency table
     */
    
    public int size() {
        return size;
    }
  
    /** Creates an entry in the frequency table and initializes its
     *  count to zero.
     *
     * @param key key with which the specified value is to be associated
     */
  
    public void init(String key) {
        if (key==null)
            throw new IllegalArgumentException("null in init");
        if (root==null)
            root=new Elem(key);

        boolean flag = false;
        Elem current = root; 
        
        while(!flag){            
            if (key.compareTo(current.key)<0){
                if (current.left==null){
                    current.left=new Elem(key);
                    flag=true; //element was placed, can stop iterating through tree
                }
                else 
                    current=current.left;
            }
            else if (key.compareTo(current.key)>0){
                if(current.right==null){
                    current.right=new Elem(key);
                    flag=true;
                }
                else
                    current=current.right;
            }
            //else
                //throw new IllegalArgumentException("already in table -> in init");
            
        } //end while loop
        size++;
    
    }
  
    /** The method updates the frequency associed with the key by one.
     *
     * @param key key with which the specified value is to be associated
     */
  
    public void update(String key) {
        Elem current=root;
        boolean flag=false;
        int counter=0;

        while(counter<size){
            if (key.compareTo(current.key)==0){
                current.count++; 
                flag=true;
            }
            else if (key.compareTo(current.key)<0)
                current=current.left;
            else
                current=current.right;
            

            counter++;
        }
        if (flag==false)
            throw new NoSuchElementException("not in table -> in update method");
    
    }
  
    /**
     * Looks up for key in this TreeFrequencyTable, returns associated value.
     *
     * @param key value to look for
     * @return value the value associated with this key
     * @throws NoSuchElementException if the key is not found
     */
  
    public long get(String key) {
        Elem current=root;
        int counter=0;

        while(counter<size){
            if (key.compareTo(current.key)==0){
                return current.count;
            }
            else if (key.compareTo(current.key)<0)
                current=current.left;
            else
                current=current.right;

            counter++;
        }

        throw new NoSuchElementException("not in table -> in get method");

    }
  
    /** Returns the list of keys in order, according to the method compareTo of the key
     *  objects.
     *
     *  @return the list of keys in order
     */

    public LinkedList<String> keys() {
        keysList = new LinkedList<String>(); 

        inOrder1(root);

        return keysList; 

    }

    public void inOrder1(Elem current){
        if (current!=null){
            inOrder1(current.left);
            keysList.addLast(current.key);
            inOrder1(current.right);
        }
    }

    /** Returns the values in the order specified by the method compareTo of the key
     *  objects.
     *
     *  @return the values
     */

    public long[] values() {
        int counter=0; 
        countList = new long[size];

        inOrder2(root,counter);

        return countList;

    }

    public void inOrder2(Elem current, int counter){
        if (current!=null){
            inOrder2(current.left, counter);
            countList[counter]=current.count;
            counter++;
            inOrder2(current.right,counter);
        }
    }

    /** Returns a String representation of the tree.
     *
     * @return a String representation of the tree.
     */

    public String toString() {
        return toString( root );
    }

    // Helper method.
  
    private String toString(Elem current) {
    
        if (current == null) {
            return "{}";
        }
    
        return "{" + toString(current.left) + "[" + current.key + "," + current.count + "]" + toString(current.right) + "}";
    }
  
}
