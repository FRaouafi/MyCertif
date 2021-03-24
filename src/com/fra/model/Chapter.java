package com.fra.model;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Chapter {
    private String name;
    private List<Question> questionList = new ArrayList<>();

    public static Chapter fromJson(JSONObject jsonObject) {
        Chapter chapter = new Chapter();
        chapter.name = jsonObject.getString("name");
        JSONObject questionsJson = jsonObject.getJSONObject("questions");
        questionsJson.keys().forEachRemaining(key -> {
            chapter.questionList.add(Question.fromJson(questionsJson.getJSONObject(key)));
        });
        return chapter;
    }
}
