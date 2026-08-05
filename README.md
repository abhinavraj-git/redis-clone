# Redis Clone

A Redis-inspired in-memory key-value database built completely from scratch in Java. The project implements the Redis Serialization Protocol (RESP), supports concurrent client connections over TCP sockets, and recreates several core Redis features including persistence, transactions, Pub/Sub messaging, TTL expiration, authentication, and server monitoring.

---

## Features

- RESP protocol implementation
- Multi-threaded TCP server
- Thread-safe in-memory key-value database
- String data type support
- Snapshot persistence
- TTL (Time-To-Live) support
- Background expiry cleanup thread
- Transactions (MULTI / EXEC / DISCARD)
- Publish / Subscribe messaging
- Client authentication (AUTH)
- Server statistics (INFO)
- Thread pool for concurrent client handling

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
- Maven
- Git

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

Or run `Main.java` directly from IntelliJ IDEA.

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

## Benchmark

A custom benchmarking client (`Benchmark.java`) is included to evaluate the performance of the server.

The benchmark:

- Establishes a TCP connection to the server
- Authenticates using `AUTH`
- Sends **100,000 RESP SET commands**
- Measures execution time using `System.nanoTime()`
- Calculates throughput (operations/second)

### Sample Result

```text
Commands   : 100000
Time       : 5.21 sec
Throughput : 19,176 ops/sec
```

Across multiple benchmark runs after JVM warm-up, the server consistently achieved **18K–20K operations per second**.

---

## Learning Outcomes

Through this project I gained practical experience with:

- Socket programming
- Multi-threaded server architecture
- Concurrent programming in Java
- RESP protocol implementation
- Thread-safe data structures
- In-memory database design
- Snapshot persistence
- Publish / Subscribe messaging
- Authentication and session management
- Transaction processing
- Performance benchmarking
- Systems programming fundamentals

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

GitHub: https://github.com/abhinavraj-git