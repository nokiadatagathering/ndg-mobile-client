package br.org.indt.ndg.lwuit.ui.style;

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StyleHandler  extends DefaultHandler {
    private NDGStyleToolbox toolbox;
    private Stack tagStack = new Stack();
    private StyleProxy currentStyleProxy;

    public void setStyleStructure( NDGStyleToolbox _toolbox ) {
        toolbox = _toolbox;
    }

    public void startDocument() throws SAXException { }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("style")) {
            String name = attributes.getValue(attributes.getIndex("name"));
            if ( name!= null && name.equals("list") ) {
                currentStyleProxy = toolbox.listStyle;
            } else if ( name!= null && name.equals("menu") ) {
                currentStyleProxy = toolbox.menuStyle;
            } else if ( name!= null && name.equals("dialogTitle") ) {
                currentStyleProxy = toolbox.dialogTitleStyle;
            }
        } else if ( qName.equals("bgUnselectedColor")
                 || qName.equals("bgSelectedStartColor")
                 || qName.equals("bgSelectedEndColor")
                 || qName.equals("selectedFontColor")
                 || qName.equals("unselectedFontColor")
                  ) {
            tagStack.addElement(qName);
        } else if( qName.equals("settings") ) {
            int size = NDGStyleToolbox.DEFAULT;
            try{
                String fontSize = attributes.getValue( attributes.getIndex( "fontSize" ) );
                size = Integer.parseInt( fontSize );
            } catch( Exception ex ) {
            }
            toolbox.fontSizeSetting = size;
            tagStack.addElement(qName);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if( ((String)tagStack.peek()).equals("settings") ) {
            return;
        }
        if( currentStyleProxy == null ) {
             throw new SAXException("Contex error!");
         }
         String chars = new String(ch, start, length).trim();
         int value = 0;
         try{
             value = Integer.parseInt(chars);
        } catch (Exception ex ) {
            //nothing to do. default value.
        }

         if (chars.length() > 0) {

            String qName = (String)tagStack.peek();

            if ( qName.equals("bgUnselectedColor")) currentStyleProxy.bgUnselectedColor = value;
            else if (qName.equals("bgSelectedStartColor")) currentStyleProxy.bgSelectedStartColor = value;
            else if (qName.equals("bgSelectedEndColor"))currentStyleProxy.bgSelectedEndColor = value;
            else if (qName.equals("selectedFontColor")) currentStyleProxy.selectedFontColor = value;
            else if (qName.equals("unselectedFontColor")) currentStyleProxy.unselectedFontColor = value;
         }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("style")) {
                currentStyleProxy = null;
        } else if ( qName.equals("bgUnselectedColor")
                 || qName.equals("bgSelectedStartColor")
                 || qName.equals("bgSelectedEndColor")
                 || qName.equals("selectedFontColor")
                 || qName.equals("unselectedFontColor")
                  ) {
            tagStack.pop();
        } else if ( qName.equals("style") ) {
            tagStack.pop();
        }
    }

    public void endDocument() throws SAXException {  }
}
