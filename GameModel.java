import java.util.Random;
import java.io.*;

/**
 * The class <b>GameModel</b> holds the model, the state of the systems. 
 * It stores the followiung information:
 * - the state of all the ``dots'' on the board (color, captured or not)
 * - the size of the board
 * - the number of steps since the last reset
 * - the current color of selection
 *
 * The model provides all of this informations to the other classes trough 
 *  appropriate Getters. 
 * The controller can also update the model through Setters.
 * Finally, the model is also in charge of initializing the game
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */
public class GameModel implements Cloneable, Serializable{

    /**
    * the current settings
    */
    private boolean diagonal;
    private boolean torus;
    private boolean plane;
    private boolean orthogonal;
    public boolean initialDot;

    /**
     * predefined values to capture the color of a DotInfo
     */
    public static final int COLOR_0           = 0;
    public static final int COLOR_1           = 1;
    public static final int COLOR_2           = 2;
    public static final int COLOR_3           = 3;
    public static final int COLOR_4           = 4;
    public static final int COLOR_5           = 5;
    public static final int NUMBER_OF_COLORS  = 6;

    /**
    * The stacks of former gamemodels
    **/
    public GenericLinkedStack<GameModel> undoStack;
    public GenericLinkedStack<GameModel> redoStack;

    /**
     * The current selection color
     */
	private int currentSelectedColor;

    

    /**
     * The size of the game.
     */
    private  int sizeOfGame;
 
    /**
     * A 2 dimentionnal array of sizeOfGame*sizeOfGame recording the state of each dot
     */
	private DotInfo[][] model;


   /**
     * The number of steps played since the last reset
     */
	private int numberOfSteps;
 
   /**
     * The number of captured dots
     */
    private int numberCaptured;

   /**
     * Random generator
     */
	private Random generator;



    /**
     * Constructor to initialize the model to a given size of board.
     * 
     * @param size
     *            the size of the board
     */
    public GameModel(int size) {
        generator = new Random();
        sizeOfGame = size;
        reset();
    }


    /**
     * Resets the model to (re)start a game. The previous game (if there is one)
     * is cleared up . 
     */
    public void reset(){

    	model = new DotInfo[sizeOfGame][sizeOfGame];
        undoStack=new GenericLinkedStack<GameModel>();
        redoStack=new GenericLinkedStack<GameModel>();

    	for(int i = 0; i < sizeOfGame; i++){
		   	for(int j = 0; j < sizeOfGame; j++){
    			model[i][j] = new DotInfo(i,j,generator.nextInt(NUMBER_OF_COLORS));
    		}
    	}

    	// initially, the top left DotInfo is captured
        initialDot=false;        

    	numberOfSteps = 0;
        numberCaptured = 0;
        diagonal=false;
        torus=false;
        plane=true;
        orthogonal=true;
    }

    /**
     * Setter method for diagonal
     * 
     * @param b
     *            the new value for diagonal
    */  
    public void setDiagonal(boolean b)
    {
        diagonal=b;
    }

    /**
     * Setter method for torus
     * 
     * @param b
     *            the new value for torus
    */  
    public void setTorus(boolean b)
    {
        torus=b;
    }

    /**
     * Setter method for plane
     * 
     * @param b
     *            the new value for plane
    */  
    public void setPlane(boolean b)
    {
        plane=b;
    }

    public void setOrthogonal(boolean b)
    {
        orthogonal=b;
    }

    /**
     * Getter method for the plane setting
     * 
     * @return the value of the attribute plane
     */  
    public boolean getPlane()
    {
        return plane;
    }

    /**
     * Getter method for the orthogonal setting
     * 
     * @return the value of the attribute orthogonal
     */  
    public boolean getOrthogonal()
    {
        return orthogonal;
    }

    /**
     * Getter method for the diagonal setting
     * 
     * @return the value of the attribute diagonal
     */  
    public boolean getDiagonal()
    {
        return diagonal;
    }

    /**
     * Getter method for the torus setting
     * 
     * @return the value of the attribute torus
     */  
    public boolean getTorus()
    {
        return torus;
    }


    /**
     * Getter method for the size of the game
     * 
     * @return the value of the attribute sizeOfGame
     */   
    public int getSize(){
        return sizeOfGame;
    }

    /**
     * returns the color  of a given dot in the game
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public int getColor(int i, int j){
        if(isCaptured(i, j)) {
            return currentSelectedColor;
        } else {
    	   return model[i][j].getColor();
        }
    }

    /**
     * returns true is the dot is captured, false otherwise
    * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     * @return the status of the dot at location (i,j)
     */   
    public boolean isCaptured(int i, int j){
        return model[i][j].isCaptured();
    }

    /**
     * Sets the status of the dot at coordinate (i,j) to captured
     * 
     * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     */   
    public void capture(int i, int j){
 		model[i][j].setCaptured(true);
        if(initialDot==false)
        {
            initialDot=true;
            currentSelectedColor=model[i][j].getColor();
        }
        numberCaptured++;
    }


    /**
     * Getter method for the current number of steps
     * 
     * @return the current number of steps
     */   
    public int getNumberOfSteps(){
    	return numberOfSteps;
    }

    /**
     * Setter method for currentSelectedColor
     * 
     * @param val
     *            the new value for currentSelectedColor
    */   
    public void setCurrentSelectedColor(int val) {
        currentSelectedColor = val;
    }

    /**
     * Getter method for currentSelectedColor
     * 
     * @return currentSelectedColor
     */   
    public int getCurrentSelectedColor() {
        return currentSelectedColor ;
    }


    /**
     * Getter method for the model's dotInfo reference
     * at location (i,j)
     *
      * @param i
     *            the x coordinate of the dot
     * @param j
     *            the y coordinate of the dot
     *
     * @return model[i][j]
     */   
    public DotInfo get(int i, int j) {
        return model[i][j];
    }

    /**
    *Setter method for model's dotInfo reference at (i,j)
    * @param i
    *   the x coordinate of the dot
    * @param j
    *   the y coordinate of the dot
    *@param dotInfo
    *   the new dotInfo
    */
    public void set(int i,int j,DotInfo dotInfo)
    {
        model[i][j]=dotInfo;
    }


   /**
     * The metod <b>step</b> updates the number of steps. It must be called 
     * once the model has been updated after the payer selected a new color.
     */
     public void step(){
        numberOfSteps++;
    }
 
   /**
     * The metod <b>isFinished</b> returns true iff the game is finished, that
     * is, all the dats are captured.
     *
     * @return true if the game is finished, false otherwise
     */
    public boolean isFinished(){
        return numberCaptured == sizeOfGame*sizeOfGame;
    }


   /**
     * Builds a String representation of the model
     *
     * @return String representation of the model
     */
    public String toString(){
        StringBuffer b = new StringBuffer();
        for(int i = 0; i < sizeOfGame; i++){
            for(int j = 0; j < sizeOfGame; j++){
                b.append(getColor(i, j) + " ");
            }
            b.append("\n");
        }
        return b.toString();
    }

    /**
     * Setter method for initialDot
     * 
     * @param b
     *            the new value for initialDot
    */
    public void setInitialDot(boolean b)
    {
        initialDot=b;
    }

    /**
     * Setter method for numberCaptured
     * 
     * @param i
     *            the new value for numberCaptured
    */
    public void setNumberCaptured(int i)
    {
        numberCaptured=i;
    }

    /**
     * Setter method for numberOfSteps
     * 
     * @param i
     *            the new value for numberOfSteps
    */
    public void setNumberOfSteps(int i)
    {
        numberOfSteps=i;
    }

    /**
     * Getter method for numberCaptured
     * 
     * @return numberCaptured
     */  
    public int getNumberCaptured()
    {
        return numberCaptured;
    }

    /**
    * override of method clone() from interface cloneable
    * creates deep copy of object
    * @return deep copy of object
    */
    @Override
    public Object clone() 
    {
    
            GameModel cloned=new GameModel(sizeOfGame);
            DotInfo[][] cloneModel=new DotInfo[sizeOfGame][sizeOfGame];

            if(this.diagonal)
            {
                cloned.setDiagonal(true);
            }
            if(this.torus)
            {
                cloned.setTorus(true);
            }
            if(this.plane)
            {
                cloned.setTorus(true);
            }
            if(this.orthogonal)
            {
                cloned.setOrthogonal(true);
            }
            if(!this.orthogonal)
            {
                cloned.setOrthogonal(false);
            }
            if(!this.plane)
            {
                cloned.setPlane(false);
            }
            if(this.initialDot)
            {
                cloned.setInitialDot(true);
            }
            cloned.setCurrentSelectedColor(this.getCurrentSelectedColor());
            cloned.setNumberOfSteps(this.getNumberOfSteps());
            cloned.setNumberCaptured(this.getNumberCaptured());
            for(int x=0; x<sizeOfGame; x++)
            {
                for(int y=0; y<sizeOfGame; y++)
                {                        
                    cloned.set(x,y,new DotInfo(x,y,getColor(x,y)));
                    if(isCaptured(x,y))
                    {
                        cloned.capture(x,y);
                    }
                }
            }

            cloned.undoStack=undoStack;
            cloned.redoStack=redoStack;

            System.out.println("Cloned: "+cloned.undoStack.getSize());
            System.out.println("Original: "+undoStack.getSize());

            return cloned;
        

    }
}
