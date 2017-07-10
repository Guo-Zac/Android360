package com.zacguo.android360;

import java.util.HashMap;

/**
 * Created by Z on 7/8/2017.
 */

public class ApplicationController {

    private HashMap<String, Handler> handlerMap = new HashMap<String, Handler>();

    public void handleRequest(String command, HashMap<String,Object> data){
        Handler aCommandHandler = handlerMap.get(command);
        if (aCommandHandler != null){
            aCommandHandler.handleIt(data);
        }
    }

    public String handleBackStringRequest(String command, HashMap<String,Object> data){
        Handler aCommandHandler = handlerMap.get(command);

        String returnString = "";

        if (aCommandHandler != null){
            returnString = aCommandHandler.handleBackString(data);
        }

        return returnString;
    }

    public Object handleBackObjectRequest(String command, HashMap<String,String> data){
        Handler aCommandHandler = handlerMap.get(command);

        Object returnObject = new Object();

        if (aCommandHandler != null){
            returnObject = aCommandHandler.handleBackObject(data);
        }

        return returnObject;
    }


    public void mapCommand(String aCommand, Handler acHandler){
        handlerMap.put(aCommand,acHandler);
    }

}
