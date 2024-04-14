## OBDJava 🚗

**OBDJava is a Java Maven project with a JavaFX user interface aimed at communicating with the OBD system and the ELM327 interface.**

The project is far from perfect, it's just a draft, feel free to reuse it if you want.

## Features
* Retrieve Values
  * Speed
  * RPM
  * Air In Take
  * Engine Temperature
* Clear DTC
* Calculate 0-100 km/h

## Basic use
Run ```MainApplication.java``` with a IDE like Intellij

## Compatibility
*OBDJava* has been tested on :

* Windows 11 | JDK 17.0.10
* MacOS Sonoma (Apple M1) | JDK 17.0.10


## Contributing

If you wish to develop other functionalities,

I recommend using an ELM327 emulator such as this Python project [ELM327-emulator](https://github.com/Ircama/ELM327-emulator).

It's quite simple to add other commands thanks to this [page](https://en.wikipedia.org/wiki/OBD-II_PIDs) which lists OBD codes with formula