-- =====================================================
-- DBMS PRACTICAL EXAM: HOSPITAL MANAGEMENT SYSTEM
-- =====================================================

-- =====================================================
-- PART 1: DATA DEFINITION LANGUAGE (DDL)
-- =====================================================

-- 1. Create the database
CREATE DATABASE HospitalDB;
USE HospitalDB;

-- 2. Create the Patients table
CREATE TABLE Patients (
    PatientID   INT PRIMARY KEY,
    FullName    VARCHAR(100) NOT NULL,
    Email       VARCHAR(100) UNIQUE,
    DateOfBirth DATE
);

-- Create the Appointments table
CREATE TABLE Appointments (
    AppointmentID   INT PRIMARY KEY,
    PatientID       INT,
    DoctorName      VARCHAR(100),
    AppointmentDate DATE,
    Fee             DECIMAL(10,2),
    FOREIGN KEY (PatientID) REFERENCES Patients(PatientID)
);

-- 3. Alter Patients table to add BloodGroup column
ALTER TABLE Patients
ADD BloodGroup VARCHAR(5);


-- =====================================================
-- PART 2: DATA INSERTION
-- =====================================================

-- 1. Insert patient records (complete attributes)
INSERT INTO Patients (PatientID, FullName, Email, DateOfBirth, BloodGroup) VALUES
(1, 'Rahim Uddin',  'rahim@example.com',  '1990-05-14', 'A+'),
(2, 'Karima Begum', 'karima@example.com', '1985-11-02', 'B+'),
(3, 'Sabbir Hasan', 'sabbir@example.com', '2000-03-27', 'AB-');

-- 2. Insert appointment records (Patient 1 has multiple appointments)
INSERT INTO Appointments (AppointmentID, PatientID, DoctorName, AppointmentDate, Fee) VALUES
(1, 1, 'Dr. Ahmed',   '2023-12-15', 500.00),
(2, 2, 'Dr. Sultana', '2024-02-10', 800.00),
(3, 1, 'Dr. Karim',   '2024-03-05', 600.00),
(4, 3, 'Dr. Ahmed',   '2024-04-20', 700.00);


-- =====================================================
-- PART 3: DATA UPDATING
-- =====================================================

-- 1. Update BloodGroup of patient with PatientID = 1
UPDATE Patients
SET BloodGroup = 'O+'
WHERE PatientID = 1;

-- 2. Increase Fee by 15% for appointments after 2024-01-01
UPDATE Appointments
SET Fee = Fee * 1.15
WHERE AppointmentDate > '2024-01-01';


-- =====================================================
-- PART 4: DATA DELETION & JOINS
-- =====================================================

-- 1. Delete the appointment with AppointmentID = 2
DELETE FROM Appointments
WHERE AppointmentID = 2;

-- 2. INNER JOIN: patient name, doctor, date and fee
SELECT P.FullName,
       A.DoctorName,
       A.AppointmentDate,
       A.Fee
FROM Patients P
INNER JOIN Appointments A
        ON P.PatientID = A.PatientID;


-- =====================================================
-- PART 5: ADVANCED QUERYING & TABLE TRUNCATION
-- =====================================================

-- 1. Total fees collected per patient (GROUP BY + SUM)
SELECT P.PatientID,
       P.FullName,
       SUM(A.Fee) AS TotalFeeSpent
FROM Patients P
INNER JOIN Appointments A
        ON P.PatientID = A.PatientID
GROUP BY P.PatientID, P.FullName;

-- 2. Truncate the Appointments table
-- DELETE FROM Appointments : DML command; removes rows one by one, can use a
--   WHERE clause to remove specific rows, can be rolled back (inside a
--   transaction), and does NOT reset the AUTO_INCREMENT counter.
-- TRUNCATE TABLE Appointments : DDL command; removes ALL rows at once by
--   dropping and recreating the table, cannot use WHERE, is much faster,
--   cannot be rolled back (auto-commit), and resets AUTO_INCREMENT to 1.
TRUNCATE TABLE Appointments;
