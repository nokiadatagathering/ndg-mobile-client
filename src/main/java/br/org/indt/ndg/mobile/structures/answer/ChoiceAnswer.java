package br.org.indt.ndg.mobile.structures.answer;

import java.util.Vector;
import java.util.Hashtable; 

public class ChoiceAnswer extends Answer {
	
        Vector selectedIndexes = new Vector();
        Hashtable selectedOthers = new Hashtable();
        
        public Vector getSelectedIndexes() { return selectedIndexes; }
        public void setSelectedIndex(String _index) { selectedIndexes.addElement(_index); }
       
        public String getOtherText(String _index) { return (String) selectedOthers.get(_index); }
        public void setOtherText(String _index, String _text) { selectedOthers.put(_index, _text); }
}