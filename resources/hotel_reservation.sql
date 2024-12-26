-- Create the database
CREATE DATABASE IF NOT EXISTS hotel_db;

-- Use the database
USE hotel_db;

-- Create the reservations table
CREATE TABLE IF NOT EXISTS reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(50) NOT NULL,
    room_number INT NOT NULL,
    contact_number VARCHAR(15) NOT NULL,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
