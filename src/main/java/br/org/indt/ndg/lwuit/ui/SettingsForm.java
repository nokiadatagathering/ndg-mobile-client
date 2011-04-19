package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.BackToSurveyListCommand;
import br.org.indt.ndg.lwuit.control.CommandControl;
import br.org.indt.ndg.lwuit.control.GPSCommand;
import br.org.indt.ndg.lwuit.control.ResolutionSelectionViewCommand;
import br.org.indt.ndg.lwuit.control.SelectDateFormatCommand;
import br.org.indt.ndg.lwuit.control.SelectSettingsCommand;
import br.org.indt.ndg.lwuit.control.SelectStyleViewCommand;
import br.org.indt.ndg.lwuit.ui.renderers.SimpleListCellRenderer;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import java.util.Vector;

/**
 *
 * @author damian.janicki
 */
public class SettingsForm extends Screen implements ActionListener{

    private String title2 = Resources.SETTINGS;//NEWUI_TITLE_SURVEY_LIST;
    private String title1 = Resources.NEWUI_NOKIA_DATA_GATHERING;
    private ListModel settingsListModel;
    private List list;
    private Vector commandStringList;
    private Vector commandList;

    protected void loadData() {
        commandList = new Vector();
        commandList.addElement( GPSCommand.getInstance() );
        commandList.addElement( ResolutionSelectionViewCommand.getInstance() );
        commandList.addElement( SelectStyleViewCommand.getInstance() );
        commandList.addElement( SelectDateFormatCommand.getInstance());

        commandStringList = new Vector();
        for( int idx = 0; idx < commandList.size(); idx++){
            CommandControl cmdControl = (CommandControl)commandList.elementAt(idx);
            commandStringList.addElement( cmdControl.getCommand().getCommandName());
        }
    }

    protected void customize() {
        setTitle(title1, title2);
        form.removeAllCommands();
        form.addCommand(BackToSurveyListCommand.getInstance().getCommand());
        form.addCommand(SelectSettingsCommand.getInstance().getCommand());

        if(list != null){
            form.removeComponent(list);
        }

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        settingsListModel = new DefaultListModel(commandStringList);

        list = new List(settingsListModel);
        list.setItemGap(0);
        list.addActionListener(this);
        list.setListCellRenderer(new SimpleListCellRenderer());
        list.setFixedSelection(List.FIXED_NONE_CYCLIC);
        form.addComponent(list);
        form.setScrollable(false);
    }

    public void actionPerformed(ActionEvent ae) {
         Object cmd = ae.getSource();
         if (cmd == BackToSurveyListCommand.getInstance().getCommand()){
             BackToSurveyListCommand.getInstance().execute(null);
         }else if(cmd == SelectSettingsCommand.getInstance().getCommand() || cmd == list){
             if(list.size() >= 0 && list.getSelectedIndex() >= 0){
                  CommandControl com = (CommandControl)commandList.elementAt(list.getSelectedIndex());
                  com.execute(null);
             }
         }

    }

}
