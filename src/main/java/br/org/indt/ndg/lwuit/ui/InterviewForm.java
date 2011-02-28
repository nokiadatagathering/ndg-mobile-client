package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.AcceptQuestionListFormCommand;
import br.org.indt.ndg.lwuit.control.BackInterviewFormCommand;
import br.org.indt.ndg.lwuit.control.OpenFileBrowserCommand;
import br.org.indt.ndg.lwuit.control.PersistenceManager;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.control.TakePhotoCommand;
import br.org.indt.ndg.lwuit.extended.CheckBox;
import br.org.indt.ndg.lwuit.extended.DateField;
import br.org.indt.ndg.lwuit.extended.DescriptiveField;
import br.org.indt.ndg.lwuit.extended.FilterProxyListModel;
import br.org.indt.ndg.lwuit.extended.List;
import br.org.indt.ndg.lwuit.extended.NumericField;
import br.org.indt.ndg.lwuit.extended.RadioButton;
import br.org.indt.ndg.lwuit.extended.TimeField;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import br.org.indt.ndg.lwuit.model.DateQuestion;
import br.org.indt.ndg.lwuit.model.DescriptiveQuestion;
import br.org.indt.ndg.lwuit.model.ImageAnswer;
import br.org.indt.ndg.lwuit.model.ImageData;
import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.NumberAnswer;
import br.org.indt.ndg.lwuit.model.NumericQuestion;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import br.org.indt.ndg.lwuit.ui.camera.NDGCameraManager;
import br.org.indt.ndg.lwuit.ui.camera.NDGCameraManagerListener;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Button;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.plaf.Border;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author mturiel, amartini
 */
public class InterviewForm extends Screen implements ActionListener {
    private String title1;
    private String title2;

    private NDGQuestion currentQuestion;
    private Vector vContainers;
    private boolean answerChanged = false;
    
    protected void loadData() {
        createScreen();
        vContainers = new Vector();
        title1 = SurveysControl.getInstance().getOpenedSurveyTitle();
        title2 = Resources.NEW_INTERVIEW;
    }

    protected void customize() {
        form.removeAllCommands();
        form.removeAll();

        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.getContentPane().getStyle().setBorder(Border.createEmpty(), false);
        form.setScrollAnimationSpeed(100);
        form.setFocusScrolling(true);

        NDGQuestion[] questions = SurveysControl.getInstance().getSelectedCategory().getQuestions();

        ContainerUI lastUI = null;
        for ( int j=0; j< questions.length; j++ ){
            currentQuestion = questions[j];
            if (currentQuestion instanceof DescriptiveQuestion) {
                 DescriptiveFieldUI df = new DescriptiveFieldUI( currentQuestion );
                 df.registerQuestion();
                 form.addComponent(df);
                 lastUI = df;
            }
            else if (currentQuestion instanceof NumericQuestion) {
                NumericFieldUI  nf = new NumericFieldUI(currentQuestion);
                nf.registerQuestion();
                form.addComponent(nf);
                lastUI = nf;
            }
            else if (currentQuestion instanceof DateQuestion) {
               DateFieldUI df = new DateFieldUI(currentQuestion);
               df.registerQuestion();
               form.addComponent(df);
               lastUI = df;
            }
            else if (currentQuestion instanceof ChoiceQuestion) {
                if (((ChoiceQuestion) currentQuestion).isExclusive()) {
                    if (((ChoiceQuestion) currentQuestion).getChoices().size() <= 4) {
                        ExclusiveChoiceFieldUI ecf = new ExclusiveChoiceFieldUI(currentQuestion);
                        ecf.registerQuestion();
                        form.addComponent(ecf);
                        lastUI = ecf;
                    } else { //if there are more 4 choices (radiobutton)
                        ExclusiveChoiceFieldAutoCompleteUI ecfa = new ExclusiveChoiceFieldAutoCompleteUI(currentQuestion);
                        ecfa.registerQuestion();
                        form.addComponent(ecfa);
                        lastUI = ecfa;
                    }
                    updateSkippedQuestion((ChoiceQuestion)currentQuestion);
                } else {
                    ChoiceFieldUI cf = new ChoiceFieldUI(currentQuestion);
                    cf.registerQuestion();
                    form.addComponent(cf);
                    lastUI = cf;
                }
            }
            else if(currentQuestion instanceof ImageQuestion){
                ImageFieldUI imgf = new ImageFieldUI(currentQuestion);
                imgf.registerQuestion();
                form.addComponent(imgf);
                lastUI = imgf;
            } else if (currentQuestion instanceof TimeQuestion) {
                if (((TimeQuestion) currentQuestion).getConvention() == 24) {
                    TimeFieldUI tf = new TimeFieldUI(currentQuestion);
                    tf.registerQuestion();
                    form.addComponent(tf);
                    lastUI = tf;
                } else if (((TimeQuestion) currentQuestion).getConvention() == 12) {
                    if(currentQuestion.getFirstTime()) {
                        ((TimeQuestion) currentQuestion).setAm_pm(-1); // clear
                    }
                    TimeField12UI tf = new TimeField12UI(currentQuestion);
                    tf.registerQuestion();
                    form.addComponent(tf);
                    lastUI = tf;
                }
            }

            if( currentQuestion.getSkiped())
            {
                lastUI.setEnabled(false);
            }
            vContainers.addElement(lastUI);
        }

        form.addCommand(BackInterviewFormCommand.getInstance().getCommand());
        form.addCommand(AcceptQuestionListFormCommand.getInstance().getCommand());
        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
        
        if (PersistenceManager.getInstance().isEditing()) {
            title2 = Resources.EDITING;
        }
        setTitle(title1, title2);
        //disable skipped questions. needed when user starts survey at random category
        for( int i=0; i<SurveysControl.getInstance().getQuestionsFlat().size(); i++ )
        {
            NDGQuestion question = (NDGQuestion)SurveysControl.getInstance().getQuestionsFlat().elementAt(i);
            if( question instanceof ChoiceQuestion )
            {
                updateSkippedQuestion((ChoiceQuestion)question);
            }
        }
    }

    private void setModifiedInterview(boolean _val) {
        answerChanged = _val;
    }

    public boolean isModifiedInterview() {
        return answerChanged;
    }

    private void commitAllAnswers() {
        for ( int i = 0; i < vContainers.size(); i++) {
            ((ContainerUI)vContainers.elementAt(i)).commitValue();
        }
    }

    public boolean validateAllAnswersAndResetModifiedFlag() {
        boolean result = validateAllAnswers();
        if (result)
            setModifiedInterview(false);
        return result;
    }

    private boolean validateAllAnswers() {
        commitAllAnswers();
        for ( int i = 0; i< vContainers.size(); i++)
        {
            if (!((ContainerUI)vContainers.elementAt(i)).getQuestion().passConstraints() )
            {
                ((ContainerUI)vContainers.elementAt(i)).requestFocus();
                return false;
            }
        }
        return true;
    }

    private void updateSkippedQuestion(ChoiceQuestion question) {
        try
        {
        int selectedChoiceItem = Integer.parseInt((String) question.getAnswer().getValue());
        boolean sentence1 = ((selectedChoiceItem != ((ChoiceQuestion) question).getChoiceItem()) && (((ChoiceQuestion) question).isInverse()));
        boolean sentence2 = ((selectedChoiceItem == ((ChoiceQuestion) question).getChoiceItem()) && (!((ChoiceQuestion) question).isInverse()));

        Vector questions = SurveysControl.getInstance().getQuestionsFlat();
        int start = questions.indexOf(question);
        int endCat = question.getCatTo();
        int endQuestion = question.getSkipTo();

        if( endCat > 0 && endCat <= SurveysControl.getInstance().getCategoriesFromOpenedSurvey().length )
        {
            Category category = SurveysControl.getInstance().getCategoriesFromOpenedSurvey()[endCat-1];
            if( endQuestion > 0 && endQuestion <= category.getQuestions().length )
            {
                int endQuestionLinear = questions.indexOf(category.getQuestions()[endQuestion-1]);
                for( int i = start+1; i<endQuestionLinear; i++)
                {
                    ((NDGQuestion)questions.elementAt(i)).setSkiped(sentence1 || sentence2);
                }
            }

            for( int i = 0; i< vContainers.size(); i++ )
            {
                ContainerUI container = (ContainerUI)vContainers.elementAt(i);
                boolean skip = container.getQuestion().getSkiped();
                container.setEnabled(!skip);
            }
        }
        }
        catch(Exception ex)
        {
        }
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == BackInterviewFormCommand.getInstance().getCommand() ) {
            BackInterviewFormCommand.getInstance().execute(this);
        } else if ( cmd == AcceptQuestionListFormCommand.getInstance().getCommand() ){
            AcceptQuestionListFormCommand.getInstance().execute(this);
        } else if( cmd == OpenFileBrowserCommand.getInstance().getCommand() ) {
            OpenFileBrowserCommand.getInstance().execute(null);
        } else if ( cmd == TakePhotoCommand.getInstance().getCommand() ) {
            TakePhotoCommand.getInstance().execute(null);
        }
    }

abstract class ContainerUI extends Container implements FocusListener {
    private final int NUMBER_OF_COLUMNS = 20;

    protected TextArea qname;
    protected NDGQuestion question;

    public abstract void commitValue();
    public abstract void setEnabled(boolean enabled);

    public void handleMoreDetails( Object cmd )
    {
    }

    public ContainerUI(NDGQuestion question)
    {
        getStyle().setBorder(Border.createBevelLowered( NDGStyleToolbox.getInstance().focusLostColor,
                                                        NDGStyleToolbox.getInstance().focusLostColor,
                                                        NDGStyleToolbox.getInstance().focusLostColor,
                                                        NDGStyleToolbox.getInstance().focusLostColor ));
        this.question  = question;
    }

    protected TextArea createQuestionName(String name) {
        TextArea questionName = new TextArea();
        questionName.setEditable(false);
        questionName.setFocusable(false);
        questionName.setColumns(NUMBER_OF_COLUMNS);
        questionName.setRows(1);
        questionName.setGrowByContent(false);
        questionName.setText(name);

        int pw = Display.getInstance().getDisplayWidth();
        int w = questionName.getStyle().getFont().stringWidth(name);
        if (w > pw)
        {
            questionName.setGrowByContent(true);
            questionName.setRows(2);
        }
        else
        {
            questionName.setGrowByContent(false);
            questionName.setRows(1);
        }

        return questionName;
    }

    public NDGQuestion getQuestion()
    {
        return question;
    }

    public void focusGained(Component cmpnt){
        getStyle().setBorder(Border.createBevelLowered( NDGStyleToolbox.getInstance().focusGainColor,
                                                        NDGStyleToolbox.getInstance().focusGainColor,
                                                        NDGStyleToolbox.getInstance().focusGainColor,
                                                        NDGStyleToolbox.getInstance().focusGainColor ), false);
        refreshTheme();
    }

    public void focusLost(Component cmpnt) {
        commitValue();	//TODO ensure it can be removed
        if (!question.passConstraints())
        {
            cmpnt.requestFocus();
            return;
        }
        getStyle().setBorder(Border.createBevelLowered( NDGStyleToolbox.getInstance().focusLostColor,
                                                        NDGStyleToolbox.getInstance().focusLostColor,
                                                        NDGStyleToolbox.getInstance().focusLostColor,
                                                        NDGStyleToolbox.getInstance().focusLostColor ), false);
        refreshTheme();
    }
}


     class DescriptiveFieldUI extends ContainerUI{
        DescriptiveField tfDesc;

        public DescriptiveFieldUI(NDGQuestion obj) {
            super(obj);
        }

        public void registerQuestion(){
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            qname = createQuestionName(question.getName());
            addComponent(qname);

            tfDesc = new DescriptiveField(((DescriptiveQuestion)question).getLength());
            tfDesc.setText((String) question.getAnswer().getValue());
            tfDesc.setInputMode("Abc");
            tfDesc.setEditable(true);
            tfDesc.setFocusable(true);
            addComponent(tfDesc);
            tfDesc.addFocusListener(this);
            tfDesc.addDataChangeListener(new DataChangedListener() {
                public void dataChanged(int type, int index) {
                    revalidate();
                }
            });

            if (((DescriptiveQuestion)question).getChoices().size() >= 1) {
                Vector vChoices = ((DescriptiveQuestion)question).getChoices();
                int totalChoices = vChoices.size();
                String[] choices = new String[totalChoices];

                final ListModel underlyingModel = new DefaultListModel();
                for (int i = 0; i < totalChoices; i++) {
                    choices[i] = (String) vChoices.elementAt(i);
                    underlyingModel.addItem(choices[i]);
                }

                final FilterProxyListModel proxyModel = new FilterProxyListModel(underlyingModel);
                proxyModel.setMaxDisplay(4);
                final List choice = new List(proxyModel);

                choice.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    tfDesc.setText(((List) ae.getSource()).getSelectedItem().toString());
                    }
                });

                tfDesc.addDataChangeListener(new DataChangedListener() {

                public void dataChanged(int arg0, int arg1) {
                  //  skipGameKey = true;
                    proxyModel.filter(tfDesc.getText());
                    //setModifiedInterview(true);
                    }
                });

                addComponent(choice);
            } else {
                tfDesc.addDataChangeListener(new HandleInterviewAnswersModified());
            }
            Label spacer = new Label("");
            spacer.setFocusable(false);
            addComponent(spacer);
        }

        public void commitValue() {
            question.getAnswer().setValue(tfDesc.getText());
            question.setVisited(true);
        }

        public void setEnabled( boolean enabled ) {
            qname.setEnabled(enabled);
            tfDesc.setEnabled(enabled);
        }
     }
    ///////////////////////////// Numeric Question /////////////////////////////
    class NumericFieldUI extends ContainerUI{
        NumericField nfNumber;

        public NumericFieldUI(NDGQuestion obj){
            super(obj);
        }

        public void registerQuestion( ) {
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            qname = createQuestionName(question.getName());
            addComponent(qname);

            nfNumber = new NumericField(((NumericQuestion) question).getLength() );
            nfNumber.setFocusable(true);
            String value =((NumberAnswer)question.getAnswer()).getValueString();
            nfNumber.setText(value);
            nfNumber.addFocusListener(this);
            nfNumber.addDataChangeListener(new HandleInterviewAnswersModified());

            addComponent(nfNumber);
            Label spacer = new Label("");
            spacer.setFocusable(false);
            addComponent(spacer);
        }

        public void commitValue( ) {
            question.getAnswer().setValue(nfNumber.getText());
            question.setVisited(true);
        }

        public void setEnabled(boolean enabled) {
            qname.setEnabled(enabled);
            nfNumber.setEnabled(enabled);
        }
    }

    ////////////////////////////// Date Question ///////////////////////////////
    class DateFieldUI extends ContainerUI{
        DateField dfDate;

        public DateFieldUI( NDGQuestion obj ) {
            super(obj);
        }

        public void registerQuestion() {
            setLayout( new BoxLayout(BoxLayout.Y_AXIS));
            qname = createQuestionName(question.getName());
            
            addComponent(qname);
            dfDate = new DateField(DateField.DDMMYYYY);
            long datelong = Long.parseLong((String) question.getAnswer().getValue());
            dfDate.setDate(new Date(datelong));
            dfDate.setEditable(true);
            dfDate.addFocusListener(this);
            dfDate.addDataChangeListener(new HandleInterviewAnswersModified());

            addComponent(dfDate);

            Label spacer = new Label("");
            spacer.setFocusable(false);
            addComponent(spacer);
        }
        
        public void commitValue() {
            Date date = dfDate.getDate();
            Long datelong = new Long(date.getTime());
            question.getAnswer().setValue(datelong.toString());
            question.setVisited(true);
        }

        public void setEnabled(boolean enabled) {
            qname.setEditable(enabled);
            dfDate.setEnabled(enabled);
        }
    }

    class ExclusiveChoiceFieldUI extends ContainerUI {
        private ButtonGroup groupButton;

        public ExclusiveChoiceFieldUI(NDGQuestion obj) {
            super(obj);
        }

        public void registerQuestion(){
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            qname = createQuestionName(question.getName());
            
            addComponent(qname);

            groupButton = new ButtonGroup();
            Vector vChoices = ((ChoiceQuestion)question).getChoices();
            Vector vOthers = ((ChoiceQuestion)question).getOthers();
            
            int totalChoices = vChoices.size();
            String[] choices = new String[totalChoices];
            for (int i = 0; i < totalChoices; i++) {
                choices[i] = (String) vChoices.elementAt(i);
                RadioButton rb = new RadioButton(choices[i]);
                rb.setOther(((String) vOthers.elementAt(i)).equals("1"));
                rb.setOtherText(""); // Initializes with empty string
                rb.addActionListener(new HandleMoreDetails()); // More Details
                rb.addFocusListener(this); // Controls when changing to a new question
                groupButton.add(rb);
                addComponent(rb);
            }
            
            Vector vDefault = ((ChoiceQuestion)question).getDefaultAnswers();
            String answerValue = (String) question.getAnswer().getValue();
            if (answerValue != null && !answerValue.equals("")) {
                int index = Integer.parseInt((String) question.getAnswer().getValue());
                groupButton.setSelected(index);
                RadioButton rb = (RadioButton) groupButton.getRadioButton(index);
                Vector v = ((ChoiceQuestion)question).getOthersText();
                String str = (String) v.elementAt(0);
                rb.setOtherText(str);
            }
            else if (vDefault.size() > 0){
                groupButton.setSelected(Integer.parseInt((String) vDefault.elementAt(0)));
            }
            
            Label spacer = new Label("");
            spacer.setFocusable(false);
            addComponent(spacer);
        }

        public void commitValue()
        {
            String selectedIndex = "";
            Vector vOthersText = new Vector();
            for (int i = 0; i < groupButton.getButtonCount(); i++)
            {
                RadioButton rb = (RadioButton)groupButton.getRadioButton(i);
                if (rb.isSelected())
                {
                    selectedIndex = (Integer.toString(i));
                }
                vOthersText.addElement(rb.getOtherText());
            }
            question.getAnswer().setValue(selectedIndex);
            ((ChoiceQuestion)question).setOthersText(vOthersText);
            question.setVisited(true);
        }

        public void setEnabled(boolean enabled) {
            qname.setEnabled(enabled);
            for( int i = 0; i<groupButton.getButtonCount(); i++)
            {
                groupButton.getRadioButton(i).setEnabled(enabled);
            }
        }

        public void handleMoreDetails( Object obj )
        {
            for( int i = 0; i<groupButton.getButtonCount();i++)
            {
                RadioButton rb = (RadioButton) groupButton.getRadioButton(i);
                if ((rb.hasOther()) && (rb.isSelected())) {
                    DetailsForm.show(rb.getText(), rb.getOtherText());
                    rb.setOtherText(SurveysControl.getInstance().getItemOtherText());
                }
                if ( (rb.hasOther()) && (!rb.isSelected()))
                {
                    rb.setOtherText("");
                }
            }
            commitValue();
        }
    }

    class ExclusiveChoiceFieldAutoCompleteUI extends ContainerUI {
        ButtonGroup groupButton;
        DescriptiveField exclusiveChoiceTextField;
        ListModel underlyingModel;





        public ExclusiveChoiceFieldAutoCompleteUI(NDGQuestion obj) {
            super(obj);
        }

        public void focusGained(Component cmpnt) {
            super.focusGained(cmpnt);
            exclusiveChoiceTextField.keyPressed(Display.GAME_FIRE);
        }

        public void registerQuestion(){
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            qname = createQuestionName(question.getName());
            addComponent(qname);

            
            groupButton = new ButtonGroup();

            Vector vChoices = ((ChoiceQuestion)question).getChoices();
            Vector vOthers = ((ChoiceQuestion)question).getOthers();
            int totalChoices = vChoices.size();
            String[] choices = new String[totalChoices];


            underlyingModel = new DefaultListModel();
            final ManagerOptionSelectableRadio managerOptionSelectableRadio = new ManagerOptionSelectableRadio();
            int maxQuestionLength = 0;
            for (int i = 0; i < totalChoices; i++) {
                    choices[i] = (String) vChoices.elementAt(i);
                    RadioButton rb = new RadioButton(choices[i]);
                    underlyingModel.addItem(new OptionSelectableRadio(choices[i], managerOptionSelectableRadio));
                    rb.setOther(((String) vOthers.elementAt(i)).equals("1"));
                    rb.setOtherText(""); // Initializes with empty string
                    rb.setFocusable(false);
                    groupButton.add(rb);
                    maxQuestionLength = (choices[i].length() > maxQuestionLength) ? choices[i].length() : maxQuestionLength;
            }
            
            exclusiveChoiceTextField = new DescriptiveField(maxQuestionLength);

            Vector vDefault = ((ChoiceQuestion)question).getDefaultAnswers();
            String answerValue = (String) question.getAnswer().getValue();
            if (answerValue != null && !answerValue.equals("")) {
                int index = Integer.parseInt((String) question.getAnswer().getValue());
                groupButton.setSelected(index);
                ((OptionSelectableRadio) underlyingModel.getItemAt(index)).setSelected(true);
                
                RadioButton rb = (RadioButton) groupButton.getRadioButton(index);
                Vector v = ((ChoiceQuestion)question).getOthersText();
                String str = (String) v.elementAt(0);
                rb.setOtherText(str);
            }
            else if (vDefault.size() > 0){
                int idx = Integer.parseInt((String) vDefault.elementAt(0));
                groupButton.setSelected(idx);
                ((OptionSelectableRadio) underlyingModel.getItemAt(idx)).setSelected(true);
            }

            final FilterProxyListModel proxyModel = new FilterProxyListModel(underlyingModel);
            proxyModel.setMaxDisplay(-1);//Unlimited.

            final List choice = new List(proxyModel);
            choice.setFocusable(true);
            choice.setPaintFocusBehindList(true);
            choice.setHandlesInput(false);
            choice.setListCellRenderer(new RadioButtonRenderer(""));
            choice.addActionListener(new HandleMoreDetails());
            if( groupButton.getSelectedIndex() >=1 )
            {
                choice.setSelectedIndex(groupButton.getSelectedIndex()-1);
            }

            exclusiveChoiceTextField.addDataChangeListener(new DataChangedListener() {
                public void dataChanged(int arg0, int arg1) {
                proxyModel.filter(exclusiveChoiceTextField.getText());
                }
            });

            exclusiveChoiceTextField.addFocusListener(this);
            Container cList = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            cList.setFocusable(false);
            cList.addComponent(choice);
            cList.setPreferredSize(new Dimension(30, 90));

            addComponent(exclusiveChoiceTextField);
            addComponent(cList);

            Label spacer = new Label("");
            spacer.setFocusable(false);
            addComponent(spacer);
        }

        public void commitValue()
        {
            String selectedIndex = "";
            Vector vOthersText = new Vector();
            for (int i = 0; i < groupButton.getButtonCount(); i++)
            {
                RadioButton rb = (RadioButton)groupButton.getRadioButton(i);
                if (rb.isSelected())
                {
                    selectedIndex = (Integer.toString(i));
                }
                vOthersText.addElement(rb.getOtherText());
            }
            question.getAnswer().setValue(selectedIndex);
            ((ChoiceQuestion)question).setOthersText(vOthersText);
            question.setVisited(true);
        }

        public void setEnabled(boolean enabled) {
            qname.setEnabled(enabled);
            exclusiveChoiceTextField.setEnabled(enabled);
            for( int i = 0; i<groupButton.getButtonCount(); i++)
            {
                groupButton.getRadioButton(i).setEnabled(enabled);
            }
        }

        public void handleMoreDetails( Object obj )
        {
            List list = (List)obj;
            int filterOffset = list.getSelectedIndex();
            int selItem = ((FilterProxyListModel)(list.getModel())).getFilterOffset(filterOffset);

            groupButton.setSelected(selItem);
            for( int i = 0; i<groupButton.getButtonCount();i++)
            {
                RadioButton rb = (RadioButton) groupButton.getRadioButton(i);
                if ((rb.hasOther()) && (rb.isSelected())) {
                    DetailsForm.show(rb.getText(), rb.getOtherText());
                    rb.setOtherText(SurveysControl.getInstance().getItemOtherText());
                }
                if ( (rb.hasOther()) && (!rb.isSelected()))
                {
                    rb.setOtherText("");
                }
            }
            commitValue();
        }
    }

    ////////////////////////// Choice Multiple Question ////////////////////////
    class ChoiceFieldUI extends ContainerUI{
        Vector groupButton;

        public ChoiceFieldUI( NDGQuestion obj) {
            super(obj);
        }

        public void registerQuestion(){
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            qname = createQuestionName(question.getName());
            
            addComponent(qname);

            groupButton = new Vector();
            Vector vChoices = ((ChoiceQuestion)question).getChoices();
            Vector vOthers = ((ChoiceQuestion)question).getOthers();
            
            int totalChoices = vChoices.size();
            String[] choices = new String[totalChoices];
            for (int i = 0; i < totalChoices; i++) {
                choices[i] = (String) vChoices.elementAt(i);
                CheckBox cb = new CheckBox(choices[i]);
                cb.setOther(((String) vOthers.elementAt(i)).equals("1"));
                cb.setOtherText(""); // Initializes with empty string
                cb.addActionListener(new HandleMoreDetails()); // More Details
                cb.addFocusListener(this); // Controls when changing to a new question
                groupButton.addElement(cb);
                addComponent(cb);
            }
            int nIndex;
            Vector vChoicesAnswer = (Vector) question.getAnswer().getValue();
            Vector vDefault = ((ChoiceQuestion)question).getDefaultAnswers();
            for (int i = 0; i < vChoicesAnswer.size(); i++) {
                nIndex = Integer.parseInt((String) vChoicesAnswer.elementAt(i));
                ((CheckBox) groupButton.elementAt(nIndex)).setSelected(true);
                ((CheckBox) groupButton.elementAt(nIndex)).setOtherText((String) ((ChoiceQuestion)question).getOthersText().elementAt(i));
            }
            
            if(vChoicesAnswer.isEmpty() || vDefault.size() > 0){
                for(int idx = 0; idx < vDefault.size(); idx++ ){
                    nIndex = Integer.parseInt((String) vDefault.elementAt(idx));
                    ((CheckBox) groupButton.elementAt(nIndex)).setSelected(true);
                }
            }


            Label spacer = new Label("");
            spacer.setFocusable(false);
            addComponent(spacer);
       }

        public void commitValue()
        {
            Vector vIndexes = new Vector();
            Vector vOthersText = new Vector();
            for (int i = 0; i < groupButton.size(); i++)
            {
                CheckBox cb = (CheckBox) groupButton.elementAt(i);
                if (cb.isSelected())
                {
                    vIndexes.addElement(Integer.toString(i));
                }
                vOthersText.addElement(cb.getOtherText());
            }
            question.getAnswer().setValue(vIndexes);
            ((ChoiceQuestion)question).setOthersText(vOthersText);
            question.setVisited(true);
        }

        public void setEnabled(boolean enabled) {
            qname.setEnabled(enabled);
            for( int i = 0; i< groupButton.size(); i++)
            {
                ((CheckBox)groupButton.elementAt(i)).setEnabled(enabled);
            }
        }

        public void handleMoreDetails( Object obj )
        {
            CheckBox cb = (CheckBox)obj;
            if ((cb.hasOther()) && (cb.isSelected()))
            {
               DetailsForm.show(cb.getText(), cb.getOtherText());
               cb.setOtherText(SurveysControl.getInstance().getItemOtherText());
            }
            if ((cb.hasOther()) && (!cb.isSelected()))
            {
                 cb.setOtherText("");
            }
        }
   }

    class ImageFieldUI extends ContainerUI implements ActionListener, NDGCameraManagerListener {
        private static final int FOUR_ACTIONS_CONTEXT_MENU = 4;//TakePhotoCommand,OpenFileBrowserCommand,ShowPhotoCommand,RemovePhotoCommand
        private static final int TWO_ACTIONS_CONTEXT_MENU = 2;//TakePhotoCommand,OpenFileBrowserCommand

        private Button thumbnailImageButton;
        private Container imageContainer;

        public ImageFieldUI(NDGQuestion obj) {
            super(obj);
        }

        public void update(){
            ImageAnswer imgAnswer = (ImageAnswer)question.getAnswer();
            if(imageContainer.getComponentCount() <= imgAnswer.getImages().size()) {
                addCameraIconButton();
            }
           setModifiedInterview(true);
           form.showBack();
        }

        public void registerQuestion(){
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));

            ImageAnswer imgAnswer = (ImageAnswer)question.getAnswer();

            imageContainer = new Container (new FlowLayout());

            ImageData imgData = null;

            Label spacer = new Label(question.getName());
            spacer.setFocusable(false);
            addComponent(spacer);
            Label maxPhotoCount = new Label( Resources.MAX_IMG_NO + String.valueOf(((ImageQuestion)question).getMaxCount()) );
            maxPhotoCount.getStyle().setFont(NDGStyleToolbox.fontSmall);
            addComponent(maxPhotoCount);

            if(imgAnswer.getImages().size() > 0){
                for(int idx = 0; idx < imgAnswer.getImages().size(); idx++){
                    imgData = (ImageData)imgAnswer.getImages().elementAt(idx);
                    addButton(imgData.getThumbnail());
                }
            }
            addCameraIconButton();
            addComponent(imageContainer);
            addFocusListener(this);
        }

        public void addCameraIconButton(){
            ImageAnswer imgAnswer = (ImageAnswer)question.getAnswer();
            if(imgAnswer.getImages().size() < ((ImageQuestion)question).getMaxCount()){
                Image img = Screen.getRes().getImage("camera-icon");
                addButton(img);
            }
        }

        private void addButton(Image img){
                Button button = new Button();
                button.setIcon(img);
                button.addActionListener(this);
                button.setAlignment(Component.LEFT);
                button.setFocusable(true);
                button.addFocusListener(this);
                imageContainer.addComponent(button);
        }
        
        public void focusGained(Component cmpnt) {
            super.focusGained(cmpnt);
            form.addCommand(OpenFileBrowserCommand.getInstance().getCommand());
            form.addCommand(TakePhotoCommand.getInstance().getCommand());
            NDGCameraManager.getInstance().sendPostProcessData(this, cmpnt,
                    (ImageQuestion) question, imageContainer);
        }

        public void focusLost(Component cmpnt) {
            super.focusLost(cmpnt);
            form.removeCommand(OpenFileBrowserCommand.getInstance().getCommand());
            form.removeCommand(TakePhotoCommand.getInstance().getCommand());
        }

        public void commitValue() {
            question.setVisited(true);
        }

        public void actionPerformed(ActionEvent cmd) {
            if ( cmd.getSource() instanceof Button ) {
                NDGCameraManager.getInstance().sendPostProcessData( this,
                                                                    (Button)cmd.getSource(),
                                                                    (ImageQuestion) question,
                                                                    imageContainer);

                int index = imageContainer.getComponentIndex((Button)cmd.getSource());

                if( index  < ((ImageAnswer)question.getAnswer()).getImages().size() ){
                    new ImageQuestionContextMenu(0, FOUR_ACTIONS_CONTEXT_MENU).show();
                } else {
                    new ImageQuestionContextMenu(0, TWO_ACTIONS_CONTEXT_MENU).show();
                }
            }
        }

        public void setEnabled(boolean enabled) {
            qname.setEnabled(enabled);
            thumbnailImageButton.setEnabled(enabled);
        }
    }

    class TimeFieldUI extends ContainerUI {
        TimeField tfTime;
        
        public TimeFieldUI(NDGQuestion obj) {
             super(obj);
        }

        public void registerQuestion() {
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            qname = createQuestionName(question.getName());
            
            addComponent(qname);
            tfTime = new TimeField(TimeField.HHMM1);

            long datelong = Long.parseLong((String) question.getAnswer().getValue());

            tfTime.setTime(new Date(datelong));
            tfTime.setEditable(true);
            tfTime.addFocusListener(this);
            tfTime.addDataChangeListener(new HandleInterviewAnswersModified());

            addComponent(tfTime);
        
            Label spacer = new Label("");
            spacer.setFocusable(false);
            addComponent(spacer);
        }

        public void commitValue() {
            Date time = tfTime.getTime();
            Long timelong = new Long(time.getTime());
            question.getAnswer().setValue(timelong.toString());
            question.setVisited(true);
        }

        public void setEnabled(boolean enabled) {
            qname.setEnabled(enabled);
            tfTime.setEnabled(enabled);
        }
    }

    class TimeField12UI extends ContainerUI {
        TimeField tfTime;
        ButtonGroup groupButton;

        public TimeField12UI(NDGQuestion obj) {
            super(obj);
        }

        public void registerQuestion(){
            setLayout(new BoxLayout(BoxLayout.Y_AXIS));
            qname = createQuestionName(question.getName());
            
            addComponent(qname);
            tfTime = new TimeField(TimeField.HHMM);
            tfTime.addFocusListener(this);

            groupButton = new ButtonGroup();
            final RadioButton am = new RadioButton("am");
            final RadioButton pm = new RadioButton("pm");
            am.addFocusListener(this);
            pm.addFocusListener(this);

            groupButton.add(am);
            groupButton.add(pm);
            am.addActionListener(new HandleMoreDetails());
            pm.addActionListener(new HandleMoreDetails());

            long datelong = Long.parseLong((String) question.getAnswer().getValue());
            Date date = new Date(datelong);

            if (((TimeQuestion)question).getAm_pm() == 1) {
                groupButton.setSelected(am);
            } else if (((TimeQuestion)question).getAm_pm() == 2) {
                groupButton.setSelected(pm);
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                if( cal.get(Calendar.AM_PM) == Calendar.AM ) {
                    groupButton.setSelected(am);
                     ((TimeQuestion)getQuestion()).setAm_pm(TimeQuestion.AM);
                } else {
                    groupButton.setSelected(pm);
                     ((TimeQuestion)getQuestion()).setAm_pm(TimeQuestion.PM);
                }
            }

            tfTime.setTime(date);
            tfTime.setEditable(true);
            tfTime.addFocusListener(this);
            tfTime.addDataChangeListener(new HandleInterviewAnswersModified());

        addComponent(tfTime);
        addComponent(am);
        addComponent(pm);

        Label spacer = new Label("");
        spacer.setFocusable(false);
        addComponent(spacer);
        }

        public void commitValue() {
            for (int i = 0; i < groupButton.getButtonCount(); i++)
            {
                RadioButton rb = (RadioButton)groupButton.getRadioButton(i);
                if (rb.isSelected())
                {
                    ((TimeQuestion)question).setAm_pm(i == 0? TimeQuestion.AM : TimeQuestion.PM);
                    Date time = tfTime.getTime();
                    Long timelong = new Long(time.getTime());
                    question.getAnswer().setValue(timelong.toString());
                    question.setVisited(true);
                    return;
                }
            }
        }

        public void setEnabled(boolean enabled) {
            qname.setEditable(enabled);
            tfTime.setEnabled(enabled);
            for( int i = 0; i < groupButton.getButtonCount(); i++)
            {
                groupButton.getRadioButton(i).setEnabled(enabled);
            }
        }

        public void handleMoreDetails( Object obj )
        {
            if ( ((TimeQuestion)getQuestion()).getConvention() != 24) {
            RadioButton rb = (RadioButton)groupButton.getRadioButton(groupButton.getSelectedIndex());
                if ( rb.getText().equals("am")) {
                       ((TimeQuestion)getQuestion()).setAm_pm(TimeQuestion.AM);
                } else {
                     if ( rb.getText().equals("pm")) {
                         ((TimeQuestion)getQuestion()).setAm_pm(TimeQuestion.PM);
                     }
                }
            }
        }
     }

    class HandleInterviewAnswersModified implements DataChangedListener {

        public void dataChanged(int arg0, int arg1) {
            // Modification in InterviewForm content
            setModifiedInterview(true);
        }
    }

  class HandleMoreDetails implements ActionListener {

    public void actionPerformed(ActionEvent evt) {
            setModifiedInterview(true);
            Object cmd = evt.getSource();
            if ( cmd instanceof CheckBox )
            {
                ContainerUI parent = (ContainerUI)((CheckBox)cmd).getParent();
                parent.handleMoreDetails( cmd );
            }
            else if (cmd instanceof RadioButton)
            {
                ContainerUI parent = (ContainerUI)((RadioButton)cmd).getParent();
                parent.handleMoreDetails(cmd);
                if ( parent instanceof ExclusiveChoiceFieldUI
                  && parent.getQuestion() instanceof ChoiceQuestion )
                {
                    updateSkippedQuestion((ChoiceQuestion)((ExclusiveChoiceFieldUI)parent).getQuestion());
                }
            }
            else if((cmd instanceof List))
            {
                final List list = (List) cmd;
                if (list.getRenderer() instanceof CheckBox) {
                    ((OptionSelectable) list.getSelectedItem()).toggleSelection();
                    //Component[] group = (Component[]) vGroups.elementAt(focusIndex);
                    //((CheckBox) group[list.getSelectedIndex()]).setSelected(((OptionSelectable) list.getSelectedItem()).getSelected());
                } else if (list.getRenderer() instanceof RadioButton) {
                    Object obj = list.getSelectedItem();
                    if( obj != null )
                    {
                        ((OptionSelectableRadio) list.getSelectedItem()).setSelected(true);
                        ExclusiveChoiceFieldAutoCompleteUI parent = (ExclusiveChoiceFieldAutoCompleteUI)list.getParent().getParent();
                        parent.handleMoreDetails(list);
                        updateSkippedQuestion((ChoiceQuestion)parent.getQuestion());
                    }
                }
            }
        }
    }

   class ManagerOptionSelectableRadio {

        Vector options = new Vector();

        public void addOption(OptionSelectableRadio option) {
            if (!options.contains(option)) {
                options.addElement(option);
            }
        }
        //todo remove from vector

        public void selectOption(OptionSelectableRadio option) {
            if (option.getSelected()) {
                Enumeration options = this.options.elements();
                while (options.hasMoreElements()) {
                    OptionSelectableRadio opt = (OptionSelectableRadio) options.nextElement();
                    if (!opt.toString().equals(option.toString()))
                    {
                        opt.setSelected(false);
                    }
                }

            }
        }
    }

   class OptionSelectable {

        private boolean selected = false;
        private String value;

        public OptionSelectable(String value) {
            this.value = value;
        }

        //public void toggleSelection() {
        public boolean toggleSelection() {
            this.selected = !selected;
            //return selected;
            return selected;
        }

        public String toString() {
            return value;
        }

        public boolean getSelected() {
            return selected;
        }
    }

    class OptionSelectableRadio {

        private boolean selected = false;
        private String value;
        ManagerOptionSelectableRadio group;

        public OptionSelectableRadio(String value, ManagerOptionSelectableRadio group) {
            this.value = value;
            this.group = group;
            this.group.addOption(this);
        }

        //public void toggleSelection() {
        public boolean toggleSelection() {
            this.selected = !selected;
            if (this.selected) {

                group.selectOption(this);
            }
            return selected;
        }

        public String toString() {
            return value;
        }

        public boolean getSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            if (this.selected) {
                group.selectOption(this);
            }
        }

        public boolean equals(Object obj) {
            return value.equals(obj);
        }

        public int hashCode() {
            return value.hashCode();
        }
    }

    class RadioButtonRenderer extends RadioButton implements ListCellRenderer {

        public RadioButtonRenderer(String text) {
            super(text);
        }

        public Component getListCellRendererComponent(com.sun.lwuit.List list, Object o, int i, boolean isSelected) {
            if( o == null )
            {
                return this;
            }
            setSelected(((OptionSelectableRadio) o).getSelected());
            setText(o.toString());

            if (isSelected) {
                setFocus(true);
                getStyle().setBgPainter(focusBGPainter);
//                getStyle().setFont( NDGStyleToolbox.fontLargeBold );
            } else {
                setFocus(false);
                getStyle().setBgPainter(bgPainter);
//                getStyle().setFont( NDGStyleToolbox.fontLarge );
            }
            return this;
        }

        public Component getListFocusComponent(com.sun.lwuit.List list) {
            return this;
        }
    }
}
