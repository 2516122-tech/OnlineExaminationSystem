-- ============================================
-- ONLINE EXAMINATION SYSTEM
-- Database: online_exam
-- ============================================


-- 1. CREATE DATABASE
CREATE DATABASE IF NOT EXISTS online_exam;

USE online_exam;


-- 2. CREATE STUDENT TABLE
CREATE TABLE IF NOT EXISTS exam_students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);


-- 3. CREATE QUESTIONS TABLE
CREATE TABLE IF NOT EXISTS questions (
    question_id INT PRIMARY KEY AUTO_INCREMENT,
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL
);


-- 4. CREATE EXAM RESULTS TABLE
CREATE TABLE IF NOT EXISTS exam_results (
    result_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (student_id)
    REFERENCES exam_students(student_id)
);


-- 5. CREATE ADMIN TABLE
CREATE TABLE IF NOT EXISTS exam_admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);


-- 6. INSERT SAMPLE QUESTIONS
INSERT INTO questions
(question_text, option_a, option_b, option_c, option_d, correct_option)
VALUES

('Which language is used for Java programming?',
 'Python', 'Java', 'C++', 'HTML', 'B'),

('Which keyword is used to create an object in Java?',
 'class', 'static', 'new', 'void', 'C'),

('Which database language is used to retrieve data?',
 'HTML', 'CSS', 'SQL', 'Java', 'C'),

('Which method is the entry point of a Java program?',
 'start()', 'main()', 'run()', 'execute()', 'B'),

('Which of the following is a Java loop?',
 'for', 'select', 'insert', 'display', 'A');


-- 7. DISPLAY ALL TABLES
SHOW TABLES;


-- 8. VIEW STUDENT RECORDS
SELECT * FROM exam_students;


-- 9. VIEW QUESTIONS
SELECT * FROM questions;


-- 10. VIEW EXAMINATION RESULTS
SELECT * FROM exam_results;


-- 11. VIEW ADMIN RECORDS
SELECT admin_id, username
FROM exam_admins;


-- ============================================
-- END OF ONLINE EXAMINATION SYSTEM DATABASE
-- ============================================