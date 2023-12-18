# SFTP Client with Apache Mina SSHD

This project contains a basic SFTP (SSH File Transfer Protocol) client implemented in Java using the Apache Mina SSHD library.

## Features

- Establishes a secure connection to an SFTP server.
- Handles known server keys for safe and secure transactions.
- Lists remote directory contents.

## Java Dependencies

- Apache Mina SSHD library: Provides SSH capabilities.
- SLF4J: Used for application logging.

## Usage

The main application is `org.example.App`, which connects to a remote SFTP server and lists the directory contents.

For the client to work, you need to replace the placeholder values in `App.java` with real values:

- `user`: The username for your SFTP server connection.
- `host`: The host address of your SFTP server.
- `port`: The port number to use for the connection (22 is the default port for SFTP).
- `privateKeyPath`: The path to your private SSH key.
- `knownHostsPath`: The path to your `known_hosts` file that lists the public keys for servers your client trusts.

## Setup

This project is managed with Maven and it's recommended to use an IDE with Maven support. Import the project as a Maven project, and your IDE should take care of dependency management.

## Testing

The `src/test/java/org/example/AppTest.java` file contains a unit test suite for the application.

## Contributing

Please open an issue if you find a bug or have a feature request.