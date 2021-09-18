package users;

import java.net.Socket;


/**
 *
 * @author Matt
 */
public class RemoteUser extends AbstractUser {
    private Socket socket;
    
    public RemoteUser(String name){
        super(name);
    }

    /**
     * The server handles invoking this method
     * once it receives user information
     * 
     * @param socket the Socket which provided this user's data
     */
    public void setSocket(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public Socket getSocket() {
        if(socket == null){
            throw new NullPointerException("OrpheusServer didn't invoke setIpAddress for this!");
        }
        return socket;
    }
}
