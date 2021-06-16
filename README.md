# Weather App Spring Cloud Stream Demo:

## Why
To learn more about creating event-driven architectures/data pipelines using Spring Cloud Stream.

## What
It's an app that reads events from a Kafka message broker, processes them, and stores the processed events to a Postgres database.

## Architecure
![image](https://user-images.githubusercontent.com/5508288/121729507-6c46ee80-caa3-11eb-8169-094eadd5180c.png)

## Prerequisite
1. Java 8+
2. Docker & Docker Composer 
3. Setup and run a local Kafka message broker and Postgres database by running the following command from project root:
   `> docker-composer up -d`

## Core Functionality
![image](https://user-images.githubusercontent.com/5508288/121730731-efb50f80-caa4-11eb-89ad-ccc39415a620.png)

## Core Learning
1. We practiced TDD and pair programming by building a Spring Cloud Stream app.
2. We learned how to write unit test for Spring Cloud Stream framework.
3. We learned how to write integration tests using Testcontainers.
4. We learned how to write simple message handlers.
5. We learned how to compose the simple message handlers via configuration properties. 



