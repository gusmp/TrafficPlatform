# Mosaic-Server

## Intro

Browsers have a limitation in the number of connections can be established against the same server.

In case of wanting to display a large number of video streams, por exemaple in video world, browser will reject some connections preventing from displaying some cameras. So solve this problem mosaic-server gather all video sources and generates a single image with all the video sources. With this technique, it is only need one connection to the server.

## Database

This project requires a database. Create it with the command:

*create database <YOUR_DDBB> CHARACTER SET utf8 COLLATE utf8_spanish_ci;*

You might want to create a specific user:

*CREATE USER '<YOUR_USER>'@'%' IDENTIFIED BY '<YOUR_PASSWORD>';*

*GRANT ALL PRIVILEGES ON <YOUR_DDBB>.* TO '<YOUR_USER>'@'%';*

*FLUSH PRIVILEGES;*

## Basic configuration

1. Duplicate the file *<YOUR_PATH>\TrafficPlatform\products\mosaic-server\src\main\resources\application-template.properties*. Example application-**myinst**.properties

2. Edit the file *application-myinst.properties*
3. Replace the place holders <MYSQL_*> with your own data
4. Edit the file *TrafficPlatform\products\mosaic-server\src\main\resources\application.properties*
5. Set the property *spring.profiles.active* with *myinst*. This is the name given in the point 1.

## Further configuration

### Server port

Default port is 9091. This can be change setting the new port in the property *server.port* in *application.properties*

## Run the project

Once these changes have been done:

* Compile the whole project.
* Copy the files *application.properties* and *application-myinst.properties* into the same folder of the module to run (*<YOUR_PATH>\TrafficPlatform\products\mosaic-server\target\* contains a file with name *.jar*).

Run the module with:

*java -D -Djava.library.path=<YOU_PATH_OPENCV_LIBS> -jar mosaic-server-VERSION.jar*

## How to use mosaic-server

First of all, you need to create some input sources. These can be:

* A camera
* A source from a VMS. See the video-server module.

Once all input souces has been create you can create a mosaic based on the input source previously created.

## Input source management

### Adding a new input source

#### Request header

| Property      | Value        |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/inputSource/save |
| Type          | POST                                  |
| Content-Type  | application/json                      |


#### Request body

| Property      | Description   |
| :------------ |:--------------|
| name          |  name of the input source |
| url           |  url of the video source |


Example:

```
{
    "name" : "INPUT_SOURCE_NAME",
    "url" : "http://my_video_source
}
```

#### Response body

| Property      | Description  |
| :------------ |:-------------|
| success       | Boolean. True if the request was execute successfully or false if there was an error |
| errorCode     | Integer. Code of error                |
| message       | Text message of the operation         |


### Update an existing input source

#### Request header

| Property      | Value        |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/inputSource/update |
| Type          | POST                                  |
| Content-Type  | application/json                      |


#### Request body

| Property      | Description   |
| :------------ |:--------------|
| name          |  name of the input source |
| url           |  url of the video source |


Example:

```
{
    "name" : "INPUT_SOURCE_NAME",
    "url" : "http://my_video_source
}
```

#### Response body

| Property      | Description  |
| :------------ |:-------------|
| success       | Boolean. True if the request was execute successfully or false if there was an error |
| errorCode     | Integer. Code of error                |
| message       | Text message of the operation         |


### List input sources 

#### Request header

| Property      | Value        |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/list |
| Type          | GET                                 |


#### Response body

This endpoint returns an array of input sources with:

* input source name
* input source url 


### Delete an input source

#### Request header

| Property      | Value        |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/inputSource/delete/INPUT_SOURCE_NAME |
| Type          | POST                                  |
| Content-Type  | application/json                      |


#### Response body

| Property      | Description  |
| :------------ |:-------------|
| success       | Boolean. True if the request was execute successfully or false if there was an error |
| errorCode     | Integer. Code of error                |
| message       | Text message of the operation         |

## Mosaic management


### Create a new mosaic

#### Request header

| Property      | Value        |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/mosaic/save |
| Type          | POST                                  |
| Content-Type  | application/json                      |

#### Request body

| Property      | Description   |
| :------------ |:--------------|
| name          |  mosaic name |
| displayName   |  write in the mosaic the name of the input source |
| inputSourceList | list of input source which will be used by the mosaic. Each item has to contain the name of the input source and the position on the mosaic |

Example:

```
{
    "name" : "MY MOSAIC",
    "displayName" : true,
    "inputSourceList": [
        { "name" : "INPUT_SORCE_1", "position" : 1  },
        { "name" : "INPUT_SORCE_2", "position" : 2  },
        { "name" : "INPUT_SORCE_3", "position" : 3  },
        { "name" : "INPUT_SORCE_4", "position" : 4  },
        { "name" : "INPUT_SORCE_5", "position" : 5  },
        { "name" : "INPUT_SORCE_6", "position" : 6  }
    ]
}
```

#### Response body

| Property      | Description  |
| :------------ |:-------------|
| success       | Boolean. True if the request was execute successfully or false if there was an error |
| errorCode     | Integer. Code of error                |
| message       | Text message of the operation         |


### Reload mosaic configuration

#### Request header

| Property      | Value        |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/reload/MOSAIC_NAME |

#### Response body

| Property      | Description  |
| :------------ |:-------------|
| success       | Boolean. True if the request was execute successfully or false if there was an error |
| errorCode     | Integer. Code of error                |
| message       | Text message of the operation         |


### Get a mosaic

**NOTE:** This endpoint returns the image, not the details of the mosaic.

#### Request header

| Property      | Value        |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/mosaic/get/MOSAIC_NAME/columns |
| Type          | GET                                  |



Where *columns* is the number of columns of the mosaic. For example, if you have a mosaic make up by 6 input sources and *columns* has the value of 2, then the mosaic produced has 3 rows and 2 columns (3 x 2 = 6)

#### Response body

This endpoint returns a endless sequence of images whose content type is *multipart/x-mixed-replace*. The images are in jpeg format.

### Delete a mosaic

#### Request header

| Property      | Value        |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/mosaic/delete/MOSAIC_NAME |
| Type          | POST                                  |
| Content-Type  | application/json                      |

#### Response body

| Property      | Description  |
| :------------ |:-------------|
| success       | Boolean. True if the request was execute successfully or false if there was an error |
| errorCode     | Integer. Code of error                |
| message       | Text message of the operation         |