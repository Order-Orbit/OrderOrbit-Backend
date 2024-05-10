# Backend(OrderOrbit)
> This project is been developed using Spring Boot and REST APIs to help restaurants to list their info and menu to customers and also helps customers to select a restaurant and using it's menu book a meal with wasting time in queue to book meal.

## Tools: 
> Spring Boot, REST APIs, MySQL Database, JWT(JSON Web Token) based authorization, BCrypt for data encryption, AWS S3 bucket to store images and Postman for API testing

## Steps to setup and use this project
> ### Step 1:
> * Clone this repo as,  ``` git clone https://github.com/Order-Orbit/OrderOrbit-Backend.git ```
> ### Step 2:
> * Open OrderOrbit-Backend folder in a IDE like Visual Studio Code
> * Create a database in Mysql
> * Create a S3 bucket to store images
> * Take a gmail account and create app password for it
> * **Create .env file** inside OrderOrbit-Backend folder
> ### Step 3:
> * Add the attributes along with your own values inside **.env file**
```
DB_URL = jdbc:mysql://localhost:3306/<your_db_name>
DB_USERNAME = 
DB_PWD = 
MAIL_ID = 
MAIL_PWD = 
SECRETKEY = 

AWS_ACCESS_KEY = 
AWS_SECRET_KEY = 
S3_BUCKET_NAME = 
S3_BUCKET_URL = 
S3_REGION = 
```
> ### Step 4:
> * Run the application using IDE or using maven command: ``` mvn spring-boot:run ```
