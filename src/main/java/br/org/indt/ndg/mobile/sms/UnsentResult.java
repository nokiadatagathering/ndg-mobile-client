/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.sms;

import java.util.Vector;
import javax.wireless.messaging.Message;


public class UnsentResult {
    private SMSMessage message;
    private String xmlFileName;
    
    private Vector unsent;
    public UnsentResult(SMSMessage message, String xmlFileName){
        this.message = message;
        this.xmlFileName = xmlFileName;
        unsent = new Vector();
    }
    SMSMessage getMessage(){
        return message;
    }
    String getXMLFileName(){
        return xmlFileName;
    }
    
    //THIS NEED TO BE UNIQUE, as the same msg cannot be sent twice
    void addUnsentMessage(Message unsentMsg){
        unsent.addElement(unsentMsg);
    }

}
