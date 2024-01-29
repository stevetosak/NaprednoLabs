package Labs2.Zadaca3.MovingCircle;

public interface Movable {
    Movable getClone(Movable other);
    TYPE getType();
    void moveUp();
    void moveLeft();
    void moveRight();
    void moveDown();
    int getCurrentXPosition();
    int getCurrentYPosition();

}
