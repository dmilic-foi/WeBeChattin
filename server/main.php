<?php

include_once "./Database.php";

$user = "air1813";
$password = "uv0K8QLWMo5qBnbr";
$database = "air1813";
$db = new Database($user, $password, $database);

$host = filter_input(INPUT_SERVER, 'SERVER_ADDR');
$port = 51345;


$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP) or die("Could not create socket\n");
socket_set_option($socket, SOL_SOCKET, SO_REUSEADDR, 1) or die("Could not set option to socket.\n");
socket_bind($socket, $host, $port) or die("Could not bind to socket\n");
socket_listen($socket, 3) or die("Could not set up socket listener\n");

$clients = array();

do {
    $read = array_merge(array($socket), $clients);
    $write = NULL;
    $except = NULL;
    $tv_sec = 5;
    if (!socket_select($read, $write, $except, $tv_sec)) {
        continue;
    }
    
    // Handle connections
    if (in_array($socket, $read)) {
        $conn = socket_accept($socket) or die("Could not accept incoming connection\n");
        $clients[] = $conn;
        $key = array_keys($clients, $conn);
        $msg = "All good\n";
        socket_write($conn, $msg, strlen($msg));
    }
    
    // Handle messages
    foreach ($clients as $key => $client) {
        if (in_array($client, $read)) {
            $recv = socket_read($client, 2048) or die("Could not read received message\n");
            $recv = json_decode($recv, true);
            
            $phone = $recv["phone_number"];
            $chat = $recv["chat"];
            $msg = $recv["message"];
            
            if ($msg == 'bye') {
                unset($clients[$key]);
                socket_close($client);
                break;
            }
            if ($msg == 'quit') {
                break 2;
            }
            
            $sql = "INSERT INTO message VALUES('$phone', $chat, DEFAULT, '$msg');";
            
            $answer = "Client {$key}: $sql.\n";
            socket_write($client, $answer, strlen($answer));
        }
    }
    
} while(1);

socket_close($socket);
