package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class Calendrier extends AppCompatActivity {
    private static final String CAT = "IME4";


    public void alerter(String s) {

        Log.i(CAT,s);
        Toast t = Toast.makeText(this,s,Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier);

        final CalendarView calendarView = findViewById(R.id.calendarView);
        Date date = new Date();
        final long millis = date.getTime();
        calendarView.setDate(millis);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(millis));

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //long changedMillis = view.getDate();
                //calendarView.setDate(changedMillis);
                alerter((year)+"/"+(month+1)+"/"+(dayOfMonth));
            }
        });
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
                startActivity(versDashboard);
                break;
            case R.id.action_liste_espaces :
                Intent versListeEspace = new Intent(this, ListeEspace.class);
                startActivity(versListeEspace);
                break;
            case R.id.action_add_espace :
                Intent versAjouterEspace = new Intent(this, AjoutEspace.class);
                startActivity(versAjouterEspace);
                break;
            case R.id.action_historique :
                Intent versHistorique = new Intent(this, Historique.class);
                startActivity(versHistorique);
                break;
            case R.id.action_calendrier :
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
