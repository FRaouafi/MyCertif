package com.fra.model;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Answer {
    private List<String> codeList = new ArrayList<>();
    private String value;

    private Answer(){}
    public static Answer fromJson(JSONObject jsonObject) {
        Answer answer = new Answer();
        jsonObject.getJSONArray("codes").forEach(entry -> {
            answer.codeList.add((String) entry);
        });
        answer.value = jsonObject.getString("explanation");
        return answer;
    }
}
