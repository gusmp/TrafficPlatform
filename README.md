# TrafficPlatform

## Intro

This is the project in which I am currently working in my spare time. The main goal is to control the traffic, detecting cars, reading plates and thus sending automatically alerts or generating reports.

The completion of this project is far from conclusion. Currently, there are implemented some modules which are being tested and bug fixed.

**Under no circumstances should you use any of these projects!**

## Compile

### Requirements

This project has been written in Java. You will need:

* JDK installation. I am using OpenJDK 15.0.1.
* Maven. I use 3.3.3 version.

On top of that, this project requires OpenCV. I have been using the versions:
* 4.5.1
* 4.5.2 (current version)

Due to the use of the video, it must be compiled with ffmpeg support.

Not now, but in the future we will use neural network (YOLO to be precise). Thus the *contrib* package must be included in the OpenCV used to run this project.

### OpenCV in Windows

The easiest way to install OpenCV on Windows is download the binary package from [https://opencv.org/releases/](https://opencv.org/releases/). Extract it and copy the following files in a new folder where to store the external libraries:

* OPENCV_DIR\opencv\build\bin\opencv_videoio_ffmpeg452_64.dll
* OPENCV_DIR\opencv\build\java\x86\opencv_java452.dll

In order to use OpenCV with Java you will need a brigde library between Java and C++. This library is located in:

* OPENCV_DIR\opencv\build\java\opencv-452.jar

Install in your maven repository with the command:

*mvn install:install-file -Dfile=opencv-452.jar -DgroupId=org.opencv -DartifactId=opencv -Dversion=4.5.2 -Dpackaging=jar -DgeneratePom=true*

### OpenCV in Linux

As far as I know, there is not official binary for OpenCV. You can download a binary one using the package manager of your Linux (apt, yum, yagurt...). However, I have prefered compile it from source code. The steps in Ubuntu are:

**NOTE: The official guide is in this link: [https://docs.opencv.org/4.5.2/d7/d9f/tutorial_linux_install.html](https://docs.opencv.org/4.5.2/d7/d9f/tutorial_linux_install.html)**

**NOTE: Another useful resource, specifically for Ubuntu can be found in [https://vitux.com/opencv_ubuntu/](https://vitux.com/opencv_ubuntu/)**

1. sudo apt install build-essential cmake git pkg-config libgtk-3-dev \
libavcodec-dev libavformat-dev libswscale-dev libv4l-dev \
libxvidcore-dev libx264-dev libjpeg-dev libpng-dev libtiff-dev \
gfortran openexr libatlas-base-dev python3-dev python3-numpy \
libtbb2 libtbb-dev libdc1394-22-dev libopenexr-dev \
libgstreamer-plugins-base1.0-dev libgstreamer1.0-dev

2. mkdir ~/opencv_build && cd ~/opencv_build
3. git clone https://github.com/opencv/opencv.git
4. git clone https://github.com/opencv/opencv_contrib.git
5. cd ~/opencv_build/opencv
6. mkdir -p build && cd build
7. cmake -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX=<YOUR_PATH>/opencv_build/opencv/build -DOPENCV_EXTRA_MODULES_PATH=~/opencv_build/opencv_contrib/modules -DBUILD_SHARED_LIBS=OFF -DBUILD_EXAMPLES=OFF -DBUILD_TESTS=OFF -DBUILD_PERF_TESTS=OFF ..
8. *make -j4* where 4 is the number of processors
9. Install the Java library to use OpenCV library with: *mvn install:install-file -Dfile=<YOUR_PATH>/opencv_build/opencv/build/bin/opencv-452.jar -DgroupId=org.opencv -DartifactId=opencv -Dversion=4.5.2 -Dpackaging=jar*

### Compile the project

With your JDK and maven bin folder in your PATH varible just type inside the folder which contains the root pom.xml:

*mvn clean package*

With this command, the folders *<YOUR_PATH>\TrafficPlatform\products\<MODULE>\targer\* contains a file with name *<MODULE_NAME>.jar* which is the module compiled, ready to be executed.

## List of modules

Nowadays, there are three modules:

* Video-Server: a simple video management service (VMS).
* Mosaic-Server: allow to create a single image from diferent sources.
* Anpr-Server: a bridge to access to several automatic number-plate recognition.











 
