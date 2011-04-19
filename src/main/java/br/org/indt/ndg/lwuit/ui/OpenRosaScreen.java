package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.xfolite.xforms.dom.UserInterface;
import com.nokia.xfolite.xforms.dom.XFormsDocument;
import com.nokia.xfolite.xforms.submission.MultipartFormDataSerializer;
import com.nokia.xfolite.xforms.submission.MultipartRelatedSerializer;
import com.nokia.xfolite.xforms.submission.XFormsXMLSerializer;
import com.nokia.xfolite.xml.dom.Document;
import com.nokia.xfolite.xml.dom.Element;
import com.nokia.xfolite.xml.dom.WidgetFactory;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 *
 * @author damian.janicki
 */
public abstract class OpenRosaScreen extends Screen implements UserInterface{

    private XFormsDocument m_doc = null;
    private WidgetFactory widgetFactory = null;

    public abstract WidgetFactory createWidgetFactory();

    public void load(String url) {

        try {
            FileConnection fc = (FileConnection) Connector.open(url, Connector.READ);
            InputStream is = fc.openInputStream();

            loadDocument(is);

            is.close();
            fc.close();
        } catch (Exception ex){
            ex.printStackTrace();
            loadError();
        }
    }

    private void loadError(){
        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
        GeneralAlert.getInstance().show(Resources.ERROR_TITLE, Resources.OR_FORM_LOADING_FAILURE, GeneralAlert.ERROR);
    }

    public void createXFormsDocument(){
        m_doc = new XFormsDocument();
        m_doc.setStrictMode(false);
    }

    public XFormsDocument getXFormsDocument(){
        return m_doc;
    }

    private void loadDocument(InputStream inputStream)  {
        KXmlParser parser = new KXmlParser();
        try {
            widgetFactory = createWidgetFactory();

            parser.setInput(inputStream, "UTF-8");
            m_doc.setRendererFactory(widgetFactory);
            m_doc.setUserInterface(this);

            m_doc.addSerializer(new XFormsXMLSerializer());
            m_doc.addSerializer(new MultipartRelatedSerializer());
            m_doc.addSerializer(new MultipartFormDataSerializer());

            m_doc.parse(parser);

        }catch(IOException ex ){
            ex.printStackTrace();
        }catch (XmlPullParserException ex) {
            ex.printStackTrace();
        }
    }

    public void addResultData(){
        Document resultData = AppMIDlet.getInstance().getFileStores().getXFormResult();

        if(m_doc != null && resultData != null ){
            m_doc.addInstance(resultData, "");
        }
    }

    public void log(int lvl, String msg, Element el) {
    }

    public void callSerially(Runnable task) {
    }

    public void callParallel(Runnable task) {
    }

    public void close() {
    }

    public void showMessage(String msg) {
    }

    public void setTitle(String title) {
    }

    public String getProperty(String name) {
        return "";
    }

}
