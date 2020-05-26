package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entities.User;

public class Historique extends AppCompatActivity {

    private User loggedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        loggedUser = (User) getIntent().getSerializableExtra("User");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // affichage des boutons du menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_dashboard :
                Intent versDashboard = new Intent(this, Dashboard.class);
                versDashboard.putExtra("User",loggedUser);
                startActivity(versDashboard);
                break;
            case R.id.action_liste_espaces :
                Intent versListeEspace = new Intent(this, ListeEspace.class);
                versListeEspace.putExtra("User",loggedUser);
                startActivity(versListeEspace);
                break;
            case R.id.action_add_espace :
                Intent versAjouterEspace = new Intent(this, AjoutEspace.class);
                versAjouterEspace.putExtra("User",loggedUser);
                startActivity(versAjouterEspace);
                break;
            case R.id.action_historique :
                break;
            case R.id.action_calendrier :
                Intent versCalendrier = new Intent(this, Calendrier.class);
                versCalendrier.putExtra("User",loggedUser);
                startActivity(versCalendrier);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
