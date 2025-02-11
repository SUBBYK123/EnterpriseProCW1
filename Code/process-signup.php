<?php

// Server-side validation
if (empty($_POST["first_name"])) {
    die("First Name is required");
}

if (empty($_POST["second_name"])){
    die("Second Name is required");
}

if ( ! filter_var($_POST["email_address"], FILTER_VALIDATE_EMAIL)){
    die("Valid Email Address is required");
}

if (strlen($_POST["password"]) < 8){
    die("Password must be at least 8 characters");
}

if ( ! preg_match("/[a-z]/i", $_POST["password"])) {
    die("Password must contain at least one letter");
}

if ( ! preg_match("/[0-9]/", $_POST["password"])) {
    die("Password must contain at least one number");
}

if ($_POST["password"] !== $_POST["password_confirmation"]) {
    die("Passwords must match");
}

// Establish database connection
$mysqli = require __DIR__ . "/database.php";

// Check if an admin already exists
$query = "SELECT COUNT(*) AS admin_count FROM user WHERE is_admin = 1";
$result = $mysqli->query($query);
$row = $result->fetch_assoc();
$adminExists = $row['admin_count'] > 0;

// Check if admin exists, if not, set is_admin flag to 1
$is_admin = $adminExists ? 0 : 1;

// Hash password
$password_hash = password_hash($_POST["password"], PASSWORD_DEFAULT);

// Prepare SQL statement
$sql = "INSERT INTO user (first_name, second_name, email_address, password_hash, is_admin)
        VALUES (?, ?, ?, ?, ?)";
$stmt = $mysqli->prepare($sql);

if (!$stmt) {
    die("SQL error: " . $mysqli->error);
}

// Bind parameters
$stmt->bind_param("ssssi",
                  $_POST["first_name"],
                  $_POST["second_name"],
                  $_POST["email_address"],
                  $password_hash,
                  $is_admin);

// Execute SQL statement
if ($stmt->execute()) {
    // Redirect to success page
    header("Location: signup-success.html");
    exit;
} else {
    if ($mysqli->errno === 1062) {
        die("Email address already taken");
    } else {
        die("Error: " . $mysqli->error);
    }
}

?>
