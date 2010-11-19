/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

/**
 *
 * @author mluz, amartini, mturiel
 */
public class Category {

    private int totalQuestions;
    private String name;
    private boolean fullFilled = false;
    private NDGQuestion[] questions;

    public NDGQuestion[] getQuestions() {
        return questions;
    }

    public void setQuestions(NDGQuestion[] questions) {
        this.questions = questions;
    }

    public boolean isFullFilled() {
        for ( int i = 0;i< questions.length; i++) {
            if( questions[i].getVisited() == true )
                return true;
        }
        return false;
    }

    public void setFullFilled(boolean fullFilled) {
        this.fullFilled = fullFilled;
    }

    public int getTotalQuestions() {
        return questions.length;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
