# DRIVER-BOOK

The project was created as a simple demo application that presents using Elasticsearch in Java project.
It shows how to index, query and aggregate the data with Java High Rest Level Client.

## Project description

The service provides the information about the F1 drivers that match the query parameters. Moreover, it gives some aggregation result like aggregation 'by nationality' or 'by having the title'

### tech stack
- Java 11
- Elasticsearch 7.15.2
- Spring Boot 2.6.1
- Maven

## Requirements
- Docker 20.10.17 or newer installed(refer to [official user guide](https://docs.docker.com/engine/install/))

## Installation
- to run containers run command: `docker-compose up`

## Shut down
- to shut down containers run command: `docker-compose down`

### API
The service provides the api for trigger the data indexing or get the drivers' information.
- `GET /api/refresh-all` - trigger all data indexing
- `GET /api/refresh-drivers` - trigger indexing drivers data enriched with statistics
- `GET /api/refresh-statistics` - trigger indexing only statistics data
- `GET /api/drivers` - return the list of drivers and aggregations. It takes following query parameters:

  - nationality
  - active
  - bornYearFrom
  - bornYearTo
  - hasTitle
  - hasWin

Example: `http://localhost:8080/api/drivers?active=true&nationality=British&bornYearFrom=1982&bornYearTo=1987&hasTitle=true`