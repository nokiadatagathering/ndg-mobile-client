/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.lwuit.model.NDGQuestion;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.error.NullWidgetException;
import br.org.indt.ndg.lwuit.model.Question;
import java.util.Vector;

/**
 *
 * @author Alexandre Martini
 */
public class QuestionsControl {
    private static QuestionsControl instance = new QuestionsControl();

    private QuestionsControl(){}

    public static QuestionsControl getInstance() {
        return instance;
    }

    public Question[] getQuestions(){
        int categoryIndex = AppMIDlet.getInstance().getCategoryList().getSelectedIndex();
        Vector questions = AppMIDlet.getInstance().getFileStores().getSurveyStructure().getQuestions(categoryIndex);
        int size = questions.size();
        Question[] questionsArray = new Question[size];
        Question question;
        for(int i = 0; i < size; i++){
            NDGQuestion tempQuest = (NDGQuestion) questions.elementAt(i);
            question = new Question();
            question.setName(tempQuest.getDescription());
            if(tempQuest.getVisited())
                question.setVisited(true);
            questionsArray[i] = question;
        }
        return questionsArray;
    }

}
