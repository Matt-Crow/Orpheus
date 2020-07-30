package users;


/**
 *
 * @author Matt
 */
public class RemoteUser extends AbstractUser {
    private String ipAddress;
    
    public RemoteUser(String name){
        super(name);
    }

    /**
     * The server handles invoking this method
     * once it receives user information
     * 
     * @param ipAddr the IP address of the Socket
     * which provided this user's data
     */
    public void setIpAddress(String ipAddr){
        ipAddress = ipAddr;
    }
    
    @Override
    public String getIpAddress() {
        if(ipAddress == null){
            throw new NullPointerException("OrpheusServer didn't invoke setIpAddress for this!");
        }
        return ipAddress;
    }
}
