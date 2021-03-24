package com.fra.model;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Question {
    private int index;
    private String value;
    private List<Response> responseList = new ArrayList<>();
    private Answer answer;

    private Question(){}
    public static Question fromJson(JSONObject jsonObject) {
        Question question = new Question();
        question.index = jsonObject.getInt("nbr");
        question.value = jsonObject.getString("value");
        jsonObject.getJSONArray("responses").forEach(entry -> {
            question.responseList.add(Response.fromJson((JSONObject) entry));
        });
        question.answer = Answer.fromJson(jsonObject.getJSONObject("answer"));
        return question;
    }
}
