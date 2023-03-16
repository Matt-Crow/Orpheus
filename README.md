# Orpheus

A video game designed to give me practice with Java.

## Running the program

The game is contained in a JAR file in the `/Client/build/libs folder, and doesn't need to run any installer to work.
Feel free to move this to anywhere on your computer, as it is completely self-contained.

To run the program, either double click the JAR file, or run the following:
1. `gradle uberjar`
2. `java -jar ./Client/build/libs/OrpheusClient.jar`
3. You should be confronted by an ugly GUI. If so, the game is ready to play!

## Multiplayer
* Multiplayer only seems to work within a single network, so the program doesn't seem able to connect to users outside of its own LAN (this is likely because of routers hiding IP addresses).
* Some computers have issues starting or joining a game. I've had limited success with Apple computers running this project: some just can't receive clients, and some just can't join games. Once again, I don't know enough about networks to know why this happens.
* Don't share your IP address with strangers! Since this project seems to not work across networks, you should only need your private IP address for communicating within your network.