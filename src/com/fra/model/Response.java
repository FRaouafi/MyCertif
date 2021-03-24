package com.fra.model;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
public class Response {
    String code;
    String value;

    private Response(final String code, final String value) {
        this.code = code;
        this.value = value;
    }

    public static Response fromJson(JSONObject jsonObject) {
        return new Response(jsonObject.getString("code"), jsonObject.getString("value"));
    }

    @Override
    public String toString() {
        return code + ". " + value;
    }
}
