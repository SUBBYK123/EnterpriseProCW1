-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 03, 2024 at 05:18 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `login_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `first_name` varchar(128) NOT NULL,
  `second_name` varchar(128) NOT NULL,
  `email_address` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `is_admin` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `first_name`, `second_name`, `email_address`, `password_hash`, `is_admin`) VALUES
(1, 'Mustafa', 'Kamran', 'mustafakamran46@hotmail.com', '$2y$10$PdCmGpFiV2pJNZ2ia/htHuVt3DEKhO60V7BVZpn0P.l.DdWu8Nho.', 0),
(2, 'dave', 'example', 'dav@example.com', '$2y$10$qldDzd7RamiOverU3xSaGuO6lPwJH7ldq8ahKl.j4BOC3/FCVxFA2', 0),
(3, 'hello', 'world', 'hw@example.com', '$2y$10$44ckVvCiwcDMgEoRwH75b.g69BY0mmxGbB7c4An.ld2DBeHEtu2Bm', 0),
(11, 'hd', 'doc', 'hdoc@example.com', '$2y$10$8mRcgkdagzJq2syZ6GvKI.oDZk3v0BCvvoS6omh5PfUr4T.wo2Nyu', 0),
(12, 'kabir', 'sohag', 'k.sohag@example.com', '$2y$10$AFnydW9QDH5rMfsGRt22d.4ssiGfoFqlLCrs2iaB.B8nvVHuu/wjK', 1),
(13, 'ehti', 'nafs', 'e.nafs@example.com', '$2y$10$rAxyxjHLjP8Osi5/W257p.LBMtL.NqQgfrJPJywSoEP1FwZbIV4Lm', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email_address` (`email_address`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
