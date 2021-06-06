# Anpr-Server

## Intro

Anpr-server is not a real Anpr server. It is just a proxy to real anpr providers. Currently there two implementations:

* Anpr cloud of Adaptative Recognition
* Vialsecure's anpr web service

## Database

This module does not requies a database.

**NOTE:** The database section on *application-template.properties* is not used.

## Basic configuration

1. Duplicate the file *<YOUR_PATH>\TrafficPlatform\products\mosaic-server\src\main\resources\application-template.properties*. Example application-**myinst**.properties

2. Edit the file *application-myinst.properties*

3. Provide the credentials to use the Anpr provider. If the credentials are not provided, the anpr provider won't be enabled.

## Further configuration

### Server port

Default port is 9093. This can be changed setting the new port in the property *server.port* in *application.properties*

## Get plates

| Property      | Value        |
| :------------ |:-------------|
| URL           | http://SERVER_NAME:SERVER_PORT/anpr/get/ANPR_PROVIDER |
| Type          | POST                                |
| Content-Type  | multipart/form-data                 |

where ANPR_PROVIDER can be:

* ANPR_CLOUD_ANPR
* ANPR_CLOUD_MMR
* VIALSECUREWS

The request has to provide an image in the body. The parameter with the image is **imageFile**.

## Response

The response of this endpoint is an array of PlateInfo object, if any plate was found. Each PlateInfo has the following attributes:

* plateNumber
* probability
* topLeftX
* topLeftY
* topRightX
* topRightY
* bottomRightX
* bottomRightY	
* bottomLeftX
* bottomLeftY	
* carMaker (only in ANPR_CLOUD_MMR)
* model (only in ANPR_CLOUD_MMR)
* category (only in ANPR_CLOUD_MMR)
* carProbability (only in ANPR_CLOUD_MMR)


