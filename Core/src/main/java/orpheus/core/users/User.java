package orpheus.core.users;

import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;

import serialization.JsonSerialable;

/**
 * a user that also tracks their remote player ID
 */
public class User implements JsonSerialable {

    /**
     * The display name for this user - may not be unique
     */
    private final String name;

    /**
     * A globally unique identifier for this user
     */
    private final UUID id;

    public User(String name) {
        this(name, UUID.randomUUID());
    }

    public User(String name, UUID id) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * @return the globally unique identifier for this user
     */
    public UUID getId() {
        return id;
    }

    /**
     * De-serializes the json representation of a user sent by another user.
     * Note that this means a LocalUser is received as just a LocalUser.
     * @param obj the JSON object to de-serialize
     * @return the LocalUser described by the given JSON object
     */
    public static User fromJson(JsonObject obj) {
        return new User(
            obj.getString("name"),
            UUID.fromString(obj.getString("id"))
        );
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof User) && ((User)other).id.equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public JsonObject toJson(){
        return Json.createObjectBuilder()
            .add("name", name)
            .add("id", id.toString())
            .build();
    }
}
