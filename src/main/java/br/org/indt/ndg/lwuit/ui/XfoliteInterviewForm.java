package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.XfoliteInterviewSaveCommand;
import br.org.indt.ndg.lwuit.control.BackCategoriesListCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.ui.xfolite.XfoliteWidgetFactory;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.xfolite.xforms.dom.UserInterface;
import com.nokia.xfolite.xforms.dom.XFormsDocument;
import com.nokia.xfolite.xforms.submission.MultipartFormDataSerializer;
import com.nokia.xfolite.xforms.submission.MultipartRelatedSerializer;
import com.nokia.xfolite.xforms.submission.XFormsXMLSerializer;
import com.nokia.xfolite.xml.dom.Element;
import com.sun.lwuit.Container;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
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

public class XfoliteInterviewForm extends Screen implements UserInterface, ActionListener{

    private Container rootContainer;
    private XFormsDocument m_doc = null;

    private String title1;
    private String title2;

    public XfoliteInterviewForm() {
        m_doc = new XFormsDocument();
    }

    protected void loadData() {
        createScreen();
        title1 = SurveysControl.getInstance().getSurveyTitle();
        title2 = Resources.NEW_INTERVIEW;
        rootContainer = new Container();
    }

    protected void customize() {
        form.removeAllCommands();
        form.removeAll();

        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.getContentPane().getStyle().setBorder(Border.createEmpty(), false);
        form.setScrollAnimationSpeed(500);
        form.setFocusScrolling(true);

        form.addCommand(BackCategoriesListCommand.getInstance().getCommand());
        form.addCommand(XfoliteInterviewSaveCommand.getInstance().getCommand());

        try {
            form.removeCommandListener(this);
        } catch (NullPointerException npe) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        form.addComponent(rootContainer);
        String fileName = Resources.SURVEY_NAME;
        if(AppMIDlet.getInstance().getFileSystem().getResultFilename() != null){
            fileName = AppMIDlet.getInstance().getFileSystem().getResultFilename();
        }

        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        String file = Resources.ROOT_DIR + dirName + fileName;
        setTitle(title1, title2);
        load(file);


    }

    public void log(int lvl, String msg, Element el) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void callSerially(Runnable task) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void callParallel(Runnable task) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void close() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void showMessage(String msg) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void load(String url) {

        try {
            FileConnection fc = (FileConnection) Connector.open(url, Connector.READ);
            InputStream is = fc.openInputStream();

            loadDocument(is);

            is.close();
            fc.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setTitle(String title) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getProperty(String name) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return "";
    }

    private void loadDocument(InputStream inputStream)  {
        KXmlParser parser = new KXmlParser();
        try {
            XfoliteWidgetFactory widgetFactory = new XfoliteWidgetFactory(rootContainer);

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

    public void actionPerformed(ActionEvent ae) {
        Object cmd = ae.getSource();
        if (cmd == BackCategoriesListCommand.getInstance().getCommand()) {
            BackCategoriesListCommand.getInstance().execute(this);
        }
        else if (cmd == XfoliteInterviewSaveCommand.getInstance().getCommand() ) {
            XfoliteInterviewSaveCommand.getInstance().execute(m_doc);
        }
    }
}
