/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.ui;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
public class MenuCellRenderer extends Label implements ListCellRenderer, Painter{

    private Style commandStyle;
    private int menuFgColor, commandFgColor, endColor, startColor, borderColor;

    public MenuCellRenderer() {
        commandStyle = UIManager.getInstance().getComponentStyle("Command");
        setStyle(commandStyle);
        menuFgColor = UIManager.getInstance().getComponentStyle("Menu").getFgColor();
        commandFgColor = UIManager.getInstance().getComponentStyle("Command").getFgColor();
        endColor = UIManager.getInstance().getComponentStyle("").getFgColor();
        startColor = commandStyle.getBgSelectionColor();
        borderColor = UIManager.getInstance().getComponentStyle("Menu").getBgColor();
    }

    public Component getListCellRendererComponent(List list, Object value, int index, boolean isSelected) {
        if (value instanceof Command) {
            Command cmd = (Command)value;
            setText(" " + cmd.getCommandName());
            setIcon(cmd.getIcon());
        }
        if (isSelected) {
            setFocus(true);
            getStyle().setFgColor(menuFgColor);
            getStyle().setBgPainter(this);
        } else {
            getStyle().setFgColor(commandFgColor);
            setFocus(false);
            getStyle().setBgPainter(null);
        }
        return this;
    }

    public Component getListFocusComponent(List list) {
        setText("");
        getStyle().setBgPainter(this);
        setIcon(null);
        setFocus(true);
        return this;
    }

    public void paint(Graphics g, Rectangle rect) {
        int width = rect.getSize().getWidth();
        int height = rect.getSize().getHeight();
        g.fillLinearGradient(startColor, endColor, rect.getX(), rect.getY(), width, height, false);
        g.setColor(borderColor);
        g.fillRect(rect.getX(), rect.getY(), 1, 1);
        g.fillRect(rect.getX()+width-1, rect.getY(), 1, 1);
        g.fillRect(rect.getX(), rect.getY()+height-1, 1, 1);
        g.fillRect(rect.getX()+width-1, rect.getY()+height-1, 1, 1);
    }

}
