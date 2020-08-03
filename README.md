# Orpheus

A video game designed to give me practice with Java.

## Multiplayer WARNINGS
* Multiplayer only seems to work within a single network, so the program doesn't seem able to connect to users outside of its own LAN (this is likely because of routers hiding IP addresses).
* Some computers have issues starting or joining a game. I've had limited success with Apple computers running this project: some just can't receive clients, and some just can't join games. Once again, I don't know enough about networks to know why this happens.
* Because this is an open source project, 
USERS MAY MODIFY THEIR VERSION OF THE GAME, since multiplayer requires the host send their game contents to clients via an ObjectOutputStream,
HOSTS MAY SEND POTENTIALLY DANGEROUS CODE TO CLIENTS, WHICH WILL BE RUN ON THEIR OWN MACHINES. As such, users should
ONLY CONNECT TO PEOPLE THEY TRUST. Maybe. I don't know enough about serialization to know for sure.
https://stackoverflow.com/questions/37387703/how-to-ensure-safety-when-reading-objects-from-objectinputstream


## What I Learned

I've learned so much from doing this project, far more than what I would have just from watching tutorials:
* Basic Java class structures, such as:
  * Classes.
  * Abstract Classes.
  * Interfaces.
  * Enums.
* Swing classes to construct a GUI.
* IO to read and write files.
* Serializing objects in both JSON and the Serializable interface.
* Creating a peer-to-peer network using Sockets.
* Synchronizing data across the network using ObjectStream.
* Responding to key controls via key bindings.
* Preventing concurrent modification of objects in a multithreaded application using syncronization and locks.
* And so much more!

## Getting Started

To use this project, you need Java installed on your computer. You can check if Java is installed by attempting to run the project JAR file (see the section "Running the program"), or you can open your terminal, and type
```
java -version
```
if your computer can find java, you're good! Otherwise, you can download it [here](https://www.java.com/en/). 
To start, you will want to download the project by clicking the "clone or download button", clicking "download zip", and extracting the files on your computer.

### Running the program

The game is contained in a JAR file in the build/libs folder, and doesn't need to run any installer to work.
Feel free to move this to anywhere on your computer, as it is completely self-contained.

To run the program, either double click the JAR file, or navigate to it in the command line, and run the following:

```
java -jar Orpheus.jar
```

If double clicking the JAR file doesn't work, you can can see what's going wrong by opening up your terminal (Command Prompt for Windows, or Terminal for Mac), and type
```
java -jar
```
(make sure you have a space after -jar) Drag and drop the JAR file to the terminal window, and you should see something like this:
```
java -jar C:\Users\****\Desktop\Orpheus.jar
```
You can then hit enter, at which point, if the application doesn't start, the terminal will tell you what went wrong. Feel free to email me, and I can tell you what went wrong (screenshots are helpful).


You should be confronted by an ugly GUI. If so, the game is ready to play!

### How to edit the application (for developers)

You will need Netbeans IDE version 8.2 with the Gradle plugin installed to run the project.
Obviously, Java is needed to allow the project to run. You can use
```
gradle jar
```
to rebuild the JAR file for the project.

## Built With

* [Netbeans 8.2](https://netbeans.org/downloads/8.2/) - IDE
* [Gradle](https://gradle.org/) - Build tool

## Contributing

This is a personal project for my own enjoyment, so outside contributions are not accepted.

## Authors

* **Matt Crow** - *Everything* - [IronHeart7334](https://github.com/IronHeart7334)
