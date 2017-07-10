package com.zacguo.android360;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Z on 7/8/2017.
 */

public class DataHandler implements Handler {
    public void handleIt(HashMap<String, Object> data) {

    }

    public Object handleBackObject(HashMap<String, String> data) {
        return new Object();
    }

    public String handleBackString(HashMap<String, Object> data) {
        //Object returnObj = new Object();
        String returnString = "";

        String command = (String) data.remove("command");
        HashMap<String, String> auth = (HashMap<String, String>) data.remove("auth");
        String email = auth.get("email");
        String password = auth.get("password");
        String url = (String) data.remove("url");


        //System.out.println("123123 " + command);

        switch (command) {
            case "list":
                returnString = doList(email, password, url);
                break;
            case "read":
                String noteId = (String)data.remove("data");
                returnString = doRead(email, password, url, noteId);
                break;
            case "create":
                HashMap innerData = (HashMap)data.remove("data");
                String title = (String)innerData.get("title");
                String content = (String)innerData.get("content");
                returnString = doCreate(email, password, url, title, content);
                break;
            case "update":
                HashMap innerDataUpdate = (HashMap)data.remove("data");
                String titleUpdate = (String)innerDataUpdate.get("title");
                String contentUpdate = (String)innerDataUpdate.get("content");
                String noteIdUpdate = (String)innerDataUpdate.get("noteId");
                returnString = doUpdate(email, password, url, noteIdUpdate ,titleUpdate, contentUpdate);
                break;
            case "delete":
                String noteIdDelete = (String)data.remove("data");
                returnString = doDelete(email, password, url, noteIdDelete);
                break;
        }

        //System.out.println(returnString);

        return returnString;
    }

    private String doList(String email, String password, String urlString) {

        String urlParameters = "data={\"auth\":{\"email\": \"" + email + "\",\"password\":\"" + password + "\"},\"request\": \"list\"}";

        String returnString = "";

        try {
            returnString = doPost(urlString, urlParameters);
        } catch (Exception e) {
        }

        return returnString;
    }

    private String doCreate(String email, String password, String urlString, String title, String content) {

        String urlParameters = "data={\"auth\":{\"email\": \"" + email + "\",\"password\":\"" + password + "\"},\"request\": \"create\", \"data\":{\"title\":\""
                + title + "\", \"content\":\"" + content + "\"}}";

        String returnString = "";

        try {
            returnString = doPost(urlString, urlParameters);
        } catch (Exception e) {
        }

        return returnString;
    }

    private String doRead(String email, String password, String urlString, String noteId) {
        String urlParameters = "data={\"auth\":{\"email\": \"" + email + "\",\"password\":\"" + password + "\"},\"request\": \"read\", \"data\": \"" + noteId + "\"}";

        String returnString = "";

        try {
            returnString = doPost(urlString, urlParameters);
        } catch (Exception e) {
        }

        return returnString;
    }

    private String doUpdate(String email, String password, String urlString, String noteId, String title, String content) {
        String urlParameters = "data={\"auth\":{\"email\": \"" + email + "\",\"password\":\"" + password +
                "\"},\"request\": \"update\", \"data\":{\"noteId\":\"" + noteId + "\", \"title\":\"" + title + "\", \"content\":\"" + content + "\"}}";

        System.out.println(urlParameters);

        String returnString = "";

        try {
            returnString = doPost(urlString, urlParameters);
        } catch (Exception e) {
        }

        return returnString;
    }

    private String doDelete(String email, String password, String urlString, String noteId) {
        String urlParameters = "data={\"auth\":{\"email\": \"" + email + "\",\"password\":\"" + password + "\"},\"request\": \"delete\", \"data\" : \"" + noteId + "\"}";

        System.out.println(urlParameters);

        String returnString = "";

        try {
            returnString = doPost(urlString, urlParameters);
        } catch (Exception e) {
        }

        return returnString;
    }


    private String doPost(String urlString, String requestString) throws Exception {
        System.out.println(urlString);

        String url = urlString;
        URL obj = new URL(url);
        System.out.println(urlString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        System.out.println(urlString);
        con.setDoOutput(true);
        //add reuqest header
        con.setRequestMethod("POST");
        //con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        System.out.println(urlString);


        con.setInstanceFollowRedirects(false);
        //     conn.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("charset", "utf-8");
        //conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        con.setUseCaches(false);

        System.out.println(requestString);

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(requestString);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + requestString);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
