/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AppMIDlet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 *
 * @author roda
 */
public class AES {

    private PaddedBufferedBlockCipher encryptCipher;
    private PaddedBufferedBlockCipher decryptCipher;

    // Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[16];                 //input buffer
    byte[] obuf = new byte[512];            //output buffer

    byte[] key = null;

    public AES() {
        key = AppMIDlet.getInstance().getKey();
        InitCiphers();
    }

    public AES(byte[] keyBytes) {
        key = new byte[keyBytes.length];
        System.arraycopy(keyBytes, 0 , key, 0, keyBytes.length);
        InitCiphers();
    }

    private void InitCiphers() {
        encryptCipher = new PaddedBufferedBlockCipher(new AESEngine());
        System.out.println("Key length in bytes: " + key.length);
        encryptCipher.init(true, new KeyParameter(key));
        decryptCipher =  new PaddedBufferedBlockCipher(new AESEngine());
        decryptCipher.init(false, new KeyParameter(key));
    }

    public void ResetCiphers() {
        if(encryptCipher!=null)
            encryptCipher.reset();
        if(decryptCipher!=null)
            decryptCipher.reset();
    }

    public void encrypt(InputStream in, OutputStream out) throws ShortBufferException,
                                                                 IllegalBlockSizeException,
                                                                 BadPaddingException,
                                                                 DataLengthException,
                                                                 IllegalStateException,
                                                                 InvalidCipherTextException {
        try {
            // Bytes written to out will be encrypted
            // Read in the cleartext bytes from in InputStream and
            //      write them encrypted to out OutputStream

            int noBytesRead = 0;              //number of bytes read from input
            int noBytesProcessed = 0;   //number of bytes processed

            while ((noBytesRead = in.read(buf)) >= 0) {
                    //System.out.println(noBytesRead +" bytes read");
                noBytesProcessed = encryptCipher.processBytes(buf, 0, noBytesRead, obuf, 0);
                    //System.out.println(noBytesProcessed +" bytes processed");
                if(noBytesProcessed > 0)
                    out.write(obuf, 0, noBytesProcessed);
            }

             //System.out.println(noBytesRead +" bytes read");
             noBytesProcessed = encryptCipher.doFinal(obuf, 0);

             //System.out.println(noBytesProcessed +" bytes processed");
             out.write(obuf, 0, noBytesProcessed);

             out.flush();
        }
        catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void decrypt(InputStream in, OutputStream out) throws ShortBufferException,
                                                                 IllegalBlockSizeException,
                                                                 BadPaddingException,
                                                                 DataLengthException,
                                                                 IllegalStateException,
                                                                 InvalidCipherTextException {
        try {
            // Bytes read from in will be decrypted
            // Read in the decrypted bytes from in InputStream and and
            //      write them in cleartext to out OutputStream

            int noBytesRead = 0;        //number of bytes read from input
            int noBytesProcessed = 0;   //number of bytes processed

            while ((noBytesRead = in.read(buf)) >= 0) {
                    //System.out.println(noBytesRead +" bytes read");
                    noBytesProcessed = decryptCipher.processBytes(buf, 0, noBytesRead, obuf, 0);
                    //System.out.println(noBytesProcessed +" bytes processed");
                    out.write(obuf, 0, noBytesProcessed);
            }
            //System.out.println(noBytesRead +" bytes read");
            noBytesProcessed = decryptCipher.doFinal(obuf, 0);
            //System.out.println(noBytesProcessed +" bytes processed");
            out.write(obuf, 0, noBytesProcessed);

            out.flush();
        }
        catch (java.io.IOException e) {
             System.out.println(e.getMessage());
        }
    }

    public void encryptFile(String fileName) {
         try {
            FileConnection fci = (FileConnection)Connector.open(fileName);

            if (!fci.exists()) {
                throw new IOException("File does not exists");
            }
            //createFile("encrypt.txt", false);
            FileConnection fco = (FileConnection)Connector.open(fileName + ".encrypted");

            if (!fco.exists())
                fco.create();

            if (!fco.exists()) {
                throw new IOException("Can not create encrypted file");
            }

            InputStream fis = fci.openInputStream();
            OutputStream fos = fco.openOutputStream();

            // Encrypt
            encrypt(fis, fos);

            fis.close();
            fos.close();
        } catch (Exception e) {
        }
    }

    public void encryptByteArray(String fileName, ByteArrayOutputStream output) {
        try {
            FileConnection fco = (FileConnection)Connector.open(fileName);

            if (!fco.exists())
                fco.create();

            if (!fco.exists()) {
                throw new IOException("Can not create encrypted file");
            }

            InputStream fis = new ByteArrayInputStream(output.toByteArray());
            OutputStream fos = fco.openOutputStream();

	    // Encrypt
            encrypt(fis, fos);

            fis.close();
            fos.close();
        } catch (Exception e) {
        }
    }

    public void decryptFile(String fileName) {
        try {
            FileConnection fci = (FileConnection)Connector.open(fileName);
            if (!fci.exists()) {
                throw new IOException("File does not exists");
            }
            //createFile("encrypt.txt", false);
            FileConnection fco = (FileConnection)Connector.open(fileName + ".not");

            if (!fco.exists()) {
                fco.create();
            }

            if (!fco.exists()) {
                throw new IOException("Can not create encrypted file");
            }

            InputStream fis = fci.openInputStream();
            OutputStream fos = fco.openOutputStream();

	    // Decrypt
            decrypt(fis, fos);

            fis.close();
            fos.close();
        } catch (Exception e) {
        }
    }

    public String decryptFileToString(String filename) throws ShortBufferException,
                                                              IllegalBlockSizeException,
                                                              BadPaddingException,
                                                              DataLengthException,
                                                              IllegalStateException,
                                                              InvalidCipherTextException {
        ByteArrayOutputStream out = null;
        String strTemp = null;

        try {
            FileConnection fci = (FileConnection)Connector.open(filename);
            if (!fci.exists()) {
                throw new IOException("File does not exists");
            }

            InputStream in = fci.openInputStream();
            out = new ByteArrayOutputStream();

            // Bytes read from in will be decrypted
            // Read in the decrypted bytes from in InputStream and and
            //      write them in cleartext to out OutputStream

            int noBytesRead = 0;        //number of bytes read from input
            int noBytesProcessed = 0;   //number of bytes processed

            while ((noBytesRead = in.read(buf)) >= 0) {
                    //System.out.println(noBytesRead +" bytes read");
                    noBytesProcessed = decryptCipher.processBytes(buf, 0, noBytesRead, obuf, 0);
                    //System.out.println(noBytesProcessed +" bytes processed");
                    out.write(obuf, 0, noBytesProcessed);
            }
            //System.out.println(noBytesRead +" bytes read");
            noBytesProcessed = decryptCipher.doFinal(obuf, 0);
            //System.out.println(noBytesProcessed +" bytes processed");
            out.write(obuf, 0, noBytesProcessed);
            strTemp = out.toString();
            in.close();
            out.close();
            //out.flush();
        } catch (java.io.IOException e) {
             System.out.println(e.getMessage());
        }

        return strTemp;
    }

    public InputStream decryptInputStreamToInputStream(InputStream in) throws ShortBufferException,
                                                                              IllegalBlockSizeException,
                                                                              BadPaddingException,
                                                                              DataLengthException,
                                                                              IllegalStateException,
                                                                              InvalidCipherTextException {
        ByteArrayOutputStream out = null;
        ByteArrayInputStream input = null;

        try {
            out = new ByteArrayOutputStream();

            // Bytes read from in will be decrypted
            // Read in the decrypted bytes from in InputStream and and
            //      write them in cleartext to out OutputStream

            int noBytesRead = 0;        //number of bytes read from input
            int noBytesProcessed = 0;   //number of bytes processed

            while ((noBytesRead = in.read(buf)) >= 0) {
                //System.out.println(noBytesRead +" bytes read");
                noBytesProcessed = decryptCipher.processBytes(buf, 0, noBytesRead, obuf, 0);
                //System.out.println(noBytesProcessed +" bytes processed");
                out.write(obuf, 0, noBytesProcessed);
            }
            //System.out.println(noBytesRead +" bytes read");
            noBytesProcessed = decryptCipher.doFinal(obuf, 0);
            //System.out.println(noBytesProcessed +" bytes processed");
            out.write(obuf, 0, noBytesProcessed);
            input = new ByteArrayInputStream(out.toByteArray());
            out.close();
        } catch (java.io.IOException e) {
             System.out.println(e.getMessage());
        } finally {
            if ( in!= null ) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return input;
    }
}