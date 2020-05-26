package com.example.myapplication;

import android.content.Intent;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entities.Espace;
import com.example.myapplication.Entities.Indicateur;
import com.example.myapplication.Entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditIndicateur extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private Indicateur indicateur;
    private Espace espace;


    private EditText nomIndicateur;
    private EditText valeurInit;
    private Spinner spinnerType;

    private Button btnSuppIndicateur;
    private Button btnSaveIndicateur;

    private String httpType;
    private JSONObject reqBody;

    class JSONAsyncTask extends AsyncTask<String, Void, JSONObject> {
        // Params, Progress, Result

        @Override
        protected void onPreExecute() { // S’exécute dans l’UI Thread
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... qs) {
            String res = null;
            try {
                res = EditIndicateur.this.gs.requete(qs[0], httpType, reqBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject json;
                if (indicateur == null) {
                    JSONArray jsonTab = new JSONArray(res);
                    json = jsonTab.getJSONObject(0);
                } else {
                    json = new JSONObject(res);
                }
                return json;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject jsonObject) { // S’exécute dans l’UI Thread
            if (jsonObject != null) {
                super.onPostExecute(jsonObject);

                Gson gson = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();
                try {
                    JSONArray jsa = jsonObject.getJSONArray("indicateurs");
                    System.out.println(jsa);
                    JSONObject jsonIndicateur = jsa.getJSONObject(0);
                    String s = jsonIndicateur.toString();
                    Indicateur ind = gson.fromJson(s, Indicateur.class);
                    valeurInit.setText(ind.getValeurInit());
                    nomIndicateur.setText(ind.getNomIndicateur());
                    spinnerType.setSelection(getIndex(spinnerType, ind.getType()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (httpType == "DELETE" || httpType == "PUT") {
                Intent versEditEspace = new Intent(getApplicationContext(), EditEspace.class);
                versEditEspace.putExtra("User", user);
                versEditEspace.putExtra("Espace", espace);
                startActivity(versEditEspace);
            }
        }
    }

    GlobalState gs;


    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_indicateur);
        espace = (Espace) getIntent().getSerializableExtra("Espace");
        user = (User) getIntent().getSerializableExtra("User");
        indicateur = (Indicateur) getIntent().getSerializableExtra("Indicateur");
        gs = (GlobalState) getApplication();

        nomIndicateur = (EditText) findViewById(R.id.editNomIndicateur);
        valeurInit = (EditText) findViewById(R.id.editValeurInitiale);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);

        btnSaveIndicateur = (Button) findViewById(R.id.button_sauvegarder_indicateur);
        btnSaveIndicateur.setOnClickListener(this);
        btnSuppIndicateur = (Button) findViewById(R.id.button_supprimer_indicateur);
        btnSuppIndicateur.setOnClickListener(this);

        EditIndicateur.JSONAsyncTask jsAT = new JSONAsyncTask();
        httpType = "GET";
        reqBody = null;
        jsAT.execute("indicateurs/" + indicateur.getId());
    }


    public void onClick(View v) {
        EditIndicateur.JSONAsyncTask jsAT = new JSONAsyncTask();
        switch (v.getId()) {
            case R.id.button_sauvegarder_indicateur:
                httpType = "PUT";
                reqBody = null;
                reqBody = new JSONObject();
                String name = nomIndicateur.getText().toString();
                String init = valeurInit.getText().toString();
                String type = spinnerType.getSelectedItem().toString();
                if (name.isEmpty() || init.isEmpty() || type.isEmpty()) {
                    gs.alerter("Saisir tout les champs");
                    return;
                }
                try {
                    reqBody.put("idEspace", espace.getId());
                    reqBody.put("type", type);
                    reqBody.put("valeurInit", init);
                    reqBody.put("nomIndicateur", name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsAT.execute("indicateurs/" + indicateur.getId());
                break;
            case R.id.button_supprimer_indicateur:
                httpType = "DELETE";
                reqBody = null;
                jsAT.execute("indicateurs/" + indicateur.getId());
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
                Intent versListeEspace = new Intent(this, ListeEspace.class);
                versListeEspace.putExtra("User",user);
                startActivity(versListeEspace);
                break;
            case R.id.action_add_espace :
                Intent versAjoutEspace = new Intent(this, AjoutEspace.class);
                versAjoutEspace.putExtra("User",user);
                startActivity(versAjoutEspace);
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
