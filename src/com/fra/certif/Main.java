package com.fra.certif;

import com.fra.model.Answer;
import com.fra.model.Handler;
import com.fra.model.Question;
import com.fra.model.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.fra.model.Handler.Status.OK;

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
    private JComboBox<String> chapterSelector;

    Handler handler = new Handler();


    public Main() {
        chapterSelector.addItem("All chapters");
        handler.getChapterNames().forEach(chapterSelector::addItem);
        startButton.addActionListener(actionEvent -> {
            startButton.setEnabled(false);
            checkButton.setEnabled(true);
            nextButton.setEnabled(true);
            moveNext();
        });
        checkButton.addActionListener(actionEvent -> checktResponse());
        nextButton.addActionListener(actionEvent -> {
            prevButton.setEnabled(true);
            updateText();
            moveNext();

        });
        prevButton.addActionListener(actionEvent -> {
            updateText();
            moveBack();
                }
        );
        chapterSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
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

    private void updateText() {
        handler.update(questionText.getText());
    }
    private void moveNext() {
        handler.moveNext(chapterSelector.getSelectedIndex(), sequentialCheckBox.isSelected());
        printQuestion();
    }

    private void moveBack() {
        handler.moveBack();
        printQuestion();
    }

    private void printQuestion() {
        reset();
        Question question = handler.getCurrentQuestion();
        String chapterName = handler.getCurrentChapterNames();
        questionText.setText("\t***   "
                + chapterName
                + " (Rem: "
                + "chapter.getQuestionList().size()"
                + ")   ***\n" + question.getValue());
        AtomicInteger idx = new AtomicInteger();
        question.getResponseList().forEach(rep -> {
            setChoice(rep, idx.getAndIncrement());
        });
        printScore();
    }

    private void checktResponse() {
        handler.check(isCorrect());
        printScore();
    }

    private boolean isCorrect() {
        boolean correct = true;
        Question currentQuestion = handler.getCurrentQuestion();
        Answer answer = currentQuestion.getAnswer();
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

    private void printScore() {
        score.setText(handler.getScore());
        score.setForeground(handler.getStatus() == OK ? Color.GREEN.darker() : Color.red);
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

}
