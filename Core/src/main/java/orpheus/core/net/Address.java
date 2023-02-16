package orpheus.core.net;

import java.util.Objects;

/**
 * a wrapper for IP address & port
 */
public class Address {
    private final String ip;
    private final int port;

    public Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return String.format("%s:%d", ip, port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Address) {
            var asAddr = (Address)other;
            return asAddr.ip.equals(ip) && asAddr.port == port;
        }
        return false;
    }
}
