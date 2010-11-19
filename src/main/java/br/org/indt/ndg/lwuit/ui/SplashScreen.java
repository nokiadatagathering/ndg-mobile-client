package br.org.indt.ndg.lwuit.ui;


import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;

public class SplashScreen extends Screen {
   
    protected void loadData() {
    }

    protected void customize() {
        form.removeAll();
        form.setFocusable(false);
        
        form.setLayout( new BorderLayout() );
        Label label = new Label( ScaleToCurrentDisplay() );
        form.setScrollable(false);
        form.addComponent(BorderLayout.SOUTH, label );
    }
    
    private Image ScaleToCurrentDisplay() {
        int dispWidth = form.getWidth();
        int dispHeigh = form.getHeight();
        int imageWidth = Resources.splashLWUID.getWidth();
        int imageHeigh = Resources.splashLWUID.getHeight();
        if( imageHeigh/dispHeigh < imageWidth/dispWidth ) {
            return Resources.splashLWUID.scaledWidth( form.getWidth() );
        } else {
            return Resources.splashLWUID.scaledHeight( form.getHeight() );
        }
    }
}
