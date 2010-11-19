package br.org.indt.ndg.mobile.submit;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class TestConnectionRunnable implements Runnable {
    public static final int TEST_GPRS = 1;

    private boolean isCanceled = false;


    public void run() {
            testGPRS();
    }

    public void setCanceled(boolean _val) {
        isCanceled = _val;
    }


    private void testGPRS() {
        String urlServlet = AppMIDlet.getInstance().getSettings().getStructure().getServerUrl();
        HttpConnection httpConn = null;
        try {
            httpConn = (HttpConnection) Connector.open(urlServlet);
            httpConn.setRequestMethod(HttpConnection.GET);
            int responseCode = httpConn.getResponseCode();
            //int responseCode = httpConn.HTTP_OK;
            if (!isCanceled) {
                if (responseCode == httpConn.HTTP_OK) {
                    TestConnection.getInstance().UpdateForm(Resources.GPRS_LABEL, Resources.CONNECTION_OK + "!");
                }
                else {
                    TestConnection.getInstance().UpdateForm(Resources.GPRS_LABEL, Resources.CONNECTION_FAILED+ "!");
                }
            }

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            if (!isCanceled) {
                TestConnection.getInstance().UpdateForm(Resources.GPRS_LABEL, Resources.CONNECTION_FAILED+ "!");
            }
        }
        catch (SecurityException ex )
        {
            if (!isCanceled) {
                TestConnection.getInstance().UpdateForm(Resources.GPRS_LABEL, Resources.HTTP_UNAUTHORIZED + "!");
            }
        }
        catch ( IllegalArgumentException ex )
        {
            if (!isCanceled) {
                TestConnection.getInstance().UpdateForm(Resources.GPRS_LABEL, Resources.CONNECTION_FAILED+ "!");
            }
        }
    }
}
