package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.lwuit.control.BackResultViewCommand;
import br.org.indt.ndg.lwuit.control.DeleteCurrentResultCommand;
import br.org.indt.ndg.lwuit.control.OpenResultCommand;
import br.org.indt.ndg.lwuit.control.SendResultCommand;
import br.org.indt.ndg.lwuit.control.SurveysControl;
import br.org.indt.ndg.lwuit.extended.DateField;
import br.org.indt.ndg.lwuit.extended.TimeField;
import br.org.indt.ndg.lwuit.model.Category;
import br.org.indt.ndg.lwuit.model.CategoryAnswer;
import br.org.indt.ndg.lwuit.model.CategoryConditional;
import br.org.indt.ndg.lwuit.model.ChoiceAnswer;
import br.org.indt.ndg.lwuit.model.ChoiceQuestion;
import br.org.indt.ndg.lwuit.model.DateAnswer;
import br.org.indt.ndg.lwuit.model.DateQuestion;
import br.org.indt.ndg.lwuit.model.ImageAnswer;
import br.org.indt.ndg.lwuit.model.ImageData;
import br.org.indt.ndg.lwuit.model.ImageQuestion;
import br.org.indt.ndg.lwuit.model.NDGAnswer;
import br.org.indt.ndg.lwuit.model.NumericAnswer;
import br.org.indt.ndg.lwuit.model.NumericQuestion;
import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.lwuit.model.Survey;
import br.org.indt.ndg.lwuit.model.TimeAnswer;
import br.org.indt.ndg.lwuit.model.TimeQuestion;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.structures.ResultStructure;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.plaf.UIManager;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author mluz
 */
public class ResultView extends Screen implements ActionListener {

    private String title2 = Resources.RESULTS_LIST_TITLE;
    private String title1;
    private Font categoryFont = NDGStyleToolbox.fontMediumBold;
    private Font questionFont = NDGStyleToolbox.fontMediumItalic;
    private Font answerFont = NDGStyleToolbox.fontMedium;
    private Survey survey;
    private ResultStructure results;
    int labelheight = 18;
    int labelheightspace = 8;

    private SurveysControl surveysControl = SurveysControl.getInstance();

    protected void loadData() {
        title1 = surveysControl.getSurveyTitle();
        survey = surveysControl.getSurvey();
        results = SurveysControl.getInstance().getResult();
    }

    private void setPreferredHeight(Component component, int labelHeight) {
        Dimension d = component.getPreferredSize();
        d.setHeight(labelHeight);
        component.setPreferredSize(d);
    }

    protected void customize() {

        form.removeAllCommands();
        form.removeAll();

        form.setCyclicFocus(false);

        form.addCommand(BackResultViewCommand.getInstance().getCommand());
        form.addCommand(SendResultCommand.getInstance().getCommand());
        form.addCommand(DeleteCurrentResultCommand.getInstance().getCommand());
        form.addCommand(OpenResultCommand.getInstance().getCommand());
        form.setSmoothScrolling(true);
        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);
        setTitle(title1, title2);
        form.setSmoothScrolling(true);

        Vector categories = survey.getCategories();

        for ( int categoryIndex=0; categoryIndex < categories.size(); categoryIndex++) {
            Category category = (Category) categories.elementAt( categoryIndex );
            Label labelCategory = new Label(category.getName());
            labelCategory.getStyle().setFont(categoryFont);
            labelCategory.setSelectedStyle( labelCategory.getUnselectedStyle() );
            setPreferredHeight(labelCategory, labelheight);

            form.addComponent(labelCategory);

            Vector questions = category.getQuestions();

            String catIndex  = String.valueOf( categoryIndex+1 );
            CategoryAnswer categoryAnswer =  SurveysControl.getInstance().getResult().getCategoryAnswers(catIndex);
            int subCategoriesCount = categoryAnswer.getSubcategoriesCount();

            if( subCategoriesCount == 0 ) {
                TextArea labelSubCategory = createWrappedTextArea( "Category has been disabled", categoryFont );
                form.addComponent( labelSubCategory );
            }


            for( int subCatIndex = 0; subCatIndex < subCategoriesCount; subCatIndex++ ) {
                if( category instanceof CategoryConditional ) {
                    Label labelSubCategory = new Label( "Sub-category: #" + (subCatIndex + 1) );//TODO localize
                    labelSubCategory.getStyle().setFont(categoryFont);
                    labelSubCategory.setSelectedStyle( labelSubCategory.getUnselectedStyle() );
                    form.addComponent( labelSubCategory );
                }

                Hashtable table = categoryAnswer.getSubCategoryAnswers( subCatIndex );

                for ( int questionIndex=0; questionIndex < questions.size(); questionIndex++) {
                    NDGQuestion question = (NDGQuestion)questions.elementAt( questionIndex );
                    NDGAnswer answer = (NDGAnswer)table.get( String.valueOf( question.getIdNumber()));

                    TextArea componentQuestion = createWrappedTextArea( question.getName() + ":", questionFont );
                    componentQuestion.getStyle().setFont(questionFont);
                    Component componentAnswer = getFormattedAnswer(question, answer);
                    Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                    container.addComponent(componentQuestion);
                    container.addComponent(componentAnswer);
                    form.addComponent(container);
                }
            }
            Label space = new Label(" ");
            setPreferredHeight(space, labelheightspace);
            form.addComponent(space);
        }
    }

    private Component getFormattedAnswer( NDGQuestion aQuestion, NDGAnswer aAnswer ){
        Component componentAnswer = null;
        if ( aQuestion instanceof ImageQuestion ) {
            // calculate columns count based on screen width in portrait orientation
            int width = Math.min(form.getWidth(), form.getHeight());
            int columns = (int)(width/ImageData.THUMBNAIL_SIZE);
            Vector images = ((ImageAnswer)(aAnswer)).getImages();
            // calculate rows count based on images and columns count
            int imgCount = images.size();
            int rows = (imgCount%columns == 0? 0 : 1) + imgCount/columns;
            rows = Math.max(rows, 1); // in case of empty picture list
            columns = Math.max(columns, 1); // in case of empty picture list
            Container imgContainer = new Container (new GridLayout(rows, columns));
            for ( int imgIndex = 0; imgIndex < imgCount; imgIndex++ )
            {
                ImageData image = ((ImageData)images.elementAt(imgIndex));
                Component imgComponenet = null;
                if (image != null && image.getThumbnail() != null) {
                    imgComponenet = new Label(image.getThumbnail());
                } else { // empty label for null images (should not happen)
                    imgComponenet = new Label(Screen.getRes().getImage("camera-icon"));
                }
                imgComponenet.setSize(new Dimension(ImageData.THUMBNAIL_SIZE,ImageData.THUMBNAIL_SIZE));
                imgContainer.addComponent(imgComponenet);
            }
            componentAnswer = imgContainer;
        } else {
            if( aQuestion instanceof ChoiceQuestion ){
                ChoiceQuestion cquestion = (ChoiceQuestion) aQuestion;
                String choiceAnswer = " ";
                int nIndex;
                if( !cquestion.isExclusive() ) {
                    Vector vChoicesAnswer = (Vector) ((ChoiceAnswer)aAnswer).getSelectedIndexes();
                    int vsize = vChoicesAnswer.size();
                    for (int k = 0; k < vsize; k++) {
                        nIndex = Integer.parseInt( (String) vChoicesAnswer.elementAt(k) );
                        String other = (String)((ChoiceAnswer)aAnswer).getOtherText( String.valueOf( k ) );
                        choiceAnswer += cquestion.getChoiceText(nIndex);
                        if ( other != null && other.length()>0 ) {
                            choiceAnswer+= "="+other;
                        }
                        choiceAnswer += "\n";
                        if((k+1)<vsize) {
                            choiceAnswer+= " ";
                        }
                    }
                    componentAnswer = createWrappedTextArea(choiceAnswer, answerFont);

                } else {
                    try {
                        nIndex = Integer.parseInt( (String)((Vector)((ChoiceAnswer)aAnswer).getSelectedIndexes()).elementAt( 0 ) );
                        choiceAnswer += cquestion.getChoiceText(nIndex);
                        String other =  (String)((ChoiceAnswer)aAnswer).getOtherText( String.valueOf( nIndex ) );
                        if( other!= null && other.length()>0) {
                            choiceAnswer += "="+other;
                        }
                        componentAnswer = createWrappedTextArea(choiceAnswer, answerFont);
                    } catch ( NumberFormatException ex ) {
                        componentAnswer = createWrappedTextArea(choiceAnswer, answerFont); // empty question
                    } catch ( ArrayIndexOutOfBoundsException ex ) {
                        componentAnswer = createWrappedTextArea(choiceAnswer, answerFont); // empty question
                    }
                }
            } else if ( aQuestion instanceof DateQuestion ) {
                DateField dfDate = new DateField(DateField.DDMMYYYY);
                dfDate.setDate( new Date( ((DateAnswer)aAnswer).getDate() ) );
                componentAnswer = new Label(dfDate.getText());
            }
            else if ( aQuestion instanceof TimeQuestion ) {
                TimeQuestion timeQuestion = (TimeQuestion) aQuestion;
                TimeAnswer timeAnswer = (TimeAnswer)aAnswer;
                TimeField tfDate = null;
                if(timeQuestion.getConvention()==12){
                   tfDate = new TimeField(TimeField.HHMM);
                }else{
                   tfDate = new TimeField(TimeField.HHMM1);
                }
                tfDate.setTime(new Date(timeAnswer.getTime()));
                String convention = "";

                if(timeAnswer.getAmPm24() == 1){
                    convention = " am";
                }else if(timeAnswer.getAmPm24() == 2){
                    convention = " pm";
                }
                componentAnswer = new Label(tfDate.getText()+convention);
            }
            else if ( aQuestion instanceof NumericQuestion ) {
                componentAnswer = createWrappedTextArea(((NumericAnswer)aAnswer).getValueString(), answerFont) ;
            } else {
                componentAnswer = createWrappedTextArea((String)aAnswer.getValue(), answerFont);
            }
        }
        return componentAnswer;
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == OpenResultCommand.getInstance().getCommand())
            OpenResultCommand.getInstance().execute(null);
        else if (cmd == BackResultViewCommand.getInstance().getCommand())
            BackResultViewCommand.getInstance().execute(null);
        else if (cmd == DeleteCurrentResultCommand.getInstance().getCommand())
            DeleteCurrentResultCommand.getInstance().execute(null);
        else if (cmd == SendResultCommand.getInstance().getCommand())
            SendResultCommand.getInstance().execute(null);
    }

        private TextArea createWrappedTextArea(String name, Font font) {
        TextArea item = new TextArea();
        item.setUnselectedStyle(UIManager.getInstance().getComponentStyle("Label"));
        item.setSelectedStyle( item.getUnselectedStyle() );
        item.getStyle().setFont(font);
        item.setEditable(false);
        item.setFocusable(false);
        item.setColumns(20);
        item.setGrowByContent(true);
        item.setText(name);

        // code below seems uncecessary
        int pw = Display.getInstance().getDisplayWidth();
        int w = item.getStyle().getFont().stringWidth(name);
        if (w > pw)
        {
            item.setRows(2);
        }
        else
        {
            item.setRows(1);
        }
        return item;
    }
}
