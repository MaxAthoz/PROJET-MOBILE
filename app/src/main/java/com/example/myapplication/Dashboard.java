package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity implements View.OnClickListener{

    private Button btnListeEspace;
    private Button btnAjoutEspace;
    private Button btnHistorique;
    private Button btnCalendrier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        btnListeEspace = (Button)findViewById(R.id.button_list_espaces);
        btnListeEspace.setOnClickListener(this);
        btnAjoutEspace = (Button)findViewById(R.id.button_ajout_espace);
        btnAjoutEspace.setOnClickListener(this);
        btnHistorique = (Button)findViewById(R.id.button_historique);
        btnHistorique.setOnClickListener(this);
        btnCalendrier = (Button)findViewById(R.id.button_calendrier);
        btnCalendrier.setOnClickListener(this);
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
                break;
            case R.id.action_liste_espaces :
                Intent versListeEspace = new Intent(this, ListeEspace.class);
                startActivity(versListeEspace);
                break;
            case R.id.action_add_espace :
                Intent versAjoutEspace = new Intent(this, AjoutEspace.class);
                startActivity(versAjoutEspace);
                break;
            case R.id.action_historique :
                Intent versHistorique = new Intent(this, Historique.class);
                startActivity(versHistorique);
                break;
            case R.id.action_calendrier :
                Intent versCalendrier = new Intent(this, Calendrier.class);
                startActivity(versCalendrier);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_list_espaces:
                Intent versListeEspace = new Intent(this, ListeEspace.class);
                startActivity(versListeEspace);
                break;
            case R.id.button_ajout_espace :
                Intent versAjoutEspace = new Intent(this, AjoutEspace.class);
                startActivity(versAjoutEspace);
                break;
            case R.id.button_historique :
                Intent versHistorique = new Intent(this, Historique.class);
                startActivity(versHistorique);
                break;
            case R.id.button_calendrier :
                Intent versCalendrier = new Intent(this, Calendrier.class);
                startActivity(versCalendrier);
                break;
        }

    }
}
