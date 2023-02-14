package orpheus.server;

import java.io.IOException;

public class Main {
    
    public static void main(String[] args) throws IOException {
        var server = OrpheusServer.create();
        System.out.printf("Started server on %s...\n", server.getConnectionString());
    }
}
