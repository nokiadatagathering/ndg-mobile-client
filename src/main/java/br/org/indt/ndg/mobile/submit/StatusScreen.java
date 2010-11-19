package br.org.indt.ndg.mobile.submit;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Item;

import br.org.indt.ndg.mobile.Resources;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.ResultList;

public class StatusScreen extends Form implements CommandListener {
	private Gauge httpGauge;
	private Gauge fileGauge;
    private int fileGaugeIndex;
    private SubmitServer ss;
	
	public StatusScreen(SubmitServer _parent) {
		super(Resources.SUBMIT_SERVER);
                
                ss = _parent;
                
		httpGauge = new Gauge(null,false,Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);
		httpGauge.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_BOTTOM);

		fileGauge = new Gauge(null,true, 1, 0);
		httpGauge.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_BOTTOM);

		append(httpGauge);
		fileGaugeIndex = append(fileGauge);
		addCommand(Resources.CMD_CANCEL);
		setCommandListener(this);
	}
	
	public void setFileLabel(String label) {
		fileGauge.setLabel(Resources.SUBMIT_FILE + label);
	}
	
	public void setHttpLabel(String label) {
		httpGauge.setLabel(Resources.SUBMIT_SERVER);
	}
	
	public void setCurrentFileIndex(int current) {
		fileGauge.setValue(current);
	}
	
	public void reset(int maxValue) {
		fileGauge.setMaxValue(maxValue);
		fileGauge.setLabel(null);
		httpGauge.setLabel(null);
	}

	public void commandAction(Command c, Displayable d) {
            if (c == Resources.CMD_CANCEL) {
                ss.cancel();
                AppMIDlet.getInstance().setResultList(new ResultList());  //updated result list
                AppMIDlet.getInstance().setDisplayable(AppMIDlet.getInstance().getResultList());
            }
	}
}
