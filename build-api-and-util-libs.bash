#!/usr/bin/env bash


## build the api library
cd api/
bash .././gradlew build \
--warning-mode all \
# --stacktrace


## build the util library
cd ../
cd util/
bash .././gradlew build \
--warning-mode all \
--stacktrace


## build the all others
cd ../
bash ./gradlew build \
--warning-mode all \
# --stacktrace