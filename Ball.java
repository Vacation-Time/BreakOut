package application;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Ball extends Circle {

    private int speed;
    private int xVelocity;
    private int yVelocity;

    public Ball(double centerX, double centerY, double radius, Paint fill) {
        super(centerX, centerY, radius, fill);
    }

    public void move() {
        // FR06
        setCenterX(getCenterX() + getxVelocity());
        setCenterY(getCenterY() + getyVelocity());

        // if ball has collided with left or right edges of scene
        if(getCenterX() < getRadius() || getCenterX() > 400 - getRadius()) {
            setxVelocity(-getxVelocity());	// ball "bounces" by changing direction
        }

        // hits top of scene
        if(getCenterY() < getRadius() + 40) {
            setyVelocity(-getyVelocity());
        }

        // hits bottom of scene
        if(getCenterY() > 500 - getRadius()) {
            // "game over" or "life lost" could be triggered here, but we'll just bounce for now
            setyVelocity(-getyVelocity());
        }
    }

    public int getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public int getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
