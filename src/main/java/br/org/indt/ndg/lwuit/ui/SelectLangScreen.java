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

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackToSettingsFormCommand;
import br.org.indt.ndg.lwuit.control.CheckLanguagesCommand;
import br.org.indt.ndg.lwuit.control.LanguageUpdateCommand;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupListener;
import br.org.indt.ndg.lwuit.extended.ChoiceGroupSelectionListener;
import br.org.indt.ndg.lwuit.extended.LangChoiceGroup;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.download.LangListDownloaderListener;
import br.org.indt.ndg.mobile.download.LocalizationDownloader;
import br.org.indt.ndg.mobile.download.LocalizationDownloaderListener;
import br.org.indt.ndg.mobile.structures.Language;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import java.util.Vector;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 *
 * @author damian.janicki
 */
public class SelectLangScreen extends Screen implements ActionListener, 
                                ChoiceGroupListener,
                                LocalizationDownloaderListener,
                                LangListDownloaderListener,
                                ChoiceGroupSelectionListener {
    
    private Vector/*<Language>*/ languagesList = null;
    private LangChoiceGroup langChoice = null;
    private int currentLangIndex = 0;

    protected void loadData() {   
    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.LANGUAGE);
        form.removeAll();
        form.removeAllCommands();
        form.addCommand(BackToSettingsFormCommand.getInstance().getCommand());
        form.addCommand(CheckLanguagesCommand.getInstance().getCommand());

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
        addChoiceList();
    }

    private void addChoiceList(){
        languagesList = AppMIDlet.getInstance().getSettings().getStructure().getLanguages();
        if(languagesList.size() == 0){
            return;
        }
        
        String currentLang = AppMIDlet.getInstance().getSettings().getStructure().getLanguage();

        currentLangIndex = 0;
        String[] choices = new String[languagesList.size()];

        for(int idx = 0; idx < languagesList.size(); idx++){
            Language lang = (Language)languagesList.elementAt(idx);
            choices[idx] = lang.getLangName();

            if(lang.getLocale().equals(currentLang)){
                currentLangIndex = idx;
            }
        }

        langChoice = new LangChoiceGroup(languagesList, choices, currentLangIndex);

        langChoice.setCgListener(this);
        langChoice.setSelectionListener(this);
        // to prevent scrolling on top of the settings list
        langChoice.setSelectedIndex(currentLangIndex);
        langChoice.blockLosingFocusUp();
        
        form.addComponent(langChoice);
        form.setFocused(langChoice);
        langChoice.setItemFocused(currentLangIndex);
    }

    private void reloadChoiceList(){
        if(langChoice != null && form.contains(langChoice)){
            form.removeComponent(langChoice);
        }
        addChoiceList();
        form.repaint();
    }

    public void actionPerformed(ActionEvent ae) {
        Object cmd = ae.getSource();
        if (cmd == BackToSettingsFormCommand.getInstance().getCommand()) {
            BackToSettingsFormCommand.getInstance().execute(null);
        }else if(cmd == CheckLanguagesCommand.getInstance().getCommand()) {
            CheckLanguagesCommand.getInstance().execute(this);
        }else if(cmd == LanguageUpdateCommand.getInstance().getCommand()) {
            if(langChoice.getFocusedIndex() == -1){
                return;
            }
            Language lang = (Language)languagesList.elementAt(langChoice.getFocusedIndex());
            Object[] params = new Object[2];
            params[0] = this;
            params[1] = lang.getLocale();
            LanguageUpdateCommand.getInstance().execute(params);
        }
    }

    public void itemChoosed(int i) {
        if(i < languagesList.size()){
            Language lang = (Language)languagesList.elementAt(i);
            String currentLang = AppMIDlet.getInstance().getSettings().getStructure().getLanguage();

            if(!lang.getLocale().equals(currentLang)){
                if(!lang.downloaded()){
                    LocalizationDownloader downloader = new LocalizationDownloader(this);
                    downloader.downloadLocale(lang.getLocale());
                }else{
                    saveSettings(lang.getLocale());
                }
            }
        }
    }

    private void updateCommands(boolean addUpdateCommand){
        form.removeAllCommands();
        form.addCommand(BackToSettingsFormCommand.getInstance().getCommand());
        form.addCommand(CheckLanguagesCommand.getInstance().getCommand());
        if( addUpdateCommand ){
            form.addCommand(LanguageUpdateCommand.getInstance().getCommand());
        }
    }

    private void saveSettings(String locale){
        AppMIDlet.getInstance().getSettings().getStructure().setLanguage( locale );
        AppMIDlet.getInstance().getSettings().writeSettings();

        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_YES_NO, true);
        int resVal = GeneralAlert.getInstance().show(Resources.RESTART, Resources.MSG_RESTART_NEEDED, GeneralAlert.CONFIRMATION);

        if(resVal == GeneralAlert.RESULT_YES){
            try {
                AppMIDlet.getInstance().destroyApp(true);
            } catch (MIDletStateChangeException ex) {
                ex.printStackTrace();
            }
        }else{
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(Resources.INFO, Resources.MSG_NEW_LANGUAGE, GeneralAlert.INFO);
        }
    }

    public void localizationDowonloadFinished(String locale) {
        if(langChoice.getSelectedIndex() == langChoice.getFocusedIndex()){
            saveSettings(locale);
            reloadChoiceList();
        }else{
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(Resources.INFO, Resources.UPDATE_SUCCESS, GeneralAlert.INFO);
        }
    }
    public void downloadFailed(){
        langChoice.setSelectedIndex(currentLangIndex);
        langChoice.setItemFocused(currentLangIndex);
        
        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
        GeneralAlert.getInstance().show(Resources.WARNING_TITLE, Resources.DOWNLOAD_LOCALE_FAILED, GeneralAlert.WARNING);
    }

    public void langListDownloadFinished() {
        Vector newLangList = AppMIDlet.getInstance().getSettings().getStructure().getLanguages();        
        Vector difference = getDiffrence(languagesList, newLangList);

        String messageStr = "";
        if(difference.size() > 0){
            messageStr = Resources.NEW_LANGUAGES + ":\n"; 
            for( int idx = 0; idx < difference.size(); idx++ ){
                messageStr += ((Language)difference.elementAt(idx)).getLangName();
                if( idx != difference.size() -1 ){
                    messageStr += ", ";
                }
            }
        }else{
            messageStr = Resources.NO_NEW_LANGUAGE;
        }

        GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
        GeneralAlert.getInstance().show(Resources.LANGUAGE, messageStr, GeneralAlert.INFO);
        
        reloadChoiceList();
    }

    private Vector getDiffrence(Vector oldLangList, Vector newLangList){
        Vector diff = new Vector();
        for( int idx = 0; idx < newLangList.size(); idx++ ){
            if(!containsLanguage(oldLangList, (Language)newLangList.elementAt(idx))){
                diff.addElement(newLangList.elementAt(idx));
            }
        }
        return diff;
    }

    private boolean containsLanguage(Vector langVect, Language langElement){
        for( int idx = 0; idx < langVect.size(); idx++ ){
            if(langElement.getLocale().equals(((Language)langVect.elementAt(idx)).getLocale())){
                return true;
            }
        }
        return false;
    }

    public void itemSelected(int i) {
        if(i < languagesList.size()){ 
            Language lang = (Language)languagesList.elementAt(i);
            if(i > 0 && lang.downloaded() ){
                updateCommands(true);
            }else{
                updateCommands(false);
            }
        }
    }
}
