package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entities.Espace;
import com.example.myapplication.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AjoutEspace extends AppCompatActivity implements View.OnClickListener{

    private Button btnPostEspace;
    private EditText editName;
    private User loggedUser;

    GlobalState gs;


    class JSONAsyncTask extends AsyncTask<String, Void, JSONObject> {
        // Params, Progress, Result

        @Override
        protected void onPreExecute() { // S’exécute dans l’UI Thread
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... qs) {
            // pas d'interaction avec l'UI Thread ici
            // On exécute la requete
            String res = null;
            try {
                res = AjoutEspace.this.gs.requete(qs[0],"POST",null);
                JSONObject json;
                JSONArray jsonTab = new JSONArray(res);
                json = jsonTab.getJSONObject(0);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) { // S’exécute dans l’UI Thread
                Intent versListe = new Intent(getApplicationContext(), ListeEspace.class);
                versListe.putExtra("User",loggedUser);
                startActivity(versListe);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_espace);
        editName = (EditText) findViewById(R.id.editNomEspace);
        btnPostEspace = (Button)findViewById(R.id.button_sauvegarder_espace);
        btnPostEspace.setOnClickListener(this);

        loggedUser = (User) getIntent().getSerializableExtra("User");
        gs = (GlobalState) getApplication();
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
                break;
            case R.id.action_historique :
                Intent versHistorique = new Intent(this, Historique.class);
                versHistorique.putExtra("User",loggedUser);
                startActivity(versHistorique);
                break;
            case R.id.action_calendrier :
                Intent versCalendrier = new Intent(this, Calendrier.class);
                versCalendrier.putExtra("User",loggedUser);
                startActivity(versCalendrier);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_sauvegarder_espace:
                String name = editName.getText().toString();
                if(name.isEmpty()){
                    gs.alerter("Saisir un nom pour l'espace");
                    return;
                }
                else{
                    JSONAsyncTask jsAT = new JSONAsyncTask();
                    jsAT.execute("espaces/?nomEspace=" + name+"&idUser="+ loggedUser.getId());
                }
                break;
        }

    }

}
