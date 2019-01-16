<?php

class Database {

    public $new_id;

    public function __construct($user, $password, $database, $host = 'localhost') {
        $this->user = $user;
        $this->password = $password;
        $this->database = $database;
        $this->host = $host;
    }

    protected function connect() {
        $conn = new mysqli($this->host, $this->user, $this->password, $this->database);
        if ($conn->connect_errno) {
            echo "Failed connecting to database: " . $conn->connect_errno . ", " .
            $conn->connect_error;
        }
        $conn->set_charset("utf8");
        if ($conn->connect_errno) {
            echo "Charset setup failed: " . $conn->connect_errno . ", " .
            $conn->connect_error;
        }
        return $conn;
    }

    public function query($query) {
        $db = $this->connect();
        $result = $db->query($query);

        while ($row = $result->fetch_object()) {
            $results[] = $row;
        }

        return $results;
    }

    // TODO exception handling
    public function preparedQuery($query, $params = array()) {
        $db = $this->connect();
        $statement = $db->stmt_init();
        $statement->prepare($query);

        $format = "";
        $paramRefs = array();
        for ($i = 0; $i < count($params); $i++) {
            $format .= "s";
            $paramRefs[] = &$params[$i];
        }
        array_unshift($paramRefs, $format);

        call_user_func_array(array($statement, 'bind_param'), $paramRefs);

        $statement->execute();
        $meta = $statement->result_metadata() or null;
        $result = array();
        if ($meta) {
            $fields = $meta->fetch_fields();
            foreach ($fields as $field) {
                $fieldnames[] = $field->name;
            }
            // container for one row
            $resultRow = array_fill(0, count($fieldnames), null);
            for ($i = 0; $i < count($fieldnames); $i++) {
                $resultRow[$i] = &$resultRow[$i];
            }

            call_user_func_array(array($statement, "bind_result"), $resultRow);

            // dereference
            while ($statement->fetch()) {
                $deref = array();
                foreach ($resultRow as $value) {
                    $deref[] = $value;
                }
                $result[] = array_combine($fieldnames, $deref);
            }
        } else {
            $result = $statement->affected_rows;
            $this->new_id = $db->insert_id;
        }
        $statement->close();
        $db->close();
        return $result;
    }
}

