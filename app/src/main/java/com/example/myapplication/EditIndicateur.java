package com.example.myapplication;

import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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

public class EditIndicateur extends AppCompatActivity {

    private Espace espace ;
    private User user ;
    private Indicateur indicateur;


    class JSONAsyncTask extends AsyncTask<String, Void, JSONObject> {
        // Params, Progress, Result

        @Override
        protected void onPreExecute() { // S’exécute dans l’UI Thread
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... qs) { return null;}

        protected void onPostExecute(JSONObject jsonObject) { // S’exécute dans l’UI Thread
            if (jsonObject != null) {

            }
        }
    }
    GlobalState gs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_indicateur);
        espace = (Espace) getIntent().getSerializableExtra("Espace");
        user = (User) getIntent().getSerializableExtra("User");
        indicateur= (Indicateur) getIntent().getSerializableExtra("Indicateur");
        gs = (GlobalState) getApplication();
    }


}
