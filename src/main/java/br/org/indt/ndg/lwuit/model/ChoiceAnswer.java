package br.org.indt.ndg.lwuit.model;

import java.io.PrintStream;
import java.util.Vector;
import java.util.Hashtable;

public class ChoiceAnswer extends NDGAnswer {
    private Vector selectedIndexes = new Vector();
    private Hashtable selectedOthers = new Hashtable();


    public ChoiceAnswer() {
    }

    public Vector getSelectedIndexes() {
        return selectedIndexes;
    }

    public void setSelectedIndex( Vector aSelectedIndexes ) {
        selectedIndexes = aSelectedIndexes;
    }

    public void addSelectedIndex(String _index) {
        selectedIndexes.addElement(_index);
    }

    public String getOtherText(String _index) {
        return (String) selectedOthers.get(_index);
    }

    public void setOtherText( Hashtable aOtherText ) {
        selectedOthers = aOtherText;
    }

    public void addOtherText(String _index, String _text) {
        selectedOthers.put(_index, _text);
    }

    public void save( PrintStream _output ){
        if ( !selectedIndexes.isEmpty() ) {
            if ( selectedOthers.isEmpty() ) {//item
                for( int i = 0; i< selectedIndexes.size(); i++ ) {
                    _output.print("<item>");
                    _output.print( (String)selectedIndexes.elementAt(i) );
                    _output.println("</item>");
                }
            } else { //other
                for( int i =0; i< selectedIndexes.size(); i++ ) {
                    if( selectedOthers.containsKey( (String)selectedIndexes.elementAt(i) ) ) {
                        _output.print("<other " + "index=\"" + (String)selectedIndexes.elementAt(i) + "\">");
                        _output.print((String)selectedOthers.get( (String)selectedIndexes.elementAt(i) ) );
                        _output.println("</other>");
                    } else {
                        _output.print("<item>");
                        _output.print( (String)selectedIndexes.elementAt(i) );
                        _output.println("</item>");
                    }
                }
            }
        }
    }
}