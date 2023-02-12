package orpheus.core.users;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import serialization.JsonSerialable;

/**
 * represents a user playing the game
 */
public class User implements JsonSerialable {
    private final String name;

    public User(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * De-serializes the json representation of a user sent by another user.
     * Note that this means a LocalUser is received as just a User.
     * @param obj the JSON object to de-serialize
     * @return the User described by the given JSON object
     */
    public static User fromJson(JsonObject obj) {
        return new User(obj.getString("name"));
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof User) && ((User)other).name.equals(name);
    }

    @Override
    public JsonObject serializeJson(){
        JsonObjectBuilder objBuild = Json.createObjectBuilder();
        objBuild.add("name", name);
        return objBuild.build();
    }
}
