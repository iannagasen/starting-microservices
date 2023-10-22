#!/usr/bin/env bash


### Create the settings.gradle, which describes what projects Gradle should build
cat <<EOF > settings.gradle
include ':microservices:product-service'
include ':microservices:review-service'
include ':microservices:recommendation-service'
include ':microservices:product-composite-service'
include ':api'
include ':util'
EOF

### Copy Gradle executable files that were generated from one of the projects
### So that we can reuse them for the multi-project builds
cp -r microservices/product-service/gradle .
cp microservices/product-service/gradlew . 
cp microservices/product-service/gradlew.bat . 
cp microservices/product-service/.gitignore .

### After copying we no logner need the generated Gradle executable files in each microservice
### We remove them with these commands
# find microservices -depth -name "gradle" -exec rm -rfv "{}" \;
# find microservices -depth -name "gradlew*" -exec rm -rfv "{}" \;

### remove all files naming build.
find microservices -type d -name "build" -exec rm -rf "{}" \;

### rebuild
bash ./gradlew build