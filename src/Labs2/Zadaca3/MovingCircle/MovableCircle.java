package Labs2.Zadaca3.MovingCircle;

import java.lang.reflect.Type;

public class MovableCircle implements Movable {
    private final int radius;
    private final MovablePoint center;
    private final TYPE type;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
        type = TYPE.CIRCLE;
    }

    public MovableCircle(MovableCircle other){
        this.radius = other.radius;
        this.center = other.center;
        type = other.type;
    }
    @Override
    public Movable getClone(Movable other) {
        return new MovableCircle(this);
    }

    @Override
    public TYPE getType(){
        return this.type;
    }

    @Override
    public void moveUp() {
        center.moveUp();
    }

    @Override
    public void moveLeft() {
        center.moveLeft();
    }

    @Override
    public void moveRight() {
        center.moveUp();
    }

    @Override
    public void moveDown() {
        center.moveDown();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }


    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates " + "(" + getCurrentXPosition() + "," + getCurrentYPosition()
                +") and radius " + radius;
    }
}
