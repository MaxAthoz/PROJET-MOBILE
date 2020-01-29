package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Inscription extends AppCompatActivity implements  View.OnClickListener{

    private Button btnValidateInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription);
        btnValidateInscription = (Button)findViewById(R.id.button_inscription);
        btnValidateInscription.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_inscription :
                Intent versDashboard = new Intent(this, Dashboard.class);
                startActivity(versDashboard);
                break;
        }

    }
}
