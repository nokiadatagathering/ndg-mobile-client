/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.settings;

import java.io.UnsupportedEncodingException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 *
 * @author amartini
 */
public class IMEIHandler {
    RecordStore recordStore;

    public IMEIHandler(){
        try {
              recordStore = RecordStore.openRecordStore("imeiAlreadyRegisteredInServer", true);
            }
        catch (RecordStoreFullException ex) {
                ex.printStackTrace();
            }
        catch (RecordStoreNotFoundException ex) {
                ex.printStackTrace();
            }
        catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
    }


    public boolean isIMEIRegistered(){
        String imeiRegistered = null;
        try {
            if(recordStore.getNumRecords()>0){
                byte[] data = recordStore.getRecord(1);
                try {
                    imeiRegistered = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    return false;
                }
                if("true".equals(imeiRegistered)){
                    return true;
                }
            }
            return false;
        }
        catch(RecordStoreNotFoundException record){
           return false;
        }
        catch (RecordStoreException ex) {
           return false;
        }
    }

    public void registerIMEI(){
        String isRegistered = "true";
        try {
            recordStore.addRecord(isRegistered.getBytes(), 0, isRegistered.length());           
        } catch (RecordStoreNotOpenException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public void closeRegisterIMEI(){
        try {
            recordStore.closeRecordStore();
        } catch (RecordStoreNotOpenException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

}
