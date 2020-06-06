package com.example.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entities.Activite;
import com.example.myapplication.Entities.Espace;
import com.example.myapplication.Entities.Indicateur;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Views;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Historique extends AppCompatActivity {

    private User user;

    private String httpType;
    private JSONObject reqBody;
    private List<Views> mesViews;

    private Views tempView;

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
                res = Historique.this.gs.requete(qs[0], httpType, reqBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject json;
                if (mesViews == null) {
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
                    JSONArray jsa = jsonObject.getJSONArray("views");
                    System.out.println(jsa);
                    for (int i = 0; i < jsa.length(); i++) {
                        JSONObject views = jsa.getJSONObject(i);

                        String s = views.toString();
                        Views v = gson.fromJson(s, Views.class);
                        System.out.println(v);
                        mesViews.add(v);
                    }

                    displayViews();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(httpType == "DELETE"){
                finish();
                startActivity(getIntent());
            }
        }
    }

    GlobalState gs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        user = (User) getIntent().getSerializableExtra("User");
        gs = (GlobalState) getApplication();
        mesViews = new ArrayList<Views>();

        Historique.JSONAsyncTask jsAT = new JSONAsyncTask();
        httpType = "GET";
        reqBody = null;
        jsAT.execute("views/" + user.getId());
    }

    public void onClick(View v) {
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
                Intent versAjouterEspace = new Intent(this, AjoutEspace.class);
                versAjouterEspace.putExtra("User", user);
                startActivity(versAjouterEspace);
                break;
            case R.id.action_historique:
                break;
            case R.id.action_calendrier:
                Intent versCalendrier = new Intent(this, Calendrier.class);
                versCalendrier.putExtra("User", user);
                startActivity(versCalendrier);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayViews() {


        LinearLayout rl = findViewById(R.id.linearLayoutHistorique);

        List<Views> displayedView = new ArrayList<>();
        for (Views v1 : mesViews) {
            if (!displayedView.contains(v1)) {
                LinearLayout ll = new LinearLayout(this);
                ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.setBackgroundResource(R.drawable.custom_background);
                ViewGroup.LayoutParams layoutParams = ll.getLayoutParams();
                layoutParams.width = 1000;
                ll.setLayoutParams(layoutParams);
                rl.addView(ll);


                TextView tv1 = new TextView(Historique.this);
                tv1.setText(v1.getNomEspace());
                tv1.setGravity(Gravity.CENTER);
                tv1.setTypeface(tv1.getTypeface(), Typeface.BOLD);


                TextView tv4 = new TextView(Historique.this);

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                String strDate = formatter.format(v1.getDate());
                tv4.setText(strDate + "\n");
                tv4.setGravity(Gravity.RIGHT);

                TextView tv2 = new TextView(Historique.this);
                tv2.setText(v1.getNomIndicateur() + " :    " + v1.getValeur());


                ll.addView(tv1);
                ll.addView(tv4);
                ll.addView(tv2);

                for (Views v2 : mesViews) {
                    if (!v1.equals(v2)) {
                        if (v1.getDate().equals(v2.getDate())) {
                            if (v1.getIdEspace() == v2.getIdEspace()) {
                                if (!displayedView.contains(v2)) {
                                    TextView tv = new TextView(Historique.this);
                                    tv.setText(v2.getNomIndicateur() + " :    " + v2.getValeur());

                                    ll.addView(tv);
                                    displayedView.add(v2);
                                    displayedView.add(v1);
                                }
                            }
                        }

                    }
                }

                Button btn1 = new Button(this);
                btn1.setText("Supprimer");
                btn1.setTag(v1);

                tempView = v1;
                ll.addView(btn1);

                btn1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        // put code on click operation
                        Views viewToUse = (Views) v.getTag();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = formatter.format(viewToUse.getDate());
                        httpType="DELETE";
                        reqBody=null;
                        System.out.println(viewToUse.getIdEspace());
                        System.out.println(strDate);
                        JSONAsyncTask jsAT = new JSONAsyncTask();
                        jsAT.execute("views/" + viewToUse.getIdEspace() + "/" + strDate);
                    }
                });
            }
        }
    }
}
