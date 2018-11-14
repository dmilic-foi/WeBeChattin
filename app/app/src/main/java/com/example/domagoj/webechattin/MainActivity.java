package com.example.domagoj.webechattin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("SOCKETS: ", "Creating socket...");

                Socket sock;

                try {
                    sock = new Socket("161.53.120.171", 51345);
                    Log.d("SOCKETS: ", "Socket created.");
                } catch(Exception e) {
                    Log.d("SOCKETS: ", "Socket creation failed."+e);
                    return;
                }
                BufferedReader in;
                PrintWriter out;
                try {
                    in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    out = new PrintWriter(sock.getOutputStream(), true);
                    Log.d("SOCKETS: ", "Created socket streams.");
                } catch(IOException e) {
                    Log.d("SOCKETS: ", "Cant create socket streams");
                    return;
                }


                JSONObject json = new JSONObject();
                try {
                    json.put("phone_number", "0987654321");
                    json.put("chat", "0");
                    json.put("message", "Hello world");
                    Log.d("SOCKETS: ", "Encoded to JSON");
                } catch(JSONException e) {
                    Log.d("SOCKETS: ", "Encodint to JSON failed.");
                    return;
                }

                try {
                    Log.d("SOCKETS: ", in.readLine());
                } catch(IOException e) {
                    Log.d("SOCKETS: ", "Failed receiving first message.");
                    return;
                }

                while(true) {
                    try {
                        out.println(json);
                        Log.d("SOCKETS: ", in.readLine());
                    } catch(IOException e) {
                        Log.d("SOCKETS: ", "Failed sending/receiving message.");
                        return;
                    }
                }
            }
        }).start();

    }
}
