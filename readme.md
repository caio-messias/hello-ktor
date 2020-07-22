Simple REST API to learn the KTOR framework.

### Stack and libs
- Language: Kotlin
- Web framework: KTOR
- Database ORM: Exposed
- JDBC connection pool: Hikari
- Mocking framework: Mockk
- Data class validation: Valiktor
- Json parsing: Jackson

## Running
```$ ./gradew run```

By default the server will be running on port 8800

## Endpoints
* GET /users/{id}
    * 200 OK if user was found
    * 404 Not Found otherwise
* POST /users
    * 201 Created if the user information is valid, and the user was created successfully
    * 400 Bad Request if the user information is not valid
    * 409 Conflict otherwise
