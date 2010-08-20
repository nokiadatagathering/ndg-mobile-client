/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackQuestionListCommand;
import br.org.indt.ndg.lwuit.control.EnterQuestionListCommand;
import br.org.indt.ndg.lwuit.control.QuestionsControl;
import br.org.indt.ndg.lwuit.model.Question;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;

/**
 *
 * @author Alexandre Martini
 */
public class QuestionList extends Screen implements ActionListener{

    private Question[] questions;
    private String path = Resources.QUESTION_LIST_TITLE;
    private String title;

    private List list;
    private ListModel underlyingModel;

    protected void loadData() {
        title = AppMIDlet.getInstance().getFileStores().getSurveyStructure().getTitle();
        questions = QuestionsControl.getInstance().getQuestions();
    }

    protected void customize() {
        setTitle(title, path);

        form.removeAllCommands();

        if (list != null)
            form.removeComponent(list);

        underlyingModel = new DefaultListModel(questions);

        list = new List(underlyingModel);
        list.setItemGap(0);
        list.addActionListener(this);

        DefaultNDGListCellRenderer renderer = new QuestionListCellRenderer();
        list.setListCellRenderer(renderer);

        list.setFixedSelection(List.FIXED_NONE_CYCLIC);
        form.addComponent(list);
        form.setScrollable(false);
        
        form.addCommand(BackQuestionListCommand.getInstance().getCommand());
        if (list.size() > 0) {
           form.addCommand(EnterQuestionListCommand.getInstance().getCommand()); 
        }
        form.setCommandListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == BackQuestionListCommand.getInstance().getCommand()) {
            BackQuestionListCommand.getInstance().execute(null);
        }
        else if (cmd == EnterQuestionListCommand.getInstance().getCommand() || cmd == list) {
            EnterQuestionListCommand.getInstance().execute(new Integer(list.getSelectedIndex()));
        }
    }

}
