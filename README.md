# eMAG E-Commerce Microservices Project

## Overview
This project is an advanced e-commerce system composed of two interconnected microservices. The first microservice manages core functionalities such as user management, product catalog, orders, shopping cart, sales, and reviews. The second microservice is dedicated to handling email notifications, supporting both synchronous (REST API) and asynchronous (RabbitMQ) email delivery methods. The architecture follows best practices for modularity, scalability, and maintainability.

## Microservices Architecture

### 1. **E-Commerce (eMAG) Service**
This microservice serves as the core of the system and handles all business logic related to e-commerce transactions. It consists of the following key components:

#### **Entities**
- **User**: Represents users of the system, including buyers and administrators. It contains details such as email, password, and a boolean field indicating admin privileges.
- **Product**: Stores details about items available for purchase, including price, stock availability, and category.
- **Order**: Represents user purchases and consists of multiple **OrderItems**.
- **OrderItem**: Represents an individual product within an order, containing quantity and price details.
- **Review**: Allows users to provide feedback and ratings on purchased products.
- **Sale**: Manages promotional discounts and special offers.
- **ShoppingCart**: Temporarily holds items before they are purchased.
- **EmailRequest**: Used to send notifications to the Email Service microservice.

#### **Business Logic & Service Layers**
- **User Management**: Handles authentication, registration, and role-based access control.
- **Product Management**: Allows CRUD operations on products, including filtering and categorization.
- **Order Processing**: Manages order placement, payment processing, and order tracking.
- **Shopping Cart Management**: Allows users to add, remove, and update cart items before checkout.
- **Discount & Sales Management**: Applies promotions and calculates discounted prices.
- **Review System**: Enables customers to provide feedback and rate products.
- **Email Integration**: Sends notifications related to orders, promotions, and invoices.

#### **Communication with the Email Service**
- The `EmailRequest` entity is used to send emails **synchronously** via REST API.
- Alternatively, emails can be sent **asynchronously** through **RabbitMQ**, ensuring better performance and non-blocking requests.

### 2. **Email Service**
The Email Service microservice is responsible for sending email notifications related to order confirmations, password resets, promotional offers, and invoices.

#### **Functionality**
- Receives email requests from the e-commerce service.
- Processes email content dynamically.
- Sends emails using SMTP or external mail services.
- Logs email statuses and errors for monitoring.

#### **Communication Methods**
- **REST API** (Synchronous communication) for immediate email delivery.
- **RabbitMQ message queue** (Asynchronous communication) for improved performance.

## System Functionality

### **User Roles & Capabilities**
#### **Buyer**
- Register and log in.
- Browse and filter products by category and price.
- Add products to the shopping cart.
- Modify or remove cart items.
- Place orders and track their status.
- Submit product reviews and ratings.

#### **Administrator**
- Manage users (CRUD operations and role assignments).
- Manage products (CRUD operations, filtering, and promotions).
- Handle orders (CRUD operations and shipment tracking).
- Create and manage sales promotions.
- Generate monthly sales reports and business insights.
- Send invoices via email notifications.

## Technologies Used
- **Spring Boot** (for both microservices)
- **Spring Data JPA** (for database interactions)
- **PostgreSQL** (as the database management system)
- **RabbitMQ** (for asynchronous communication)
- **Thymeleaf / Email Templates** (for formatting dynamic email content)
- **Lombok** (for reducing boilerplate code in entities and services)

## System Architecture Design
### **Database Schema Overview**
- **User Table**: Stores user credentials and roles.
- **Product Table**: Stores product details, categories, and stock levels.
- **Order & OrderItem Tables**: Manages user purchases and related details.
- **ShoppingCart Table**: Temporarily stores cart items.
- **Review Table**: Stores user-generated product feedback.
- **Sale Table**: Manages ongoing promotions.
- **EmailRequest Table**: Logs outgoing email requests and statuses.

### **Sequence Diagrams**
#### **Order Placement Process**
1. User adds items to the cart.
2. User proceeds to checkout and places an order.
3. The system validates the order and updates inventory.
4. An `EmailRequest` is generated and sent to the Email Service.
5. The Email Service processes and sends the confirmation email.

#### **Add to Cart Process**
1. User selects a product.
2. System checks stock availability.
3. Product is added to the shopping cart.
4. Cart total is updated dynamically.


## Running the Application
1. Start PostgreSQL and RabbitMQ.
2. Run the E-Commerce Service. 
3. Run the Email Service.
4. Perform actions such as placing an order or managing products.
5. Email notifications are processed accordingly.

## Future Enhancements
- Implement additional notification channels such as SMS and push notifications.
- Improve monitoring with **Spring Boot Actuator** and **Prometheus/Grafana**.
- Introduce AI-driven product recommendations.
- Deploy using Kubernetes for enhanced scalability.

---
This project is designed for scalability, modularity, and high availability, making it adaptable for future business expansions.

