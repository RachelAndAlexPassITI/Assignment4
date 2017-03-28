import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * The class <b>GameView</b> provides the current view of the entire Game. It extends
 * <b>JFrame</b> and lays out an instance of  <b>BoardView</b> (the actual game) and 
 * two instances of JButton. The action listener for the buttons is the controller.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */

public class GameView extends JFrame {


    /**
     * The board is a two dimensionnal array of DotButtons instances
     */
    private DotButton[][] board;

    private JButton redoButton;

    private JButton undoButton;

 
    /**
     * Reference to the model of the game
     */
    private GameModel  gameModel;
 
    private GameController gameController;

    private JLabel scoreLabel;
    /**
     * Constructor used for initializing the Frame
     * 
     * @param model
     *            the model of the game (already initialized)
     * @param gameController
     *            the controller
     */

    public GameView(GameModel model, GameController gameController) {
        super("Flood it -- the ITI 1121 version");

        this.gameModel = model;
        this.gameController = gameController;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setBackground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(gameModel.getSize(), gameModel.getSize()));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        board = new DotButton[gameModel.getSize()][gameModel.getSize()];

        for (int row = 0; row < gameModel.getSize(); row++) {
            for (int column = 0; column < gameModel.getSize(); column++) {
                board[row][column] = new DotButton(row, column, gameModel.getColor(row,column), 
                    (gameModel.getSize() < 26 ? DotButton.MEDIUM_SIZE : DotButton.SMALL_SIZE));
                board[row][column].addActionListener(gameController);
                panel.add(board[row][column]);
            }
        }
    	add(panel, BorderLayout.CENTER);

        JButton buttonReset = new JButton("Reset");
        buttonReset.setFocusPainted(false);
        buttonReset.addActionListener(gameController);

        JButton buttonExit = new JButton("Quit");
        buttonExit.setFocusPainted(false);
        buttonExit.addActionListener(gameController);

        JPanel control = new JPanel();
        control.setBackground(Color.WHITE);
        scoreLabel = new JLabel("");
        control.add(scoreLabel);
        control.add(buttonReset);
        control.add(buttonExit);

        undoButton=new JButton("Undo");
        redoButton=new JButton("Redo");
        JButton settingsButton=new JButton("Settings");
        undoButton.setFocusPainted(false);
        redoButton.setFocusPainted(false);
        settingsButton.setFocusPainted(false);
        undoButton.addActionListener(gameController);
        redoButton.addActionListener(gameController);
        settingsButton.addActionListener(gameController);
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);

        JPanel options=new JPanel();
        options.setBackground(Color.WHITE);
        options.add(undoButton);
        options.add(redoButton);
        options.add(settingsButton);
        options.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(options, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(2,1));
        southPanel.add(control);
        southPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
        southPanel.setBackground(Color.WHITE);
        add(southPanel, BorderLayout.SOUTH);

    	pack();
    	//setResizable(false);
    	setVisible(true);

    }

    public void setGameModel(GameModel gameModel)
    {
        this.gameModel=gameModel;
    }

    /**
     * update the status of the board's DotButton instances based on the current game model
     */

    public void update(){
        for(int i = 0; i < gameModel.getSize(); i++){
            for(int j = 0; j < gameModel.getSize(); j++){
                board[i][j].setColor(gameModel.getColor(i,j));
            }
        }
        if(gameModel.initialDot==true)
            scoreLabel.setText("Number of steps: " + gameModel.getNumberOfSteps());
        else
            scoreLabel.setText("Select Initial Dot");

        if(gameController.undoStack.getSize()>1)
        {
            undoButton.setEnabled(true);
        }
        else
        {
            undoButton.setEnabled(false);
        }

        if(gameController.redoStack.isEmpty())
        {
            System.out.println("isEmpty is true");
            redoButton.setEnabled(false);
        }
        else
        {
            redoButton.setEnabled(true);
        }
        repaint();
    }

}
