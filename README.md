## About the project
This project uses redisson library for redis for Hibernate cache L2. You can find more details on [Redisson configuration for L2 over here](https://github.com/redisson/redisson/tree/master/redisson-hibernate). For extensive details you can find its [wiki page](https://github.com/redisson/redisson/wiki/Table-of-Content)

## Useful links

[Hibernate L2 with Redis](https://dzone.com/articles/caching-in-hibernate-with-redis)<br/>
[Hibernate Issue with loading collection of entities](https://stackoverflow.com/questions/36823272/how-to-force-spring-data-to-find-entities-for-findall-method-of-crudrepository-i)<br/>

## Postman
```
http://localhost:8085/users/only-users-pagable?page=0&size=10 -> getUsers with HQL, query cahce should be enabled
http://localhost:8085/users/ - get all users
http://localhost:8085/users/1997 - get a single user
curl --location --request POST 'http://localhost:8085/users?id=2001' - save a user
http://localhost:8085/users?id=2001 - delete a user
http://localhost:8085/users?id=2001&name=Dhawan - update a user
```
    