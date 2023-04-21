package application;

import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

public class GameLoop extends AnimationTimer {
    private Ball ball;
    private Paddle paddle;
    private Brick[][] bricks;	// variable bricks not used in GameLoop class, but by the CollisionDetector object,
    private int score;			//_									created in the constructor of the GameLoop class.
    private int highScore;
    private Label scoreLabel;
    private Label highScoreLabel;
    private CollisionDetector collisionDetector;

    public GameLoop(Ball ball, Paddle paddle, Brick[][] bricks, Label scoreLabel, Label highScoreLabel, int score, int highScore) {
        this.ball = ball;
        this.paddle = paddle;
        this.bricks = bricks;
        this.scoreLabel = scoreLabel;
        this.highScoreLabel = highScoreLabel;
        this.score = score;
        this.highScore = highScore;
        this.collisionDetector = new CollisionDetector(ball, paddle, bricks);
    }

    @Override
    public void handle(long currentNanoTime) {
        ball.move();
        paddle.move();

        collisionDetector.handleBallPaddleCollision();						// Check for collision between the ball and the paddle

        int bricksBroken = collisionDetector.handleBallBrickCollision();	// Check for collision between the ball and the bricks

        if (bricksBroken > 0) {												// If one or more bricks are broken:
            score += bricksBroken;											// increase score
            scoreLabel.setText("Score: " + score);							// set the score label to be accurate
            if (score > highScore) {										// if current score is greater than high score:
                highScore = score;											// set value of high score to be the value score
                highScoreLabel.setText("High Score: " + highScore);			// update the high score label
                Main.setHighScore(score);									// score is just a primative datatype passed by value, so we need to use a setter
            }
        }
    }
}