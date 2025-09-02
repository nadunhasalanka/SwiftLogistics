
#### Docker Command to Run MySQL DB Otherwise use the 3306 port and create the database manually.
```bash
docker run --name mysql-db -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=swiftlogistics -e MYSQL_USER=auth-user -e MYSQL_PASSWORD=1234 -p 3306:3306 -d mysql:8.0
```