package br.org.indt.ndg.mobile.submit;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 * Sends given bytes in HTTP POST either compressed or uncompressed
 */
public class HttpNormalPostRequest implements HttpPostRequest {

    private boolean m_useCompression = false;

    private byte[] m_postBytes = null;
    private String m_postUrl = null;
    private boolean m_stopRequest = false;

    public HttpNormalPostRequest(String url, byte[] fileBytes, boolean useCompression ) throws IOException {
        m_postUrl = url;
        m_postBytes = fileBytes;
        m_useCompression = useCompression;
    }

    public void cancel() {
        m_stopRequest = true;
    }

    public byte[] send() throws IOException {
        DataInputStream httpInput = null;
        HttpConnection httpConn = null;
        DataOutputStream httpOutput = null;

        httpConn = (HttpConnection) Connector.open(m_postUrl);
        httpConn.setRequestMethod(HttpConnection.POST);
        httpOutput = httpConn.openDataOutputStream();

         // sending request
        if (!m_stopRequest){
            if (m_useCompression)
                submitCompressFile( httpOutput, m_postBytes );
            else
                submitFile( httpOutput, m_postBytes );
        }

         // receiving response
        byte in = 0;
        if (!m_stopRequest){
            httpInput = new DataInputStream(httpConn.openDataInputStream());
            in = httpInput.readByte();
        }
        byte[] result = {in};

        try {
            if(httpInput != null)
                httpInput.close();
            if(httpOutput != null)
                httpOutput.close();
            if(httpConn != null)
                httpConn.close();
        } catch( Exception ex ) {
            ex.printStackTrace();
        }

        return result;
    }

    private void submitFile( DataOutputStream output, byte[] content ) throws IOException {
        output.writeInt(content.length);
        output.write(content, 0, content.length);
        output.flush();
    }

    private void submitCompressFile( DataOutputStream output, byte[] content ) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        out.reset();

        ZOutputStream zOut=new ZOutputStream(out, JZlib.Z_BEST_COMPRESSION);
        DataOutputStream objOut=new DataOutputStream(zOut);

        objOut.write(content);
        zOut.close();

        output.writeInt(content.length);
        output.writeInt(out.size());
        output.write(out.toByteArray(), 0, out.size());
        output.flush();
    }
}
