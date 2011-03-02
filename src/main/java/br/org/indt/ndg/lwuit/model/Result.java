/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

/**
 *
 * @author mluz
 */
public class Result implements DisplayableItem, CheckableItem {

    private String name;
    private String fileName;
    private boolean isChecked = false;

    public String getName() {
        return name;
    }

    public void setPhisicallyFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getPhisicallyFileName(){
        return fileName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayableName() {
        return getName();
    }

    public void setChecked( boolean check ) {
        isChecked = check;
    }

    public boolean isChecked() {
        return isChecked;
    }

}
