package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.AcceptCategoryConditionalCommand;
import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackCategoryConditionalFormCommand;
import br.org.indt.ndg.lwuit.control.EnterCategoryConditionalCommand;
import br.org.indt.ndg.lwuit.control.SaveResultsObserver;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.model.CategoryConditional;
import br.org.indt.ndg.lwuit.ui.renderers.SimpleListCellRenderer;
import br.org.indt.ndg.mobile.AppMIDlet;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import java.util.Vector;


public class CategoryConditionalList extends Screen implements ActionListener, SaveResultsObserver {

    private String title2 = Resources.CATEGORY_LIST_TITLE;
    private String title1;

    private List list;
    private ListModel underlyingModel;

    private SurveysControl surveysControl = SurveysControl.getInstance();
    private Category[] categories;
    private Vector entries;
    private CategoryConditional selectedCategory;

    protected void loadData() {
        selectedCategory = (CategoryConditional)SurveysControl.getInstance().getSelectedCategory();
        boolean xx = selectedCategory instanceof CategoryConditional;
        if ( !xx ) {
            //throw new UnsupportedOperationException("This cannot happen!!!");
        }
        entries = new Vector();
        for( int i=0; i < selectedCategory.getQuantity(); i++ ) {
            entries.addElement( "No. " + String.valueOf(i) );
        }

        title1 = surveysControl.getSurveyTitle();
    }

    protected void customize() {
        setTitle(title1, title2);

        form.removeAllCommands();
        if (list != null)
            form.removeComponent(list);

        form.addCommand(BackCategoryConditionalFormCommand.getInstance().getCommand());
        form.addCommand(AcceptCategoryConditionalCommand.getInstance().getCommand());
        form.addCommand(EnterCategoryConditionalCommand.getInstance().getCommand());

        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        // Client 2.0 can use the list.modelChanged(int, int) callback to refresh Lists

        underlyingModel = new DefaultListModel(entries);
        underlyingModel.setSelectedIndex( SurveysControl.getInstance().getSelectedSubCategoryIndex());
        list = new List(underlyingModel);
        list.setItemGap(0);
        list.addActionListener(this);

        list.setListCellRenderer( new SimpleListCellRenderer() );

        list.setFixedSelection(List.FIXED_NONE_CYCLIC);
        form.addComponent(list);
        form.setScrollable(false);
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd ==  BackCategoryConditionalFormCommand.getInstance().getCommand()) {
            BackCategoryConditionalFormCommand.getInstance().execute(null);
        }
        else if (cmd == AcceptCategoryConditionalCommand.getInstance().getCommand()) {
            //AcceptCategoryConditionalCommand.getInstance().setObserver(this);
            AcceptCategoryConditionalCommand.getInstance().execute(null);
        }
        else if (cmd == EnterCategoryConditionalCommand.getInstance().getCommand() || cmd == list) {
            EnterCategoryConditionalCommand.getInstance().execute(new Integer(list.getSelectedIndex()));
        }
    }

    public void onResultsSaved() {
        // Refresh ResultList since a new result was probbaly created
        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }
}
