/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

package br.org.indt.ndg.lwuit.extended;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.Display;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.impl.midp.VirtualKeyboard;
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
        setUseSoftkeys(false);
        if(Display.getInstance().isTouchScreenDevice()) {
            VirtualKeyboard onScreenKeyboard = new VirtualKeyboard();
            onScreenKeyboard.setInputModeOrder(new String[]{VirtualKeyboard.NUMBERS_MODE, VirtualKeyboard.QWERTY_MODE});
            VirtualKeyboard.bindVirtualKeyboard(this, onScreenKeyboard);
        }
    }



    public void setDate(Date date) {
        this.date = date;
        splitDate(date);
        setText(formatDate());
    }

    public Date getDate() {
        validDate(true);
        buildDate();
        return date;
    }

    private void buildDate() {
        Calendar calendar = Calendar.getInstance();
        if (dateFormat == DDMMYYYY) {
            calendar.set(Calendar.YEAR, Integer.parseInt(dateFields[2]));
            calendar.set(Calendar.MONTH, Integer.parseInt(dateFields[1])-1);
            calendar.set(Calendar.DAY_OF_MONTH, getDayOfMonth(calendar, Integer.parseInt(dateFields[0])));
        } else if (dateFormat == MMDDYYYY) {
            calendar.set(Calendar.YEAR, Integer.parseInt(dateFields[2]));
            calendar.set(Calendar.MONTH, Integer.parseInt(dateFields[0])-1);
            calendar.set(Calendar.DAY_OF_MONTH, getDayOfMonth(calendar, Integer.parseInt(dateFields[1])));
        } else if (dateFormat == YYYYMMDD) {
            calendar.set(Calendar.YEAR, Integer.parseInt(dateFields[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(dateFields[1])-1);
            calendar.set(Calendar.DAY_OF_MONTH, getDayOfMonth(calendar, Integer.parseInt(dateFields[2])));
        }
        date = calendar.getTime();
    }

    public char getSeparator() {
        return separator;
    }

    public String getField(int i) {
        return dateFields[i-1];
    }

    public void setCurrentDate() {
        Calendar c = Calendar.getInstance();
        String day = formatDayOrMonth(c.get(Calendar.DAY_OF_MONTH) + "");
        String month = formatDayOrMonth((c.get(Calendar.MONTH) + 1) + "");
        String year = formatDayOrMonth(c.get(Calendar.YEAR) + "");
        if (dateFormat == MMDDYYYY) {
            dateFields[0] = month;
            dateFields[1] = day;
            dateFields[2] = year;
        } else if (dateFormat == DDMMYYYY) {
            dateFields[0] = day;
            dateFields[1] = month;
            dateFields[2] = year;
        } else if (dateFormat == YYYYMMDD) {
            dateFields[0] = year;
            dateFields[1] = month;
            dateFields[2] = day;
        }
        setText(formatDate());
        setFieldSelected(1);
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

        if (index < 0)
            return;

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
        }  else if (type == DataChangedListener.REMOVED || type == DataChangedListener.CHANGED) {
            // do nothing
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

    private void validDate( boolean parseTextFirst ) {
        try {
            if ( parseTextFirst )
                parseDate();

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
                c.set(Calendar.YEAR, Integer.parseInt(dateFields[2]));
                c.set(Calendar.MONTH, Integer.parseInt(dateFields[0])-1);
                c.set(Calendar.DAY_OF_MONTH, getDayOfMonth(c, Integer.parseInt(dateFields[1])));
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
                c.set(Calendar.YEAR, Integer.parseInt(dateFields[2]));
                c.set(Calendar.MONTH, Integer.parseInt(dateFields[1])-1);
                c.set(Calendar.DAY_OF_MONTH, getDayOfMonth(c, Integer.parseInt(dateFields[0])) );
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
                c.set(Calendar.DAY_OF_MONTH, getDayOfMonth(c, Integer.parseInt(dateFields[2])));
                dateFields[0] = formatYear(c.get(Calendar.YEAR) + "");
                dateFields[1] = formatDayOrMonth((c.get(Calendar.MONTH) + 1) + "");
                dateFields[2] = formatDayOrMonth(c.get(Calendar.DAY_OF_MONTH) + "");
            }
        } catch ( Exception e) {
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(Resources.WARNING, Resources.DATE_FORMAT_ERROR, GeneralAlert.WARNING);
            setCurrentDate();
        }
    }

    /*
     * This method determines if currently set month has actually that much days
     * If not tries to determine the maximum day
     * Calendar has to have current year and month already set
     */
    private int getDayOfMonth(Calendar c, int dayOfMonth ) {
        while(dayOfMonth >= 28) {
            try {
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                c.getTime(); // will throw if day does not exist
                break;
            } catch (Exception e) {
                --dayOfMonth;
            }
        }
        return dayOfMonth;
    }

    private void setField(int field, String value) {
        dateFields[field-1] = value;
        if (selectMode) validDate(false);
        setText(formatDate());
    }

    /*
     * WORKAROUND for touch
     * WARN: This is only a quick workaround method for touch devices.
     * Most probbaly some better solution will be needed
     */
    private void parseDate() throws Exception {
        String dateString = getText();
        String fieldOne = "", fieldTwo = "", fieldThree = "";
        int firstSeparatorIndex = dateString.indexOf(separator);
        int secondSeparatorIndex = dateString.indexOf(separator, firstSeparatorIndex+1);

        fieldOne = dateString.substring(0, firstSeparatorIndex);
        if ( secondSeparatorIndex >= 0 && (firstSeparatorIndex+1 < dateString.length()) ) {
            fieldTwo = dateString.substring(firstSeparatorIndex+1, secondSeparatorIndex);
            fieldThree = dateString.substring(secondSeparatorIndex+1, dateString.length());
        }
        dateFields[0] = fieldOne;
        dateFields[1] = fieldTwo;
        dateFields[2] = fieldThree;
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
        validDate(true);
        setText(formatDate());
    }

    public int getFieldSelected() {
        return fieldSelected;
    }

    public boolean isSelectMode() {
        return selectMode;
    }
}
