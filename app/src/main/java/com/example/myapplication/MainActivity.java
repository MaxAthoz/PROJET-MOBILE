package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    private Button btnInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceuil);
        btnInscription = (Button)findViewById(R.id.inscription);
        btnInscription.setOnClickListener(this);
    }





    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.inscription :
                Intent versInscription = new Intent(this, Inscription.class);
                startActivity(versInscription);
                break;
        }

    }
}
