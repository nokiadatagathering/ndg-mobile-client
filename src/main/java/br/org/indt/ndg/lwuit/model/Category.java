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
    private boolean fullFilled;
    private NDGQuestion[] questions;

    public NDGQuestion[] getQuestions() {
        return questions;
    }

    public void setQuestions(NDGQuestion[] questions) {
        this.questions = questions;
    }

    public boolean isFullFilled() {
        return fullFilled;
    }

    public void setFullFilled(boolean fullFilled) {
        this.fullFilled = fullFilled;
    }

    public int getTotalQuestions() {
        return totalQuestions;
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
