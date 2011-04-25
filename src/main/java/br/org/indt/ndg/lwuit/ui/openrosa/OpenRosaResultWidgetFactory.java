package br.org.indt.ndg.lwuit.ui.openrosa;

import br.org.indt.ndg.lwuit.ui.UIUtils;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.multimedia.Base64Coder;
import com.nokia.xfolite.xforms.dom.BoundElement;
import com.nokia.xfolite.xforms.model.datatypes.DataTypeBase;
import com.nokia.xfolite.xml.dom.Element;
import com.nokia.xfolite.xml.dom.WidgetFactory;
import com.nokia.xfolite.xml.xpath.XPathNSResolver;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BoxLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

/**
 *
 * @author damian.janicki
 */
public class OpenRosaResultWidgetFactory implements WidgetFactory, XPathNSResolver{

    private static OpenRosaResourceManager resourceManager = new OpenRosaResourceManager();
    protected static Hashtable resources = new Hashtable();
    final public static int PREVIEW_PHOTO_SIZE = 80;

    private Container rootContainer = null;
    private Font questionFont = NDGStyleToolbox.fontMedium;
    private Font answerFont = NDGStyleToolbox.fontMedium;

    public OpenRosaResultWidgetFactory(Container cont){
        rootContainer = cont;
        rootContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        resources.clear();
    }

    public void elementParsed(Element el) {
    }

    public void childrenParsed(Element el) {
        String tagName = el.getLocalName();
        BoundElement binding = null;
        if (el instanceof BoundElement) {
            binding = (BoundElement) el;
        }

        if( tagName.equals("input")  || tagName.equals("secret")){
            addInputPreview(binding);
        } else if (tagName.equals("select") || tagName.equals("select1")) {
            addSelectPreview(binding);
        } else if (tagName.equals("value")) {
            addTextValue(el);
        }else if (tagName.equals("upload")) {
            String mediatype = el.getAttribute("mediatype");
            if(mediatype != null && mediatype.indexOf("image") > -1) {
                addPhotoPreview(binding);
            }
        }
    }

    public void addPhotoPreview(BoundElement element){
        String questionLabel = resourceManager.tryGetLabelForElement(element);

        Image samllImgage = null;
        if(element.getStringValue() != null && !element.getStringValue().equals("")){
            byte[] byteArray = Base64Coder.decode(element.getStringValue());
            Image img = Image.createImage(byteArray, 0, byteArray.length);
            samllImgage = img.scaled(PREVIEW_PHOTO_SIZE, PREVIEW_PHOTO_SIZE);
        }
        Container container = new Container( new BoxLayout( BoxLayout.Y_AXIS ) );
        container.addComponent( UIUtils.createTextArea( questionLabel + ":", questionFont, NDGStyleToolbox.getInstance().questionPreviewColor) );
        container.addComponent(new Label(samllImgage));
        rootContainer.addComponent(container);
    }

    public void addSelectPreview(BoundElement element){
        String questionLabel = resourceManager.tryGetLabelForElement(element);
        String questionValue = element.getStringValue();
        questionValue = questionValue.replace(' ', '#');

        addQuestionComponent(questionLabel, questionValue);
    }

    public void addInputPreview(BoundElement element){

        String questionLabel = resourceManager.tryGetLabelForElement(element);
        String questionValue = "";

        DataTypeBase a = element.getDataType();
        if (a != null && a.getBaseTypeID() == DataTypeBase.XML_SCHEMAS_UNKNOWN) {
            questionValue = Resources.UNSUPPORTED_TYPE;
        } else{
            questionValue = element.getStringValue();
        }

        if(element.getDataType().getBaseTypeID() == DataTypeBase.XML_SCHEMAS_DATE){
//            questionValue = toUserFormat(questionValue);
            Date date = OpenRosaUtils.getDateFromString(questionValue);
            questionValue = OpenRosaUtils.getUserFormatDate(date);
        }

        addQuestionComponent(questionLabel, questionValue);
    }

    private void addQuestionComponent(String label, String value){
        Container container = new Container( new BoxLayout( BoxLayout.Y_AXIS ) );
        container.addComponent( UIUtils.createTextArea( label + ":", questionFont, NDGStyleToolbox.getInstance().questionPreviewColor) );
        container.addComponent( UIUtils.createTextArea( value, answerFont, NDGStyleToolbox.getInstance().answerPreviewColor ) );

        rootContainer.addComponent(container);
    }

    public void addTextValue(Element el){
        Element parent = (Element)el.getParentNode();
        if(!parent.getNodeName().equals("text")){
            return;
        }

        String id = parent.getAttribute("id");
        String value = el.getText();

        resourceManager.put(id, value);
    }

    public void removingElement(Element el) {
    }

    public void elementInitialized(Element el) {
    }

    public void childrenInitialized(Element el) {
    }

    public String lookupNamespaceURI(String prefix) {
        return "";
    }
}
