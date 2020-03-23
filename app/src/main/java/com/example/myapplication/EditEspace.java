package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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

public class EditEspace extends AppCompatActivity implements View.OnClickListener{

    private Espace espace ;
    private User user ;
    private List<Indicateur> mesIndicateurs;
    private Button btnAjoutIndicateur;

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
                res = EditEspace.this.gs.sendGet(qs[0]);
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
                    JSONArray jsa = jsonObject.getJSONArray("indicateurs");
                    System.out.println(jsa);
                    for (int i = 0; i < jsa.length(); i++) {
                        JSONObject indicateur = jsa.getJSONObject(i);

                        String s = indicateur.toString();
                        Indicateur ind  = gson.fromJson(s, Indicateur.class);
                        System.out.println(ind);
                        mesIndicateurs.add(ind);
                    }
                    fillIndicateurListView(mesIndicateurs);
                    EditText textNomEspace = (EditText) findViewById(R.id.editTextNomEspace);
                    textNomEspace.setText(espace.getNomEspace());
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
        setContentView(R.layout.activity_edit_espace);
        espace = (Espace) getIntent().getSerializableExtra("Espace");
        user = (User) getIntent().getSerializableExtra("User");

        btnAjoutIndicateur = (Button)findViewById(R.id.button_ajout_indicateur);
        btnAjoutIndicateur.setOnClickListener(this);


        gs = (GlobalState) getApplication();
        mesIndicateurs = new ArrayList<>();
        EditEspace.JSONAsyncTask jsAT = new JSONAsyncTask();
        jsAT.execute("espaces-indicateurs/"+espace.getId());
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_ajout_indicateur:
                Intent versAjoutIndicateur = new Intent(this, AjoutIndicateur.class);
                versAjoutIndicateur.putExtra("User",user);
                versAjoutIndicateur.putExtra("Espace",espace);
                startActivity(versAjoutIndicateur);
                break;
        }

    }

    private void fillIndicateurListView(List<Indicateur> list){
        // Définition des colonnes
        // NB : SimpleCursorAdapter a besoin obligatoirement d'un ID nommé "_id"
        String[] columns = new String[] { "_id","ID", "NOM", "TYPE","VALEUR_INIT" };

        // Définition des données du tableau
        // les lignes ci-dessous ont pour seul but de simuler
        // un objet de type Cursor pour le passer au SimpleCursorAdapter.
        // Si vos données sont issues d'une base SQLite,
        // utilisez votre "cursor" au lieu du "matrixCursor"
        MatrixCursor matrixCursor= new MatrixCursor(columns);
        startManagingCursor(matrixCursor);

        matrixCursor.addRow(new Object[]{0,"Id", "Nom", "Type","Valeur initiale"});
        for( Indicateur ind : list) {
            matrixCursor.addRow(new Object[]{ind.getId(),ind.getId(), ind.getNomIndicateur(),ind.getType(),ind.getValeurInit()});
        }
        // on prendra les données des colonnes "ID", "NOM", "EDIT","DELETE" ...
        String[] from = new String[] {"ID", "NOM","TYPE","VALEUR_INIT"  };

        // ...pour les placer dans les TextView définis dans "row_item.xml"
        int[] to = new int[] { R.id.textViewIndicateurColId, R.id.textViewIndicateurColNom,R.id.textViewColType,R.id.textViewColValue};

        // création de l'objet SimpleCursorAdapter...
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.xml.list_indicateurs_row, matrixCursor, from, to, 0);

        // ...qui va remplir l'objet ListView
        ListView lv = (ListView) findViewById(R.id.listViewIndicateurs);
        lv.setAdapter(adapter);
        // Utilisation avec notre listview
        lv.setOnItemClickListener(itemClickListener);

    }

    // Gestion des clics sur les lignes
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
            // faites ici ce que vous voulez
            Intent versEditIndicateur= new Intent(gs,EditIndicateur.class);
            versEditIndicateur.putExtra("User",user);
            versEditIndicateur.putExtra("Espace",espace);
            versEditIndicateur.putExtra("Indicateur",getIndicateurById(id));
            startActivity(versEditIndicateur);
        }
    };

    private Indicateur getIndicateurById(long id){
        for( Indicateur ind : this.mesIndicateurs) {
            if(ind.getId() == id){
                return ind;
            }
        }
        return null ;
    }


}
