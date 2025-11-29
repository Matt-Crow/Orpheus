package orpheus.core.net;

import orpheus.core.users.User;

import java.io.IOException;

/**
 * Represents a connection to either a client or a server running Orpheus
 * @author Matt Crow
 */
// TODO add local connection subclass
public abstract class Connection {
    private User remoteUser;

    /**
     * @param u the user playing Orpheus on the machine this connects to
     */
    public void setRemoteUser(User u){
        remoteUser = u;
    }

    /**
     * @return the user playing Orpheus on the machine this connects to
     */
    public User getRemoteUser(){
        return remoteUser;
    }
    
    // TODO don't throw IOException
    public abstract void writeServerMessage(Message sm) throws IOException;
    
    // TODO don't throw IOException
    // blocks until the client writes something
    public abstract Message readServerMessage() throws IOException;
    
    // TODO not needed for local connection?
    public abstract void close();
}
