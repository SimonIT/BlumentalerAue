-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 06. Dezember 2011 um 09:10
-- Server Version: 5.5.8
-- PHP-Version: 5.3.5


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `Blumenthal`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `pflanzenmittel`
--

CREATE TABLE IF NOT EXISTS `pflanzenmittel` (
  `Nr` varchar(3) DEFAULT NULL,
  `Mittel` varchar(12) DEFAULT NULL
);

--
-- Daten für Tabelle `pflanzenmittel`
--

INSERT INTO `pflanzenmittel` (`Nr`, `Mittel`) VALUES
('1', 'Altrazin'),
('2', 'Terbutylazin'),
('3', 'Simazin');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `umweltdaten`
--

CREATE TABLE IF NOT EXISTS `umweltdaten` (
  `index` varchar(5) DEFAULT NULL,
  `1` decimal(18,3) DEFAULT NULL,
  `2` decimal(18,3) DEFAULT NULL,
  `3` decimal(18,3) DEFAULT NULL
);

--
-- Daten für Tabelle `umweltdaten`
--

INSERT INTO `umweltdaten` (`index`, `1`, `2`, `3`) VALUES
('1', 0.150, 0.010, 0.010),
('2', 0.070, 0.010, 0.010),
('3', 0.060, 0.080, 0.010),
('4', 0.030, 0.060, 0.010),
('5', 0.030, 0.080, 0.040),
('6', 0.020, 0.270, 0.040),
('7', 0.140, 0.120, 0.020),
('8', 0.070, 0.070, 0.010),
('9', 0.050, 0.100, 0.010),
('10', 0.030, 0.090, 0.010),
('11', 0.020, 0.030, 0.010),
('12', 0.040, 0.030, 0.010),
('13', 0.030, 0.030, 0.010),
('14', 0.030, 0.030, 0.010),
('15', 0.030, 0.030, 0.140),
('16', 0.020, 0.030, 0.100),
('17', 0.080, 0.160, 0.080),
('18', 0.090, 1.100, 0.070),
('19', 0.110, 0.700, 0.090),
('20', 0.100, 0.550, 0.050),
('21', 0.080, 0.290, 0.020),
('22', 0.080, 0.230, 0.020),
('23', 0.040, 0.140, 0.020),
('24', 0.060, 0.250, 0.040),
('25', 0.060, 0.250, 0.020),
('26', 0.040, 0.240, 0.010),
('27', 0.040, 0.140, 0.010),
('28', 0.030, 0.090, 0.010),
('29', 0.020, 0.090, 0.010),
('30', 0.020, 0.080, 0.010),
('31', 0.020, 0.100, 0.010),
('32', 0.030, 0.090, 0.010),
('33', 0.030, 0.090, 0.010),
('34', 0.030, 0.070, 0.010),
('35', 0.310, 0.130, 0.490),
('36', 0.070, 0.080, 0.020),
('37', 0.050, 0.060, 0.010),
('38', 0.040, 0.070, 0.060),
('39', 0.130, 0.110, 0.500),
('40', 0.040, 0.070, 0.020),
('41', 0.030, 0.070, 0.020),
('42', 0.100, 0.200, 0.210),
('43', 0.040, 0.370, 0.090),
('44', 0.090, 0.240, 0.080),
('45', 0.070, 0.170, 0.040),
('46', 0.190, 0.130, 0.030);
