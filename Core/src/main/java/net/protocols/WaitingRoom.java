package net.protocols;

import java.util.LinkedList;
import java.util.List;
import orpheus.core.users.User;

/**
 * Holds users who are waiting to play
 */
public class WaitingRoom {
    private final LinkedList<User> users = new LinkedList<>();

    public void addUser(User user) {
        if (!containsUser(user)) {
            users.add(user);
        }
    }

    public boolean containsUser(User user) {
        return users.stream().anyMatch(u -> u.getId().equals(user.getId()));
    }

    public List<User> getAllUsers() {
        return List.copyOf(users);
    }
}
