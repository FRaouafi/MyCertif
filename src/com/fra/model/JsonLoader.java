package com.fra.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonLoader {
    public static List<Chapter> Load() {
        List<Chapter> chapters = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            String text = Files.readString(Path.of("chapters.json"));
            jsonArray = new JSONArray(text);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            System.exit(1);
        }
        jsonArray.forEach(entry -> {
            chapters.add(Chapter.fromJson((JSONObject) entry));
        });
        return chapters ;
    }
}
