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

package br.org.indt.ndg.lwuit.control;

/**
 *
 * @author mluz
 */
public abstract class Event {

    public void execute(Object parameter) {
        if (!inProcess){
            inProcess = true;
            try {
                doBefore();
                doAction(parameter);
                doAfter();
            } finally { // to avoid blocking on unexpected exceptions
                inProcess = false;
            }
        }
    };

    public void doBefore() {}

    public void doAfter() {}

    private boolean inProcess;

    // @deprecated
    private void startLazy(final Object parameter, final int sleepTime) {
        if (!inProcess){
            inProcess = true;
            doAction(parameter);
            inProcess = false;
        }
    }

    // @deprecated
    private void startLazy(final Object parameter) {
        startLazy(parameter, 800); //default time
    }

    protected abstract void doAction(Object parameter);

}
