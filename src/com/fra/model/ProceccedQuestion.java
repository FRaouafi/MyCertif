package com.fra.model;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class ProceccedQuestion {
    private Question question;
    private List<JCheckBox>  selected ;

    public static ProceccedQuestion of(final Question question, JCheckBox[] choices){
        ProceccedQuestion proceccedQuestion = new ProceccedQuestion();
        proceccedQuestion.question = question;
        Arrays.stream(choices).forEach(choice -> {
            if(choice.isSelected()){
                proceccedQuestion.selected.add(choice);
            }
        });
        return proceccedQuestion;
    }
}
