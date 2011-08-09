package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.Screen;
import br.org.indt.ndg.mobile.structures.Language;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Image;
import java.util.Vector;

/**
 *
 * @author damian.janicki
 */
public class LangChoiceGroup extends ChoiceGroup {

    private Image local = null;
    private Image remote = null;
    private Vector langs = null;

    public LangChoiceGroup(Vector langs, String[] texts, int selectedIndex ){
        super(langs.size());
        this.type = EXCLUSIVE;
        this.selectedIndex = selectedIndex;
        this.radioGroup = new ButtonGroup();
        this.langs = langs;
        local = Screen.getRes().getImage("Local");
        remote = Screen.getRes().getImage("Remote");
        init();
    }
    
     private void init() {

        RadioButton radioButton = null;
        for (int i=0; i<langs.size(); i++) {
            Language language = (Language)langs.elementAt(i);
            if(language.downloaded()){
                radioButton = new RadioButton(language.getLangName(), local);
            }else{
                radioButton = new RadioButton(language.getLangName(), remote);
            }

            choices[i] = radioButton;
            if (i == selectedIndex)
                radioButton.setSelected(true);
            radioButton.addActionListener(this);
            radioButton.addFocusListener(this);
            radioGroup.add(radioButton);
            addComponent(radioButton);
         }
     }
}
