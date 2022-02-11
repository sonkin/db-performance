### H2 server






### PostgreSQL in Docker 

First, you need to install docker image for PostgreSQL:

```
docker pull postgres
```

Then you can start an instance of PostgreSQL by using Docker:

```
docker run --name pg -p 5432:5432 -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=books -d postgres
```

Add this configuration to ***application.properties***:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/books
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=user
spring.datasource.password=password
spring.jpa.database-platform = org.hibernate.dialect.PostgreSQL94Dialect
```

