package com.example.dong.meishi.httpUtil;
import com.example.dong.meishi.foodUtil.Food;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ParseJSON {
    public static List<Food> parseJSONWithGson(String jsonData){
        Gson gson = new Gson();
        List<Food> foodList = gson.fromJson(jsonData,new TypeToken<List<Food>>(){}.getType());
        return foodList;
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


