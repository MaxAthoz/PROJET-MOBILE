package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entities.Espace;
import com.example.myapplication.Entities.User;

public class AjoutIndicateur extends AppCompatActivity {

    private User user;
    private Espace espace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_indicateur);
        user = (User) getIntent().getSerializableExtra("User");
        espace = (Espace) getIntent().getSerializableExtra("Espace");
    }

}
