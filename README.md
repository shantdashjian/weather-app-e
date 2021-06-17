# Weather App Spring Cloud Stream Demo:

## Why
To learn more about creating event-driven architectures/data pipelines using Spring Cloud Stream.

## What
It's an app that reads events from a Kafka message broker, processes them, and stores the processed events to a Postgres database.

## Architecture
![Screen Shot 2021-06-16 at 5 21 38 PM](https://user-images.githubusercontent.com/2113918/122311612-6d26b880-cec7-11eb-87fa-96a54f0c76c9.png)

## Prerequisite
1. Java 8+
2. Docker & Docker Composer 
3. Setup and run a local Kafka message broker, Avro schema registry, and Postgres database by running the following command from project root:
   `> docker-compose up -d`

## Core Functionality
![Screen Shot 2021-06-16 at 5 31 01 PM](https://user-images.githubusercontent.com/2113918/122312240-aa3f7a80-cec8-11eb-8f78-efd3f544cab4.png)

## Core Learning
1. TDD and pair programming by building a Spring Cloud Stream app.
2. Writing unit test for Spring Cloud Stream framework.
3. Writing integration tests using Testcontainers.
4. Writing simple message handlers.
5. Composing the simple message handlers via configuration properties. 
6. Using Avro for data serialization.
7. Using Docker Compose to setup local environment of containers. 



