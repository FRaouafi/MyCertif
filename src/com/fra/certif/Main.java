package com.fra.certif;

import com.fra.model.*;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JComboBox chapterSelector;

    List<Chapter> chapterList = JsonLoader.Load();
    int chapterIndex = 0;
    int questionIndex = 0;
    int correctAnswers = 0;
    int allAnswers = 0;
    int historyIndex = -1;
    boolean chapterSelection = false;
    List<Question> proceccedQuestions = new ArrayList<>();
    List<Boolean> checkedQuestions = new ArrayList<>();


    public Main() {
        chapterSelector.addItem("All chapters");
        chapterList.forEach(chapter -> chapterSelector.addItem(chapter.getName()));
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
        chapterSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chapterIndex = Math.max(chapterSelector.getSelectedIndex()-1, 0);
                chapterSelection = chapterSelector.getSelectedIndex() == 0;
            }
        });
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

        allAnswers++;
        historyIndex++;
        showQuestion();
    }

    private void showQuestion() {
        final Chapter chapter = chapterList.get(chapterIndex);
        chapter.getQuestionList().remove(currentQuestion());
        questionText.setText("\t***   "
                + chapter.getName()
                + " (Rem: "
                + chapter.getQuestionList().size()
                + ")   ***\n" + currentQuestion().getValue());
        AtomicInteger idx = new AtomicInteger();
        currentQuestion().getResponseList().forEach(rep -> {
            setChoice(rep, idx.getAndIncrement());
        });
        printScore();
    }

    @NonNull
    private Question currentQuestion() {
        return proceccedQuestions.get(historyIndex);
    }

    private void checktResponse() {
        boolean correct = isCorrect();
        disable();
        if (!checkedQuestions.get(historyIndex)) {
            correctAnswers += correct ? 1 : 0;
            printScore();
        }
    }

    private void printScore() {
        score.setText(correctAnswers + "/" + allAnswers);
        score.setForeground(correctAnswers > allAnswers * 2 / 3 ? Color.GREEN.darker() : Color.red);
    }

    @Deprecated
    private boolean isCorrect2() {
        boolean correct = true;
        Answer answer = currentQuestion().getAnswer();
        explanationTextArea.setText(answer.getValue());
        List<String> codes = answer.getCodeList();
        for (int i = 0; i < 6; ++i) {
            if ((choices[i].isSelected() && !codes.contains(String.valueOf((char) ('A' + i)))) ||
                    (!choices[i].isSelected() && codes.contains(String.valueOf((char) ('A' + i))))) {
                correct = false;
                choices[i].setSelected(!choices[i].isSelected());
                choices[i].setForeground(Color.red);
            } else if (choices[i].isSelected() && codes.contains(String.valueOf((char) ('A' + i)))) {
                choices[i].setForeground(Color.green.darker());
            }
        }
        return correct;
    }

    private boolean isCorrect() {
        boolean correct = true;
        Answer answer = currentQuestion().getAnswer();
        explanationTextArea.setText(answer.getValue());
        List<String> codes = answer.getCodeList();
        for (int i = 0; i < 6; ++i) {
            if (codes.contains(String.valueOf((char) ('A' + i)))) {
                choices[i].setForeground(Color.green.darker());
            }
            if ((choices[i].isSelected() && !codes.contains(String.valueOf((char) ('A' + i)))) ||
                    (!choices[i].isSelected() && codes.contains(String.valueOf((char) ('A' + i))))) {
                correct = false;
            }
        }
        return correct;
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
        if (/*!sequentialCheckBox.isSelected() && !*/chapterSelection) {
            chapterIndex = (int) (Math.random() * (chapterList.size() - 1));
        }
        if (sequentialCheckBox.isSelected()) {
            questionIndex = (int) (Math.random() * (chapterList.get(chapterIndex).getQuestionList().size() - 1));
        }
    }

    private void right(JCheckBox checkBox) {
        checkBox.setForeground(Color.green.darker());
    }

    private void wrong(JCheckBox checkBox) {
        checkBox.setForeground(Color.red);
    }
}
