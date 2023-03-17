package orpheus.core.users;

import java.util.Optional;
import java.util.UUID;

/**
 * a user that also tracks their remote player ID
 */
public class LocalUser extends User {
    private Optional<UUID> remotePlayerId = Optional.empty();

    public LocalUser(String name) {
        super(name);
    }

    public void setRemotePlayerId(UUID remotePlayerId) {
        if (remotePlayerId == null) {
            throw new IllegalArgumentException("Remote player ID cannot be null");
        }
        this.remotePlayerId = Optional.of(remotePlayerId);
    }

    /**
     * throws an exception if remote ID is not set
     * @return the ID of the entity in the world this is controlling
     */
    public UUID getRemotePlayerId() {
        return remotePlayerId.get();
    }
}
