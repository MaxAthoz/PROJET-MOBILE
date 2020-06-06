package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entities.Activite;
import com.example.myapplication.Entities.Espace;
import com.example.myapplication.Entities.Indicateur;
import com.example.myapplication.Entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AjoutActivity extends AppCompatActivity implements View.OnClickListener {

    private Espace espace;
    private User user;
    private Date date;
    private List<Indicateur> mesIndicateurs;
    private Button buttonSaveActivity;
    private List<EditText> allIndicateurs;
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
                res = AjoutActivity.this.gs.requete(qs[0], httpType, reqBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject json;
                if (espace == null) {
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
                    for (int i = 0; i < jsa.length(); i++) {
                        JSONObject indicateur = jsa.getJSONObject(i);

                        String s = indicateur.toString();
                        Indicateur ind = gson.fromJson(s, Indicateur.class);
                        System.out.println(ind);
                        mesIndicateurs.add(ind);
                    }
                    displayIndicateurs();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (httpType == "POST") {
                finish();
            }
        }
    }

    GlobalState gs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_activity);
        espace = (Espace) getIntent().getSerializableExtra("Espace");
        user = (User) getIntent().getSerializableExtra("User");
        date = (Date) getIntent().getSerializableExtra("Date");
        gs = (GlobalState) getApplication();
        mesIndicateurs = new ArrayList<>();
        allIndicateurs = new ArrayList<EditText>();

        buttonSaveActivity = (Button) findViewById(R.id.button_sauvegarder_activity);
        buttonSaveActivity.setOnClickListener(this);


        AjoutActivity.JSONAsyncTask jsAT = new JSONAsyncTask();
        httpType = "GET";
        reqBody = null;
        jsAT.execute("espaces-indicateurs/" + espace.getId());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sauvegarder_activity:
                System.out.println(date);
                System.out.println(espace);
                for (EditText et : allIndicateurs) {
                    Activite activite = new Activite();
                    activite.setDate(date);
                    activite.setIdEspace(espace.getId());
                    activite.setIdIndicateur(et.getId());
                    activite.setValeur(et.getText().toString());

                    System.out.println("POST activité :" + activite.toString());

                    JSONAsyncTask jsAT = new JSONAsyncTask();
                    httpType = "POST";
                    reqBody = null;
                    System.out.println(activite.getDate().toString());
                    System.out.println(activite.getDate().getYear() + 1900);
                    System.out.println(activite.getDate().getMonth() + 1);
                    System.out.println(activite.getDate().getDate());
                    String y = Integer.toString(activite.getDate().getYear()+1900);
                    String m = Integer.toString(activite.getDate().getMonth() + 1);
                    String j = Integer.toString(activite.getDate().getDate());
                    jsAT.execute("activites/?idEspace=" + activite.getIdEspace() + "&idIndicateur=" + activite.getIdIndicateur() + "&valeur=" + activite.getValeur() + "&date=" + y + "-" + m + "-"+j);

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
        switch (id) {
            case R.id.action_dashboard:
                Intent versDashboard = new Intent(this, Dashboard.class);
                versDashboard.putExtra("User", user);
                startActivity(versDashboard);
                break;
            case R.id.action_liste_espaces:
                Intent versListeEspace = new Intent(this, ListeEspace.class);
                versListeEspace.putExtra("User", user);
                startActivity(versListeEspace);
                break;
            case R.id.action_add_espace:
                Intent versAjoutEspace = new Intent(this, AjoutEspace.class);
                versAjoutEspace.putExtra("User", user);
                startActivity(versAjoutEspace);
                break;
            case R.id.action_historique:
                Intent versHistorique = new Intent(this, Historique.class);
                versHistorique.putExtra("User", user);
                startActivity(versHistorique);
                break;
            case R.id.action_calendrier:
                Intent versCalendrier = new Intent(this, Calendrier.class);
                versCalendrier.putExtra("User", user);
                startActivity(versCalendrier);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayIndicateurs() {


        RelativeLayout rl = findViewById(R.id.relativeLayoutAddActivity);

        ScrollView sv = new ScrollView(this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = formatter.format(date);
        TextView tvDate = new TextView(AjoutActivity.this);
        tvDate.setText(strDate + "\n");
        ll.addView(tvDate);

        for (Indicateur indic : mesIndicateurs) {
            // Add text
            TextView tv = new TextView(AjoutActivity.this);
            tv.setText(indic.getNomIndicateur() + " :");
            EditText editText = new EditText(AjoutActivity.this);
            editText.setText(indic.getValeurInit());
            editText.setId(indic.getId());
            allIndicateurs.add(editText);
            ll.addView(tv);
            ll.addView(editText);
        }

        rl.addView(sv);

    }
}
