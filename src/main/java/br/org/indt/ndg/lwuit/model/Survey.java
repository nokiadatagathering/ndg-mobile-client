/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

/**
 *
 * @author mluz
 */
public class Survey implements DisplayableItem{

    private String name;
    private int totalQuestions;
    private int totalResults;
    private Category[] categories;

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public String getDisplayableName() {
        return getName();
    }
}
