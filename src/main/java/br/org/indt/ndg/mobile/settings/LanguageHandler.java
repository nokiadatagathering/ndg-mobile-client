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
