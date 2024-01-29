package Labs7;

import com.sun.source.tree.Tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

class User implements Comparable<User> {
    private final String username;
    private final Set<ChatRoom> rooms;

    public User(String username) {
        this.username = username;
        rooms = new HashSet<>();
    }

    public void addRoom(ChatRoom room){
        rooms.add(room);
    }

    public void removeRoom(ChatRoom room){
        rooms.remove(room);
    }


    public String getUsername() {
        return username;
    }

    public Set<ChatRoom> getRooms() {
        return rooms;
    }

    @Override
    public int compareTo(User o) {
        return this.username.compareTo(o.username);
    }

}

class ChatRoom implements Comparable<ChatRoom> {
    private final String roomName;
    private final Map<String, User> currentUsers;

   public ChatRoom(String name) {
        this.roomName = name;
        currentUsers = new TreeMap<>();
    }

    public void addUser(String username) {
        currentUsers.putIfAbsent(username, new User(username));
        currentUsers.get(username).addRoom(this);
    }

    public void removeUser(String username) {
        currentUsers.remove(username);
    }

    public boolean hasUser(String username) {
        return currentUsers.containsKey(username);
    }

    public int numUsers() {
        return currentUsers.size();
    }


    @Override
    public String toString() {
        if (currentUsers.isEmpty()) {
            return roomName + "\n" + "EMPTY\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(roomName).append("\n");
        currentUsers.forEach((key, val) -> sb.append(key).append("\n"));
        return sb.toString();
    }

    @Override
    public int compareTo(ChatRoom o) {
        return this.roomName.compareTo(o.roomName);
    }
}

class ChatSystem {
    private final Map<String, ChatRoom> rooms;
    private final Map<String, User> users;


    public ChatSystem() {
        rooms = new TreeMap<>();
        users = new HashMap<>();
    }

    public void addRoom(String roomName) {
        rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) {
        return rooms.get(roomName);
    }

    public void register(String username) {
        users.put(username,new User(username));
        Optional<ChatRoom> room = rooms.values()
                .stream()
                .min(Comparator.comparing(ChatRoom::numUsers));

        if(room.isPresent()){
            ChatRoom first = room.get();
            first.addUser(username);
            users.get(username).addRoom(first);
        }
    }

    public void registerAndJoin(String userName, String roomName){
        ChatRoom chatRoom = rooms.get(roomName);
        users.put(userName,new User(userName));
        users.get(userName).addRoom(chatRoom);
        chatRoom.addUser(userName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException {
        if(!rooms.containsKey(roomName)) throw new NoSuchRoomException(roomName);

        ChatRoom chatRoom = rooms.get(roomName);
        chatRoom.addUser(userName);
        users.get(userName).addRoom(chatRoom);
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!rooms.containsKey(roomName)) throw new NoSuchRoomException(roomName);
        if(!users.containsKey(username)) throw new NoSuchUserException(username);

        ChatRoom chatRoom = rooms.get(roomName);
        chatRoom.removeUser(username);
        users.get(username).removeRoom(chatRoom);

    }
    public void followFriend(String username, String friend_username) throws NoSuchUserException {
        if(!users.containsKey(username)) throw new NoSuchUserException(username);
        if(!users.containsKey(friend_username)) throw new NoSuchUserException(friend_username);

        Set<ChatRoom> chatRoomList = users.get(friend_username).getRooms();
        User user = users.get(username);

        for(ChatRoom chatRoom : chatRoomList){
            chatRoom.addUser(username);
            user.addRoom(chatRoom);
        }
    }

}

class NoSuchRoomException extends Exception{
    public NoSuchRoomException(String roomName){
        super(roomName);
    }
}

class NoSuchUserException extends Exception{
    public NoSuchUserException(String username){
        super(username);
    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
            if(k != 0) System.out.println();
            System.out.println(cr);
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }
                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        String params[] = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        m.invoke(cs, (Object[]) params);
                    }
                }
            }
        }
    }

}
