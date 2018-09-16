package com.example.dong.meishi.httpUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class parseJSON {
    public static List<Cai> parseJSONWithGson(String jsonData){
        Gson gson = new Gson();
        List<Cai> caiList = gson.fromJson(jsonData,new TypeToken<List<Cai>>(){}.getType());
        return caiList;
    }

    public static boolean parseJSONWithJSONObject(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String status = jsonObject.getString("status");
            if ("failed".equals(status)) return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

}

class Cai{
    private String id;
    private String name;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
