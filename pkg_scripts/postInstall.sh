#!/bin/bash

# create log folder
install  --mode=755 --directory  /var/log/hello-karyon-rxnetty

# It has been tested that the service could not start on neither Ubuntu 14, 16 nor 18. Leaving the broken start-service command will block this package being installed as a debian package. 
# Removing the below command could unblock installing this app in a debian package. To make it function, users can use external command, for example: java -jar build/libs/hello-karyon-rxnetty-all-0.1.0.jar.
 
# start services
# start hello-karyon-rxnetty
