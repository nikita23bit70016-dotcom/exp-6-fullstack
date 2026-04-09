# Experiment 6: Configure JPA & Hibernate with MySQL

This repository contains completely functioning code for Experiment 6.

## Features implemented:
1. **Database Configuration:** Configured in `src/main/resources/application.properties` using MySQL. (It includes `ddl-auto=update` and `show-sql=true`).
2. **Relationships Modelled:**
   - **One-to-Many & Many-to-One:** `Category` (One) to `Product` (Many).
   - **Many-to-Many:** `User` (Many) to `Role` (Many).
3. **Custom JPQL queries & Pagination:**
   - Filters products by price range with pagination and sorting.
   - Filters users by role name with pagination and sorting.
4. **Data Bootstrapping:**
   - The `DataInitializer.java` file gets executed automatically on app startup. It inserts mock records and triggers the paginated custom queries, printing the result natively over the console so that the Hibernate generated SQL can be analyzed easily.

## Prerequisites
1. Open your MySQL server (via XAMPP, Workbench, etc.).
2. Ensure you have a username `root` and password `root` with port `3306`. (If your DB credentials vary, you can easily change them inside `application.properties`).

## How to run
1. Navigate to the project root folder in your terminal.
2. Run the application (if you have maven configured natively) using:
   ```bash
   mvn spring-boot:run
   ```
3. Alternatively, open this project structure inside IntelliJ IDEA or Eclipse and run `Exp6Application.java`.

## Checking Output & Query Analysis
Once running, check the console output. You will notice:
- Hibernate auto-creating the required tables (`categories`, `products`, `users`, `roles`, `user_roles`).
- Output showing inserted data.
- The actual executed SQL generated from the Custom JPQL queries due to `spring.jpa.show-sql=true`.
