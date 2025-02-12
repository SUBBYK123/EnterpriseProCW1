<?php
//used to validate if a email has been already submitted in the the database or not
$mysqli = require __DIR__ . "/database.php";

$sql = sprintf("SELECT * FROM user
                WHERE email = '%s'",
                $mysqli->real_escape_string($_GET["email"]));
                
$result = $mysqli->query($sql);

$is_available = $result->num_rows === 0;

header("Content-Type: application/json");

echo json_encode(["available" => $is_available]);