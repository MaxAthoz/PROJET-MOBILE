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

import com.example.myapplication.Entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Inscription extends AppCompatActivity implements  View.OnClickListener{

    private Button btnValidateInscription;
    private EditText login;
    private EditText prenom;
    private EditText nom;
    private EditText pass;
    private EditText passConfirm;
    GlobalState gs;

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
                res = Inscription.this.gs.requete(qs[0],"POST",null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject jsonObject) { // S’exécute dans l’UI Thread
            if (jsonObject != null) {
                super.onPostExecute(jsonObject);
            }
            gs.alerter("Veuillez vous connecter");
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription);
        btnValidateInscription = (Button)findViewById(R.id.button_inscription);
        btnValidateInscription.setOnClickListener(this);
        login= findViewById(R.id.editPseudo);
        prenom= findViewById(R.id.editPrenom);
        nom= findViewById(R.id.editNom);
        pass= findViewById(R.id.editPasse);
        passConfirm= findViewById(R.id.editPasseConfirm);

        gs = (GlobalState) getApplication();
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_inscription :
                String login = this.login.getText().toString();
                String nom = this.nom.getText().toString();
                String prenom = this.prenom.getText().toString();
                String pass = this.pass.getText().toString();
                String passConfirm = this.passConfirm.getText().toString();
                if(login.isEmpty() ||nom.isEmpty() ||prenom.isEmpty() ||pass.isEmpty() ||passConfirm.isEmpty()) {
                    gs.alerter("Saisir tout les champs");
                    return;
                }
                if( !pass.equals(passConfirm)) {
                    gs.alerter("Les mots de passe sont différents");
                    return;
                }
                JSONAsyncTask jsAT = new JSONAsyncTask();
                jsAT.execute("users/?nom="+nom+"&prenom="+prenom+"&login="+login+"&passwd="+pass);
                break;
        }

    }
}
