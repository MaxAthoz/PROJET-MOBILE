package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import com.example.myapplication.Entities.Espace;
import com.example.myapplication.Entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListeEspace extends AppCompatActivity implements View.OnClickListener{

    private List<Espace> mesEspaces;
    private User loggedUser;
    private Button btnAjoutEspace;

    class JSONAsyncTask extends AsyncTask<String, Void, JSONObject>{
        // Params, Progress, Result

        @Override
        protected void onPreExecute() { // S’exécute dans l’UI Thread
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... qs) {
            String res = null;
            try {
                res = ListeEspace.this.gs.sendGet(qs[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(res);
                return jsonObject;
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
                    JSONArray jsa = jsonObject.getJSONArray("espaces");
                    for (int i = 0; i < jsa.length(); i++) {
                        JSONObject espace = jsa.getJSONObject(i);

                        String s = espace.toString();
                        Espace e  = gson.fromJson(s, Espace.class);
                        System.out.println(e);
                        mesEspaces.add(e);
                    }
                    fillEspacesListView(mesEspaces);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    GlobalState gs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_espace);
        loggedUser = (User) getIntent().getSerializableExtra("User");

        TextView textLoggedUser = (TextView) findViewById(R.id.loggedUser);
        textLoggedUser.setText(loggedUser.getLogin());

        btnAjoutEspace = (Button)findViewById(R.id.button_ajout_espace2);
        btnAjoutEspace.setOnClickListener(this);

        gs = (GlobalState) getApplication();
        mesEspaces = new ArrayList<>();
        ListeEspace.JSONAsyncTask jsAT = new JSONAsyncTask();
        jsAT.execute("users-espaces/"+loggedUser.getId());
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_ajout_espace2:
                Intent versAjoutEspace = new Intent(this, AjoutEspace.class);
                versAjoutEspace.putExtra("User",loggedUser);
                startActivity(versAjoutEspace);
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
                startActivity(versDashboard);
                break;
            case R.id.action_liste_espaces :
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
                Intent versCalendrier = new Intent(this, Calendrier.class);
                startActivity(versCalendrier);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillEspacesListView(List<Espace> list){
        // Définition des colonnes
        // NB : SimpleCursorAdapter a besoin obligatoirement d'un ID nommé "_id"
        String[] columns = new String[] { "_id","ID", "NOM", "EDIT","DELETE" };

        // Définition des données du tableau
        // les lignes ci-dessous ont pour seul but de simuler
        // un objet de type Cursor pour le passer au SimpleCursorAdapter.
        // Si vos données sont issues d'une base SQLite,
        // utilisez votre "cursor" au lieu du "matrixCursor"
        MatrixCursor matrixCursor= new MatrixCursor(columns);
        startManagingCursor(matrixCursor);

        for( Espace e : list) {
            matrixCursor.addRow(new Object[]{e.getId(),e.getId(), e.getNomEspace(), "EDIT","DELETE"});
        }
        // on prendra les données des colonnes "ID", "NOM", "EDIT","DELETE" ...
        String[] from = new String[] {"ID", "NOM", "EDIT","DELETE" };

        // ...pour les placer dans les TextView définis dans "row_item.xml"
        int[] to = new int[] { R.id.textViewColId, R.id.textViewColNom,R.id.textViewColActionEdit,R.id.textViewColActionDelete};

        // création de l'objet SimpleCursorAdapter...
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.xml.list_espaces_row, matrixCursor, from, to, 0);

        // ...qui va remplir l'objet ListView
        ListView lv = (ListView) findViewById(R.id.listViewEspaces);
        lv.setAdapter(adapter);
        // Utilisation avec notre listview
        lv.setOnItemClickListener(itemClickListener);

    }

    // Gestion des clics sur les lignes
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
            // faites ici ce que vous voulez
            Intent versEditEspace= new Intent(gs,EditEspace.class);
            versEditEspace.putExtra("User",loggedUser);
            versEditEspace.putExtra("Espace",getEspaceById(id));
            startActivity(versEditEspace);
        }
    };

    private Espace getEspaceById(long id){
        for( Espace e : this.mesEspaces) {
            if(e.getId() == id){
                return e;
            }
        }
        return null ;
    }
}
