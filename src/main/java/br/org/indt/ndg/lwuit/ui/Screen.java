package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.extended.Form;
import br.org.indt.ndg.lwuit.control.Event;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.UIManager;
import com.sun.lwuit.util.Resources;
import java.util.Hashtable;

/**
 *
 * @author mluz
 */
public abstract class Screen {

    private static Resources res;

    protected Form form;
    private TitleBar titlebar;

    static Hashtable screens = new Hashtable();

    Hashtable events = new Hashtable();

    protected static final Integer ON_SHOW = new Integer(1);
    protected static final Integer ON_CREATE = new Integer(2);

    protected Screen() {
        createScreen();
    }


    public static final void show(Class c, boolean onShow) {

        boolean onCreate = false;

        if (!Screen.class.isAssignableFrom(c)) {
            throw new IllegalArgumentException("The argument must extends br.org.indt.ndg.lwuit.ui.Screen class");
        }

        Screen s = null;

        if (screens.containsKey(c)) {
            s = (Screen)screens.get(c);
        } else {
            try {
                s = (Screen) c.newInstance();
                onCreate = true;
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
            screens.put(c, s);
        }

        s.loadData();
        s.customize();
        s.form.refreshTheme();
        s.form.getTitleStyle().setBgPainter(s.titlebar);

        if (WaitingScreen.isShowed())
            WaitingScreen.dispose();
        
        s.show();
        if (onShow)
            s.onShow();
        if (onCreate)
            s.onCreate();
    }

    public static final boolean isVisible(Class c) {
        if (!Screen.class.isAssignableFrom(c)) {
            throw new IllegalArgumentException("The argument must extends br.org.indt.ndg.lwuit.ui.Screen class");
        }

        Screen s = null;

        if (screens.containsKey(c)) {
            s = (Screen)screens.get(c);
            return s.form.isVisible();
        }

        return false;
    }

    final void setTitle(String title1, String title2) {
        titlebar.setTitle1(title1);
        titlebar.setTitle2(title2);
        form.setTitle(" ");
    }

    private final void show() {
        form.setTransitionInAnimator(UIManager.getInstance().getLookAndFeel().getDefaultFormTransitionIn());
        form.show();
    }

    protected final void createScreen() {
        form = new Form();
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        titlebar = new TitleBar( " ", " " );
        MenuCellRenderer mlcr = new MenuCellRenderer();
        form.setMenuCellRenderer(mlcr);
        form.getMenuStyle().setFont( UIManager.getInstance().getComponentStyle("Command").getFont());
    }


    protected abstract void loadData();

    protected abstract void customize();

    public static void setRes(Resources resources) {
        res = resources;
    }
    
    public static Resources getRes() {
        return res;
    }

    protected void registerEvent(Event e, Integer type) {
        if (events.containsKey(type)) {
            events.remove(type);
        }
        events.put(type, e);
    }

    private void onShow() {
        Event e = (Event)events.get(ON_SHOW);
        if (e != null)
            e.execute(this);
    }

    private void onCreate() {
        Event e = (Event)events.get(ON_CREATE);
        if (e != null)
            e.execute(this);
    }
}