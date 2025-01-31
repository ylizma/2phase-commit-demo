Here's a **simple README** for your project:  

---

# **Distributed University Management System – 2PC Implementation**  

## **📌 Project Overview**  
This project implements a **Distributed University Management System** using the **2-Phase Commit (2PC) protocol** to ensure atomic transactions across multiple microservices. It simulates a university enrollment process where multiple services (Student, Course, and Billing) must complete their tasks **consistently** and **reliably**.

## **🔹 What is a Distributed Transaction?**  
A **distributed transaction** spans multiple microservices or databases, requiring **coordination** to maintain **data consistency**. Since each service manages its own data, a failure in one service could lead to **inconsistent states** if not handled correctly.

## **🔹 What is 2-Phase Commit (2PC)?**  
**2PC (Two-Phase Commit)** is a protocol that ensures **all-or-nothing** execution in distributed transactions. It consists of two phases:  

1. **Prepare Phase** – The coordinator asks all services if they can commit the transaction.  
2. **Commit Phase** – If all services agree, the transaction is committed; otherwise, all changes are rolled back.  

This prevents **partial updates** and ensures **consistency** across multiple services.  

## **🛠️ Microservices Involved**
The system consists of **three microservices**:  

1. **Student Service** – Manages student records and enrollment eligibility.  
2. **Course Service** – Handles course availability and seat reservations.  
3. **Billing Service** – Processes payments and fees for enrolled courses.  

The **Coordinator Service** orchestrates transactions using the **2PC protocol**.

## **🔹 API Endpoints**
### **1️⃣ Coordinator Service**
- `POST /transaction` – Initiates a distributed transaction.  
- `POST /commit` – Commits the transaction.  
- `POST /rollback` – Rolls back the transaction.  

### **2️⃣ Student, Course, and Billing Services**
- `POST /prepare` – Checks if the service is ready for the transaction.  
- `POST /commit` – Confirms the transaction.  
- `POST /rollback` – Cancels the transaction if something goes wrong.  

## **🔹 Technologies Used**
- **Spring Boot 3** (Microservices)  
- **Spring MVC** (REST APIs)  
- **Spring Data JPA** (Database persistence)  
- **PostgreSQL/MySQL** (Database)  
- **RestTemplate** (Service communication)  

## **📌 How It Works**
1. The Coordinator generates a **transaction ID**.  
2. It sends a **prepare request** to all services.  
3. If all services respond **READY**, the Coordinator sends a **commit request**.  
4. If any service fails, the Coordinator triggers a **rollback request**.  

## **🚀 Future Improvements**
- Implement **message queues (Kafka/RabbitMQ)** for reliable service communication.  
- Add **timeouts and retries** for fault tolerance.  
- Implement **compensation transactions** for handling commit failures.  
