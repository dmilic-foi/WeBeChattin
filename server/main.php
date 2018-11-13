<?php

include_once "./Database.php";

$user = "air1813";
$password = "uv0K8QLWMo5qBnbr";
$database = "air1813";
$db = new Database($user, $password, $database);

$host = filter_input(INPUT_SERVER, 'SERVER_ADDR');
$port = 51345;


$socket = socket_create(AF_INET, SOCK_STREAM, 0) or die("Could not create socket\n");
socket_bind($socket, $host, $port) or die("Could not bind to socket\n");
socket_listen($socket, 3) or die("Could not set up socket listener\n");
$conn = socket_accept($socket) or die("Could not accept incoming connection\n");

do {
    $recv = socket_read($conn, 1024) or die("Could not read received message\n");
    echo $recv;
    $output = "All great.";
    socket_write($conn, $output, strlen ($output)) or die("Could not write output\n"); 
} while ($recv != "bye");

socket_close($conn);
socket_close($socket);

echo "Sockets closed.";
