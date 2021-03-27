package com.fra.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.fra.model.Handler.Status.KO;
import static com.fra.model.Handler.Status.OK;

public class Handler {
    List<Chapter> inChapters;
    List<Question> processed = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<Boolean> checked = new ArrayList<>();
    int correctAnswers = 0;
    int allAnswers = 0;
    int currentIndex = -1;

    public Handler() {
        inChapters = JsonLoader.Load();
    }

    public void moveNext(int index, boolean sequential) {
        if (currentIndex >= processed.size() - 1) {
            int chapterIndex = index != 0
                    ? index - 1
                    : (int) (Math.random() * (inChapters.size() - 1));
            Chapter chapter = inChapters.get(chapterIndex);
            Question question = sequential ? chapter.getFirst() : chapter.getRandom();
            chapter.removeQuestion(question);
            if (chapter.isEmpty()) {
                inChapters.remove(chapter);
            }
            processed.add(question);
            names.add(chapter.getName());
            checked.add(false);
            allAnswers++;
        }
        currentIndex++;
    }

    public void moveBack() {
        if(currentIndex>0) {
            currentIndex--;
        }
    }
    public void update(final String text){
        processed.get(currentIndex).setValue(text.substring(text.indexOf('\n')+1));
    }
    public Stream<String> getChapterNames() {
        return inChapters.stream().map(Chapter::getName);
    }

    public String getScore() {
        return correctAnswers + "/" + allAnswers;
    }

    public Status getStatus() {
        return correctAnswers >= allAnswers * 2 / 3 ? OK : KO;

    }

    public Question getCurrentQuestion() {
        return processed.get(currentIndex);
    }

    public String getCurrentChapterNames() {
        return names.get(currentIndex);
    }

    public void check(boolean correct) {
        if (!checked.get(currentIndex) && correct) {
            correctAnswers++;
        }
        checked.set(currentIndex, true);
    }

    public enum Status {OK, KO}

    ;
}
