package users;


/**
 *
 * @author Matt
 */
public class RemoteUser extends AbstractUser {
    private final String ipAddress;
    
    public RemoteUser(String name, String ipAddr){
        super(name);
        ipAddress = ipAddr;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }
}
