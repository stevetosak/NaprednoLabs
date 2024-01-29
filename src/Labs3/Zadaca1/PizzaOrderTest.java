package Labs3.Zadaca1;

import java.util.*;

enum ItemType{
    EXTRA_ITEM,
    PIZZA_ITEM
}
abstract class Menu{
    private static List<Map<String,Integer>> itemCollection; // sekoj item e mapiran vo cenata
    private static Map<String,Integer> extraItemTypes;
    private static Map<String,Integer> pizzaItemTypes;
    private static boolean wasCreated = false;

     private static void createMenu(){
         if(wasCreated) return;

         itemCollection = new ArrayList<>();

         extraItemTypes = new HashMap<>();
         extraItemTypes.put("Coke",5);
         extraItemTypes.put("Ketchup",3);

         itemCollection.add(extraItemTypes);

         pizzaItemTypes = new HashMap<>();
         pizzaItemTypes.put("Standard",10);
         pizzaItemTypes.put("Pepperoni",12);
         pizzaItemTypes.put("Vegetarian",8);

         itemCollection.add(pizzaItemTypes);

         wasCreated = true;
    }
    public static boolean isValidType(String name,ItemType type){
         createMenu();
         if(type == ItemType.EXTRA_ITEM){
            return extraItemTypes.containsKey(name);
         }
        else if(type == ItemType.PIZZA_ITEM){
            return pizzaItemTypes.containsKey(name);
        }
        return false;
    }
    public static int getItemPrice(Item item){
        int indexOfTypeMap = item.getType();
        return itemCollection.get(indexOfTypeMap).get(item.getName());//***
    }
}
interface Item extends Comparable<Item>{
    String getName();
    int getPrice();
    int getType();
}
class InvalidExtraTypeException extends Exception{
    InvalidExtraTypeException(){}
}
class ExtraItem implements Item{
    private final String name;
    private final ItemType itemType = ItemType.EXTRA_ITEM;

    ExtraItem(String name) throws InvalidExtraTypeException {
        if(!Menu.isValidType(name,itemType)){
            throw new InvalidExtraTypeException();
        }
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return Menu.getItemPrice(this);
    }

    @Override
    public int getType() {
        return itemType.ordinal();
    }

    @Override
    public int compareTo(Item o) {
        return name.compareTo(o.getName());
    }

}

class InvalidPizzaTypeException extends Exception{
    InvalidPizzaTypeException(){}
}
class PizzaItem implements Item{
    private final String name;
    private final ItemType itemType = ItemType.PIZZA_ITEM;

    PizzaItem(String name) throws InvalidPizzaTypeException {
        if(!Menu.isValidType(name,itemType)){
            throw new InvalidPizzaTypeException();
        }
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return Menu.getItemPrice(this);
    }

    @Override
    public int getType() {
        return itemType.ordinal();
    }

    @Override
    public int compareTo(Item o) {
        return name.compareTo(o.getName());
    }
}

class ItemOutOfStockException extends Exception{
    ItemOutOfStockException(Item item){}
}
class OrderLockedException extends Exception{
    OrderLockedException(){}
}
class Order {
    private final List<Item> itemList;
    private final Map<String,Integer> itemHash;
    private boolean isLocked = false;
    Order() {
        itemList = new ArrayList<>();
        itemHash = new HashMap<>();
    }
    void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if(isLocked) throw new OrderLockedException();
        if(count > 10) throw new ItemOutOfStockException(item);

        if(!itemHash.containsKey(item.getName()))
            itemList.add(item);

        itemHash.put(item.getName(),count);
    }
    void removeItem(int index) throws OrderLockedException {
        if(isLocked) throw new OrderLockedException();

        if(index >= itemList.size() || index < 0){
            throw new ArrayIndexOutOfBoundsException(index);
        }

        Item toDelete = itemList.get(index);
        itemList.remove(toDelete);
        itemHash.remove(toDelete.getName());
    }
    int getPrice(){
        int total = 0;

        for(int i = 0; i < itemHash.size(); i++){
            Item current = itemList.get(i);
            total += current.getPrice() * itemHash.get(current.getName());
        }

        return total;
    }

    void lock() throws EmptyOrder {
        if(itemList.isEmpty()) throw new EmptyOrder();

        isLocked = true;
    }

    void displayOrder(){
        for(int i = 0; i < itemList.size(); i++){
            Item current = itemList.get(i);
            int quantity = itemHash.get(current.getName());
            System.out.printf("%3s.%-15sx %s %5s\n",i+1,current.getName(),quantity,(current.getPrice() * quantity) + "$");
        }
        System.out.printf("%-25s%-5s\n","Total:",getPrice() + "$");
    }

}

class EmptyOrder extends Exception{
    EmptyOrder(){}
}


public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}
