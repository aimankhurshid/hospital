-- Use the desired database
USE user_management;

-- Drop the Users table if it exists
DROP TABLE IF EXISTS Users;

-- Create the Users table with the new structure
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    bmi DECIMAL(5, 2)
);

-- Verify the table structure
DESCRIBE Users;

-- Verify the table content
SELECT * FROM Users;
