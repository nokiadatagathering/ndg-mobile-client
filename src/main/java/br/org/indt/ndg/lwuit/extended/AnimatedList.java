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

import com.sun.lwuit.list.ListModel;
import br.org.indt.ndg.lwuit.ui.SurveyListContextMenu;
import br.org.indt.ndg.lwuit.ui.renderers.SurveyListCellRenderer;
import com.sun.lwuit.List;
import com.sun.lwuit.events.SelectionListener;

public class AnimatedList extends List implements SelectionListener {

    private static final int ANIMATION_DELAY = 300;
    private static final int ANIMATION_START_DELAY = 1000;

    private long tickTime = System.currentTimeMillis();
    private int lastSelection = -1;
    private SurveyListCellRenderer slcr;
    private ListAnimation la;

    public AnimatedList(ListModel model) {
        super(model);
        slcr = new SurveyListCellRenderer();
        setListCellRenderer(slcr);
        addSelectionListener(this);
    }

    public void startAnimation() {
        la = new ListAnimation();
        la.setList(this);
        Thread tAnimate = new Thread(la);
        slcr.resetPosition();
        tickTime = System.currentTimeMillis();
        tAnimate.start();
    }

    public void stopAnimation() {
        if ( la != null ) {
            la.stop();
        }
    }

    public boolean animate() {
        boolean val = super.animate();
        if (hasFocus()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - tickTime > ANIMATION_START_DELAY) { // index!=0 to avoid a hw bug
                if (lastSelection == getSelectedIndex() && getSelectedIndex()!=0) {
                    slcr.incrementPosition();
                    repaint();
                } else {
                    lastSelection = getSelectedIndex();
                    slcr.resetPosition();
                }
                val = true;
            }
        } else {
            slcr.resetPosition();
            stopAnimation();
        }
        return val;
    }

    public void selectionChanged(int i, int i1) {
        stopAnimation();
        startAnimation();
    }

    public synchronized void longPointerPress(int x, int y) {
        super.longPointerPress(x, y);
        if ( getSelectedSurvey() >= 0 ) {
            SurveyListContextMenu resultContextMenu = new SurveyListContextMenu(getSelectedSurvey(), this.size());
            resultContextMenu.show(x, y);
        }
    }

    private int getSelectedSurvey() {
        return getSelectedIndex() - 1; // skip 'check for surveys' list element
    }

    static class ListAnimation implements Runnable {

        private List list;
        private boolean stop = false;

        public void setList(List _list) {
            list = _list;
        }

        public void stop() {
            stop = true;
        }

        public void run() {
            while (!stop) {
                try {
                    list.animate();
                    Thread.sleep(ANIMATION_DELAY);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
