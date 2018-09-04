-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 18, 2018 at 11:25 AM
-- Server version: 10.1.32-MariaDB
-- PHP Version: 7.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `institude_id_card`
--

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
  `id` int(11) NOT NULL,
  `title` varchar(50) NOT NULL,
  `path` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `images`
--

INSERT INTO `images` (`id`, `title`, `path`) VALUES
(7, 'fog.jpg', 'uploads/fog.jpg'),
(8, 'fog.jpg', 'uploads/fog.jpg'),
(9, 'fog.jpg', 'uploads/fog.jpg'),
(10, 'fogg.jpg', 'uploads/fogg.jpg'),
(11, 'fogg.jpg', 'uploads/fogg.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `intitude_details_tb`
--

CREATE TABLE `intitude_details_tb` (
  `IID` int(10) NOT NULL,
  `code` varchar(10) NOT NULL,
  `name_of_intitude` varchar(100) NOT NULL,
  `address` varchar(200) NOT NULL,
  `logo` text NOT NULL,
  `signature` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `intitude_details_tb`
--

INSERT INTO `intitude_details_tb` (`IID`, `code`, `name_of_intitude`, `address`, `logo`, `signature`) VALUES
(1, 'B101', 'Bal Vidyaamandir ', 'Pune', '', ''),
(2, 'C102', 'Podar International School', 'pune', '', ''),
(3, 'D201', 'Sai English School', 'Ichalkaranji', '', ''),
(4, 'E201', 'JSPM Institude', 'Pune', '', ''),
(9, 'Rcr', '', 'rcvr', 'logo/f', 'hh'),
(10, 'sdv', '', 'sdv', 'logo/f', 'hh');

-- --------------------------------------------------------

--
-- Table structure for table `student_detail_tb`
--

CREATE TABLE `student_detail_tb` (
  `SID` int(50) NOT NULL,
  `IID` int(10) NOT NULL,
  `picture` text NOT NULL,
  `name` varchar(50) NOT NULL,
  `class` varchar(10) NOT NULL,
  `division` varchar(10) NOT NULL,
  `dob` date NOT NULL,
  `blood_group` varchar(10) NOT NULL,
  `mobile` varchar(10) NOT NULL,
  `address` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `student_detail_tb`
--

INSERT INTO `student_detail_tb` (`SID`, `IID`, `picture`, `name`, `class`, `division`, `dob`, `blood_group`, `mobile`, `address`) VALUES
(6, 1, 'uploads/sdfv', 'sdfv', 'III', 'B', '2018-07-03', 'B+', '4564565465', 'sazdgfbgdb'),
(7, 3, 'uploads/sfdv', 'sfdv', 'IV', 'C', '2018-07-12', 'B+', '4564565465', 'fgb'),
(8, 3, 'uploads/sdv', 'sdv', 'III', 'C', '2018-07-09', 'B+', '4576768787', 'pune');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `images`
--
ALTER TABLE `images`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `intitude_details_tb`
--
ALTER TABLE `intitude_details_tb`
  ADD PRIMARY KEY (`IID`);

--
-- Indexes for table `student_detail_tb`
--
ALTER TABLE `student_detail_tb`
  ADD PRIMARY KEY (`SID`),
  ADD KEY `IID` (`IID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `images`
--
ALTER TABLE `images`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `intitude_details_tb`
--
ALTER TABLE `intitude_details_tb`
  MODIFY `IID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `student_detail_tb`
--
ALTER TABLE `student_detail_tb`
  MODIFY `SID` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `student_detail_tb`
--
ALTER TABLE `student_detail_tb`
  ADD CONSTRAINT `student_detail_tb_ibfk_1` FOREIGN KEY (`IID`) REFERENCES `intitude_details_tb` (`IID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
