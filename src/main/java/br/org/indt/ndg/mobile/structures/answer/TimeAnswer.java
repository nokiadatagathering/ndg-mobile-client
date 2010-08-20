/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.structures.answer;

public class TimeAnswer extends Answer {
    private long time = 0;
    private long convention;

    public void setTime(long _time) { time = _time; }    
    public long getTime() {  return time; }

    /**
     * @return the convention
     */
    public long getConvention() {
        return convention;
    }

    /**
     * @param convention the convention to set
     */
    public void setConvention(long convention) {
       
        this.convention = convention;
    }
}
