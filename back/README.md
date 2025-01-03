# Yoga App !


For launch and generate the jacoco code coverage:
> mvn clean test

GL


## DB

command to init a new db in docker :

``` bash
docker run --name test -e MYSQL_USER=user -e MYSQL_PASSWORD=123456 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=test -p 3306:3306 -d mysql
```

to start db :

``` bash
docker start test
```

to stop db :

``` bash
docker stop test
```

to remove db :

``` bash
docker rm test
```