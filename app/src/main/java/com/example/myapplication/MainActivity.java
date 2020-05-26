package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    private Button btnInscription;
    private Button btnConnexion;
    private EditText editTextLogin;
    private EditText editTextPasse;
    private User user;
    private static final String CAT = "IME4";

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
                System.out.println("requete GET USER");
                res = MainActivity.this.gs.requete(qs[0],"GET",null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONArray jsonArray = new JSONArray(res);
                System.out.println("Resultat JSON array : "+ jsonArray);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                return jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject jsonObject) { // S’exécute dans l’UI Thread
            if (jsonObject != null) {
                super.onPostExecute(jsonObject);

                String s = jsonObject.toString();
                System.out.println("user a connecter" + s);

                Gson gson = new GsonBuilder()
                        .serializeNulls()
                        .disableHtmlEscaping()
                        .setPrettyPrinting()
                        .create();

                user = gson.fromJson(s, User.class);

                Intent versListeEspace= new Intent(gs,ListeEspace.class);
                versListeEspace.putExtra("User",user);
                startActivity(versListeEspace);

            }
            else
                alerter("Identifiants incorrects.");
        }
    }
    GlobalState gs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceuil);
        btnInscription = (Button)findViewById(R.id.inscription);
        btnInscription.setOnClickListener(this);
        btnConnexion = (Button)findViewById(R.id.connexion);
        btnConnexion.setOnClickListener(this);
        editTextLogin = (EditText) findViewById(R.id.editPseudo);
        editTextLogin.setOnClickListener(this);
        editTextPasse = (EditText) findViewById(R.id.editPasse);
        editTextPasse.setOnClickListener(this);
        gs = (GlobalState) getApplication();
    }



    public void alerter(String s) {

        Log.i(CAT,s);
        Toast t = Toast.makeText(this,s,Toast.LENGTH_LONG);
        t.show();
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.inscription :
                Intent versInscription = new Intent(this, Inscription.class);
                startActivity(versInscription);
                break;
            case R.id.connexion :
                String login = editTextLogin.getText().toString();
                String passwd = editTextPasse.getText().toString();
                if(login.isEmpty()){
                    alerter("Saisir votre login");
                    return;
                }
                if(passwd.isEmpty()){
                    alerter("Saisir votre passe");
                    return;
                }
                JSONAsyncTask jsAT = new JSONAsyncTask();
                jsAT.execute("users/"+login+"/"+passwd);
                break;
        }

    }
}
