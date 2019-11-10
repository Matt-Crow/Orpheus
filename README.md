# Orpheus

A video game designed to give me practice with Java.

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

To get started, download and unzip the project to your computer.

### Prerequisites

In order to run the program, you must have Java installed, which you can download [here](https://www.java.com/en/).
The Program currently uses ANT to build the project, but I plan on switching to Gradle soon.
I always just have Netbeans 8.2 perform the building and running, but if you just plan on running the JAR file, then you needn't install any of these tools, just Java.

### Running the program

The game is contained in a JAR file in the dist folder, and doesn't need to run any installer to work.
If you want, you can safely move the JAR file out of this directory, so long as you move the lib folder from dist with it.

To run the program, either double click the JAR file, or navigate to it in the command line, and run the following:

```
java -jar Orpheus.jar
```

You should be confronted by an ugly GUI. If so, the game is ready to play!

## Built With

* [Netbeans 8.2](https://netbeans.org/downloads/8.2/) - IDE
* [ANT](https://ant.apache.org/) - Build tool

## Contributing

This is a personal project for my own enjoyment, so outside contributions are not accepted.

## Authors

* **Matt Crow** - *Everything* - [IronHeart7334](https://github.com/IronHeart7334)
