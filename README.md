# RSO: Image metadata microservice

## Prerequisites

Running pg-admin postgres container (available on port 5435):
```shell
docker run -d --name pg-admin -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=admin -p 5435:5432 --network rsonet postgres:13
```
Or simply start the container if it already exsits:
```shell
docker start pg-admin
```

## Building the docker container

Clean and package the IntelliJ project
```shell
mvn clean package
```
Build the docker image locally
```shell
docker build -f .\Dockerfile_with_maven_build -t admin:latest .
```
## Running the docker container

###Run docker container (available on port 8085)
Pass database and consul agent url's as parameters
```shell
docker run -p 8083:8080 --network rsonet -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-admin:5435/admin -e KUMULUZEE_CONFIG_CONSUL_AGENT=http://consul:8500 --name admin-instance admin
```
Use variables from .yaml file in api module.
```shell
docker run -p 8083:8080 --network rsonet --name admin-instance admin
```
Or simply run if you already created the container.
```shell
docker start admin-instance
```
