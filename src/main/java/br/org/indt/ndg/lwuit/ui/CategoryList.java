package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackCategoriesListCommand;
import br.org.indt.ndg.lwuit.control.EnterCategoryCommand;
import br.org.indt.ndg.lwuit.control.EnterDirectCategoryConditionalCommand;
import br.org.indt.ndg.lwuit.control.SaveResultCommand;
import br.org.indt.ndg.lwuit.control.SaveResultsObserver;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.model.CategoryConditional;
import br.org.indt.ndg.lwuit.ui.renderers.CategoryListCellRenderer;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import java.util.Vector;

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
    private Vector categories;

    protected void loadData() {
        categories = SurveysControl.getInstance().getSurvey().getCategories();
        title1 = surveysControl.getSurveyTitle();
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

        list.addSelectionListener(new HandleSelectedItem());
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == BackCategoriesListCommand.getInstance().getCommand()) {
            BackCategoriesListCommand.getInstance().execute(null);
        }
        else if (cmd == SaveResultCommand.getInstance().getCommand()) {
            SaveResultCommand.getInstance().setObserver(this);
            SaveResultCommand.getInstance().execute(null);
        }
        else if (cmd == EnterCategoryCommand.getInstance().getCommand() || cmd == list) {
            EnterCategoryCommand.getInstance().execute(new Integer(list.getSelectedIndex()));
        }
    }

    public void onResultsSaved() {
        // Refresh ResultList since a new result was probbaly created
        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }

    private void resetAllCommands() {
        form.removeAllCommands();
        form.addCommand(BackCategoriesListCommand.getInstance().getCommand());
        form.addCommand(SaveResultCommand.getInstance().getCommand());
        form.addCommand(EnterCategoryCommand.getInstance().getCommand());
        form.addCommand(EnterDirectCategoryConditionalCommand.getInstance().getCommand());
    }


    private class HandleSelectedItem implements SelectionListener{

        public HandleSelectedItem() {
        }

        public void selectionChanged(int oldSelected, int newSelected) {
            if( SurveysControl.getInstance().getSurvey().getCategories().elementAt(newSelected) instanceof CategoryConditional ) {
                resetAllCommands();
            } else {
                form.removeCommand( EnterDirectCategoryConditionalCommand.getInstance().getCommand() );
            }
        }
    }
}
