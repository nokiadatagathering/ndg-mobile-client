package br.org.indt.ndg.mobile.structures.question;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;
import br.org.indt.ndg.mobile.AppMIDlet;

import br.org.indt.ndg.mobile.logging.Logger;
import br.org.indt.ndg.mobile.structures.question.custom.CustomChoiceGroup;


public class TypeChoice extends Question {
    
    private CustomChoiceGroup choiceWidget = null;
    private Hashtable choices = new Hashtable();
    private Vector choicesOrdered = new Vector();
    private String select = "exclusive";
    
    public Hashtable getChoices() { return choices; }
    public Vector getOrderedChoices() { return choicesOrdered; }
    public String getSelectType() { return select; }
    
    //skip logic variables default is off
    private String operator="";
    private String operand="";
    private String catTo="";
    private String skipTo="";
    private boolean skipEnabled=false;  //default is off...if tag is found in survey then skipLogic is being used
    
    //get and set skip logic variable methods
    public void setSkipEnabled(boolean _bool) { this.skipEnabled = _bool; }
    public boolean getSkipEnabled() { return this.skipEnabled; }
    public void setCatTo(String _val) { this.catTo = _val; }
    public void setSkipTo(String _val) { this.skipTo = _val; }
    public void setOperator(String _val) { this.operator = _val; }
    public void setOperand(String _val) { this.operand = _val; }
    public int getOperand() { return Integer.parseInt(operand); }
    public String getOperator() { return this.operator; }
        
    public int [] getJumpInts() {
        int [] array = new int[2];   
        array[0] = Integer.parseInt(this.catTo) - 1;
        array[1] = Integer.parseInt(this.skipTo) - 1;
        return array;
    }

    public String getJumpString() { return this.skipTo; }
    
    public TypeChoice() { 
    }
    
    public void addChoice(String choice, String _other) { 
       choices.put(choice, _other);
    }
    
    public void addChoiceOrdered(String choice) {
        choicesOrdered.addElement(choice);
    }
    
    public void setSelect(String _select) {
        select = _select;
    }
    
    public CustomChoiceGroup getWidget() {
        if (isNew()) {
            choiceWidget = new CustomChoiceGroup(super.getDescription(), this);
            setIsNew(false);
        }
        return choiceWidget.getWidget();
    }
    
    public void save(PrintStream _output, StringBuffer bfSMS){
        
        if (choiceWidget!=null) {
           boolean [] selectedItems = choiceWidget.getSelectedItems();
            
            for (int index=0; index < selectedItems.length; index++) {
                if (selectedItems[index]) {
                    String selectedName = choiceWidget.getSelectedName(index);
                    
                    if (((String) choices.get(selectedName)).equals("1")) {
                        String otherText = choiceWidget.getOtherText(selectedName);
                        otherText = AppMIDlet.getInstance().u2x(otherText);
                        _output.print("<other " + "index=\"" + index + "\">");
                        _output.print(otherText);
                        _output.println("</other>");
                        bfSMS.append(index + "^" + "_" + otherText + "_");
                    } else {
                        _output.print("<item>");
                        _output.print(index);
                        _output.println("</item>");
                        bfSMS.append(index + "^" );
                    }
                    
                }
            }
        }
    }
    
    public String getDisplayValue(){
        return getWidget().getSelectedValue();
    }

    public boolean passConstraints() {
        return true;
    }

}
