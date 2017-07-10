package com.zacguo.android360;

import java.util.HashMap;

/**
 * Created by Z on 7/8/2017.
 */

public interface Handler {
    public void handleIt(HashMap<String, Object> data);
    public Object handleBackObject(HashMap<String, String> data);
    public String handleBackString(HashMap<String, Object> data);
}
