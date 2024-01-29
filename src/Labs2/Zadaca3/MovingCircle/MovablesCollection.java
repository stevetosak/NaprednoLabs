package Labs2.Zadaca3.MovingCircle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;

public class MovablesCollection{
    private final ArrayList<Movable> movable;
    private static int xMax = 0;
    private static int yMax = 0;

    public MovablesCollection(int xMax, int yMax) {
        movable = new ArrayList<>();
        MovablesCollection.xMax = xMax;
        MovablesCollection.yMax = yMax;
    }

    public static void setxMax(int x) {
        xMax = x;
    }

    public static void setyMax(int y) {
        yMax = y;
    }
    boolean checkBounds(int x,int y){
        return x >= 0 && x <= xMax && y >= 0 && y <= yMax;
    }

    void addMovableObject(Movable m) throws MovableObjectsNotFittableException, ObjectCanNotBeMovedException {
        if(m.getType() == TYPE.POINT){
            if(checkBounds(m.getCurrentXPosition(), m.getCurrentYPosition())){
                movable.add(m);
            }
            else{
                throw new ObjectCanNotBeMovedException(m.getCurrentXPosition(), m.getCurrentYPosition());
            }
        }
        else if(m.getType() == TYPE.CIRCLE){
            MovableCircle circle = (MovableCircle) m;
            if(checkBounds(circle.getCurrentXPosition() + circle.getRadius(),circle.getCurrentYPosition() + circle.getRadius())
                    && checkBounds(circle.getCurrentXPosition() - circle.getRadius(),circle.getCurrentYPosition() - circle.getRadius())) {
                movable.add(m);
            }
            else {
                throw new MovableObjectsNotFittableException(circle.getCurrentXPosition(), circle.getCurrentYPosition(), circle.getRadius());
            }
        }
    }

    void moveObjectsFromTypeWithDirection(TYPE type,DIRECTION direction) throws ObjectCanNotBeMovedException {
        ListIterator<Movable> iterator = movable.listIterator();
        while(iterator.hasNext()){
            Movable value = iterator.next();
            if(value.getType() == type){
                Movable clone = value.getClone(value);
                if (direction == DIRECTION.UP) {
                    clone.moveUp();

                } else if (direction == DIRECTION.RIGHT) {
                    clone.moveRight();

                } else if (direction == DIRECTION.LEFT) {
                    clone.moveLeft();

                } else {
                    clone.moveDown();
                }
                if (checkBounds(clone.getCurrentXPosition(), clone.getCurrentYPosition())) {
                    iterator.set(clone);
                }
                else{
                    throw new ObjectCanNotBeMovedException(clone.getCurrentXPosition(), clone.getCurrentYPosition());
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Collection of movable objects with size %d:\n",movable.size()));
        for (Movable value : movable) {
            sb.append(value.toString());
            sb.append("\n");
        }
        return sb.toString();

    }
}
