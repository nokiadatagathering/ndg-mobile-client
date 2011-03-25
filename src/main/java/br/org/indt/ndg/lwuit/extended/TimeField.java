package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.impl.midp.VirtualKeyboard;
import java.util.Calendar;
import java.util.Date;

public class TimeField extends TextField implements DataChangedListener, FocusListener {

    public static int HHMM = 0x0001;
    public static int HHMM1 = 0x0002;

    private Date time;

    private char separator = ':';
    private int timeFormat;

    private boolean selectMode = true;
    private int fieldSelected = 1;

    private String[] timeFields = new String[2];

    public TimeField() {
        this(new Date(), HHMM, ':');
    }

    public TimeField(int timeFormat) {
        this(new Date(), timeFormat, ':');
    }

    public TimeField(int timeFormat, char separator) {
        this(new Date(), timeFormat, separator);
    }

    public TimeField(Date time, int timeFormat, char separator) {
        super();
        setInputMode("123");
        addDataChangeListener(this);
        addFocusListener(this);
        this.timeFormat = timeFormat;
        setTime(time);
        if (Display.getInstance().isTouchScreenDevice()) {
            VirtualKeyboard onScreenKeyboard = new VirtualKeyboard();
            onScreenKeyboard.setInputModeOrder(new String[]{VirtualKeyboard.NUMBERS_MODE, VirtualKeyboard.QWERTY_MODE});
            VirtualKeyboard.bindVirtualKeyboard(this, onScreenKeyboard);
        }
    }

    public void setTime(Date time) {
        this.time = time;
        splitTime(time);
        setText(formatTime());
    }

    public Date getTime() {
        validTime(true);
        buildTime();
        return time;
    }

    public void setCurrentDate() {
        Calendar c = Calendar.getInstance();
        if (timeFormat == HHMM) {
            timeFields[0] = formatDayOrMonth((c.get(Calendar.HOUR) + 1) + "");
            timeFields[1] = formatDayOrMonth(c.get(Calendar.MINUTE) + "");
        } else if (timeFormat == HHMM1) {
            timeFields[0] = formatDayOrMonth(c.get(Calendar.HOUR_OF_DAY) + "");
            timeFields[1] = formatDayOrMonth((c.get(Calendar.MINUTE)) + "");
        }
        setText(formatTime());
        setFieldSelected(1);
    }

    private void buildTime() {
        Calendar calendar = Calendar.getInstance();
        
        if ((timeFormat == HHMM)) {
            calendar.set(Calendar.HOUR, Integer.parseInt(timeFields[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeFields[1]));
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeFields[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeFields[1]));
        }
        time = calendar.getTime();
    }

    public char getSeparator() {
        return separator;
    }

    public String getField(int i) {
        return timeFields[i-1];
    }

    private void splitTime(Date time) {
        int h, m;
        String hh, mm;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        if ((timeFormat == HHMM)) {
            h = calendar.get(Calendar.HOUR);
            if (h == 0) {
                h =12;
            }
        } else {
            h = calendar.get(Calendar.HOUR_OF_DAY);
        }
        hh = (h < 10 ? "0"+h : ""+h);
        m = calendar.get(Calendar.MINUTE);
        mm = (m < 10 ? "0"+m : ""+m);
        timeFields[0] = hh;
        timeFields[1] = mm;
    }

    private String formatTime() {
        return timeFields[0] + separator + timeFields[1];
    }

    protected Command installCommands(Command clear, Command t9) {
        // do not install T9 nor CLEAR
        return null;
    }

    private char convertToNumber(char c) {
        if (!(c >= '0' && c <= '9')) {
            switch (c) {
                case 'r':
                case 'R': return '1';
                case 't':
                case 'T': return '2';
                case 'y':
                case 'Y': return '3';
                case 'f':
                case 'F': return '4';
                case 'g':
                case 'G': return '5';
                case 'h':
                case 'H': return '6';
                case 'v':
                case 'V': return '7';
                case 'b':
                case 'B': return '8';
                case 'n':
                case 'N': return '9';
                case 'm':
                case 'M': return '0';
                default : return '\0';
            }
        } else return c;
    }

    public void deleteChar() {
        // to do here backspace handle key
    }

    public void dataChanged(int type, int index) {
        if (type == DataChangedListener.ADDED) {
            if (selectMode) {
                char c = getText().charAt(index);
                c = convertToNumber(c);
                if (c == '\0') {
                    setField(getFieldSelected(), getField(getFieldSelected()));
                    setCursorPosition(getCursorPosition()-1);
                    return;
                }
                selectMode = false;
                setField(getFieldSelected(), "" + c);
                if ((timeFormat == HHMM) || (timeFormat == HHMM1)){
                    if (getFieldSelected() == 1) {
                        setCursorPosition(1);
                    } else if (getFieldSelected() == 2) {
                        setCursorPosition(4);
                    }
                }
            }else{
                    char c = getText().charAt(index);
                    c = convertToNumber(c);
                    if (c == '\0') {
                        setField(getFieldSelected(), getField(getFieldSelected()));
                        setCursorPosition(getCursorPosition()-1);
                        return;
                    }
                    selectMode = true;
                    if (getFieldSelected() == 1) {
                        setField(getFieldSelected(), getField(getFieldSelected()) + c);
                        setFieldSelected(2);
                    } else if (getFieldSelected() == 2) {
                        if (getField(2).length() != 1)
                            selectMode = false;
                        setField(getFieldSelected(), getField(getFieldSelected()) + c);
                   
                        if (getField(2).length() == 2) {
                            setFieldSelected(1);
                        }
                    }
            }
        } else if ( type == DataChangedListener.REMOVED || type == DataChangedListener.CHANGED ) {
            //do nothing
        }
    }

    private void setFieldSelected(int field) {
        fieldSelected = field;
        if ((timeFormat == HHMM) || (timeFormat == HHMM1)){
            if (field == 1){
                setCursorPosition(2);
            }else if (field == 2)
                setCursorPosition(5);
        }
    }

    private void validTime( boolean parseTextFirst ) {
        try {
            if ( parseTextFirst )
                parseDate();

            // validate minutes
            if (Integer.parseInt(timeFields[1]) < 1)
                timeFields[1] = "00";
            else if (Integer.parseInt(timeFields[1]) > 59)
                timeFields[1] = "59";

            if (timeFormat == HHMM) {
                // validate hours
                if (Integer.parseInt(timeFields[0]) < 1)
                    timeFields[0] = "01";
                else if(Integer.parseInt(timeFields[0]) > 12)
                    timeFields[0] = "12";

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR, Integer.parseInt(timeFields[0])-1);
                c.set(Calendar.MINUTE, Integer.parseInt(timeFields[1]));
                timeFields[0] = formatDayOrMonth((c.get(Calendar.HOUR) + 1) + "");
                timeFields[1] = formatDayOrMonth(c.get(Calendar.MINUTE) + "");
            } else if (timeFormat == HHMM1) {
                // validate hours
                if (Integer.parseInt(timeFields[0]) < 1)
                    timeFields[0] = "00";
                else if (Integer.parseInt(timeFields[0]) > 24)
                    timeFields[0] = "23";

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeFields[0]));
                c.set(Calendar.MINUTE, Integer.parseInt(timeFields[1]));
                timeFields[0] = formatDayOrMonth(c.get(Calendar.HOUR_OF_DAY) + "");
                timeFields[1] = formatDayOrMonth((c.get(Calendar.MINUTE)) + "");
            }
        } catch ( Exception e) {
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(Resources.WARNING, Resources.TIME_FORMAT_ERROR, GeneralAlert.WARNING);
            setCurrentDate();
        }
    }

    private void setField(int field, String value) {
        timeFields[field-1] = value;
        if (selectMode) validTime(false);
        setText(formatTime());
    }

    /*
     * WORKAROUND for touch
     * WARN: This is only a quick workaround method for touch devices.
     * Most probbaly some better solution will be needed
     */
    private void parseDate() throws Exception {
        String timeString = getText();
        String fieldOne = "", fieldTwo = "";
        int separatorIndex = timeString.indexOf(separator);
        fieldOne = timeString.substring(0, separatorIndex);
        if ( separatorIndex >= 0 && (separatorIndex+1 < timeString.length()) ) {
            fieldTwo = timeString.substring(separatorIndex+1, timeString.length());
        }
        timeFields[0] = fieldOne;
        timeFields[1] = fieldTwo;
    }

    public void focusGained(Component cmp) {
        keyPressed(Display.GAME_FIRE);
        setHandlesInput(true);
        selectMode = true;
        setFieldSelected(1);
    }

    private String formatDayOrMonth(String dayOrMonth) {
        String result = dayOrMonth;
        if (dayOrMonth.length() == 1) {
            result = "0"+dayOrMonth;
        }
        return result;
    }

    public void keyReleased(int keyCode) {
        super.keyReleased(keyCode);
        int action = com.sun.lwuit.Display.getInstance().getGameAction(keyCode);
        if (action == Display.GAME_RIGHT) {
            if (keyCode == 'g')
                return;
            if (selectMode) {
                if (getFieldSelected() == 1) {
                    setFieldSelected(2);
                } else if (getFieldSelected() == 2) {
                    setFieldSelected(1);
                }
            } else { // edit mode
                selectMode = true;
                if (timeFormat == HHMM || timeFormat == HHMM1) {
                    if (getFieldSelected() == 1) {
                        setField(1, formatDayOrMonth(getField(1)));
                        setFieldSelected(2);
                    } else if (getFieldSelected() == 2) {
                        setField(2, formatDayOrMonth(getField(2)));
                        setFieldSelected(1);
                    }
                }
            }
        } else if (action == Display.GAME_LEFT) {
            // for nokia e71
            if ((keyCode == 'l') || (keyCode == 'd'))
                return;
            if (selectMode) {
                if (getFieldSelected() == 1) {
                    setFieldSelected(2);
                } else if (getFieldSelected() == 2) {
                    setFieldSelected(1);
                }
            } else { // edit mode
                selectMode = true;
                if (timeFormat == HHMM || timeFormat == HHMM1) {
                    if (getFieldSelected() == 1) {
                        setField(1, formatDayOrMonth(getField(1)));
                        setFieldSelected(2);
                    } else if (getFieldSelected() == 2) {
                        setField(2, formatDayOrMonth(getField(2)));
                        setFieldSelected(1);
                    }
                }
            }
        }
    }

    public void focusLost(Component cmp) {
        validTime(true);
        setText(formatTime());
    }

    public int getFieldSelected() {
        return fieldSelected;
    }

    public boolean isSelectMode() {
        return selectMode;
    }
}
