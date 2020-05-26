package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entities.Espace;
import com.example.myapplication.Entities.User;

import org.json.JSONArray;
import org.json.JSONObject;

public class AjoutIndicateur extends AppCompatActivity implements View.OnClickListener{

    private User user;
    private Espace espace;
    private Spinner spinnerValue;
    private EditText nom;
    private EditText initVal;
    private Button btnAddIndicateur;

    private String httpType;
    private JSONObject reqBody;

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
                res = AjoutIndicateur.this.gs.requete(qs[0],httpType,null);
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
            Intent versEditEspace = new Intent(getApplicationContext(), EditEspace.class);
            versEditEspace.putExtra("User",user);
            versEditEspace.putExtra("Espace",espace);
            startActivity(versEditEspace);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_indicateur);
        user = (User) getIntent().getSerializableExtra("User");
        espace = (Espace) getIntent().getSerializableExtra("Espace");
        spinnerValue =(Spinner)findViewById(R.id.spinner1);
        nom=(EditText) findViewById(R.id.editNomIndicateur);;
        initVal=(EditText)findViewById(R.id.editValeurInitiale);
        btnAddIndicateur = (Button)findViewById(R.id.button_add_indicateur);
        gs = (GlobalState) getApplication();
        btnAddIndicateur.setOnClickListener(this);
        httpType="POST";
        reqBody=null;
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_add_indicateur:
                String name = nom.getText().toString();
                String init = initVal.getText().toString();
                String type = spinnerValue.getSelectedItem().toString();
                if(name.isEmpty() || init.isEmpty() || type.isEmpty()){
                    gs.alerter("Saisir tout les champs");
                    return;
                }
                else{
                    JSONAsyncTask jsAT = new JSONAsyncTask();
                    jsAT.execute("indicateurs/?idEspace="+espace.getId()+"&type="+type+"&valeurInit="+init+"&nomIndicateur="+name);
                }
                break;
        }

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
                versDashboard.putExtra("User",user);
                startActivity(versDashboard);
                break;
            case R.id.action_liste_espaces :
                Intent versListe = new Intent(this, ListeEspace.class);
                versListe.putExtra("User",user);
                startActivity(versListe);
                break;
            case R.id.action_add_espace :
                Intent versAjouterEspace = new Intent(this, AjoutEspace.class);
                versAjouterEspace.putExtra("User",user);
                startActivity(versAjouterEspace);
                break;
            case R.id.action_historique :
                Intent versHistorique = new Intent(this, Historique.class);
                versHistorique.putExtra("User",user);
                startActivity(versHistorique);
                break;
            case R.id.action_calendrier :
                Intent versCalendrier = new Intent(this, Calendrier.class);
                versCalendrier.putExtra("User",user);
                startActivity(versCalendrier);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
