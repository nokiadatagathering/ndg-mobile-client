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
            String customFontName = null;
            try{
                String fontSize = attributes.getValue( attributes.getIndex( "fontSize" ) );
                size = Integer.parseInt( fontSize );
                int idx = attributes.getIndex( "customFontName" );
                if(idx >= 0){
                    customFontName = attributes.getValue(idx);
                }

            } catch( Exception ex ) {
            }
            toolbox.setFontSizeSetting(size);
            if(customFontName != null){
                toolbox.setFontSizeNameSettings(customFontName);
            }
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
