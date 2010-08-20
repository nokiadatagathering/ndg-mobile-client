package br.org.indt.ndg.mobile.structures.question.custom;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;

import br.org.indt.ndg.mobile.structures.question.TypeChoice;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.logging.Logger;


public class CustomChoiceGroup extends Form implements CommandListener, ItemStateListener {
    
    private ChoiceGroup cg = null;
    private Hashtable others = new Hashtable();
    public Hashtable choices;
    public Vector choicesOrdered;
    private String description = null;
    private int select = Choice.EXCLUSIVE;
    private boolean finalQuestion = false;
    private TypeChoice typeChoice = null;
    
    private boolean [] state;
    
    public Command details;
    
    public CustomChoiceGroup(String _description, TypeChoice _typeChoice) {
        super(_description);
        
        typeChoice = _typeChoice;
        
        choices = _typeChoice.getChoices();
        choicesOrdered = _typeChoice.getOrderedChoices();
        description = _description;
        
        if (_typeChoice.getSelectType().equals("exclusive")) select = Choice.EXCLUSIVE;
        else if (_typeChoice.getSelectType().equals("multiple")) select = Choice.MULTIPLE;
        
        addCommand(Resources.CMD_NEXT);
        //addCommand(Resources.CMD_QUESTIONS);
        addCommand(Resources.CMD_BACK);
        details = new Command(Resources.MORE_DETAILS, Command.ITEM, 1);
        //addCommand(details);
        setCommandListener(this);
        setItemStateListener(this);
        
        StringItem si = new StringItem("", _description);
        si.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
        
        this.append(si);
        
        createWidget();
        if("exclusive".equals(_typeChoice.getSelectType())){
            setSelectedItem("0");
        }
    }
    
    public CustomChoiceGroup getWidget() {
        return this;
    }
    
    public String getSelectedValue() {
        if (select == Choice.EXCLUSIVE) return cg.getString(cg.getSelectedIndex());
        else {
            String result = "";
            boolean [] boolie = getSelectedItems();
            for (int i=0; i < boolie.length; i++) if (boolie[i]) result += cg.getString(i) + "-";
            return result;
        }
    }
    
    public boolean[] getSelectedItems() {
        boolean [] boolArray = new boolean[cg.size()];
        cg.getSelectedFlags(boolArray);
        return boolArray;
    }
    
    public void setSelectedItem(String _index) {
        
        removeCommand(details);
        int index = Integer.parseInt(_index);
        cg.setSelectedIndex(index, true);
        
        String name = (String) choicesOrdered.elementAt(index);
        String value = (String) choices.get(name);
        if (value.equals("1")) {
            addCommand(details);             
        }
        
        //if question is uneditable then just append text
        if (!typeChoice.getEdit()) {
            StringItem newitem = new StringItem("", getSelectedName(index));
            this.append(newitem);
        }
        
        state[index] = true;
    }
    
    public String getOtherText(String _name) {
        return ((CustomTextField) others.get(_name)).getString();
    }
    
    public void setOtherText(String _index, String _otherText) {
        String name = cg.getString(Integer.parseInt(_index));
        ((CustomTextField) others.get(name)).setString(_otherText);
        
        //if question is uneditable then just append text
        if (!typeChoice.getEdit()) {
            StringItem newitem = new StringItem("Other Text:", _otherText);
            this.append(newitem);
        }
    }
    
    public String getSelectedName(int _index) {
        return cg.getString(_index);
    }
    
    public void setFinalQuestion(boolean _bool) {
        finalQuestion = _bool;
    }
    
    private void createWidget() {
        if (cg == null) {
            cg = new ChoiceGroup("", select);
            
            Enumeration e = choicesOrdered.elements();                        
            String name, value;
            CustomTextField ctf;
            
            int size = choicesOrdered.size();
            for(int i = 0; i < size; i++){
                name = (String) choicesOrdered.elementAt(i);
                value = (String) choices.get(name);
                
                cg.append(name, null);
                if (value.equals("1")) {
                    ctf = new CustomTextField(name);
                    ctf.setFinalQuestion(finalQuestion);
                    others.put(name, ctf);                    
                }
            }            

            
            //create selection state array for multiple option
            state = new boolean[cg.size()];
            for (int i=0; i<cg.size(); i++) state[i]=false;
            
            //do NOT append choicegroup item if uneditable 
            if (typeChoice.getEdit()) this.append(cg);
           
        }
    }
    
    private boolean handleOtherFieldForExclusive(int selectedIndex){
        String name = cg.getString(selectedIndex);
        if (choices.get(name).equals("1")) {
            addCommand(details);
            CustomTextField ctf = (CustomTextField) others.get(name);
            ctf.setFinalQuestion(finalQuestion);
            AppMIDlet.getInstance().setDisplayable(ctf);
            return true;
        }
        return false;
    }
    private boolean handleOtherFieldForMultiple(){
        int size = cg.size();
        boolean [] selected = new boolean[size]; 
        cg.getSelectedFlags(selected);
        
        for (int i=0; i < size; i++) {
            if (state[i] != selected[i]) {
                String name = cg.getString(i);
                if (selected[i]) {
                    if (choices.get(name).equals("1")) {
                        CustomTextField ctf = (CustomTextField) others.get(name);
                        ctf.setFinalQuestion(finalQuestion);
                        AppMIDlet.getInstance().setDisplayable(ctf);
                    }
                }
                state[i] = selected[i];
            }
        }
        return true;
    }
    
    public void itemStateChanged(Item item) {
        showTextField(item);
    }
    private boolean showTextField(Item item){
        removeCommand(details);
        if (item.equals(cg)) {
            if (select == Choice.EXCLUSIVE) {
                int selected = ((ChoiceGroup) item).getSelectedIndex();
                return handleOtherFieldForExclusive(selected);                
            } else if(select == Choice.MULTIPLE) {
                return handleOtherFieldForMultiple();                
            }
            else
                return false;
        }
        else{
                return false;
            }
    }
    
    public void commandAction(Command command, Displayable displayable) {
        if (command == Resources.CMD_BACK) {
            AppMIDlet.getInstance().getQuestionList().updateList();
            AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getQuestionList());
        } else if (command == Resources.CMD_NEXT) {
            AppMIDlet.getInstance().getQuestionList().updateList();
            if (checkJump()) {
                if (typeChoice.getJumpString().equals("END")) {
                    AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getCategoryList());
                } else {
                    AppMIDlet.getInstance().getCategoryList().getNextQuestion(typeChoice.getJumpInts());
                }
            } else {
                if (finalQuestion) {
                    AppMIDlet.getInstance().getCategoryList().updateList();
                    AppMIDlet.getInstance().getCategoryList().getNextQuestion();
                } else {
                    AppMIDlet.getInstance().getQuestionList().getNextQuestion();
                }
            }
        }
        else if(command == details){
            if(!showTextField(cg)){
                AppMIDlet.getInstance().getGeneralAlert().showAlert(Resources.NO_DETAILS, Resources.NO_DETAILS_ALTERNATIVE);
            }
        }
    } 
    
    private boolean checkJump() {
        if (typeChoice.getSkipEnabled()) {    
            if (typeChoice.getOperator().equals("0"))  //0 means equals "=="
            {
                if (cg.getSelectedIndex() == typeChoice.getOperand()) return true;
            } else if (typeChoice.getOperator().equals("1"))  //1 means no equals "!="
            {
                if (cg.getSelectedIndex() != typeChoice.getOperand()) return true;
            }         
        }
        return false;
    }
}
