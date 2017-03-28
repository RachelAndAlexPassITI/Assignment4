import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.nio.*; 

/**
 * The class <b>GameController</b> is the controller of the game. It has a method
 * <b>selectColor</b> which is called by the view when the player selects the next
 * color. It then computesthe next step of the game, and  updates model and view.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */


public class GameController implements ActionListener {

    GenericLinkedStack<GameModel> undoStack;
    GenericLinkedStack<GameModel> redoStack;
    /**
     * Reference to the view of the board
     */
    private GameView gameView;
    /**
     * Reference to the model of the game
     */
    private GameModel gameModel;
 
    /**
     * Constructor used for initializing the controller. It creates the game's view 
     * and the game's model instances
     * 
     * @param size
     *            the size of the board on which the game will be played
     */
    public GameController(int size) {
        String fileName = "savedGame.ser"; 
        try
        {
           ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
           gameModel = (GameModel) is.readObject();
           is.close(); 
            
        }
        /*
        catch (FileNotFoundException f)
        {
            gameModel = new GameModel(size);
            try{
                Files.delete(savedGame.ser); 
            }
            catch(IOException g){
                System.out.println("IOException in GameController."); 
            }
        }
        */
        
        catch (IOException f){
            gameModel = new GameModel(size);
        }

        catch (ClassNotFoundException f){
            System.out.println("Class not found in GameController."); 
            gameModel = new GameModel(size); 
        }
        
        undoStack=new GenericLinkedStack<GameModel>();
        redoStack=new GenericLinkedStack<GameModel>();
        flood(); 
        gameView = new GameView(gameModel, this);
        gameView.update(); 

    }

    /**
     * resets the game
     */
    public void reset(){
        gameModel.reset();
        flood();
        gameView.update();
    }

    /**
     * Callback used when the user clicks a button (reset or quit)
     *
     * @param e
     *            the ActionEvent
     */

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof DotButton) {
            
            if(gameModel.initialDot==false)
            {
                gameModel.capture(((DotButton)(e.getSource())).getRow(), ((DotButton)(e.getSource())).getColumn());
                undoStack.push((GameModel)gameModel.clone());
                gameView.update();
            }

            else
            {
                selectColor(((DotButton)(e.getSource())).getColor());
            }
        } else if (e.getSource() instanceof JButton) {
            JButton clicked = (JButton)(e.getSource());

            if (clicked.getText().equals("Quit")) {
                try{
                    ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("savedGame.ser"));
                    os.writeObject(gameModel); 
                    os.close(); 
                }
                catch(IOException f){
                    System.out.println("IO exception but exiting because fuck you"); 
                    System.exit(0); 
                }
                
             } else if (clicked.getText().equals("Reset")){
                reset();
             }
             else if(clicked.getText().equals("Undo"))
            {
                undo();
            }
            else if(clicked.getText().equals("Redo"))
            {
                redo();
            }
            else if(clicked.getText().equals("Settings"))
            {
                openSettingsPane();
            }
        } 
    }

    private void undo()
    {
        redoStack.push(undoStack.pop());
        gameModel=undoStack.peek();
        System.out.println(gameModel);
        gameView.setGameModel(gameModel);
        gameView.update();
        System.out.println("End of undo: "+gameModel.getDiagonal());

    }

    private void redo()
    {
        gameModel=redoStack.peek();
        gameView.setGameModel(gameModel);  
        undoStack.push(redoStack.pop());
        gameView.update();
    }

    private void openSettingsPane()
    {
        JPanel panel=new JPanel();
        JRadioButton planeButton=new JRadioButton("Plane", gameModel.getPlane());
        JRadioButton torusButton=new JRadioButton("Torus", gameModel.getTorus());
        JRadioButton orthButton=new JRadioButton("Orthongonal", gameModel.getOrthogonal());
        JRadioButton diagButton=new JRadioButton("Diagonals", gameModel.getDiagonal());
        System.out.println("Start of settings pane: "+gameModel.getDiagonal());
        System.out.println("Orth: "+gameModel.getOrthogonal());



        ButtonGroup group1=new ButtonGroup();
        group1.add(planeButton);
        group1.add(torusButton);

        ButtonGroup group2=new ButtonGroup();
        group2.add(orthButton);
        group2.add(diagButton);

        panel.add(new JLabel("Play as plane or torus"));
        panel.add(planeButton);
        panel.add(torusButton);
        panel.add(new JLabel("Diagonal Moves?"));
        panel.add(orthButton);
        panel.add(diagButton);

        JOptionPane.showMessageDialog(gameView, panel);

        if(planeButton.isSelected())
            gameModel.setPlane(true);
        else
            gameModel.setPlane(false);
        if(torusButton.isSelected())
            gameModel.setTorus(true);
        else 
            gameModel.setTorus(false);
        if(orthButton.isSelected())
            gameModel.setOrthogonal(true);
        else
            gameModel.setOrthogonal(false);
        if(diagButton.isSelected())
            gameModel.setDiagonal(true);
        else
            gameModel.setDiagonal(false);

        System.out.println("End of settings pane: "+gameModel.getDiagonal());
        System.out.println("Orth: "+gameModel.getOrthogonal());
        undoStack.push((GameModel)gameModel.clone());


    }

    /**
     * <b>selectColor</b> is the method called when the user selects a new color.
     * If that color is not the currently selected one, then it applies the logic
     * of the game to capture possible locations. It then checks if the game
     * is finished, and if so, congratulates the player, showing the number of
     * moves, and gives to options: start a new game, or exit
     * @param color
     *            the newly selected color
     */
    public void selectColor(int color){
        if(color != gameModel.getCurrentSelectedColor()) {
            gameModel.setCurrentSelectedColor(color);
            flood();
            gameModel.step();
            gameView.update();

            if(gameModel.isFinished()) {
                      Object[] options = {"Play Again",
                                "Quit"};
                        int n = JOptionPane.showOptionDialog(gameView,
                                "Congratulations, you won in " + gameModel.getNumberOfSteps() 
                                    +" steps!\n Would you like to play again?",
                                "Won",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]);
                        if(n == 0){
                            reset();
                        } else{
                            System.exit(0);
                        }   
                }            
            } 
                  
    }

   /**
     * <b>flood</b> is the method that computes which new dots should be ``captured'' 
     * when a new color has been selected. The Model is updated accordingly
     */
     private void flood() {

        Stack<DotInfo> stack = new GenericLinkedStack<DotInfo>();
        for(int i =0; i < gameModel.getSize(); i++) {
           for(int j =0; j < gameModel.getSize(); j++) {
                if(gameModel.isCaptured(i,j)) {
                    stack.push(gameModel.get(i,j));
                }
           }
        }

        while(!stack.isEmpty()){
            DotInfo DotInfo = stack.pop();
            if((DotInfo.getX() > 0) && shouldBeCaptured (DotInfo.getX()-1, DotInfo.getY())) {
                gameModel.capture(DotInfo.getX()-1, DotInfo.getY());
                stack.push(gameModel.get(DotInfo.getX()-1, DotInfo.getY()));
            }  
            if((DotInfo.getX() < gameModel.getSize()-1) && shouldBeCaptured (DotInfo.getX()+1, DotInfo.getY())) {
                gameModel.capture(DotInfo.getX()+1, DotInfo.getY());
                stack.push(gameModel.get(DotInfo.getX()+1, DotInfo.getY()));
            }
            if((DotInfo.getY() > 0) && shouldBeCaptured (DotInfo.getX(), DotInfo.getY()-1)) {
                gameModel.capture(DotInfo.getX(), DotInfo.getY()-1);
                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY()-1));
            }  
            if((DotInfo.getY() < gameModel.getSize()-1) && shouldBeCaptured (DotInfo.getX(), DotInfo.getY()+1)) {
                gameModel.capture(DotInfo.getX(), DotInfo.getY()+1);
                stack.push(gameModel.get(DotInfo.getX(), DotInfo.getY()+1));
            }

            if (gameModel.getDiagonal()){
                if ((DotInfo.getX()>0) && (DotInfo.getY()>0) && shouldBeCaptured(DotInfo.getX()-1, DotInfo.getY()-1)){
                    gameModel.capture(DotInfo.getX()-1, DotInfo.getY()-1); 
                    stack.push(gameModel.get(DotInfo.getX()-1, DotInfo.getY()-1));
                }

                if ((DotInfo.getX()>0) && (DotInfo.getY()<gameModel.getSize()-1) && shouldBeCaptured(DotInfo.getX()-1, DotInfo.getY()+1)){
                    gameModel.capture(DotInfo.getX()-1, DotInfo.getY()+1); 
                    stack.push(gameModel.get(DotInfo.getX()-1, DotInfo.getY()+1));
                }

                if ((DotInfo.getX()<gameModel.getSize()-1) && (DotInfo.getY()>0) && shouldBeCaptured(DotInfo.getX()+1, DotInfo.getY()-1)){
                    gameModel.capture(DotInfo.getX()+1, DotInfo.getY()-1); 
                    stack.push(gameModel.get(DotInfo.getX()+1, DotInfo.getY()-1));
                }

                if ((DotInfo.getX()<gameModel.getSize()-1) && (DotInfo.getY()<gameModel.getSize()-1) && shouldBeCaptured(DotInfo.getX()+1, DotInfo.getY()+1)){
                    gameModel.capture(DotInfo.getX()+1, DotInfo.getY()+1); 
                    stack.push(gameModel.get(DotInfo.getX()+1, DotInfo.getY()+1));
                }
            }

            if (gameModel.getTorus()){
                if ((DotInfo.getX()==0) && shouldBeCaptured(gameModel.getSize()-1, DotInfo.getY())){
                    gameModel.capture(gameModel.getSize()-1, DotInfo.getY()); 
                    stack.push(gameModel.get(gameModel.getSize()-1, DotInfo.getY()));

                    // IF DIAGONAL IS TRUE
                    if (gameModel.getDiagonal()){ // needs if statement inside for fucking corner piece 
                        if (shouldBeCaptured(gameModel.getSize()-1, DotInfo.getY()-1)){
                            gameModel.capture(gameModel.getSize()-1, DotInfo.getY()-1); 
                            stack.push(gameModel.get(gameModel.getSize()-1, DotInfo.getY()-1));
                        }
                        if (shouldBeCaptured(gameModel.getSize()-1, DotInfo.getY()+1)){
                            gameModel.capture(gameModel.getSize()-1, DotInfo.getY()+1); 
                            stack.push(gameModel.get(gameModel.getSize()-1, DotInfo.getY()+1));
                        }
                    }
                }

                if ((DotInfo.getX()==gameModel.getSize()-1) && shouldBeCaptured(0, DotInfo.getY())){
                    gameModel.capture(0, DotInfo.getY()); 
                    stack.push(gameModel.get(0, DotInfo.getY()));

                    // IF DIAGONAL IS TRUE
                    if (gameModel.getDiagonal()){ // needs if statement inside for fucking corner piece 
                        if (shouldBeCaptured(0, DotInfo.getY()-1)){
                            gameModel.capture(0, DotInfo.getY()-1); 
                            stack.push(gameModel.get(0, DotInfo.getY()-1));
                        }
                        if (shouldBeCaptured(0, DotInfo.getY()+1)){
                            gameModel.capture(0, DotInfo.getY()+1); 
                            stack.push(gameModel.get(0, DotInfo.getY()+1));
                        }
                    }
                }

                if (DotInfo.getY()==0 && shouldBeCaptured(DotInfo.getX(), gameModel.getSize()-1)){
                    gameModel.capture(DotInfo.getX(), gameModel.getSize()-1); 
                    stack.push(gameModel.get(DotInfo.getX(), gameModel.getSize()-1));

                    // IF DIAGONAL IS TRUE
                    if (gameModel.getDiagonal()){ // needs if statement inside for fucking corner piece 
                        if (shouldBeCaptured(DotInfo.getX()-1, gameModel.getSize()-1)){
                            gameModel.capture(DotInfo.getX()-1, gameModel.getSize()-1); 
                            stack.push(gameModel.get(DotInfo.getX()-1, gameModel.getSize()-1));
                        }
                        if (shouldBeCaptured(DotInfo.getX()+1, gameModel.getSize()-1)){
                            gameModel.capture(DotInfo.getX()+1, gameModel.getSize()-1); 
                            stack.push(gameModel.get(DotInfo.getX()+1, gameModel.getSize()-1));
                        }
                    }

                }

                if (DotInfo.getY()==gameModel.getSize()-1 && shouldBeCaptured(DotInfo.getX(), 0)){
                    gameModel.capture(DotInfo.getX(), 0); 
                    stack.push(gameModel.get(DotInfo.getX(), 0));

                    // IF DIAGONAL IS TRUE
                    if (gameModel.getDiagonal()){ // needs if statement inside for fucking corner piece 
                        if (shouldBeCaptured(DotInfo.getX()-1, 0)){
                            gameModel.capture(DotInfo.getX()-1, 0); 
                            stack.push(gameModel.get(DotInfo.getX()-1, 0));
                        }
                        if (shouldBeCaptured(DotInfo.getX()+1, 0)){
                            gameModel.capture(DotInfo.getX()+1, 0); 
                            stack.push(gameModel.get(DotInfo.getX()+1, 0));
                        }
                    }
                }
            }
        }
        undoStack.push((GameModel)gameModel.clone());
        //System.out.println(gameModel.getNumberCaptured);
    }


    /**
     * <b>shouldBeCaptured</b> is a helper method that decides if the dot
     * located at position (i,j), which is next to a captured dot, should
     * itself be captured
     * @param i
     *            row of the dot
     * @param j
     *            column of the dot
     */
    
   private boolean shouldBeCaptured(int i, int j) {
        if(!gameModel.isCaptured(i, j) &&
           (gameModel.getColor(i,j) == gameModel.getCurrentSelectedColor())) {
            return true;
        } else {
            return false;
        }
    }


}
