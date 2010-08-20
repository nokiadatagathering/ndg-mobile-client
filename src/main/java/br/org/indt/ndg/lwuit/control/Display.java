/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.control;

import br.org.indt.ndg.mobile.AlertSave;
import br.org.indt.ndg.lwuit.ui.*;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.DeleteList;
import br.org.indt.ndg.mobile.SentList;
import br.org.indt.ndg.mobile.error.WaitingForm;
import br.org.indt.ndg.mobile.structures.question.custom.CustomTextField;
import br.org.indt.ndg.mobile.submit.TestConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;

/**
 *
 * @author mluz
 */
public class Display {

    private javax.microedition.lcdui.Display lcduiDisplay;
    private static Display instance;
    private Class current;

    private Display(javax.microedition.lcdui.Display lcduiDisplay) {
        this.lcduiDisplay = lcduiDisplay;
    }

    public static Display getDisplay(MIDlet m) {
        if (instance == null) {
            instance = new Display(javax.microedition.lcdui.Display.getDisplay(m));
        }
        return instance;
    }

    public void setCurrent(Displayable nextDisplayable) {
        /*if (WaitingScreen.isShowed())
            WaitingScreen.dispose();*/
        // here must repeat each new UI class   else if.....
        if (nextDisplayable instanceof javax.microedition.lcdui.Form) {
            if (nextDisplayable == AppMIDlet.getInstance().getResultView()) {
                current = ResultView.class;
                Screen.show(ResultView.class, !(lcduiDisplay.getCurrent() instanceof Alert));
            } else if(nextDisplayable instanceof CustomTextField){
                    current = QuestionForm.class;
                    SurveysControl.getInstance().setCurrentQuestionFormOldUI(nextDisplayable);
                    Screen.show(QuestionForm.class, !(lcduiDisplay.getCurrent() instanceof Alert));
            } else if(nextDisplayable instanceof AlertSave){
                current = Alert.class;
                SurveysControl.getInstance().setAlertSaveOldScreen(nextDisplayable);
                br.org.indt.ndg.lwuit.ui.Alert.show();
            } else if(nextDisplayable instanceof WaitingForm){
                current = WaitingScreen.class;
                WaitingForm wf = (WaitingForm)nextDisplayable;
                WaitingScreen.show(((StringItem)wf.get(1)).getText());
            } else if (nextDisplayable == AppMIDlet.getInstance().getAgreementScreen()) {
                current = AgreementScreen.class;
                Screen.show(AgreementScreen.class, !(lcduiDisplay.getCurrent() instanceof Alert));
            } else if (nextDisplayable instanceof br.org.indt.ndg.mobile.submit.TestConnection) {
                current = TestConnectionNewUI.class;
                Screen.show(TestConnectionNewUI.class, !(lcduiDisplay.getCurrent() instanceof Alert));
            } else if (nextDisplayable instanceof br.org.indt.ndg.mobile.settings.GpsForm) {
                current = GpsForm.class;
                SurveysControl.getInstance().setCurrentOldGpsForm(nextDisplayable);
                Screen.show(GpsForm.class, !(lcduiDisplay.getCurrent() instanceof Alert));
            } else if (nextDisplayable instanceof br.org.indt.ndg.mobile.download.StatusScreen) {
                current = StatusScreenDownload.class;
                SurveysControl.getInstance().setCurrentOldStatusScreenDownload(nextDisplayable);
                Screen.show(StatusScreenDownload.class, !(lcduiDisplay.getCurrent() instanceof Alert));
            } else if (nextDisplayable instanceof br.org.indt.ndg.mobile.submit.StatusScreen) {
                current = StatusScreenSubmit.class;
                SurveysControl.getInstance().setCurrentOldStatusScreenSubmit(nextDisplayable);
                Screen.show(StatusScreenSubmit.class, !(lcduiDisplay.getCurrent() instanceof Alert));
            } else if (nextDisplayable instanceof br.org.indt.ndg.mobile.UpdateClientApp) {
                current = UpdateClientApp.class;
                SurveysControl.getInstance().setCurrentOldUpdateClientApp(nextDisplayable);
                Screen.show(UpdateClientApp.class, !(lcduiDisplay.getCurrent() instanceof Alert));
            }
            else { // Question form is a lcdui Form only and it cannot be identified
                if (AppMIDlet.getInstance().getCategoryList() != null && nextDisplayable.getTitle().equalsIgnoreCase(AppMIDlet.getInstance().getCategoryList().getCurrentCatName())) {
                    current = QuestionForm.class;
                    SurveysControl.getInstance().setCurrentQuestionFormOldUI(nextDisplayable);
                    Screen.show(QuestionForm.class, !(lcduiDisplay.getCurrent() instanceof Alert));
                } else {
                    lcduiDisplay.setCurrent(nextDisplayable);
                }
            }
        }
        else if (nextDisplayable == AppMIDlet.getInstance().getSurveyList()) {
            current = SurveyList.class;
            Screen.show(SurveyList.class, !(lcduiDisplay.getCurrent() instanceof Alert));            
        }
        else if (nextDisplayable == AppMIDlet.getInstance().getCategoryList()) {
            //current = CategoryList.class;
            //Screen.show(CategoryList.class, !(lcduiDisplay.getCurrent() instanceof Alert));
            //current = InterviewForm.class;
            //Screen.show(InterviewForm.class, !(lcduiDisplay.getCurrent() instanceof Alert));
            current = InterviewForm2.class;
            Screen.show(InterviewForm2.class, !(lcduiDisplay.getCurrent() instanceof Alert));
        }
        else if (nextDisplayable == AppMIDlet.getInstance().getResultList()) {
            current = ResultList.class;
            Screen.show(ResultList.class, !(lcduiDisplay.getCurrent() instanceof Alert));
        }
        else if (nextDisplayable instanceof  br.org.indt.ndg.mobile.SubmitList) {
            current = SubmitList.class;
            Screen.show(SubmitList.class, !(lcduiDisplay.getCurrent() instanceof Alert));
        }

        else if (nextDisplayable == AppMIDlet.getInstance().getResultView()) {
            current = ResultView.class;
            Screen.show(ResultView.class, !(lcduiDisplay.getCurrent() instanceof Alert));
        }
        else if (nextDisplayable instanceof SentList) {
            current = SentResultList.class;
            SurveysControl.getInstance().setCurrentOldSentList(nextDisplayable);
            Screen.show(SentResultList.class, !(lcduiDisplay.getCurrent() instanceof Alert));
        }
        else if (nextDisplayable == AppMIDlet.getInstance().getQuestionList()) {
            current = QuestionList.class;
            Screen.show(QuestionList.class, !(lcduiDisplay.getCurrent() instanceof Alert));
        }
        else if (nextDisplayable instanceof DeleteList) {
            current = DeleteResultList.class;
            SurveysControl.getInstance().setCurrentOldDeleteList(nextDisplayable);
            Screen.show(DeleteResultList.class, !(lcduiDisplay.getCurrent() instanceof Alert));
        }
        else if (nextDisplayable instanceof br.org.indt.ndg.mobile.error.GeneralAlert){
            current = Alert.class;
            br.org.indt.ndg.lwuit.ui.Alert.show();
        }
        else if (nextDisplayable instanceof javax.microedition.lcdui.Alert){
            current = Alert.class;
            SurveysControl.getInstance().setOldAlert(nextDisplayable);
            br.org.indt.ndg.lwuit.ui.Alert.show();
            //lcduiDisplay.setCurrent(nextDisplayable);
        }
        else if (nextDisplayable instanceof br.org.indt.ndg.mobile.settings.SimpleLocation) {
            current = SimpleLocation.class;
            Screen.show(SimpleLocation.class, !(lcduiDisplay.getCurrent() instanceof Alert));
        }
        else if (nextDisplayable instanceof br.org.indt.ndg.mobile.download.CheckNewSurveyList) {
            current = CheckNewSurveyList.class;
            SurveysControl.getInstance().setCurrentOldSurveyList(nextDisplayable);
            Screen.show(CheckNewSurveyList.class, !(lcduiDisplay.getCurrent() instanceof Alert));
        }
        else {
            current = nextDisplayable.getClass();
            lcduiDisplay.setCurrent(nextDisplayable);
        }        
    }

    public void setCurrent(Alert alert, Displayable nextDisplayable) {
        // here must repeat each new UI class   else if.....
        if (nextDisplayable == AppMIDlet.getInstance().getSurveyList()) {

        } else  {
            lcduiDisplay.setCurrent(nextDisplayable);
        }
    }

    public Displayable getCurrent() {
        if (current == SurveyList.class) {
            return AppMIDlet.getInstance().getSurveyList();
        } else if (current == CategoryList.class) {
            return AppMIDlet.getInstance().getCategoryList();
        } else if (current == ResultList.class) {
            return AppMIDlet.getInstance().getResultList();
        } else if (current == ResultView.class) {
            return AppMIDlet.getInstance().getResultView();
        } else if (current == DeleteResultList.class) {
            return SurveysControl.getInstance().getCurrentOldDeleteList();
        } else if (current == TestConnectionNewUI.class) {
            return TestConnection.getInstance();
        } else if (current == UpdateClientApp.class) {
            return SurveysControl.getInstance().getCurrentOldUpdateClientApp();
        }
        else
            return lcduiDisplay.getCurrent();

        
    }

}
