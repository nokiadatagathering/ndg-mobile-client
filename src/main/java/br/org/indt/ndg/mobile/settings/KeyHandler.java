package br.org.indt.ndg.mobile.settings;

import br.org.indt.ndg.mobile.logging.Logger;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 *
 * @author roda
 */
public class KeyHandler {
    RecordStore recordStore;
    private byte[] key = null;
    private String keyString;
    private int keyId;

    public KeyHandler(){
        try {
              recordStore = RecordStore.openRecordStore("StoreKey", true);
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


    public void isKeyStored(){
        try {
            if(recordStore.getNumRecords() > 0){
//                byte[] pass = recordStore.getRecord(1);
                int i = 0;
                for(i = 0; i < recordStore.getSize(); i++)
                System.out.println("keystore index: " + i + "Record: " + recordStore.getRecord(i));

//                return true;
            }
//            return false;
        }
        catch(RecordStoreNotFoundException record){
//           return false;
        }
        catch (RecordStoreException ex) {
//           return false;
        }
    }

    public void storeKey(byte[] keyHash){
        this.deleteRecordStore();
        try {
            keyId = recordStore.addRecord(keyHash, 0, keyHash.length);

        } catch (RecordStoreNotOpenException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public byte[] getKey(){
        try {
            key = recordStore.getRecord(1);

            StringBuffer sb = new StringBuffer();
            int byteX = 0;
            int i = 0;
            for (byteX = 0; i < key.length; i++ ) {
                byte b = key[i];
                sb.append(Integer.toHexString((int) (b & 0xff)));
            }
            System.out.println("MD5 Digest2:" + sb.toString());

        } catch (RecordStoreNotOpenException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }

        return key;
    }

    public void closeStoreKey(){
        try {
            recordStore.closeRecordStore();
        } catch (RecordStoreNotOpenException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

     public void deleteRecordStore() {
        if(RecordStore.listRecordStores() != null){
            try {
                recordStore.closeRecordStore();
                RecordStore.deleteRecordStore("StoreKey");
                recordStore = RecordStore.openRecordStore("StoreKey", true);
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
         }
    }
}
