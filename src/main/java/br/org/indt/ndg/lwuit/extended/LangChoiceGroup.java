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
