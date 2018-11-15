package com.example.domagoj.webechattin.chat;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends AsyncTask<Void, Void, Void> {

    String serverAddress;
    int serverPort;
    String response;
    TextView textResponse;

    Socket sock;
    BufferedReader in;
    PrintWriter out;

    // TODO change type when GUI is finished
    Object textFieldSend;
    Object textFieldReceive;

    public Client(String addr, int port) {
        serverAddress = addr;
        serverPort = port;
    }

    private String createMessage(String message) {
        JSONObject json = new JSONObject();
        try {
            json.put("phone_number", "0987654321");
            json.put("chat", "0");
            json.put("message", message);
            Log.d("JSON: ", "Encoded to JSON");
        } catch(Exception e) {
            Log.d("JSON: ", "Encoding to JSON failed.");
        }

        return json.toString();
    }

    private void sendMessage(String message) {
        String msg = createMessage(message);
        String response;
        try {
            out.println(msg);
            do {
                response = in.readLine();
                Log.d("MSG", response);
            } while(false /*response != "OK"*/);
        } catch(IOException e) {
            Log.d("MSG", "Failed to read response.");
        }
    }

    private void receiveMessage() {

        // get messages since last sync
        // update last sync to now
        // decode message
        // write to textBox

        return;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            sock = new Socket(serverAddress, serverPort);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);
            Log.d("SOCKETS", "Socket created.");

            response = in.readLine();
            Log.d("SOCKETS", response);

            // TODO send message on signal
            sendMessage("We are the knights who say NI.");

            while(true) {
                receiveMessage();
            }
        } catch(IOException e) {
            Log.d("SOCKETS", "Socket creation failed."+e);
        }

        return null;
    }
}
