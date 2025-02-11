<?php
// Start the session to check if the user is logged in
session_start();

// If user is not logged in, redirect to login page
if (!isset($_SESSION["user_id"])) {
    header("Location: login.php");
    exit();
}

// Require the database connection file to establish connection
$mysqli = require __DIR__ . "/database.php";

// Check if user ID is provided in the URL
if (isset($_GET['id'])) {
    $id = $_GET['id'];

    // Fetch user details from the database based on the provided ID
    $sql = "SELECT * FROM user WHERE id = $id";
    $result = $mysqli->query($sql);

    // If user is found, fetch user details
    if ($result->num_rows === 1) {
        $user = $result->fetch_assoc();
    } else {
        // If user not found, display error message and exit
        echo "User not found.";
        exit();
    }
} else {
    // If user ID is not provided in the URL, display error message and exit
    echo "User ID not provided.";
    exit();
}

// Check if form is submitted via POST method
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Collect and sanitize form data
    $first_name = $_POST['first_name'];
    $second_name = $_POST['second_name'];
    $email_address = $_POST['email_address'];

    // Update user details in the database
    $sql_update = "UPDATE user 
                    SET first_name = '$first_name', second_name = '$second_name', email_address = '$email_address' 
                    WHERE id = $id";

    // If update is successful, display success message
    if ($mysqli->query($sql_update) === TRUE) {
        echo "User updated successfully.";
    } else {
        // If update fails, display error message
        echo "Error updating user: " . $mysqli->error;
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="./style.css"/>
    <link rel="scriptFunction" href="./javaScript.js"/>
    <title>Edit User</title>
</head>
<body>

<nav class="navbar">
    <!-- Navbar content -->
</nav>

<h2>Edit User</h2>

<!-- Form to edit user details -->
<form method="POST">
    <label for="first_name">First Name:</label><br>
    <!-- Input field to edit first name, pre-filled with existing value -->
    <input type="text" id="first_name" name="first_name" value="<?= $user['first_name'] ?>" required><br>

    <label for="second_name">Second Name:</label><br>
    <!-- Input field to edit second name, pre-filled with existing value -->
    <input type="text" id="second_name" name="second_name" value="<?= $user['second_name'] ?>" required><br>

    <label for="email_address">Email Address:</label><br>
    <!-- Input field to edit email address, pre-filled with existing value -->
    <input type="email address" id="email_address" name="email_address" value="<?= $user['email_address'] ?>" required><br>

    <!-- Button to submit the form and update user details -->
    <button type="submit">Update</button>
    <!-- Button to navigate back to the index page -->
    <button><a href="index.php">Back</a></button>
</form>

<footer>
    <!-- Footer content -->
</footer>

</body>
</html>
