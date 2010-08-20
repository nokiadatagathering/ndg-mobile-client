/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.submit;

import br.org.indt.ndg.mobile.AppMIDlet;
/**
 *
 */
public class SubmitResultRunnable implements Runnable{
    
    private String resultFileName;
    public SubmitServer submitServer;
    public StatusScreen statusScreen;
    
    
    /**
     * 
     * @param resultFileName. Must be null if results come from ResultList.
     * Otherwise, must have the result's name to be sent
     */
    public SubmitResultRunnable(String resultFileName){
        this.resultFileName = resultFileName;
    }
    public void run () {
        AppMIDlet.getInstance().setDisplayable(statusScreen);
        if(resultFileName != null) {
            submitServer.submitResult(resultFileName);
        }
        else {
            submitServer.submit(statusScreen);
        }
    }

}
