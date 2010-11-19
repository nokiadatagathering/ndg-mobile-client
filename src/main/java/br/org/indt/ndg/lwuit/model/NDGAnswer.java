/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

/**
 *
 * @author alexandre martini
 */
public abstract class NDGAnswer {
    private int answer_id;
    private String type;
    private boolean visited;

    public void setType(String _type) { type = _type; }
    public void setId(int _id) { answer_id = _id; }

    public String getType() { return type; }
    public int getId() { return answer_id; }


    public boolean getVisited() { return this.visited; }
    public void setVisited(String _boolean) {
        if (_boolean.equals("true")) visited=true;
        else visited=false;
    }
}
