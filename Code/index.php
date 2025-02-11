<?php
// Start the session to check if the user is logged in
session_start();

// Check if the user is logged in
if (isset($_SESSION["user_id"])) {
    // Require the database connection file to establish connection
    $mysqli = require __DIR__ . "/database.php";

    // Fetch user details from the database based on the session user ID
    $sql = "SELECT * FROM user WHERE id = {$_SESSION["user_id"]}";
    $result = $mysqli->query($sql);
    $user = $result->fetch_assoc();

    // Check if the user is an admin
    $isAdminMessage = "";
    if ($user["is_admin"] == 1) {
        $isAdminMessage = ", you are the admin.";
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
    <title>Login Or signup Page</title>
</head>
<body>
<!-- Navigation bar -->
<nav class="navbar">
      <div class="navdiv">
        <div class="logo">
          <!-- Link to home page with logo -->
          <a href="index.html">
            <img src="./images/logo.png" alt="Logo for Sports Online." />
          </a>
        </div>
        <!-- Navigation links -->
        <ul>
          <li><a href="index.html">HOME</a></li>
          <li><a href="products.html">PRODUCTS</a></li>
          <li><a href="aboutUs.html">ABOUT US</a></li>
          <li><a href="contact.html">CONTACT</a></li>
        </ul>
        <!-- Header icons -->
        <div class="header-icons">
          <a href="#"
            ><img
              src="./images/shopping-cart.png"
              alt="picture of a shopping cart."
          /></a>
          <a href="index.php"
            ><img src="./images/user.png" alt="photo of a user."
          /></a>
        </div>
      </div>
    </nav>
<h1>Log In</h1>

<?php if (isset($user)): ?>
    <!-- If user is logged in -->
    <div id="Login">
        <p style="text-align: center;">Hello <?= htmlspecialchars($user["first_name"]) ?><?= $isAdminMessage ?></p>

        <p style="text-align: center;"><a href="logout.php">Log Out</a></p>
    </div>
    <!-- Admin actions -->
    <?php if ($user["is_admin"] == 1): ?>
        <div id="user-list">
            <h2>User List</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <?php
                // Fetch all users from the database
                $sql_users = "SELECT * FROM user";
                $result_users = $mysqli->query($sql_users);

                if ($result_users->num_rows > 0) {
                    // Display user list with edit and delete options
                    while ($row = $result_users->fetch_assoc()) {
                        echo "<tr>";
                        echo "<td>" . $row['id'] . "</td>";
                        echo "<td>" . $row['first_name'] . "</td>";
                        echo "<td>" . $row['second_name'] . "</td>";
                        echo "<td>" . $row['email_address'] . "</td>";
                        echo "<td><a href='edit-user.php?id=" . $row['id'] . "'>Edit</a> | <a href='delete-user.php?id=" . $row['id'] . "'>Delete</a></td>";
                        echo "</tr>";
                    }
                }
                ?>
                </tbody>
            </table>
        </div>
    <?php endif; ?>
<?php else: ?>
    <!-- If user is not logged in, provide options to log in or sign up -->
    <p style="text-align: center;"><a href="login.php">Log In</a> or <a href="signup.html">Sign Up</a></p>
<?php endif; ?>

<<!-- Footer -->
<footer>
      <div class="site-footer">
        <div class="footer-content">
          <!-- Footer logo -->
          <div class="footer-logo">
            <a href="index.html"
              ><img src="./images/logo.png" alt="Your Logo"
            /></a>
          </div>
          <!-- Footer links -->
          <div class="footer-links">
            <a href="index.html">Home</a>
            <a href="aboutUs.html">About</a>
            <a href="products.html">Products</a>
            <a href="contact.html">Contact</a>
          </div>
          <!-- Social media links -->
          <div class="footer-social">
            <a href="https://www.facebook.com/SportsDirect/"
              ><img src="./images/facebook.png" alt="Facebook"
            /></a>
            <a href="https://twitter.com/sportsdirectuk?lang=en"
              ><img src="./images/twitter.png" alt="Twitter"
            /></a>
            <a
              href="https://uk.linkedin.com/company/sports-direct-international"
              ><img src="./images/linkedin.png" alt="LinkedIn"
            /></a>
          </div>
        </div>
        <!-- Copyright notice -->
        <p class="footer-copyright">
          &copy; 2024 Sports Online. All rights reserved.
        </p>
      </div>
    </footer>
</body>
</html>
