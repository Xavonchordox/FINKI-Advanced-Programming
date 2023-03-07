package Lab7.P3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String message) {
        super(message);
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String message) {
        super(message);
    }
}


class ChatRoom{
    String name;
    private Set<String> userSet;

    public ChatRoom(String name) {
        this.name = name;
        userSet = new TreeSet<>();
    }


    public void addUser(String username) {
        userSet.add(username);
    }

    public void removeUser(String username) {
        userSet.remove(username);
    }

    @Override
    public String toString() {
        String users;
        if (userSet.isEmpty())
            users = "EMPTY";
        else
            users = String.join("\n", userSet);

        return String.format("%s\n%s\n", name, users);
    }

    public boolean hasUser(String username) {
        return userSet.contains(username);
    }

    public int numUsers(){
        return userSet.size();
    }
}

class ChatSystem {
    Map<String, ChatRoom> rooms;
    TreeSet<String> users;

    public ChatSystem() {
        rooms = new TreeMap<>();
        users = new TreeSet<>();
    }

    public void addRoom(String roomName) {
        rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (!rooms.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        return rooms.get(roomName);
    }

    public void register(String userName) throws NoSuchRoomException, NoSuchUserException {
        users.add(userName);
        if (!rooms.isEmpty()) {
            String min = rooms.values().stream()
                    .min(Comparator.comparing(ChatRoom::numUsers).thenComparing(room -> room.name)).get().name;
            registerAndJoin(userName, min);
        }

    }

    public void registerAndJoin(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        users.add(userName);
        joinRoom(userName, roomName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!rooms.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        if (!users.contains(userName))
            throw new NoSuchUserException(userName);
        rooms.get(roomName).addUser(userName);

    }

    public void leaveRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!rooms.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        if (!users.contains(userName))
            throw new NoSuchUserException(userName);
        rooms.get(roomName).removeUser(userName);
    }

    public void followFriend(String userName, String friend_username) throws NoSuchUserException {
        if (!users.contains(friend_username))
            throw new NoSuchUserException(friend_username);
        rooms.values().stream().filter(room -> room.hasUser(friend_username)).forEach(room -> room.addUser(userName));
    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,params);
                    }
                }
            }
        }
    }

}
