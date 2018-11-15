package com.example.domagoj.webechattin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.domagoj.webechattin.chat.Client;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Client client = new Client("161.53.120.171", 51345);
        client.execute();

    }

    public void onClickLoginButton(View view){
        Intent intent = new Intent(this, HomescreenActivity.class);
        startActivity(intent);
    }

    public void onClickRegistrationButton(View view){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
