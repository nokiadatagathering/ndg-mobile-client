package br.org.indt.ndg.mobile.structures.question;

import br.org.indt.ndg.lwuit.model.Answer;
import br.org.indt.ndg.lwuit.model.NDGQuestion;

import br.org.indt.ndg.mobile.error.NullWidgetException;

public abstract class Question extends NDGQuestion{
    public abstract String getDisplayValue() throws NullWidgetException;
    public Answer getAnswer() {
        throw new IllegalStateException("this class must not implement this method, which exists for compatibility reasons");
    }

    public void setAnswer(Answer answer) {
        throw new IllegalStateException("this class must not implement this method, which exists for compatibility reasons");
    }

    public String getName() {
        throw new IllegalStateException("this class must not implement this method, which exists for compatibility reasons");
    }

    public void setName(String name) {
        throw new IllegalStateException("this class must not implement this method, which exists for compatibility reasons");
    }
    
}