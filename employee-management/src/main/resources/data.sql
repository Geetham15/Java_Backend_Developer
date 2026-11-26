create database employee_management;
use employee_management;

CREATE TABLE employees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    department VARCHAR(100),
    salary DOUBLE
);

SHOW TABLES;

DESCRIBE employees;

INSERT INTO employees
(first_name, last_name, email, department, salary)
VALUES
('John', 'Smith', 'john@gmail.com', 'IT', 75000),
('Mary', 'Jane', 'mary@gmail.com', 'HR', 65000),
('David', 'Brown', 'david@gmail.com', 'Finance', 80000);

SELECT * FROM employees;

INSERT INTO employees
    (first_name, last_name, email, department, salary)

WITH RECURSIVE employee_numbers AS (
    SELECT 1 AS n

    UNION ALL

    SELECT n + 1
    FROM employee_numbers
    WHERE n < 100
)

SELECT
    CONCAT('Employee', n),
    CONCAT('LastName', n),
    CONCAT('employee', n, '@company.com'),

    CASE
        WHEN MOD(n, 5) = 0 THEN 'HR'
        WHEN MOD(n, 5) = 1 THEN 'IT'
        WHEN MOD(n, 5) = 2 THEN 'Finance'
        WHEN MOD(n, 5) = 3 THEN 'Marketing'
        ELSE 'Operations'
    END,

    40000 + (n * 750)

FROM employee_numbers;