package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.extended.DateField;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.sun.lwuit.Component;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.animations.CommonTransitions;
import com.sun.lwuit.plaf.DefaultLookAndFeel;
import com.sun.lwuit.plaf.Style;
import com.sun.lwuit.plaf.UIManager;

/**
 *
 * @author mluz
 */
public class NDGLookAndFeel extends DefaultLookAndFeel {


    /**
     * Similar to getText() but works properly with password fields
     */
    private String getTextFieldString(TextField ta) {
        String text = (String) ta.getText();
        String displayText = "";
        if ((ta.getConstraint() & TextArea.PASSWORD) != 0) {
            // show the last character in a password field
            if (ta.isPendingCommit()) {
                if (text.length() > 0) {
                    for (int j = 0; j < text.length() - 1; j++) {
                        displayText += "*";
                    }
                    displayText += text.charAt(text.length() - 1);
                }
            } else {
                for (int j = 0; j < text.length(); j++) {
                    displayText += "*";
                }
            }
        } else {
            displayText = text;
        }
        return displayText;
    }

    

    private void drawDateField(Graphics g, DateField df) {
        setFG(g, df);

        String displayText = df.getText();

        // DD = QW
        // MM = ER
        // YYYY = TYUI

        Style style = df.getStyle();
        int x = 0;
        int cursorCharPosition = df.getCursorPosition();
        Font f = df.getStyle().getFont();
        int cursorX = 0;
        int xPos = 0;
        if (cursorCharPosition > 0) {
            xPos = f.stringWidth(displayText.substring(0, cursorCharPosition));
            cursorX = df.getX() + style.getPadding(Component.LEFT) + xPos;
            if (df.getWidth() > (f.getHeight() * 2) && cursorX >= df.getWidth() - style.getPadding(Component.LEFT)) {
                while (x + xPos >= df.getWidth() - style.getPadding(Component.LEFT) * 2) {
                    x--;
                }
            }
        }
        g.setColor(0xFF0000);

        int xString = df.getX() + x + style.getPadding(Component.LEFT);
        int yString = df.getY() + style.getPadding(Component.TOP);

        // first field
        String field1 = df.getField(1);
        String field2 = df.getField(2);
        String field3 = df.getField(3);
        String separator = "/";// df.getSeparator();

        // field 1
        if (df.isSelectMode() && df.getFieldSelected() == 1 && df.hasFocus() && df.isFocusPainted()) {
            setFG(g, df);
            g.fillRect(xString-1, yString, f.stringWidth(field1)+2, f.getHeight());
            //DF:g.setColor(df.getStyle().getBgSelectionColor());
            g.setColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
        } else
            setFG(g, df);
        g.drawString(field1, xString, yString);

        // separator
        xString += f.stringWidth(field1);
        setFG(g, df);
        g.drawString(separator, xString, df.getY() + style.getPadding(Component.TOP));

        //field 2
        xString += f.stringWidth(separator);
        if (df.isSelectMode() && df.getFieldSelected() == 2 && df.hasFocus() && df.isFocusPainted()) {
            setFG(g, df);
            g.fillRect(xString-1, yString, f.stringWidth(field2)+2, f.getHeight());
            //DF:g.setColor(df.getStyle().getBgSelectionColor());
            g.setColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor);
        } else
            setFG(g, df);
        g.drawString(field2, xString, df.getY() + style.getPadding(Component.TOP));

        // separator
        xString += f.stringWidth(field2);
        setFG(g, df);
        g.drawString(separator, xString, df.getY() + style.getPadding(Component.TOP));

        // field 3
        xString += f.stringWidth(separator);
        if (df.isSelectMode() && df.getFieldSelected() == 3 && df.hasFocus() && df.isFocusPainted()) {
            setFG(g, df);
            g.fillRect(xString-1, yString, f.stringWidth(field3)+2, f.getHeight());
             //DF:g.setColor(df.getStyle().getBgSelectionColor());
            g.setColor(NDGStyleToolbox.getInstance().listStyle.selectedFontColor);
        } else
            setFG(g, df);
        g.drawString(field3, xString, df.getY() + style.getPadding(Component.TOP));

        //g.drawString(displayText, xString, df.getY() + style.getPadding(Component.TOP));



        // show always
        if(df.getInputModeOrder() != null && df.getInputModeOrder().length > 0) {
            String inputMode = df.getInputMode();
            int inputModeWidth = f.stringWidth(inputMode);
            if (df.handlesInput() && df.getWidth() / 2 > inputModeWidth) {
                int drawXPos = df.getX() + style.getPadding(Component.LEFT) -1;
                if (xPos < df.getWidth() / 2) {
                    // draw on the right side
                    drawXPos = drawXPos + df.getWidth() - inputModeWidth - style.getPadding(Component.RIGHT) - style.getPadding(Component.LEFT)+2;
                }
                //DF:g.setColor(style.getFgSelectionColor());
                g.setColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
                // unfurtanally g.fillroundrect does not suppport alpha
                //g.fillRoundRect(drawXPos, ta.getY() + style.getPadding(Component.TOP)-1, inputModeWidth, ta.getHeight()-4, 4, 4);
                // so a work around is required
                byte alphaLevel = (byte)140;
                g.fillRect(drawXPos, df.getY() + style.getPadding(Component.TOP)-1, inputModeWidth,
                        df.getHeight()-4, alphaLevel);
                //g.setColor(0xFF0000);

                //DF:g.setColor(style.getBgSelectionColor());
                g.setColor( NDGStyleToolbox.getInstance().listStyle.bgSelectedStartColor );

                g.fillRect(drawXPos, df.getY() + style.getPadding(Component.TOP)-1, 1, 1, alphaLevel);
                g.fillRect(drawXPos + inputModeWidth -1, df.getY() + style.getPadding(Component.TOP)-1, 1, 1, alphaLevel);
                g.fillRect(drawXPos + inputModeWidth -1, df.getY() + style.getPadding(Component.TOP)-1 + df.getHeight()-4 -1, 1, 1, alphaLevel);
                g.fillRect(drawXPos, df.getY() + style.getPadding(Component.TOP)-1 + df.getHeight()-4 -1, 1, 1, alphaLevel);

                g.drawString(inputMode, drawXPos, df.getY() + style.getPadding(Component.TOP));
            }
        }
    }

    /**
     * @inheritDoc
     */
    public void drawTextField(Graphics g, TextField ta) {
        if (ta instanceof DateField) {
            drawDateField(g,(DateField) ta);
            return;
        }
        setFG(g, ta);

        // display ******** if it is a password field
        String displayText = getTextFieldString(ta);

        Style style = ta.getStyle();
        int x = 0;
        int cursorCharPosition = ta.getCursorPosition();
        Font f = ta.getStyle().getFont();
        int cursorX = 0;
        int xPos = 0;
        if (cursorCharPosition > 0) {
            xPos = f.stringWidth(displayText.substring(0, cursorCharPosition));
            cursorX = ta.getX() + style.getPadding(Component.LEFT) + xPos;
            if (ta.getWidth() > (f.getHeight() * 2) && cursorX >= ta.getWidth() - style.getPadding(Component.LEFT)) {
                while (x + xPos >= ta.getWidth() - style.getPadding(Component.LEFT) * 2) {
                    x--;
                }
            }
        }

        g.drawString(displayText, ta.getX() + x + style.getPadding(Component.LEFT),
                ta.getY() + style.getPadding(Component.TOP));

        // show always
        if(ta.getInputModeOrder() != null && ta.getInputModeOrder().length > 0) {
            String inputMode = ta.getInputMode();
            int inputModeWidth = f.stringWidth(inputMode);
            if (ta.handlesInput() && ta.getWidth() / 2 > inputModeWidth) {
                int drawXPos = ta.getX() + style.getPadding(Component.LEFT) -1;
                if (xPos < ta.getWidth() / 2) {
                    // draw on the right side
                    drawXPos = drawXPos + ta.getWidth() - inputModeWidth - style.getPadding(Component.RIGHT) - style.getPadding(Component.LEFT);
                }
                //DF:g.setColor(style.getFgSelectionColor());
                g.setColor( NDGStyleToolbox.getInstance().listStyle.selectedFontColor );
                // unfurtanally g.fillroundrect does not suppport alpha
                //g.fillRoundRect(drawXPos, ta.getY() + style.getPadding(Component.TOP)-1, inputModeWidth, ta.getHeight()-4, 4, 4);
                // so a work around is required
                byte alphaLevel = (byte)140;
                g.fillRect(drawXPos, ta.getY() + style.getPadding(Component.TOP)-1, inputModeWidth+2,
                        ta.getHeight()-4, alphaLevel);
                //g.setColor(0xFF0000);

                //DF:g.setColor(style.getBgSelectionColor());
                g.setColor( NDGStyleToolbox.getInstance().listStyle.bgSelectedStartColor );

                g.fillRect(drawXPos, ta.getY() + style.getPadding(Component.TOP)-1, 1, 1, alphaLevel);
                g.fillRect(drawXPos + inputModeWidth +1, ta.getY() + style.getPadding(Component.TOP)-1, 1, 1, alphaLevel);
                g.fillRect(drawXPos + inputModeWidth +1, ta.getY() + style.getPadding(Component.TOP)-1 + ta.getHeight()-4 -1, 1, 1, alphaLevel);
                g.fillRect(drawXPos, ta.getY() + style.getPadding(Component.TOP)-1 + ta.getHeight()-4 -1, 1, 1, alphaLevel);

                g.drawString(inputMode, drawXPos+1, ta.getY() + style.getPadding(Component.TOP));
            }
        }
    }

    public static void setDefaultFormTransitionInForward() {
        UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, false, 500));
    }

    public static void setDefaultFormTransitionInReversed() {
        UIManager.getInstance().getLookAndFeel().setDefaultFormTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, 500));
    }

    public static void setDefaultDialogTransitionInAndOut() {
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionIn(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, false, 500));
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionOut(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, 500));
    }
    public static void removeDefaultDialogTransitionInAndOut() {
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionIn(null);
        UIManager.getInstance().getLookAndFeel().setDefaultDialogTransitionOut(null);
    }

    public static Image getRightContextMenuImage( int parentHeight ) {
        Image arrow = Screen.getRes().getImage("right_arrow");
        if ( parentHeight > 0 ) { // avoid scaling at first run when height is undetermined
            // arrow does not scale too well so it is needed to set a reasonable limit
            int newHeight = parentHeight/2 > 15 ? 15 : parentHeight/2;
            arrow = arrow.scaledHeight(newHeight);
        }
        return arrow;
    }

}
