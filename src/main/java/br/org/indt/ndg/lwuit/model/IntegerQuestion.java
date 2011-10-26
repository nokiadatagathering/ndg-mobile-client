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

package br.org.indt.ndg.lwuit.model;

import br.org.indt.ndg.lwuit.ui.GeneralAlert;
import br.org.indt.ndg.mobile.Resources;

public class IntegerQuestion extends NumericQuestion {
    private long low;
    private long high;

   public void setHighConstraint(String _high) {
        try{
            high = Long.parseLong(_high);
        }
        catch(NumberFormatException nfe){
            high = Long.MAX_VALUE;
        }
    }

    public void setLowConstraint(String _low) {
        try{
            low = Long.parseLong(_low);
        }
        catch(NumberFormatException nfe){
            low = Long.MIN_VALUE;
        }
    }

    public String getType()
    {
        return "_int";
    }

     protected boolean passLowConstraint( String strValue ) {
        boolean result = true;
        if (strValue.equals("") ) result = true;
        else {
            try{
                double value = Long.parseLong(strValue);
                if (value >= low) result = true;
                else {
                    GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                    GeneralAlert.getInstance().show(Resources.INTEGER, Resources.VALUE_GREATER + low, GeneralAlert.WARNING);
                    result = false;
                }
            }
            catch(NumberFormatException ex)
            {
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().show(Resources.INTEGER, Resources.NOTPROPERINTEGER +" \""+strValue+"\"", GeneralAlert.WARNING);
                result = false;
            }
        }
        return result;
    }

    protected boolean passHighConstraint( String strValue ) {
        boolean result = true;
        if ( strValue.equals("") ) result = true;
        else {
            try
            {
                double value = Long.parseLong(strValue);
                if (value <= high) result = true;
                else {
                    GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                    GeneralAlert.getInstance().show(Resources.INTEGER, Resources.VALUE_LOWER + high, GeneralAlert.WARNING);
                    result = false;
                }
            }
            catch(NumberFormatException ex)
            {
                GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
                GeneralAlert.getInstance().show(Resources.INTEGER, Resources.NOTPROPERINTEGER +" \""+strValue+"\"", GeneralAlert.WARNING);
                result = false;
            }
        }
        return result;
    }

    public NDGAnswer getAnswerModel() {
        return new IntegerAnswer();
    }
}
