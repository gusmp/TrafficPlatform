# Video-Server

## Intro

This project is a *simple video management service* (VMS) based on OpenCV and ffmpeg.

You can do:

* [Management of video source](#management-of-video-source)
* [Get image / video](#get-image-/-video)
* [Consolidate videos](#consolidate-videos)

These actions are available across endpoints.

## Database

This project requires a database. Create it with the command:

*create database <YOUR_DDBB> CHARACTER SET utf8 COLLATE utf8_spanish_ci;*

You might want to create a specific user:

*CREATE USER '<YOUR_USER>'@'%' IDENTIFIED BY '<YOUR_PASSWORD>';*

*GRANT ALL PRIVILEGES ON <YOUR_DDBB>.* TO '<YOUR_USER>'@'%';*

*FLUSH PRIVILEGES;*

## Basic configuration

1. Duplicate the file *<YOUR_PATH>\TrafficPlatform\products\video-server\src\main\resources\application-template.properties*. Example application-**myinst**.properties

2. Edit the file *application-myinst.properties*
3. Replace the place holders <MYSQL_*> with your own data
4. Edit the file *TrafficPlatform\products\video-server\src\main\resources\application.properties*
5. Set the property *spring.profiles.active* with *myinst*. This is the name given in the point 1.

## Further configuration

### Server port

Default port is 9090. This can be change setting the new port in the property *server.port* in *application.properties*

### Time to preserve data

Default value is 1 hour (ideal for testing). Set the new value (in hours) in the property *scheduler.removeOldVideos.hoursToPreserve* in *application.properties*

### Remove old data

Video-Server has two periodic tasks to remove old data:

* Remove old data. Remove data comming from video sources. 
* Remove old videos. Remove video files generated on request.

Both task can be scheduled following a cron expression. The properties involved are:

* scheduler.removeOldData.cron
* scheduler.removeOldVideos.cron

Both in *application.properties*

## Run the project

Once these changes have been done:

* Compile the whole project.
* Copy the files *application.properties* and *application-myinst.properties* into the same folder of the module to run (*<YOUR_PATH>\TrafficPlatform\products\video-server\target\* contains a file with name *.jar*).

Run the module with:

*java -D -Djava.library.path=<YOU_PATH_OPENCV_LIBS> -jar video-server-VERSION.jar*

## Management of video source

### Add video source

#### Request header

|               |              |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/vs/add |
| Type          | POST                                  |
| Content-Type  | application/json                      |

#### Request body

| Property      | Description   |
| :------------ |:-------------|
| name                   |  name of the video source |
| enableVideoSource      |  this video source is active or not |
| enableSaveVideo        |  save to disk the video comming from the camera  |
| url                    |  url of the camera |
| removeOldData          |  true/false. If old data will be removed automatically|
| numberOfDaysToPreserve |  Numero of day to preserve the data|
| rootCapturePath        |  Path where store the data |
| rootConsolidatedPath   |  Path where store the consolidated data. See the *Consolidate Videos section*  |
| videoCaptureType       |  Type of capture. OPENCV is the only option available                           |


Example:

```
{
    "name" : "MY_SOURCE_VIDEO",
    "enableVideoSource": true,
    "enableSaveVideo": true,
    "url" : "http://my_camera_url",
    "removeOldData" : false,
    "numberOfDaysToPreserve" : 30,
    "rootCapturePath" : "/path/store/data/MY_SOURCE_VIDEO",
    "rootConsolidatedPath" : "/path/store/data/MY_SOURCE_VIDEO_CONS",
    "videoCaptureType" : "OPENCV"
}
```
#### Response body

| Property      | Description  |
| :------------ |:-------------|
| success       | Boolean. True if the request was execute successfully or false if there was an error |
| errorCode     | Integer. Code of error                |
| message       | Text message of the operation         |

### Update video source

**NOTE:** The video source must be disabled first.

#### Request header

|               |              |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/vs/update |
| Type          | POST                                     |
| Content-Type  | application/json                         |

#### Request body

| Property      | Description  |
| :------------ |:-------------|
| name                   |  name of the video source |
| enableVideoSource      |  this video source is active or not |
| enableSaveVideo        |  save to disk the video comming from the camera  |
| url                    |  url of the camera |
| removeOldData          |  true/false. If old data will be removed automatically|
| numberOfDaysToPreserve |  Numero of day to preserve the data|
| rootCapturePath        |  Path where store the data |
| rootConsolidatedPath   |  Path where store the consolidated data. See the *Consolidate Videos section*  |
| videoCaptureType       |  Type of capture. OPENCV is the only option available                           |


Example:

```
{
    "name" : "MY_SOURCE_VIDEO",
    "enableVideoSource": true,
    "enableSaveVideo": true,
    "url" : "http://my_camera_url",
    "removeOldData" : false,
    "numberOfDaysToPreserve" : 30,
    "rootCapturePath" : "/path/store/data/MY_SOURCE_VIDEO",
    "rootConsolidatedPath" : "/path/store/data/MY_SOURCE_VIDEO_CONS",
    "videoCaptureType" : "OPENCV"
}
```

#### Response body

| Property      | Description  |
| :------------ |:-------------|
| success       | Boolean. True if the request was execute successfully or false if there was an error |
| errorCode     | Integer. Code of error                |
| message       | Text message of the operation         |



### Get a/all video sources

#### Request header

|               |              |
| :------------ |:-------------|
| URL (only one)| http://SERVER_NAME:SERVER_PORT/vs/list/SOURCE_NAME |
| URL (all)     | http://SERVER_NAME:SERVER_PORT/vs/list |
| Type          | GET                              |

#### Response body

An array of video sources with the details.

```
[
    {
        "id": 1,
        "enableVideoSource": true,
        "enableSaveVideo": false,
        "name": "SOURCE_NAME",
        "url": "http://my_camera_url",
        "removeOldData": false,
        "numberOfDaysToPreserve": 30,
        "rootCapturePath": "/path/store/data/MY_SOURCE_VIDEO",
        "rootConsolidatedPath": "/path/store/data/MY_SOURCE_VIDEO_CONS",
        "videoCaptureType": "OPENCV"
    },
    ...
]
```

### Enable / disable a video source

|               |              |
| :------------ |:-------------|
| URL (enable)| http://SERVER_NAME:SERVER_PORT/vs/enableVideoSource/SOURCE_NAME/true |
| URL (disable)| http://SERVER_NAME:SERVER_PORT/vs/enableVideoSource/SOURCE_NAME/false |
| Type          | GET                              |


#### Response body

| Property      | Description  |
| :------------ |:-------------|
| success       | Boolean. True if the request was execute successfully or false if there was an error |
| errorCode     | Integer. Code of error                |
| message       | Text message of the operation         |


### Delete video source

#### Request header

|               |              |
| :------------ |:-------------|
| URL (enable)| http://SERVER_NAME:SERVER_PORT/vs/delete/SOURCE_NAME |
| Type          | POST                               |
| Content-Type  | application/json                   |


#### Response body

| Property      | Description  |
| :------------ |:-------------|
| success       | Boolean. True if the request was execute successfully or false if there was an error      |
| errorCode     | Integer. Code of error         |
| message       | Text message of the operation  |

## Get image / video

## Consolidate videos





 
