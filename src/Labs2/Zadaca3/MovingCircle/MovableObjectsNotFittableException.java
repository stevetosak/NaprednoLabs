package Labs2.Zadaca3.MovingCircle;

public class MovableObjectsNotFittableException extends Exception {
    int x,y,radius;

    public MovableObjectsNotFittableException(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    void message(){
        System.out.println(String.format("Movable circle with center (%d,%d) " +
                "and radius %d can not be fitted into the collection",x,y,radius));
    }

}
