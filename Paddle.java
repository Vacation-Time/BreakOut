package application;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {

    private int speed;
    private Boolean isMovingLeft = false;
    private Boolean isMovingRight = false;

    public Paddle(double width, double height, Paint fill) {
        super(width, height, fill);
    }

    public void move() {
        if(getIsMovingLeft() && getX() > 0) {
            setX(getX() - speed);
        } else if (getIsMovingRight() && getX() < 340) {
            setX(getX() + speed);
        }
    }

    public Boolean getIsMovingLeft() {
        return isMovingLeft;
    }

    public void setIsMovingLeft(Boolean isMovingLeft) {
        this.isMovingLeft = isMovingLeft;
    }

    public Boolean getIsMovingRight() {
        return isMovingRight;
    }

    public void setIsMovingRight(Boolean isMovingRight) {
        this.isMovingRight = isMovingRight;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}