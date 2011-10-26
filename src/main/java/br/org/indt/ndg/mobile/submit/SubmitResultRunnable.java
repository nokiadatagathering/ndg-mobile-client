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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile.submit;

import java.util.Vector;
/**
 *
 */
public class SubmitResultRunnable implements Runnable{
    
    private Vector resultFileNameList;
    private SubmitServer submitServer;
    
    /**
     * 
     * @param resultFileName. Must be null if results come from ResultList.
     * Otherwise, must have the result's name to be sent
     */
    public SubmitResultRunnable(String resultFileName){
        this.resultFileNameList = new Vector();
        resultFileNameList.addElement( resultFileName );
    }

    public SubmitResultRunnable( Vector resultFileNameList){
        this.resultFileNameList = resultFileNameList;
    }

    public void setSubmitServer( SubmitServer submitServer)
    {
        this.submitServer = submitServer;
    }

    public void run () {
        submitServer.submit(resultFileNameList);
    }
}
