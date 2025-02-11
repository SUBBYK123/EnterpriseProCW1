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
    // Delete user from the database
    $sql_delete = "DELETE FROM user WHERE id = $id";

    // If deletion is successful, display success message and exit
    if ($mysqli->query($sql_delete) === TRUE) {
        echo "User deleted successfully.";
        exit();
    } else {
        // If deletion fails, display error message and exit
        echo "Error deleting user: " . $mysqli->error;
        exit();
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="./style.css"/>
    <title>Delete User</title>
</head>
<body>

<nav class="navbar">
    <!-- Navbar content -->
</nav>

<h2>Delete User</h2>

<!-- Form to confirm user deletion -->
<form method="POST" onsubmit="return confirm('Are you sure you want to delete this user?');">
    <!-- Display user's first and second name -->
    <p>Are you sure you want to delete user: <?= $user['first_name'] . ' ' . $user['second_name'] ?>?</p>
    <!-- Submit button to confirm deletion -->
    <button type="submit">Delete</button>
</form>

<footer>
    <!-- Footer content -->
</footer>

</body>
</html>
