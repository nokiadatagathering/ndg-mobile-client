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
