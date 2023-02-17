package orpheus.core.net;

import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;

import serialization.JsonSerialable;

/**
 * a wrapper for IP address & port
 */
public class SocketAddress implements JsonSerialable {
    private final String address;
    private final int port;

    public SocketAddress(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return String.format("%s:%d", address, port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SocketAddress) {
            var asAddr = (SocketAddress)other;
            return asAddr.address.equals(address) && asAddr.port == port;
        }
        return false;
    }

    @Override
    public JsonObject serializeJson() {
        return Json.createObjectBuilder()
            .add("address", address)
            .add("port", port)
            .build();
    }

    public static SocketAddress fromJson(JsonObject json) {
        return new SocketAddress(json.getString("address"), json.getInt("port"));
    }
}
