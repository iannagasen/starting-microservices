

## Run create-projects.bash to create the microservices
```bash
./create-projects.bash
```

## If without permission, run this to add
```bash
chmod +x create-projects.bash
```

## You will see now that you have created a /microservices directory.
Check the created files
```bash
find microservices/product-service -type f
```

## Setting up multi-project builds in gradle
this command will create the settings.gradle for building
```bash
./setup-multi-projects.bash
```

## Build the multi-project setup
it will download the necessary files
```bash
./gradlew build
```

## Refresh dependencies
```bash
./gradlew --refresh-dependencies
```

NOTE DELETE ALL BUILDS FROM EACH DEPENDENCY IF HAVING DEPENDENCY ISSUES DURING BUILDING