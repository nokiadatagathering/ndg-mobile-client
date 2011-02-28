package br.org.indt.ndg.lwuit.ui;


import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.Component;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;

public class SplashScreen extends Screen {
    private final int WHITE = 0xffffff;

    protected void loadData() {
    }

    protected void customize() {
        form.removeAll();
        form.setFocusable(false);
        form.getStyle().setBgColor( WHITE );
        form.setScrollable(false);
        form.setLayout( new BorderLayout() );

        Label label = new Label( ScaleToCurrentDisplay() );
        label.setVerticalAlignment( Component.BOTTOM );
        label.getStyle().setMargin(0,0,0,0);
        label.getStyle().setPadding(0,0,0,0);

        form.addComponent(BorderLayout.SOUTH, label );
    }
    
    private Image ScaleToCurrentDisplay() {
        int dispWidth = form.getWidth();
        int dispHeigh = form.getHeight();
        Image image = dispHeigh < dispWidth ? Resources.splashLandscape : Resources.splashPortrait;

        return image.scaledWidth( form.getWidth() );
    }
}
