
package br.org.indt.ndg.mobile.xmlhandle;

import org.xml.sax.SAXException;

public class DoneParsingException extends SAXException {
    
    public DoneParsingException() {
        super("Done Parsing");
    }
    
}
