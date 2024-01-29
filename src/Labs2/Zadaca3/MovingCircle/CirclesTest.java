package Labs2.Zadaca3.MovingCircle;
import java.util.Scanner;

public class CirclesTest {
    public static void main(String[] args) throws MovableObjectsNotFittableException, ObjectCanNotBeMovedException {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try{
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                }
                catch (ObjectCanNotBeMovedException ex){
                    ex.message();
                }

            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try{
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                }
                catch (MovableObjectsNotFittableException ex){
                    ex.message();
                }

            }

        }
        System.out.println(collection.toString());

        try {
            System.out.println("MOVE POINTS TO THE LEFT");
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        }
        catch (ObjectCanNotBeMovedException ex){
            ex.message();
        }
        System.out.println(collection.toString());

        try {
            System.out.println("MOVE CIRCLES DOWN");
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        }
        catch (ObjectCanNotBeMovedException ex){
            ex.message();
        }
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        try {
            System.out.println("MOVE POINTS TO THE RIGHT");
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        }
        catch (ObjectCanNotBeMovedException ex){
            ex.message();
        }
        System.out.println(collection.toString());

        try{
            System.out.println("MOVE CIRCLES UP");
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        }
        catch (ObjectCanNotBeMovedException ex){
            ex.message();
        }
        System.out.println(collection.toString());


    }

}
