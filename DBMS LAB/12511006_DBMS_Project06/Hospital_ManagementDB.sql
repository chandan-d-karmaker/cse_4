-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 02, 2026 at 04:48 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Hospital_ManagementDB`
--

-- --------------------------------------------------------

--
-- Table structure for table `Appointments`
--

CREATE TABLE `Appointments` (
  `appointment_id` int(11) NOT NULL,
  `patient_id` int(11) DEFAULT NULL,
  `doctor_id` int(11) DEFAULT NULL,
  `appointment_date` date NOT NULL,
  `status` varchar(20) DEFAULT 'Scheduled'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Appointments`
--

INSERT INTO `Appointments` (`appointment_id`, `patient_id`, `doctor_id`, `appointment_date`, `status`) VALUES
(1, 1, 1, '2026-02-01', 'Completed'),
(2, 2, 2, '2026-02-03', 'Scheduled'),
(3, 3, 1, '2026-02-05', 'Completed'),
(4, 1, 3, '2026-02-10', 'Scheduled');

-- --------------------------------------------------------

--
-- Table structure for table `Doctors`
--

CREATE TABLE `Doctors` (
  `doctor_id` int(11) NOT NULL,
  `doctor_name` varchar(100) NOT NULL,
  `specialization` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Doctors`
--

INSERT INTO `Doctors` (`doctor_id`, `doctor_name`, `specialization`, `phone`) VALUES
(1, 'Dr. Tariq Ahmed', 'Cardiology', '01711112233'),
(2, 'Dr. Nusrat Jahan', 'Pediatrics', '01822223344'),
(3, 'Dr. Rafiqul Islam', 'Neurology', '01933334455'),
(4, 'Dr. Farhana Chowdhury', 'Dermatology', '01544445566');

-- --------------------------------------------------------

--
-- Table structure for table `Patients`
--

CREATE TABLE `Patients` (
  `patient_id` int(11) NOT NULL,
  `patient_name` varchar(100) NOT NULL,
  `age` int(11) NOT NULL,
  `gender` varchar(10) NOT NULL,
  `contact_number` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `Patients`
--

INSERT INTO `Patients` (`patient_id`, `patient_name`, `age`, `gender`, `contact_number`) VALUES
(1, 'Abul Kalam', 45, 'Male', '01755556677'),
(2, 'Sultana Razia', 29, 'Female', '01866667788'),
(3, 'Tanvir Hossain', 12, 'Male', '01977778899'),
(4, 'Mehedi Hasan', 60, 'Male', '01588889900');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Appointments`
--
ALTER TABLE `Appointments`
  ADD PRIMARY KEY (`appointment_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `doctor_id` (`doctor_id`);

--
-- Indexes for table `Doctors`
--
ALTER TABLE `Doctors`
  ADD PRIMARY KEY (`doctor_id`);

--
-- Indexes for table `Patients`
--
ALTER TABLE `Patients`
  ADD PRIMARY KEY (`patient_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Appointments`
--
ALTER TABLE `Appointments`
  MODIFY `appointment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `Doctors`
--
ALTER TABLE `Doctors`
  MODIFY `doctor_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `Patients`
--
ALTER TABLE `Patients`
  MODIFY `patient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Appointments`
--
ALTER TABLE `Appointments`
  ADD CONSTRAINT `Appointments_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `Patients` (`patient_id`) ON DELETE SET NULL,
  ADD CONSTRAINT `Appointments_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `Doctors` (`doctor_id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
