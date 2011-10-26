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

package br.org.indt.ndg.mobile.settings;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.structures.Language;

import java.util.Vector;

/**
 *
 * @author mturiel
 */
public class LanguageHandler {
        
    public LanguageHandler() {
    }

    public String getLanguage() {
        String result = "";
        SettingsStructure settings = AppMIDlet.getInstance().getSettings().getStructure();

        String currentLang = settings.getLanguage();
        Vector availableLang = settings.getLanguages();

        for(int idx = 0; idx < availableLang.size(); idx++){
            Language langTemp = (Language)availableLang.elementAt(idx);
            if(langTemp != null && langTemp.getLocale().equals(currentLang) && langTemp.downloaded()){
                result = langTemp.getLocale();
                break;
            }
        }

        if(result.equals("")){
            result = settings.getDefaultLanguage().getLocale();
            settings.setLanguage(result);
        }
        return result; 
    }
}
