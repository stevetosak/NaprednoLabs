package Labs2.Zadaca3.MovingCircle;

public class ObjectCanNotBeMovedException extends Exception {
    int x,y;

    public ObjectCanNotBeMovedException(int x, int y) {
        this.x = x;
        this.y = y;
    }
    void message(){
        System.out.printf("Point (%d,%d) is out of bounds%n",x,y);
    }

}
