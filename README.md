# Redis Clone

A lightweight Redis-inspired in-memory database server built from scratch in Java. This project implements the Redis Serialization Protocol (RESP), supports concurrent client connections using sockets, and includes several core Redis features such as persistence, transactions, Pub/Sub messaging, TTL expiration, and authentication.

## Features

- RESP protocol implementation
- Multi-threaded TCP server
- Thread-safe in-memory key-value database
- String data type support
- Persistence using snapshot storage
- Key expiration (TTL)
- Background expiry cleanup thread
- Transactions (MULTI / EXEC / DISCARD)
- Publish / Subscribe messaging
- Client authentication (AUTH)
- Server statistics (INFO)
- Thread pool for handling concurrent clients

---

## Supported Commands

### Basic Commands

- PING
- SET
- GET
- DEL
- EXISTS
- KEYS
- TYPE

### String Commands

- APPEND
- STRLEN
- INCR
- DECR
- SETNX
- MSET
- MGET

### Expiration

- EXPIRE

### Persistence

- SAVE

### Transactions

- MULTI
- EXEC
- DISCARD

### Publish / Subscribe

- SUBSCRIBE
- PUBLISH
- UNSUBSCRIBE

### Server

- INFO
- AUTH
- FLUSHDB

---

## Tech Stack

- Java
- Java Sockets
- Multithreading
- ConcurrentHashMap
- ExecutorService
- Object Serialization
- Git
- Maven

---

## How to Run

### Clone the repository

```bash
git clone https://github.com/abhinavraj-git/redis-clone.git
cd redis-clone
```

### Build the project

```bash
mvn clean install
```

### Run the server

```bash
mvn exec:java
```

or run the `Main.java` class directly from IntelliJ IDEA.

### Connect using redis-cli

```bash
redis-cli -p 6380
```

Authenticate before executing commands:

```redis
AUTH redis123
```

---

## Example

```redis
AUTH redis123

SET language Java

GET language

EXPIRE language 30

INFO
```

---

## Learning Outcomes

Through this project I gained hands-on experience with:

- Socket programming
- Multi-threaded server architecture
- Concurrent programming in Java
- Network protocol implementation (RESP)
- Thread-safe data structures
- Persistence mechanisms
- Publish / Subscribe messaging systems
- Authentication and session handling
- Transaction processing
- System design fundamentals

---

## Future Improvements

- Additional Redis data types (Lists, Sets, Hashes)
- AOF (Append Only File) persistence
- Configuration support
- Replication
- LRU Cache eviction
- Unit and integration testing

---

## License

This project is developed for educational purposes.

---

## Author

**Abhinav Raj**

- GitHub: https://github.com/abhinavraj-git