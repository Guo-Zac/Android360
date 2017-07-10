package com.zacguo.android360;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private String email = "1@1.com";
    private String password = "123";
    private String url = "http://10.10.6.152:8080/Server360/Server";
    private ApplicationController acp = new ApplicationController();
 //   private HashMap<String, String> noteListMap = new HashMap<String, String>();
    private ArrayList<String> keyList;
    private ArrayList<String> valueList;
    ExecutorService threadManager = Executors.newFixedThreadPool(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        acp.mapCommand("data", new DataHandler());
        acp.mapCommand("json", new JsonHandler());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //handle listview and assign adapter
        ListView lView = (ListView) findViewById(R.id.noteList);
    //    lView.setAdapter(adapter);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                noteEdit(position);
            }
        });


        //noteList();
        threadManager.submit(new noteListRunnable());
        newNote();
    }

    @Override
    protected void onResume(){
        super.onResume();

        noteList();
        //threadManager.submit(new noteListRunnable());
    }

    private void noteEdit(int position){
        Intent intent = new Intent(MainActivity.this, NoteEditActivity.class);

        intent.putExtra("noteId", keyList.get(position));
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("url", url);

        startActivity(intent);
    }

    private class noteListRunnable implements Runnable{
        public void run() {
            noteList();
        }
    }

    private void noteList(){
        HashMap<String, String> auth = new HashMap<String, String>();
        auth.put("email",email);
        auth.put("password", password);
        HashMap<String, Object> acpData = new HashMap<String, Object>();
        acpData.put("command", "list");
        acpData.put("auth", auth);
        acpData.put("url", url);
        String returnedString = acp.handleBackStringRequest("data", acpData);

        HashMap<String,String> acpJson = new HashMap<String, String>();
        acpJson.put("command", "Json2Object");
        acpJson.put("data", returnedString);
        HashMap<String, Object> returnedHashMap = (HashMap<String, Object>) acp.handleBackObjectRequest("json", acpJson);

        HashMap<String, String> noteListMap = (HashMap<String, String>) returnedHashMap.get("data");
        keyList = new ArrayList<>(noteListMap.keySet());
        valueList = new ArrayList<>();
        for (int i = 0; i< keyList.size();i++){
            valueList.add(i, noteListMap.get(keyList.get(i)));
        }

        String[] noteNames = Arrays.copyOf(valueList.toArray(),valueList.toArray().length,String[].class);
        ListView lView = (ListView) findViewById(R.id.noteList);
        lView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noteNames));
    }

    private void newNote(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                Intent intent = new Intent(MainActivity.this, NoteEditActivity.class);
                intent.putExtra("noteId", "0");
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }
}