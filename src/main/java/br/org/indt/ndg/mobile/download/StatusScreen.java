package br.org.indt.ndg.mobile.download;

import br.org.indt.ndg.mobile.AppMIDlet;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Item;

import br.org.indt.ndg.mobile.Resources;

public class StatusScreen extends Form implements CommandListener {
	private Gauge httpGauge;
        private Gauge surveysGauge;
        private DownloadNewSurveys dns;
        private int gaugeType;
	
	public StatusScreen(DownloadNewSurveys dns) {
		super("");
                this.dns = dns;
                
                httpGauge = new Gauge(Resources.CONNECTING,false,Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);
		httpGauge.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_BOTTOM);

                surveysGauge = new Gauge(Resources.DOWNLOADING_NEW_SURVEYS, false, 1, 0);
		surveysGauge.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_BOTTOM);

		addCommand(Resources.CMD_CANCEL);
		setCommandListener(this);
	}
	
	public void setCurrentSurveyIndex(int current) {
		surveysGauge.setValue(current);
	}
	
	public void reset(int maxValue) {
            if (maxValue == 0) maxValue = 1;
            surveysGauge.setMaxValue(maxValue);
	}
        
        public void showHttpConnecting() {
            super.deleteAll();
            append(httpGauge);
            gaugeType = 0;
        }
        
        public void showDownloadingSurveys() {
            super.deleteAll();
            append(surveysGauge);
            gaugeType = 1;
            AppMIDlet.getInstance().setDisplayable(this);
        }

	public void commandAction(Command c, Displayable d) {
            if (c == Resources.CMD_CANCEL) {
                dns.cancelOperation();
            }
	}

    public String getGaugeText() {
        if (gaugeType == 0) {
            return httpGauge.getLabel();
        }
        else {
            return surveysGauge.getLabel();
        }
    }
}
