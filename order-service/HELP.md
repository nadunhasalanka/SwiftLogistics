//database creation

docker run --name postgres-spring \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
-e POSTGRES_DB=order_service \
-p 5432:5432 \
-d postgres:15


//check if running
docker ps

//start the connection 
docker start postgres-spring
