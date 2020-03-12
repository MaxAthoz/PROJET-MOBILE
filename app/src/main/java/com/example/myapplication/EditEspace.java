package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.Entities.Espace;
import com.example.myapplication.Entities.User;

public class EditEspace extends AppCompatActivity {

    private Espace espace ;
    private User user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_espace);
        espace = (Espace) getIntent().getSerializableExtra("Espace");
        user = (User) getIntent().getSerializableExtra("User");
        TextView textIntentContent = (TextView) findViewById(R.id.textViewIntentContent);
        textIntentContent.setText(espace.toString()+user.toString());
    }
}
