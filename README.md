# Address Lookup Technical Test

Requirements:

* Java 17+
* Maven

## How to run

1. Clone the project
2. Run `mvn spring-boot:run` within the root directory

Once running, the API should be accessible at `http://localhost:8080/addresses`.

## How to package and run tests

1. Clone the project
2. Run `mvn clean verify`

## Application overview

This is a CRUD (Create, Read, Update & Delete) API for addresses. An address is comprised of the following elements:

* `building`
* `street`
* `town`
* `postcode`

The primary purpose of the application is to look up addresses by postcode to populate an address picker.

Endpoints:

* `GET /addresses/?postcode=` - Returns the list of all addresses, optionally filtered by the `postcode` parameter
* `GET /addresses/{id}` - Returns a single address by ID
* `POST /addresses` - Creates a new address
* `PUT /addresses/{id}` - Updates an existing address
* `DELETE /addresses/{id}` - Removes an existing address

Full API documentation is visible at [this URL](http://localhost:8080/swagger-ui/index.html) once the application is running.