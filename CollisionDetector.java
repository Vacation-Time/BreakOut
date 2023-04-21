package application;

public class CollisionDetector {
    private Ball ball;
    private Paddle paddle;
    private Brick[][] bricks;

    public CollisionDetector(Ball ball, Paddle paddle, Brick[][] bricks) {
        this.ball = ball;
        this.paddle = paddle;
        this.bricks = bricks;
    }

    /**
     * Checks for collision between the ball and the paddle and updates the ball's velocity accordingly.
     */
    public void handleBallPaddleCollision() {

        if (ball.intersects(paddle.getBoundsInLocal())) {				// if the ball intersects with the paddle

            double relativePosition = (ball.getCenterX() - paddle.getX() - paddle.getWidth()/2) / (paddle.getWidth()/2);	// Determine where the ball hit the paddle relative to the center

            double bounceAngle = relativePosition * Math.PI/3;			// Calculate the bounce angle based on the relative position

            double speed = ball.getSpeed();								// get the ball's speed
            double newXVelocity = (speed * Math.sin(bounceAngle));		// calculate the new X velocity based on the bounce angle and the ball's speed
            double newYVelocity = (-speed * Math.cos(bounceAngle));		// ^                 Y

            // Update the ball's x and y velocities with the new values
            ball.setxVelocity((int)newXVelocity);
            ball.setyVelocity((int)newYVelocity);
        }
    }

    /**
     * Checks for collision between the ball and the bricks and updates the ball's velocity accordingly.
     * Also keeps track of the number of bricks broken and updates the score and high score labels accordingly.
     *
     * @return Number of bricks broken
     */
    public int handleBallBrickCollision() {
        int bricksBroken = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                Brick currentBrick = bricks[i][j];

                // If the current brick is visible and intersects with the ball, update the ball's velocity and remove the brick.
                if (currentBrick.isVisible() && ball.intersects(currentBrick.getBoundsInLocal())) {

                    boolean fromLeft = ball.getCenterX() < currentBrick.getBoundsInLocal().getMinX();
                    boolean fromRight = ball.getCenterX() > currentBrick.getBoundsInLocal().getMaxX();

                    if (fromLeft || fromRight) {
                        ball.setxVelocity(-ball.getxVelocity());	// If the ball hit the brick from the left or right, reverse its x velocity.
                    } else {
                        ball.setyVelocity(-ball.getyVelocity());	// If the ball hit the brick from the top or bottom, reverse its y velocity.
                    }

                    currentBrick.setVisible(false);		// set current brick to not be visible
                    bricksBroken++;						// increase broken brick counter
                }
            }
        }

        if (allBricksBroken())
            resetBricks();

        return bricksBroken;
    }

    /**
     * Checks if all the bricks are broken
     *
     * @return True if all Bricks are broken
     */
    public boolean allBricksBroken() {
        return countVisibleBricks() == 0;
    }

    /**
     * Counts the number of visible bricks
     *
     * @return Number of visible bricks
     */
    public int countVisibleBricks() {
        int count = 0;
        // FR19
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                if (bricks[i][j].isVisible())
                    return count;
            }
        }
        return count;
    }

    /**
     * Resets all the bricks to their original state (visible)
     */
    public void resetBricks() {
        // FR19
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                bricks[i][j].setVisible(true);
            }
        }
    }
}