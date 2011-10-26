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

package br.org.indt.ndg.mobile.structures;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Utils;


public class Language {

    private String langName;
    private String locale;

    public Language(String langName, String locale)
    {
        this.langName = langName;
        this.locale = locale;
    }

    public String getLangName() {
        return langName;
    }

    public String getLocale() {
        return locale;
    }

    public boolean downloaded(){
        String defautlLocale = AppMIDlet.getInstance().getSettings().getStructure().getDefaultLanguage().getLocale();
        String langPath = Utils.prepereMessagesPath(locale);

        if(locale.equals(defautlLocale) || Utils.fileExists(langPath)){
            return true;
        }else{
            return false;
        } 
    }
}
