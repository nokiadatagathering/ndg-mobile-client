/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.submit;

import java.util.Vector;
/**
 *
 */
public class SubmitResultRunnable implements Runnable{
    
    private Vector resultFileNameList;
    private SubmitServer submitServer;
    
    /**
     * 
     * @param resultFileName. Must be null if results come from ResultList.
     * Otherwise, must have the result's name to be sent
     */
    public SubmitResultRunnable(String resultFileName){
        this.resultFileNameList = new Vector();
        resultFileNameList.addElement( resultFileName );
    }

    public SubmitResultRunnable( Vector resultFileNameList){
        this.resultFileNameList = resultFileNameList;
    }

    public void setSubmitServer( SubmitServer submitServer)
    {
        this.submitServer = submitServer;
    }

    public void run () {
        submitServer.submit(resultFileNameList);
    }
}
