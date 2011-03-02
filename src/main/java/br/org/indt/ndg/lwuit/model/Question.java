/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

import java.io.PrintStream;

/**
 *
 * @author mluz
 */
public class Question extends NDGQuestion implements DisplayableItem {

    private String name;
    private Answer answer;
    //private boolean isVisited;
    

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayableName() {
        return getName();
    }

    /**
     * this method should be abstract, but for refactoring reasons, it is not.
     * @return
     */
    public boolean passConstraints() {
        return true;
    }

    /**
     * this method should be abstract, but for refactoring reasons, it is not.
     * @return
     */
    public void save(PrintStream _output, StringBuffer bfSMS) {
    }

    /**
     * this method should be abstract, but for refactoring reasons, it is not.
     * @return
     */
    public void save(PrintStream _output){
    };

    public String getDisplayValue() throws Exception{
        return (String) answer.getValue();
    }
        
}
