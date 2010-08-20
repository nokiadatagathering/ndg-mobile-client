
package br.org.indt.ndg.mobile.error;

import br.org.indt.ndg.mobile.Resources;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.Spacer;

public class WaitingForm extends Form {
    
    private Gauge gauge;
    private StringItem item;
    
    public WaitingForm() {
        super(Resources.WAIT);
        CreateForm(" " + Resources.PROCESSING);
    }
    
    public WaitingForm(String _value) {
        super(Resources.WAIT);
        //CreateForm(" " + Resources.PROCESSING + ": " + _value);
        CreateForm(_value);
    }
    
    public void CreateForm(String _value) {
        this.append(new Spacer(this.getWidth(),25));
        
        item = new StringItem("", _value);
        this.append(item);
        
        this.append(new Spacer(this.getWidth(),25));
        
        gauge = new Gauge(null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);        
        this.append(gauge);
    }
}
