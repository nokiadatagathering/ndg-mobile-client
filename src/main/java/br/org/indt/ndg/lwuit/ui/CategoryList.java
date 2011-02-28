package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackCategoriesListCommand;
import br.org.indt.ndg.lwuit.control.EnterCategoryCommand;
import br.org.indt.ndg.lwuit.control.SaveResultCommand;
import br.org.indt.ndg.lwuit.control.SaveResultsObserver;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;

/**
 *
 * @author mluz
 */
public class CategoryList extends Screen implements ActionListener, SaveResultsObserver {

    private String title2 = Resources.CATEGORY_LIST_TITLE;
    private String title1;

    private List list;
    private ListModel underlyingModel;

    private SurveysControl surveysControl = SurveysControl.getInstance();
    private Category[] categories;

    protected void loadData() {
        categories = SurveysControl.getInstance().getCategoriesFromOpenedSurvey();
        title1 = surveysControl.getOpenedSurveyTitle();
    }

    protected void customize() {
        setTitle(title1, title2);

        form.removeAllCommands();
        if (list != null)
            form.removeComponent(list);

        form.addCommand(BackCategoriesListCommand.getInstance().getCommand());
        form.addCommand(SaveResultCommand.getInstance().getCommand());
        form.addCommand(EnterCategoryCommand.getInstance().getCommand());

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        // Client 2.0 can use the list.modelChanged(int, int) callback to refresh Lists
        underlyingModel = new DefaultListModel(categories);
        underlyingModel.setSelectedIndex( SurveysControl.getInstance().getSelectedCategoryIndex());
        list = new List(underlyingModel);
        list.setItemGap(0);
        list.addActionListener(this);

        CategoryListCellRenderer clcr = new CategoryListCellRenderer();
        list.setListCellRenderer(clcr);

        list.setFixedSelection(List.FIXED_NONE_CYCLIC);
        form.addComponent(list);
        form.setScrollable(false);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == BackCategoriesListCommand.getInstance().getCommand()) {
            BackCategoriesListCommand.getInstance().execute(null);
        }
        else if (cmd == SaveResultCommand.getInstance().getCommand()) {
            SaveResultCommand.getInstance().setObserver(this);
            SaveResultCommand.getInstance().execute(SurveysControl.getInstance().getQuestionsFlat());
        }
        else if (cmd == EnterCategoryCommand.getInstance().getCommand() || cmd == list) {
            EnterCategoryCommand.getInstance().execute(new Integer(list.getSelectedIndex()));
        }
    }

    public void onResultsSaved() {
        // Refresh ResultList since a new result was probbaly created
        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }
}
