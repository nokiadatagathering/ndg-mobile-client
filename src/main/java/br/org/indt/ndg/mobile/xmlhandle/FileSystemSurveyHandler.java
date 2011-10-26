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

package br.org.indt.ndg.mobile.xmlhandle;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.org.indt.ndg.mobile.structures.FileSystemSurveyStructure;

public class FileSystemSurveyHandler extends DefaultHandler {
    
    private FileSystemSurveyStructure structure;
    private Stack tagStack = new Stack();
    
    public FileSystemSurveyHandler() {
    }
    
    public void setFileSystemSurveyStructure(FileSystemSurveyStructure _structure) {
        structure = _structure;
    }
    
    public void startDocument() throws SAXException {}
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //System.out.println("QName: " + qName);
        tagStack.push(qName);
        
        if (qName.equals("survey")) {            
            structure.addName(attributes.getValue(attributes.getIndex("title")));
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
//        String chars = new String(ch, start, length).trim();
//        System.out.println("chars: " + chars);
//        
//        if (chars.length() > 0) {            
//            String qName = (String)tagStack.peek();
//            if (qName.equals("title")) {
//                System.out.println("In title");
//                structure.addName(chars);
//                throw new DoneParsingException();
//            }
//        }
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {}
    
    public void endDocument() throws SAXException {}
}