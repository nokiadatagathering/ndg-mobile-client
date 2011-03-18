package br.org.indt.ndg.lwuit.extended;

import com.sun.lwuit.list.ListModel;
import br.org.indt.ndg.lwuit.ui.renderers.SurveyListCellRenderer;
import com.sun.lwuit.events.SelectionListener;

public class AnimatedList extends List implements SelectionListener {

    final private int ANIMATION_DELAY = 300;
    final private int ANIMATION_START_DELAY = 1000;

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
        la.stop();
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
        }
        return val;
    }

    public void selectionChanged(int i, int i1) {
        stopAnimation();
        startAnimation();
    }


    class ListAnimation implements Runnable {

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
