/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.extended;

import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author mluz
 */
public class DateField extends TextField implements DataChangedListener, FocusListener {


    public static int DDMMYYYY = 0x0001;
    public static int MMDDYYYY = 0x0002;
    public static int YYYYMMDD = 0x0003;

    private Date date;

    private char separator = '/';
    private int dateFormat;

    private boolean selectMode = true;
    private int fieldSelected = 1;  // 1 = first; 2 = second; 3 = third ; format independent

    private String[] dateFields = new String[3];

    public DateField() {
        this(new Date(), MMDDYYYY, '/');
    }

    public DateField(int dateFormat) {
        this(new Date(), dateFormat, '/');
    }

    public DateField(int dateFormat, char separator) {
        this(new Date(), dateFormat, separator);
    }

    public DateField(Date date, int dateFormat, char separator) {
        super();
        setInputMode("123");
        addDataChangeListener(this);
        addFocusListener(this);
        this.dateFormat = dateFormat;
        setDate(date);
    }

    public void setDate(Date date) {
        this.date = date;
        splitDate(date);
        setText(formatDate());
    }

    public Date getDate() {
        buildDate();
        return date;
    }

    private void buildDate() {
        Calendar calendar = Calendar.getInstance();
        if (dateFormat == DDMMYYYY) {
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateFields[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(dateFields[1])-1);
            calendar.set(Calendar.YEAR, Integer.parseInt(dateFields[2]));
        } else if (dateFormat == MMDDYYYY) {
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateFields[1]));
            calendar.set(Calendar.MONTH, Integer.parseInt(dateFields[0])-1);
            calendar.set(Calendar.YEAR, Integer.parseInt(dateFields[2]));
        } else if (dateFormat == YYYYMMDD) {
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateFields[2]));
            calendar.set(Calendar.MONTH, Integer.parseInt(dateFields[1])-1);
            calendar.set(Calendar.YEAR, Integer.parseInt(dateFields[0]));
        }

        date = calendar.getTime();
    }

    public char getSeparator() {
        return separator;
    }

    public String getField(int i) {
        return dateFields[i-1];
    }

    private void splitDate(Date date) {
        int d,m;
        String dd, mm, yyyy;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        d = calendar.get(Calendar.DAY_OF_MONTH);
        dd = (d < 10 ? "0"+d : ""+d);
        m = calendar.get(Calendar.MONTH) +1;
        mm = (m < 10 ? "0"+m : ""+m);
        yyyy = ""+calendar.get(Calendar.YEAR);
        if (dateFormat == DDMMYYYY) {
            dateFields[0] = dd;
            dateFields[1] = mm;
            dateFields[2] = yyyy;
        } else if (dateFormat == MMDDYYYY) {
            dateFields[0] = mm;
            dateFields[1] = dd;
            dateFields[2] = yyyy;
        } else if (dateFormat == YYYYMMDD) {
            dateFields[0] = yyyy;
            dateFields[1] = mm;
            dateFields[2] = dd;
        }
    }
    
    private String formatDate() {
        return dateFields[0] + separator + dateFields[1] + separator + dateFields[2];
    }

    protected Command installCommands(Command clear, Command t9) {
        Form f = getComponentForm();
        Command[] originalCommands = new Command[f.getCommandCount()];
        for(int iter = 0 ; iter < originalCommands.length ; iter++) {
            originalCommands[iter] = f.getCommand(iter);
        }
        Command retVal = super.installCommands(clear, t9);
        f.removeAllCommands();
        for(int iter = originalCommands.length - 1 ; iter >= 0 ; iter--) {
            f.addCommand(originalCommands[iter]);
        }
        originalCommands = null;
        return retVal;
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

    protected void deleteChar() {
        // to do here backspace handle key
    }

    public void dataChanged(int type, int index) {
        if (type == 0) {
            // to do here backspace handle key
        }
        if (type == 1) {
            if (selectMode) {
                char c = getText().charAt(index-1);
                c = convertToNumber(c);
                if (c == '\0') {
                    setField(getFieldSelected(), getField(getFieldSelected()));
                    setCursorPosition(getCursorPosition()-1);
                    return;
                }
                selectMode = false;
                setField(getFieldSelected(), "" + c);
                if (dateFormat == DDMMYYYY || dateFormat == MMDDYYYY) {
                    if (getFieldSelected() == 1) {
                        setCursorPosition(1);
                    } else if (getFieldSelected() == 2) {
                        setCursorPosition(4);
                    } else if (getFieldSelected() == 3) {
                        setCursorPosition(7);
                    }
                } else if (dateFormat == YYYYMMDD) {
                    if (getFieldSelected() == 1) {
                        setCursorPosition(1);
                    }
                    if (getFieldSelected() == 2) {
                        setCursorPosition(6);
                    }
                    if (getFieldSelected() == 3) {
                        setCursorPosition(8);
                    }
                }
            } else {
                if (dateFormat == DDMMYYYY || dateFormat == MMDDYYYY) {
                    char c = getText().charAt(index-1);
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
                        setField(getFieldSelected(), getField(getFieldSelected()) + c);
                        setFieldSelected(3);
                    } else if (getFieldSelected() == 3) {
                        if (getField(3).length() != 3)
                            selectMode = false;
                        setField(getFieldSelected(), getField(getFieldSelected()) + c);
                        if (getField(3).length() == 4) {
                            setFieldSelected(1);
                        }
                    }

                } else if (dateFormat == YYYYMMDD) {
                    char c = getText().charAt(index-1);
                    if (c == '\0') {
                        setField(getFieldSelected(), getField(getFieldSelected()));
                        setCursorPosition(getCursorPosition()-1);
                        return;
                    }
                    selectMode = true;
                    if (getFieldSelected() == 2) {
                        setField(getFieldSelected(), getField(getFieldSelected()) + c);
                        setFieldSelected(3);
                    } else if (getFieldSelected() == 3) {
                        setField(getFieldSelected(), getField(getFieldSelected()) + c);
                        setFieldSelected(1);
                    } else if (getFieldSelected() == 1) {
                        if (getField(3).length() != 3)
                            selectMode = false;
                        setField(getFieldSelected(), getField(getFieldSelected()) + c);
                        if (getField(3).length() == 4) {
                            setFieldSelected(2);
                        }
                    }
                }
            }
        }
    }

    private void setFieldSelected(int field) {
        fieldSelected = field;
        if (dateFormat == DDMMYYYY || dateFormat == MMDDYYYY) {
            if (field == 1)
                setCursorPosition(2);
            else if (field == 2)
                setCursorPosition(5);
            else if (field == 3)
                setCursorPosition(10);
        }
    }

    private void validDate() {
        if (dateFormat == MMDDYYYY) {
            // month
            if (Integer.parseInt(dateFields[0]) < 1)
                dateFields[0] = "01";
            if (Integer.parseInt(dateFields[0]) > 12)
                dateFields[0] = "12";

            //day
            if (Integer.parseInt(dateFields[1]) < 1)
                dateFields[1] = "01";
            if (Integer.parseInt(dateFields[1]) > 31)
                dateFields[1] = "31";

            Calendar c = Calendar.getInstance();
            c.set(Calendar.MONTH, Integer.parseInt(dateFields[0])-1);
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateFields[1]));
            c.set(Calendar.YEAR, Integer.parseInt(dateFields[2]));
            dateFields[0] = formatDayOrMonth((c.get(Calendar.MONTH) + 1) + "");
            dateFields[1] = formatDayOrMonth(c.get(Calendar.DAY_OF_MONTH) + "");
            dateFields[2] = formatYear(c.get(Calendar.YEAR) + "");
        } else if (dateFormat == DDMMYYYY) {
            // month
            if (Integer.parseInt(dateFields[1]) < 1)
                dateFields[1] = "01";
            if (Integer.parseInt(dateFields[1]) > 12)
                dateFields[1] = "12";

            //day
            if (Integer.parseInt(dateFields[0]) < 1)
                dateFields[0] = "01";
            if (Integer.parseInt(dateFields[0]) > 31)
                dateFields[0] = "31";
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateFields[0]));
            c.set(Calendar.MONTH, Integer.parseInt(dateFields[1])-1);
            c.set(Calendar.YEAR, Integer.parseInt(dateFields[2]));
            dateFields[0] = formatDayOrMonth(c.get(Calendar.DAY_OF_MONTH) + "");
            dateFields[1] = formatDayOrMonth((c.get(Calendar.MONTH) + 1) + "");
            dateFields[2] = formatYear(c.get(Calendar.YEAR) + "");
        } else if (dateFormat == YYYYMMDD) {
            // month
            if (Integer.parseInt(dateFields[1]) < 1)
                dateFields[1] = "01";
            if (Integer.parseInt(dateFields[1]) > 12)
                dateFields[1] = "12";

            //day
            if (Integer.parseInt(dateFields[2]) < 1)
                dateFields[2] = "01";
            if (Integer.parseInt(dateFields[2]) > 31)
                dateFields[2] = "31";
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, Integer.parseInt(dateFields[0]));
            c.set(Calendar.MONTH, Integer.parseInt(dateFields[1])-1);
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateFields[2]));
            dateFields[0] = formatYear(c.get(Calendar.YEAR) + "");
            dateFields[1] = formatDayOrMonth((c.get(Calendar.MONTH) + 1) + "");
            dateFields[2] = formatDayOrMonth(c.get(Calendar.DAY_OF_MONTH) + "");
        }

    }

    private void setField(int field, String value) {
        dateFields[field-1] = value;
        if (selectMode) validDate();
        setText(formatDate());
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
    private String formatYear(String year) {
        String result = year;
        if (year.length() == 1) {
            result = "000"+year;
        } else if (year.length() == 2) {
            result = "00"+year;
        } else if (year.length() == 3) {
            result = "0"+year;
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
                    setFieldSelected(3);
                } else if (getFieldSelected() == 3) {
                    setFieldSelected(1);
                }
            } else { // edit mode
                selectMode = true;
                if (dateFormat == MMDDYYYY || dateFormat == DDMMYYYY) {
                    if (getFieldSelected() == 1) {
                        setField(1, formatDayOrMonth(getField(1)));
                        setFieldSelected(2);
                    } else if (getFieldSelected() == 2) {
                        setField(2, formatDayOrMonth(getField(2)));
                        setFieldSelected(3);
                    } else if (getFieldSelected() == 3) {
                        setField(3, formatYear(getField(3)));
                        setFieldSelected(1);
                    }
                } else if (dateFormat == YYYYMMDD) {
                    if (getFieldSelected() == 1) {
                        setField(1, formatYear(getField(1)));
                        setFieldSelected(2);
                    } else if (getFieldSelected() == 2) {
                        setField(2, formatDayOrMonth(getField(2)));
                        setFieldSelected(3);
                    } else if (getFieldSelected() == 3) {
                        setField(3, formatDayOrMonth(getField(3)));
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
                    setFieldSelected(3);
                } else if (getFieldSelected() == 2) {
                    setFieldSelected(1);
                } else if (getFieldSelected() == 3) {
                    setFieldSelected(2);
                }
            } else { // edit mode
                selectMode = true;
                if (dateFormat == MMDDYYYY || dateFormat == DDMMYYYY) {
                    if (getFieldSelected() == 1) {
                        setField(1, formatDayOrMonth(getField(1)));
                        setFieldSelected(3);
                    } else if (getFieldSelected() == 2) {
                        setField(2, formatDayOrMonth(getField(2)));
                        setFieldSelected(1);
                    } else if (getFieldSelected() == 3) {
                        setField(3, formatYear(getField(3)));
                        setFieldSelected(2);
                    }
                } else if (dateFormat == YYYYMMDD) {
                    if (getFieldSelected() == 1) {
                        setField(1, formatYear(getField(1)));
                        setFieldSelected(3);
                    } else if (getFieldSelected() == 2) {
                        setField(2, formatDayOrMonth(getField(2)));
                        setFieldSelected(1);
                    } else if (getFieldSelected() == 3) {
                        setField(3, formatDayOrMonth(getField(3)));
                        setFieldSelected(2);
                    }
                }
            }
        }
    }

    public void focusLost(Component cmp) {
        if (dateFormat == MMDDYYYY || dateFormat == DDMMYYYY) {
            if (getFieldSelected() == 1) {
                setField(1, formatDayOrMonth(getField(1)));
            } else if (getFieldSelected() == 2) {
                setField(2, formatDayOrMonth(getField(2)));
            } else if (getFieldSelected() == 3) {
                setField(3, formatYear(getField(3)));
            }
        } else if (dateFormat == YYYYMMDD) {
            if (getFieldSelected() == 1) {
                setField(1, formatYear(getField(1)));
            } else if (getFieldSelected() == 2) {
                setField(2, formatDayOrMonth(getField(2)));
            } else if (getFieldSelected() == 3) {
                setField(3, formatDayOrMonth(getField(3)));
            }
        }

    }

    public int getFieldSelected() {
        return fieldSelected;
    }

    public boolean isSelectMode() {
        return selectMode;
    }

}
