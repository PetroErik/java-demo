# Demo Java

## Requirements

Docker installed on local machine.

## Installation

- clone this repository
- make all
- app up and ready

## Run tests

- make test

## Description 
This is a simple Spring API to demonstrate experience. Students and courses can be created/read/updated/deleted and students can be registered/removed from courses. Each endpoint requires a valid JWT token. 

## Documentation for API Endpoints

| HTTP request                                             | Description                                  | Requires JWT       |
|----------------------------------------------------------|----------------------------------------------|--------------------|
| **GET** /healthcheck                                     | Simple endpoint to check service is up       | :x:                |
| **POST** /login                                          | Login route to get JWT                       | :x:                |
| **GET** /student/{studentId}                             | Get a student                                | :white_check_mark: |
| **GET** /student/{studentId}/courses                     | Get all courses the student is registered to | :white_check_mark: |
| **POST** /student                                        | Create a student                             | :white_check_mark: |
| **PUT** /student/{studentId}                             | Update a student                             | :white_check_mark: |
| **DELETE** /student/{studentId}                          | Delete a student                             | :white_check_mark: |
| **GET** /course/{courseId}                               | Get a Course                                 | :white_check_mark: |
| **POST** /course                                         | Create a course                              | :white_check_mark: |
| **PUT** /course/{courseId}                               | Update a course                              | :white_check_mark: |
| **DELETE** /course/{courseId}                            | Delete a course                              | :white_check_mark: |
| **POST** /register/student/{studentId}/course/{courseId} | Register a student to a course               | :white_check_mark: |
| **POST** /register/{registerId}                          | Delete a registration                        | :white_check_mark: |
| **POST** /register/student/{studentId}                   | Delete all registrations by a student        | :white_check_mark: |
| **POST** /register/course/{courseId}                     | Delete all registrations by a course         | :white_check_mark: |
