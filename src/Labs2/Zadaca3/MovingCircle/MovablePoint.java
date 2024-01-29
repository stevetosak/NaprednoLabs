package Labs2.Zadaca3.MovingCircle;

public class MovablePoint implements Movable {
    private int x, y;
    private final int xSpeed, ySpeed;
    TYPE type;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.type = TYPE.POINT;
    }

    public MovablePoint(MovablePoint other) {
        this.x = other.x;
        this.y = other.y;
        this.xSpeed = other.xSpeed;
        this.ySpeed = other.ySpeed;
        this.type = other.type;
    }

    @Override
    public Movable getClone(Movable other) {
        return new MovablePoint(this);
    }

    @Override
    public TYPE getType() {
        return type;
    }

    @Override
    public void moveUp() {
        y += ySpeed;
    }

    @Override
    public void moveLeft() {
        x += xSpeed;
    }

    @Override
    public void moveRight() {
        x -= xSpeed;
    }

    @Override
    public void moveDown() {
        y -= ySpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }


    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + "," + y + ")";
    }

}
