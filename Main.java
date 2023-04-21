// Luca HW17
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private static int score = 0;			// private = access modifier keyword meaning only accessible within this Class
    private static int highScore = 0;		// static = we set static so we don't need to have an object created to use it
    private static String difficulty;

    static Ball ball;						// create a pointer for our ball object
    static Paddle paddle;					// create a pointer for our paddle object

    static int lastBallxVelocity = 0;		// variable for storing ball's X velocity during a pause when we set them to zero
    static int lastBallyVelocity = 0;		// ^                           Y

    @Override										// indicates that the method is an override of a method in the parent class
    public void start(Stage primaryStage) {			// primaryStage is an object of the Stage class. It represents the primary window of the program

        primaryStage.setTitle("Break-Out!");		// set window title to our game name
        Group root = new Group();					// create a root object from the Group class, a container for our UI elements
        Scene scene = new Scene(root,400,500);		// Put our Group object container into a new Scene and give it window dimensions
        primaryStage.setScene(scene);				// set our Scene to be the scene for our Stage

        ball = new Ball(200, 150, 10, Color.BLUE);	// create our ball object from our Ball class that extends Rectangle
        paddle = new Paddle(60, 10, Color.GREEN);	// create our paddle object from our Paddle class that extends Rectangle
        paddle.setX(170); paddle.setY(470);			// set paddle coordinates by hand, because it will not be a part of our HBox

        readConfigFile();							// load settings from config.txt

        Label scoreLabel = new Label("Score: " + score);				// create a label to show score variable
        Label highScoreLabel = new Label("High Score: " + highScore);	// create a label to show highScore variable

        Brick[][] bricks = new Brick[5][10];				// create a two-dimensional array of Brick objects, with 5 rows, 10 columns
        for(int i = 0; i < 5; i++) {						// outer for - goes through our rows
            for(int j = 0; j < 10; j++) {					// inner for - goes through our columns
                bricks[i][j] = new Brick(35, 10, Color.RED);// create a new Brick object for this specific cell
                bricks[i][j].setX(j * 40 + 2);				// set X coordinate for this cell's specific brick
                bricks[i][j].setY(i * 15 + 40);				// set Y coordinate for this cell's specific brick
                root.getChildren().add(bricks[i][j]);		// add this cell's specific brick to our Group container "root"
            }
        }

        ComboBox<String> difficultyComboBox = new ComboBox<>();			// create a comboBox to show difficulty setting
        difficultyComboBox.getItems().addAll("Slow", "Medium", "Fast");	// add speed options to our difficulty comboBox
        difficultyComboBox.setValue(difficulty);						// set our comboBox to be the current difficulty setting

        difficultyComboBox.setOnAction(event -> {			// method call/event on our comboBox that is triggered when the UI control is activated
            difficulty = difficultyComboBox.getValue();		// get the current value of our comboBox and store it in our difficulty class variable
            setSpeed();	// BR10
        });

        Button startButton = new Button("Start");			// create a Start button object from the JavaFX Button class
        Button pauseButton = new Button("Pause");			// ^        Pause

        HBox topBar = new HBox(10, scoreLabel, highScoreLabel, difficultyComboBox, startButton);	// create an HBox layout manager with spacing of 10px and the listed nodes added
        topBar.setAlignment(Pos.CENTER);					// center the nodes horizontally within the HBox. UI elements will be arranged in the center of the HBox
        topBar.setPadding(new Insets(10));					// add padding of 10 pixels around the edge of the HBox

        // GameLoop extends AnimationTimer and will handle() each frame of our game's animation
        GameLoop gameLoop = new GameLoop(ball, paddle, bricks, scoreLabel, highScoreLabel, score, highScore);
        gameLoop.start();

        startButton.setOnAction(event -> {							// this block of code runs when the Start button is clicked
            if(lastBallxVelocity == 0 || lastBallyVelocity == 0) {	// if this is our first Start button press, these will be 0, because that's how we defined them
                lastBallxVelocity = ball.getSpeed(); 				// sets temporary variable to Ball's X velocity to speed selected by player
                lastBallyVelocity = ball.getSpeed();				// ^                                 Y
            }														// if this is not the first Start button press, X and Y velocity will have values stored by Pause

            ball.setxVelocity(lastBallxVelocity);					// sets Ball's X velocity to the speed and direction it was before paused, or our default speed
            ball.setyVelocity(lastBallyVelocity);					// ^           Y
            topBar.getChildren().remove(startButton);				// removes the Start button from our HBox, so it will disappear from our window
            topBar.getChildren().add(pauseButton);					// adds the Pause button to our HBox, so it will appear in our window
            difficultyComboBox.setDisable(true);					// disables the combo box that handles game speed so it cannot be changed during game play
            root.requestFocus();									// sets our container object root to have focus. This allows our scene to listen for keyboard presses
        });

        pauseButton.setOnAction(event -> {					// this code block runs when the Pause button is clicked
            lastBallxVelocity = ball.getxVelocity();		// get the X velocity of the Ball and store it in a temporary variable
            lastBallyVelocity = ball.getyVelocity();		// ^       Y
            ball.setxVelocity(0);							// now that current values are stored, set Ball's X velocity to 0 to stop it
            ball.setyVelocity(0);							// ^                                              Y
            topBar.getChildren().remove(pauseButton);		// removes the Pause button from our HBox, so it will disappear from our window
            topBar.getChildren().add(startButton);			// adds the Start button to our HBox, so it will appear in our window
        });

        scene.setOnKeyPressed(event -> {							// code that runs when a keyboard key is pressed while our root container has focus
            switch (event.getCode()) {								// switch on the specific key pressed
                case A:													// when the 'a' key is pressed, we have no code, but no break, so we continue through switch statement
                case LEFT:												// when the left arrow key is pressed, the following happens:
                    paddle.setIsMovingRight(false);						// set our Paddle to not be moving right
                    if(topBar.getChildren().contains(pauseButton))		// if our HBox contains our Pause button, we know the game isn't paused, so we:
                        paddle.setIsMovingLeft(true);					// set our Paddle to be moving left
                    break;												// exit the switch for cases 'a' and 'left'
                case D:													// when the 'd' key is pressed, we have no code, but no break, so we continue through switch statement
                case RIGHT:												// when the right arrow key is pressed, the following happens:
                    paddle.setIsMovingLeft(false);						// set our Paddle to be moving right
                    if(topBar.getChildren().contains(pauseButton))		// if our HBox contains our Pause button, we know the game isn't paused, so we:
                        paddle.setIsMovingRight(true);					// set our Paddle to be moving right
                    break;												// exit the switch for cases 'd' and 'right'
                default:												// for all other keyboard keys pressed:
                    break;												// exit switch without doing anything
            }
            //root.requestFocus();									// sets our container object root to have focus. This allows our scene to listen for keyboard presses
            //^^  not needed here, because we set the focus in our Start button onAction event
        });

        scene.setOnKeyReleased(event -> {			// code that runs when a keyboard key is released
            switch (event.getCode()) {				// switch on the specific key released
                case A:									// when the 'a' key is released, we have no code, but no break, so we continue through switch statement
                case LEFT:								// when the left arrow key is released, the following happens:
                    paddle.setIsMovingLeft(false);		// set our Paddle to not be moving left
                    break;								// exit switch for cases 'a' and 'left'
                case D:									// when the 'd' key is released, we have no code, but no break, so we continue through switch statement
                case RIGHT:								// when the right arrow key is released, the following happens:
                    paddle.setIsMovingRight(false);		// set our Paddle to not be moving right
                    break;								// exit switch for cases 'd' and 'right'
                default:								// for all other keyboard keys released:
                    break;								// exit switch without doing anything
            }
        });

        root.getChildren().addAll(ball, paddle, topBar);	// add ball, paddle, and our HBox of score, high score, and difficulty - all to our root Group

        primaryStage.show();								// "show" our Stage full of GUI elements

        primaryStage.setOnCloseRequest(event -> {			// sets an event handler for when the user tries to close the window created by the primaryStage object
            writeConfigFile();								// write settings to config.txt
        });
    }

    public static void main(String[] args) {				// special method in Java that is automatically executed when you run your Java program
        launch(args);	// static method of the Application class, responsible for starting the JavaFX runtime and initializing the primary stage of the program
    }

    /**
     * Reads difficulty and high score from config.txt in the same folder as the program.
     * If no file exists, defaults of "Medium" and 0 are set
     */
    public static void readConfigFile() {
        Scanner scanner = null;								// create a new scanner pointer
        try {												// (always use error-handling when dealing with file i/o)
            scanner = new Scanner(new File("config.txt"));	// create a new scanner object, set config.txt to be the input file, and point our variable scanner to it
            if (scanner.hasNextLine()) 						// the .hasNextLine() method returns true or false
                difficulty = scanner.nextLine();			// if true, we set that next line to be difficulty (String)
            else
                difficulty = "Medium";						// if false, we set difficulty to our default of Medium

            if (scanner.hasNextInt()) 						// the .hasNextInt() method returns true or false
                highScore = scanner.nextInt();				// if true, we set that next line to be high score (Integer)
            else
                highScore = 0;								// if false, we set high score to a default of 0

        } catch (FileNotFoundException e) {					// if we catch an error saying there is no config.txt file, we will set default values
            difficulty = "Medium";							// default difficulty set if no config.txt exists
            highScore = 0;									// default high score set if no config.txt exists
        } finally {											// the finally block of code runs if an error is caught or not
            if(scanner != null)								// make sure we set scanner to a file and aren't still pointed to null
                scanner.close();							// close out our file
        }

        setSpeed();			// set our Ball and Paddle speeds based on the difficulty just pulled from config.txt
    }

    /**
     * Writes difficulty and high score to config.txt in the same folder as the program
     */
    public static void writeConfigFile() {
        PrintWriter output = null;								// create a new PrintWriter pointer
        try {
            output = new PrintWriter(new File("config.txt"));	// create a new PrintWriter object, set config.txt to be the output file, and point our variable output to it
            output.println(difficulty);							// write difficulty to output PrintWriter buffer
            output.println(highScore);							// write highScore to output PrintWriter buffer
        } catch(IOException e) {								// catch a generic I/O exception e
            System.out.println("Error: " + e);					// display the error message to the console
        } finally {
            if(output != null)									// make sure output is pointed to a file, not null still
                output.close();									// close out our file. This writes the buffer out to the file
        }
    }

    /**
     * Uses difficulty selected to set Ball and Paddle speeds
     */
    public static void setSpeed() {
        switch(difficulty) {
            case "Slow":
                ball.setSpeed(3);
                paddle.setSpeed(3);
                break;
            case "Medium":
                ball.setSpeed(4);
                paddle.setSpeed(4);
                break;
            case "Fast":
                ball.setSpeed(5);
                paddle.setSpeed(5);
                break;
            default:
                System.out.println("Unknown speed: " + difficulty);
                break;
        }

        lastBallxVelocity = ball.getxVelocity();
        lastBallyVelocity = ball.getyVelocity();
    }

    public static void setHighScore(int score) {
        highScore = score;
    }
}