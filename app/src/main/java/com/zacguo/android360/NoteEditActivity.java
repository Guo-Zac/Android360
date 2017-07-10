package com.zacguo.android360;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Z on 7/8/2017.
 */

public class NoteEditActivity extends Activity {

    private ApplicationController acp = new ApplicationController();
    private String noteId;
    private String email;
    private String password;
    private String urlString;
    ExecutorService threadManager = Executors.newFixedThreadPool(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_edit);

        acp.mapCommand("data", new DataHandler());
        acp.mapCommand("json", new JsonHandler());

        //Handle buttons and add onClickListeners
        Button saveBtn = (Button) findViewById(R.id.save_btn);
        Button cancelBtn = (Button) findViewById(R.id.cancel_btn);
        Button deleteBtn = (Button) findViewById(R.id.delete_btn);

        noteId = getIntent().getExtras().getString("noteId");
        email = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");
        urlString = getIntent().getExtras().getString("url");


        if (noteId.equals("0")) {
            deleteBtn.setVisibility(View.GONE);
        } else {
            //readNote();
            threadManager.submit(new readRunnable());
        }

        System.out.println(noteId);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Snackbar.make(findViewById(R.id.save_btn), "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                if (noteId.equals("0")) {
                    //this is a new note
                    System.out.println("new note");
                    //newNote();
                    threadManager.submit(new createRunnable());
                } else {
                    //this is an existing note
                    //updateNote();
                    threadManager.submit(new updateRunnable());
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteNote();
                threadManager.submit(new deleteRunnable());
            }
        });

        //executorServiceCallable();


    }

    private class createRunnable implements Runnable {
        public void run() {
            newNote();
        }
    }

    private class readRunnable implements Runnable {
        public void run() {
            readNote();
        }
    }

    private class updateRunnable implements Runnable {
        public void run() {
            updateNote();
        }
    }

    private class deleteRunnable implements Runnable {
        public void run() {
            deleteNote();
        }
    }


    private void deleteNote() {
        HashMap<String, String> auth = new HashMap<String, String>();
        auth.put("email", email);
        auth.put("password", password);
        HashMap<String, Object> acpData = new HashMap<String, Object>();
        acpData.put("command", "delete");
        acpData.put("auth", auth);
        acpData.put("url", urlString);
//        HashMap<String, String> data = new HashMap<>();
//        data.put("noteId", noteId);
        acpData.put("data", noteId);
        String returnedString = acp.handleBackStringRequest("data", acpData);

        System.out.println(returnedString);

        HashMap<String, String> acpJson = new HashMap<String, String>();
        acpJson.put("command", "Json2Object");
        acpJson.put("data", returnedString);
        HashMap<String, Object> returnedHashMap = (HashMap<String, Object>) acp.handleBackObjectRequest("json", acpJson);

        if (returnedHashMap.get("result").equals("success")) {
            finish();
        }
    }

    private void updateNote() {
        EditText titleText = (EditText) findViewById(R.id.titleText);
        String title = titleText.getText().toString();
        EditText contentText = (EditText) findViewById(R.id.contentText);
        String content = contentText.getText().toString();

        if (title.length() < 1 || content.length() < 1) {
            Snackbar.make(findViewById(R.id.save_btn), "Title or Content Cannot be Empty.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            HashMap<String, String> auth = new HashMap<String, String>();
            auth.put("email", email);
            auth.put("password", password);
            HashMap<String, Object> acpData = new HashMap<String, Object>();
            acpData.put("command", "update");
            acpData.put("auth", auth);
            acpData.put("url", urlString);
            HashMap<String, String> data = new HashMap<>();
            data.put("title", title);
            data.put("content", content);
            data.put("noteId", noteId);
            acpData.put("data", data);
            String returnedString = acp.handleBackStringRequest("data", acpData);

            System.out.println(returnedString);

            HashMap<String, String> acpJson = new HashMap<String, String>();
            acpJson.put("command", "Json2Object");
            acpJson.put("data", returnedString);
            HashMap<String, Object> returnedHashMap = (HashMap<String, Object>) acp.handleBackObjectRequest("json", acpJson);

            if (returnedHashMap.get("result").equals("success")) {
                finish();
            }
        }
    }

    private void newNote() {

        // String title = data.get("title");
        //  String content = data.get("content");

        EditText titleText = (EditText) findViewById(R.id.titleText);
        String title = titleText.getText().toString();
        EditText contentText = (EditText) findViewById(R.id.contentText);
        String content = contentText.getText().toString();

        if (title.length() < 1 || content.length() < 1) {
            Snackbar.make(findViewById(R.id.save_btn), "Title or Content Cannot be Empty.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            HashMap<String, String> auth = new HashMap<String, String>();
            auth.put("email", email);
            auth.put("password", password);
            HashMap<String, Object> acpData = new HashMap<String, Object>();
            acpData.put("command", "create");
            acpData.put("auth", auth);
            acpData.put("url", urlString);
            HashMap<String, String> data = new HashMap<>();
            data.put("title", title);
            data.put("content", content);
            acpData.put("data", data);
            String returnedString = acp.handleBackStringRequest("data", acpData);

            System.out.println(returnedString);

            HashMap<String, String> acpJson = new HashMap<String, String>();
            acpJson.put("command", "Json2Object");
            acpJson.put("data", returnedString);
            HashMap<String, Object> returnedHashMap = (HashMap<String, Object>) acp.handleBackObjectRequest("json", acpJson);

            if (returnedHashMap.get("result").equals("success")) {
                finish();
            }
        }


//


        //HashMap<String, String> data = (HashMap<String, String>)returnedHashMap.get("data");

    }

    private void readNote() {
        HashMap<String, String> auth = new HashMap<String, String>();
        auth.put("email", email);
        auth.put("password", password);
        HashMap<String, Object> acpData = new HashMap<String, Object>();
        acpData.put("command", "read");
        acpData.put("auth", auth);
        acpData.put("url", urlString);
        acpData.put("data", noteId);
        String returnedString = acp.handleBackStringRequest("data", acpData);

        //System.out.println(returnedString);

        HashMap<String, String> acpJson = new HashMap<String, String>();
        acpJson.put("command", "Json2Object");
        acpJson.put("data", returnedString);
        HashMap<String, Object> returnedHashMap = (HashMap<String, Object>) acp.handleBackObjectRequest("json", acpJson);

        HashMap<String, String> data = (HashMap<String, String>) returnedHashMap.get("data");

        String title = data.get("title");
        String content = data.get("content");

        EditText titleText = (EditText) findViewById(R.id.titleText);
        titleText.setText(title);
        EditText contentText = (EditText) findViewById(R.id.contentText);
        contentText.setText(content);
    }
}