<?php

// Database connection parameters
$host = "localhost"; // Hostname of the database server
$dbname = "login_db"; // Name of the database
$username = "root"; // Username to connect to the database
$password = ""; // Password to connect to the database

// Create a new mysqli object for database connection
$mysqli = new mysqli(
    hostname: $host,
    username: $username,
    password: $password,
    database: $dbname
);

// Check if there's an error in the database connection
if ($mysqli->connect_errno) {
    // If there's an error, terminate the script and display the error message
    die("Connection error: " . $mysqli->connect_error);
}

// Return the mysqli object for database operations
return $mysqli;
