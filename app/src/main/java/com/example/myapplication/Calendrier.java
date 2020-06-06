package com.example.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Entities.Espace;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Views;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Calendrier extends AppCompatActivity implements View.OnClickListener {
    private static final String CAT = "IME4";

    private User loggedUser;
    private Button buttonAddActivity;
    private List<Espace> mesEspaces;
    private Date selectedDate;
    private Spinner spinner;
    private List<Views> mesViews;

    private LinearLayout rl;

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
                res = Calendrier.this.gs.requete(qs[0], "GET", null);
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
                    if (jsa != null) {
                        for (int i = 0; i < jsa.length(); i++) {
                            JSONObject espace = jsa.getJSONObject(i);

                            String s = espace.toString();
                            Espace e = gson.fromJson(s, Espace.class);
                            mesEspaces.add(e);
                        }
                        List<String> list = new ArrayList<String>();
                        for (Espace espace : mesEspaces) {
                            list.add(espace.getNomEspace());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Calendrier.this,
                                android.R.layout.simple_spinner_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);
                    }

                } catch (JSONException e) {
                    System.out.println("No value for espaces found");
                }
                try {
                    JSONArray jsa = jsonObject.getJSONArray("views");
                    mesViews = new ArrayList<>();
                    for (int i = 0; i < jsa.length(); i++) {
                        JSONObject views = jsa.getJSONObject(i);

                        String s = views.toString();
                        Views v = gson.fromJson(s, Views.class);
                        System.out.println(v);
                        mesViews.add(v);
                    }

                    rl = findViewById(R.id.llCalendrier);
                    List<Views> displayedView = new ArrayList<>();
                    for (Views v1 : mesViews) {
                        if (!displayedView.contains(v1)) {
                            LinearLayout ll = new LinearLayout(Calendrier.this);
                            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            ll.setOrientation(LinearLayout.VERTICAL);
                            ViewGroup.LayoutParams layoutParams = ll.getLayoutParams();
                            layoutParams.width = 1000;
                            ll.setLayoutParams(layoutParams);
                            rl.addView(ll);


                            TextView tv1 = new TextView(Calendrier.this);
                            tv1.setText(v1.getNomEspace());
                            tv1.setGravity(Gravity.CENTER);
                            tv1.setTypeface(tv1.getTypeface(), Typeface.BOLD);



                            TextView tv2 = new TextView(Calendrier.this);
                            tv2.setText(v1.getNomIndicateur() + " :    " + v1.getValeur());


                            ll.addView(tv1);
                            ll.addView(tv2);

                            for (Views v2 : mesViews) {
                                if (!v1.equals(v2)) {
                                    if (v1.getDate().equals(v2.getDate())) {
                                        if (v1.getIdEspace() == v2.getIdEspace()) {
                                            if (!displayedView.contains(v2)) {
                                                TextView tv = new TextView(Calendrier.this);
                                                tv.setText(v2.getNomIndicateur() + " :    " + v2.getValeur());

                                                ll.addView(tv);
                                                displayedView.add(v2);
                                                displayedView.add(v1);
                                            }
                                        }
                                    }

                                }
                            }

                        }
                    }
                } catch (JSONException e) {
                    System.out.println("No value for views found");
                }
            }
        }

    }

    GlobalState gs;

    public void alerter(String s) {

        Log.i(CAT, s);
        Toast t = Toast.makeText(this, s, Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrier);
        loggedUser = (User) getIntent().getSerializableExtra("User");
        buttonAddActivity = (Button) findViewById(R.id.button_add_activity);
        buttonAddActivity.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner_espaces);
        mesViews = new ArrayList<>();
        final CalendarView calendarView = findViewById(R.id.calendarView);
        Date date = new Date();
        final long millis = date.getTime();
        calendarView.setDate(millis);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(millis));

        gs = (GlobalState) getApplication();
        mesEspaces = new ArrayList<>();
        Calendrier.JSONAsyncTask jsAT = new JSONAsyncTask();
        jsAT.execute("users-espaces/" + loggedUser.getId());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = new Date(year - 1900, month, dayOfMonth);
                month += 1;
                Calendrier.JSONAsyncTask jsAT = new JSONAsyncTask();
                try {
                    rl.removeAllViews();
                } catch (Exception e) {
                    System.out.println("Aucun Views");
                }
                jsAT.execute("views/" + loggedUser.getId() + "/" + year + "-" + month + "-" + dayOfMonth);


            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_activity:
                if (selectedDate != null && getEspaceByName(spinner.getSelectedItem().toString()) != null) {
                    Intent versAjoutActivity = new Intent(this, AjoutActivity.class);
                    versAjoutActivity.putExtra("User", loggedUser);
                    versAjoutActivity.putExtra("Date", selectedDate);
                    versAjoutActivity.putExtra("Espace", getEspaceByName(spinner.getSelectedItem().toString()));
                    startActivity(versAjoutActivity);
                } else
                    alerter("Veuillez selectionner une date et un espace.");
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
                versDashboard.putExtra("User", loggedUser);
                startActivity(versDashboard);
                break;
            case R.id.action_liste_espaces:
                Intent versListeEspace = new Intent(this, ListeEspace.class);
                versListeEspace.putExtra("User", loggedUser);
                startActivity(versListeEspace);
                break;
            case R.id.action_add_espace:
                Intent versAjouterEspace = new Intent(this, AjoutEspace.class);
                versAjouterEspace.putExtra("User", loggedUser);
                startActivity(versAjouterEspace);
                break;
            case R.id.action_historique:
                Intent versHistorique = new Intent(this, Historique.class);
                versHistorique.putExtra("User", loggedUser);
                startActivity(versHistorique);
                break;
            case R.id.action_calendrier:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public Espace getEspaceByName(String nom) {
        for (Espace espace : mesEspaces) {
            if (espace.getNomEspace() == nom)
                return espace;
        }
        return null;
    }
}
