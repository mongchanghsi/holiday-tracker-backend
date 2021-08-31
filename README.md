# Holiday Tracker API

## Inspiration

This repository contains different API frameworks and langauges built upon the idea of tracking public holidays and also some form of user authentication. This will serve as a revision / templates for any future side projects depending on the type of frameworks I would want to use.

## Languages

- [x] Springboot in Kotlin-Maven (with H2 database)
- [x] Springboot in Java-Maven (with H2 database)
- [ ] ExpressJS in Node (with PostgresSQL)
- [ ] Flask in Python
- [ ] Gin in Golang

## How to start

In each folder, there should be a DockerFile / docker-compose.yml which allows you to start up the application. After that, you can access the endpoints via `http://localhost:8080`

## Details

Here are the different endpoints which will be available for your consumption

- User
  - [GET] `api/user` - returns a list of User objects which have an account with the application
  - [GET] `api/user/search?id={id}` - returns a single User object which have an account with `id`
  - [POST] `api/user/signup` - returns a single User object which to have their account created / sign up
  - [POST] `api/user/signin` - returns `NO CONTENT` forr a user login
  - [PUT] `api/user/changepassword/{id}` - returns a single User object which had their account changed password
- Holiday
  - [GET] `api/holiday` - returns a list of Holiday object which contains all of the public holiday
  - [GET] `api/holiday/search?id={id}` - return a single Holiday object with a specific `id`
  - [GET] `api/holiday/search?name={name}` - return a single Holiday object with a specific `name`
  - [GET] `api/holiday/range` - return a list of Holiday object which is within a range of dates
  - [POST] `api/holiday` - return a single Holiday object upon creating a new holiday
  - [PUT] `api/holiday/{id}` - return a single Holiday object upon updating it
  - [DELETE] `api/holiday/{id}` - return `NO CONTENT` for the deletion of a holiday
- Annual Leaves
  - [GET] `api/annualleave` - returns a list of type Holiday objects which it will provide a recommendation on when you should take an annual leave to have a long weekend. It assumes that the public holiday falls on either Tuesday or Thursday so the annual leave is to be taken on either Monday or Friday.

## Future Works

- [ ] To include middleware to check for session token for user authentication
- [ ] The endpoint that returns the list of holidays, can do a basic filter which only provide the holiday from the current date
