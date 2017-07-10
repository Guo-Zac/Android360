package com.zacguo.android360;

import java.util.HashMap;

import org.quickconnectfamily.json.JSONUtilities;
/**
 * Created by Z on 7/9/2017.
 */

public class JsonHandler implements Handler {
    public void handleIt(HashMap<String, Object> data){}
    public String handleBackString(HashMap<String, Object> data){return "";}

    public Object handleBackObject(HashMap<String, String> data){

        String command = (String) data.remove("command");

        Object obj = data.remove("data");

        Object returnObj = new Object();

        switch (command){
            case "Json2Object":
                returnObj = JSON2HashMap((String) obj);
                break;
            case "Object2Json":
                returnObj = HashMap2JSON((HashMap) obj);
                break;
        }

        return returnObj;
    }

    private HashMap JSON2HashMap(String JSONString) {

        HashMap parsedJSONMap = new HashMap();

        try {
            JSONUtilities jsonUtil = new JSONUtilities();

            parsedJSONMap = (HashMap) jsonUtil.parse(JSONString);

        }
        catch (Exception e) {
            System.out.print(e);;
        }

        return parsedJSONMap;
    }

    private String HashMap2JSON(HashMap aMap) {

        String jsonString = "";

        try {
            JSONUtilities jsonUtil = new JSONUtilities();

            jsonString = jsonUtil.stringify(aMap);
        }
        catch (Exception e) {
            System.out.print(e);
        }

        return jsonString;
    }
}
