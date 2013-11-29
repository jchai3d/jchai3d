# About #

The JChai3D project is the Java implementation of the [CHAI3D library](http://www.chai3d.org), which was developed at University of Stanford.

JChai3D provides rendering mechanism and a interface to communicate with haptic devices (like [ForceDimension's Omega.x](http://www.forcedimension.com/products#omega.x), [Geomatic Touch](http://geomagic.com/en/products/phantom-omni/overview) and others.

## Authors ##
* Jairo Melo -  <jssmunb@unb.br>
* Marcos Ramos - <ms.ramos@outlook.com>

## Features ##

* 3D rendering mechanism using OpenGL (via JOGL);
* Simple scene creation. You'll just need a few lines of code;
* Hardware abstaction for haptic devices. You can attach haptic devices with two or three lines of code;
* Force-feedback algorithm, as the original CHAI3D library.

## History ##
Originally this projects had as its goal to bring the CHAI3D experience to browsers, but as long as wee develop we noticed that the performance of JCHAI3D were quite good, almost the same speed as its parent.

This project originally was hosted on [Sourceforge](https://sourceforge.net/projects/jchai3d/), but for lots of reasons we decided to move to GitHub.

## License ##
JCha3D is licesed under GPLv2. You can find a copy of this license on the 'LICENSE' file, or following [this link](http://www.gnu.org/licenses/gpl-2.0.html).

## Building from source ##

To build JChai3D form its source make sure that you have a Maven environment installed. For more information please visit the [Maven starting guide](http://maven.apache.org/guides/getting-started/)

Once you have a configured Maven environment then you can just run 'mvn package' from your terminal. This will create a new JAR file inside 'target' directory.

We intend to make JChai3D available in Maven repository soon.
