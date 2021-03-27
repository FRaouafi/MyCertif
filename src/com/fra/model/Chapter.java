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

    public Chapter(String name) {
        this.name = name;
    }

    public static Chapter fromJson(JSONObject jsonObject) {
        Chapter chapter = new Chapter(jsonObject.getString("name"));
        JSONObject questionsJson = jsonObject.getJSONObject("questions");
        questionsJson.keys().forEachRemaining(key -> {
            chapter.questionList.add(Question.fromJson(questionsJson.getJSONObject(key)));
        });
        return chapter;
    }

    Question get(final int index) {
        return questionList.isEmpty() ? null : questionList.get(index);
    }

    public void add(final Question question) {
        questionList.add(question);
    }

    public Question getRandom() {
        return questionList.get((int) (Math.random() * (questionList.size() - 1)));
    }

    public Question getFirst() {
        return questionList.get(0);
    }

    public void removeQuestion(Question question) {
        questionList.remove(question);
    }

    public boolean isEmpty() {
        return questionList.isEmpty();
    }
}
