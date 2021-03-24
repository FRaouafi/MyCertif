package com.fra.certif;

import com.fra.model.*;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JPanel questions;
    private JPanel explanation;
    private JTextArea questionText;
    private JButton nextButton;
    private JButton prevButton;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;
    private JCheckBox checkBox5;
    private JCheckBox checkBox6;
    private JCheckBox checkBox7;
    private JCheckBox checkBox8;
    private JCheckBox sequentialCheckBox;

    private final JCheckBox[] choices = {checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8};
    private JButton startButton;
    private JButton checkButton;
    private JLabel score;
    private JTextArea explanationTextArea;

    List<Chapter> chapterList = JsonLoader.Load();
    int chapterIndex = 0;
    int questionIndex = 0;
    int correctAnswers = 0;
    int allAnswers = 0;
    int historyIndex = -1;

    List<Question> proceccedQuestions = new ArrayList<>();
    List<Boolean> checkedQuestions = new ArrayList<>();


    public Main() {
        startButton.addActionListener(actionEvent -> {
            startButton.setEnabled(false);
            checkButton.setEnabled(true);
            nextButton.setEnabled(true);
            nextQuestion();
        });
        checkButton.addActionListener(actionEvent -> checktResponse());
        nextButton.addActionListener(actionEvent -> {
            prevButton.setEnabled(true);
            nextQuestion();
            if (chapterList.size() == 0) {
                nextButton.setEnabled(false);
            }
        });
        prevButton.addActionListener(actionEvent -> {
                    historyIndex--;
                    showQuestion();
                    if (historyIndex == 0) {
                        prevButton.setEnabled(false);
                    }
                }
        );
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("JAVA mock exams");
        frame.setContentPane(new Main().mainPanel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setPreferredSize(new Dimension(screenSize.width * 2 / 3, screenSize.height * 2 / 3));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void nextQuestion() {
        reset();
        if (historyIndex == proceccedQuestions.size() - 1) {
            increment();
            Question question = chapterList.get(chapterIndex).getQuestionList().get(questionIndex);
            proceccedQuestions.add(question);
            checkedQuestions.add(false);
            chapterList.get(chapterIndex).getQuestionList().remove(question);
            if (chapterList.get(chapterIndex).getQuestionList().size() == 0) {
                chapterList.remove(chapterList.get(chapterIndex));
            }
        }
        historyIndex++;
        showQuestion();
    }

    private void showQuestion() {
        chapterList.get(chapterIndex).getQuestionList().remove(currentQuestion());
        questionText.setText(currentQuestion().getValue());
        AtomicInteger idx = new AtomicInteger();
        currentQuestion().getResponseList().forEach(rep -> {
            setChoice(rep, idx.getAndIncrement());
        });
    }

    @NonNull
    private Question currentQuestion() {
        return proceccedQuestions.get(historyIndex);
    }

    private void checktResponse() {
        boolean correct = true;
        Answer answer = currentQuestion().getAnswer();
        explanationTextArea.setText(answer.getValue());
        List<String> codes = answer.getCodeList();
        for (int i = 0; i < 6; ++i) {
            if ((choices[i].isSelected() && !codes.contains(String.valueOf((char) ('A' + i)))) ||
                    (!choices[i].isSelected() && codes.contains(String.valueOf((char) ('A' + i))))) {
                correct = false;
                choices[i].setForeground(Color.red);
            } else if (choices[i].isSelected() && codes.contains(String.valueOf((char) ('A' + i)))) {
                choices[i].setForeground(Color.green);
            }
        }
        disable();
        if (!checkedQuestions.get(historyIndex)) {
            correctAnswers += correct ? 1 : 0;
            allAnswers++;
            score.setText(correctAnswers + "/" + allAnswers);
            score.setForeground(correctAnswers > allAnswers * 2 / 3 ? Color.GREEN : Color.red);
        }
    }

    void setChoice(final Response response, final int idx) {
        choices[idx].setVisible(true);
        choices[idx].setText(response.toString());
    }

    void reset() {
        Arrays.stream(choices).forEach(entry -> {
            entry.setVisible(false);
            entry.setEnabled(true);
            entry.setSelected(false);
            entry.setForeground(Color.black);
            entry.setText("");
        });
    }

    void disable() {
        Arrays.stream(choices).forEach(entry -> {
            // entry.setEnabled(false);
        });
    }

    private void increment() {
        if (sequentialCheckBox.isSelected()) {
            chapterIndex = 0;
            questionIndex = 0;
        } else {
            chapterIndex = (int) (Math.random() * (chapterList.size() - 1));
            questionIndex = (int) (Math.random() * (chapterList.get(chapterIndex).getQuestionList().size() - 1));
        }
    }

    private void right(JCheckBox checkBox) {
        checkBox.setForeground(Color.green);
    }

    private void wrong(JCheckBox checkBox) {
        checkBox.setForeground(Color.red);
    }
}
