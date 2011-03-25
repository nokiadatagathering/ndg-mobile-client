package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.OpenRosaInterviewSaveCommand;
import br.org.indt.ndg.lwuit.control.BackCategoriesListCommand;
import br.org.indt.ndg.lwuit.control.SaveResultsObserver;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.ui.openrosa.OpenRosaWidgetFactory;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.nokia.xfolite.xforms.dom.UserInterface;
import com.nokia.xfolite.xml.dom.WidgetFactory;
import com.sun.lwuit.Container;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;

/**
 *
 * @author damian.janicki
 */

public class OpenRosaInterviewForm extends OpenRosaScreen implements UserInterface, ActionListener, SaveResultsObserver{

    private Container rootContainer;
    private OpenRosaWidgetFactory widgetFactory = null;

    private String title1;
    private String title2;

    public OpenRosaInterviewForm() {
    }

    protected void loadData() {
        createScreen();
        title1 = SurveysControl.getInstance().getSurveyTitle();
        title2 = Resources.NEW_INTERVIEW;
        rootContainer = new Container();
    }

    protected void customize() {
        form.removeAllCommands();
        form.removeAll();

        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.getContentPane().getStyle().setBorder(Border.createEmpty(), false);
        form.setScrollAnimationSpeed(500);
        form.setFocusScrolling(true);

        form.addCommand(BackCategoriesListCommand.getInstance().getCommand());
        form.addCommand(OpenRosaInterviewSaveCommand.getInstance().getCommand());

        try {
            form.removeCommandListener(this);
        } catch (NullPointerException npe) {
        }
        form.addCommandListener(this);
        form.addComponent(rootContainer);
        setTitle(title1, title2);

        createXFormsDocument();

        String dirName = AppMIDlet.getInstance().getFileSystem().getSurveyDirName();
        String file = Resources.ROOT_DIR + dirName + Resources.SURVEY_NAME;
        addResultData();
        load(file);
    }

    public void actionPerformed(ActionEvent ae) {
        Object cmd = ae.getSource();
        if (cmd == BackCategoriesListCommand.getInstance().getCommand()) {
            BackCategoriesListCommand.getInstance().execute(this);
        } else if (cmd == OpenRosaInterviewSaveCommand.getInstance().getCommand()) {
            if (widgetFactory.commitValues()) {
                OpenRosaInterviewSaveCommand.getInstance().setObserver(this);
                OpenRosaInterviewSaveCommand.getInstance().execute(getXFormsDocument());
            }
        }
    }

    public void onResultsSaved() {
        AppMIDlet.getInstance().setDisplayable(br.org.indt.ndg.lwuit.ui.ResultList.class);
    }

    public WidgetFactory createWidgetFactory() {
        widgetFactory = new OpenRosaWidgetFactory(rootContainer);
        return widgetFactory;
    }
}
