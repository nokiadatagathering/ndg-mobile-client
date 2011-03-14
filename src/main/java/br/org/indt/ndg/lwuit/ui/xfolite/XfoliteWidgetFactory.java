package br.org.indt.ndg.lwuit.ui.xfolite;

import br.org.indt.ndg.lwuit.extended.CheckBox;
import br.org.indt.ndg.lwuit.extended.DateField;
import br.org.indt.ndg.lwuit.extended.DescriptiveField;
import br.org.indt.ndg.lwuit.extended.NumericField;
import br.org.indt.ndg.lwuit.extended.RadioButton;
import br.org.indt.ndg.lwuit.extended.TimeField;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import com.nokia.xfolite.xforms.dom.BoundElement;
import com.nokia.xfolite.xforms.dom.XFormsElement;
import com.nokia.xfolite.xforms.model.datatypes.DataTypeBase;
import com.nokia.xfolite.xml.dom.Element;
import com.nokia.xfolite.xml.dom.Node;
import com.nokia.xfolite.xml.dom.WidgetFactory;
import com.nokia.xfolite.xml.xpath.NodeSet;
import com.nokia.xfolite.xml.xpath.XPathNSResolver;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;

/**
 *
 * @author damian.janicki
 */
public class XfoliteWidgetFactory implements WidgetFactory, XPathNSResolver {

    private Container rootContainer = null;
//    public String stringType = "xsd:string";
//    public String numericType = "xsd:integer";

    public XfoliteWidgetFactory(Container cont) {
        rootContainer = cont;
        rootContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
    }

    public void elementParsed(Element el) {
    }

    public void childrenParsed(Element el) {
        String tagName = el.getLocalName();
        BoundElement binding = null;
        if (el instanceof BoundElement) {
            binding = (BoundElement) el;
        }
        Container cont = createQuestionContainer();

        if ("input" == tagName || "secret" == tagName) {
            addInput(binding, cont);
        } else if (tagName == "select") {
            addSelect(binding, cont);
        } else if ("range" == tagName) {
        } else if ("output" == tagName) {
        } else if ("trigger" == tagName) {
        } else if ("switch" == tagName) {
        } else if ("upload" == tagName) {
        } else if ("submit" == tagName) {
        } else if ("select1" == tagName) {
            addSelect1(binding, cont);
        } else if ("img" == tagName) {
        } else if ("hr" == tagName) {
        } else if ("p" == tagName) {
        } else if ("table" == tagName) {
        } else if ("td" == tagName || "th" == tagName) {
        } else {
        }

        rootContainer.addComponent(cont);
    }

    public void removingElement(Element el) {
        System.out.println("______MYTEST _______ removingElement:  " + el.getLocalName());
    }

    public void elementInitialized(Element el) {
        System.out.println("______MYTEST _______ elementInitialized:  " + el.getLocalName());
    }

    public void childrenInitialized(Element el) {
        System.out.println("______MYTEST _______ childrenInitialized: " + el.getLocalName());
    }

    public String lookupNamespaceURI(String prefix) {
        return "";
    }

    private void addInput(BoundElement bindElem, Container parentContainer) {
        if (bindElem == null || parentContainer == null) {
            System.out.println("bindElem or parentContainrer is null");
            return;
        }

        DataTypeBase a = bindElem.getDataType();

        Component question = null;
        if (a != null) {
            switch (a.getBaseTypeID()) {
                case DataTypeBase.XML_SCHEMAS_DATE:
                    question = new XfoilDateFieldUI(bindElem);
                    break;
                case DataTypeBase.XML_SCHEMAS_DATETIME:
                    break;
                case DataTypeBase.XML_SCHEMAS_TIME:
                    question = new XfoilTimeFieldUI(bindElem);
                    break;
                case DataTypeBase.XML_SCHEMAS_STRING:
                case DataTypeBase.XML_SCHEMAS_ANYURI:
                    question = new XfoilDescriptiveFieldUI(bindElem);
                    break;
                case DataTypeBase.XML_SCHEMAS_DECIMAL:
                case DataTypeBase.XML_SCHEMAS_INTEGER:
                    question = new XfoilNumericFieldUI(bindElem);
                    break;
                default:
                    return;
            }
        } else {
            // other method to distinct questions, works on openxdata form
//            String bindAttr = bindElem.getAttribute(bindElem.useRepeatPrefix() ? "repeat-bind" : "bind");
//            String typeA = bindElem.getModel().getBind(bindAttr).getTypeString();
//            if (typeA.compareTo(stringType) == 0) {
//                question = addDescriptionQuestion(bindElem);
//            } else if (typeA.compareTo(numericType) == 0) {
//                question = addNumericQuestion(bindElem);
//            } else {
//                return;
//            }
        }
        parentContainer.addComponent(question);
    }

    private void addSelect(BoundElement bindElem, Container parentContainer) {
        if (bindElem == null || parentContainer == null) {
            System.out.println("bindElem or parentContainrer is null");
            return;
        }
        Component question = new XfoilMultipleChoiceFieldUI(bindElem);
        parentContainer.addComponent(question);

    }

    private void addSelect1(BoundElement bindElem, Container parentContainer) {
        if (bindElem == null || parentContainer == null) {
            System.out.println("bindElem or parentContainrer is null");
            return;
        }
        Component question = new XfoilExclusiveChoiceFieldUI(bindElem);
        parentContainer.addComponent(question);
    }

    private Container createQuestionContainer() {
        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        container.getStyle().setBorder(Border.createBevelLowered(NDGStyleToolbox.getInstance().focusLostColor,
                NDGStyleToolbox.getInstance().focusLostColor,
                NDGStyleToolbox.getInstance().focusLostColor,
                NDGStyleToolbox.getInstance().focusLostColor));
        return container;
    }
}

abstract class ContainerUI extends Container implements FocusListener {

    protected TextArea qname;
    protected BoundElement element;

    public abstract void commitValue();

    public abstract void setEnabled(boolean enabled);

    public void handleMoreDetails(Object cmd) {
    }

    public ContainerUI(BoundElement element) {
        getStyle().setBorder(Border.createBevelLowered(NDGStyleToolbox.getInstance().focusLostColor,
                NDGStyleToolbox.getInstance().focusLostColor,
                NDGStyleToolbox.getInstance().focusLostColor,
                NDGStyleToolbox.getInstance().focusLostColor));
        this.element = element;
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
    }

    protected void addQuestionName() {

        addComponent(new Label(""));
        XFormsElement labelEl = (XFormsElement) element.getUserData(XFormsElement.LABEL_KEY);
        TextArea questionName = new TextArea();
        questionName.setEditable(false);
        questionName.setFocusable(false);
        questionName.setRows(1);
        questionName.setGrowByContent(true);
        questionName.setText(labelEl.getText());

        int pw = Display.getInstance().getDisplayWidth();
        int w = questionName.getStyle().getFont().stringWidth(labelEl.getText());
        if (w > pw) {
            questionName.setGrowByContent(true);
            questionName.setRows(2);
        } else {
            questionName.setGrowByContent(false);
            questionName.setRows(1);
        }
        this.addComponent(questionName);
    }

    public BoundElement getElement() {
        return element;
    }

    public void focusGained(Component cmpnt) {
        getStyle().setBorder(Border.createBevelLowered(NDGStyleToolbox.getInstance().focusGainColor,
                NDGStyleToolbox.getInstance().focusGainColor,
                NDGStyleToolbox.getInstance().focusGainColor,
                NDGStyleToolbox.getInstance().focusGainColor), false);
        refreshTheme();
    }

    public void focusLost(Component cmpnt) {
        commitValue();
        if (false) // not pass constraints
        {
            cmpnt.requestFocus();
            return;
        }
        getStyle().setBorder(Border.createBevelLowered(NDGStyleToolbox.getInstance().focusLostColor,
                NDGStyleToolbox.getInstance().focusLostColor,
                NDGStyleToolbox.getInstance().focusLostColor,
                NDGStyleToolbox.getInstance().focusLostColor), false);
        refreshTheme();
    }

    protected String getLabel(XFormsElement el) {
        XFormsElement labelEl = (XFormsElement) el.getUserData(XFormsElement.LABEL_KEY);
        if (labelEl != null) {
            return labelEl.getText();
        } else {
            return "";
        }
    }

    protected String getValue(XFormsElement el) {
        XFormsElement valueEl = (XFormsElement) el.getUserData(XFormsElement.VALUE_KEY);
        if (valueEl != null) {
            return valueEl.getText();
        } else {
            return "";
        }
    }
}

class XfoilDescriptiveFieldUI extends ContainerUI {

    public XfoilDescriptiveFieldUI(BoundElement element) {
        super(element);
        addQuestionName();
        addDescriptionQuestion(element);
    }

    public void commitValue() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEnabled(boolean enabled) {
   //     throw new UnsupportedOperationException("Not supported yet.");
    }

    private void addDescriptionQuestion(BoundElement bindElem) {
        String strValue = bindElem.getStringValue();
        DescriptiveField tfDesc = new DescriptiveField(100);
        tfDesc.setInputMode("Abc");
        tfDesc.setEditable(true);
        tfDesc.setFocusable(true);
        if (strValue != null) {
            tfDesc.setText(strValue);
        }
        this.addComponent(tfDesc);
    }
}

class XfoilNumericFieldUI extends ContainerUI {

    public XfoilNumericFieldUI(BoundElement element) {
        super(element);
        addQuestionName();
        addNumericQuestion(element);
    }

    public void commitValue() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEnabled(boolean enabled) {
     //   throw new UnsupportedOperationException("Not supported yet.");
    }

    private void addNumericQuestion(BoundElement bindElem) {
        String value = bindElem.getStringValue();
        NumericField nfNumber = new NumericField(20, false);  // to be done decimal or not
        nfNumber.setFocusable(true);
        if (value != null) {
            nfNumber.setText(value);
        }
        this.addComponent(nfNumber);
    }
}

class XfoilDateFieldUI extends ContainerUI {

    public XfoilDateFieldUI(BoundElement element) {
        super(element);
        addQuestionName();
        addDateQuestion(element);
    }

    public void commitValue() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEnabled(boolean enabled) {
      //  throw new UnsupportedOperationException("Not supported yet.");
    }

    private void addDateQuestion(BoundElement bindElem) {
        String value = bindElem.getStringValue();
        DateField dfDate;
        if (value != null && value != "") {
            dfDate = new DateField(value, DateField.YYYYMMDD, '-');
        } else {
            dfDate = new DateField(DateField.YYYYMMDD);
        }
        dfDate.setEditable(true);
        //dfDate.addFocusListener(this);
        //dfDate.addDataChangeListener(new HandleInterviewAnswersModified());
        addComponent(dfDate);
    }
}

class XfoilTimeFieldUI extends ContainerUI {

    public XfoilTimeFieldUI(BoundElement element) {
        super(element);
        addQuestionName();
        addTimeQuestion(element);
    }

    public void commitValue() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEnabled(boolean enabled) {
     //   throw new UnsupportedOperationException("Not supported yet.");
    }

    private void addTimeQuestion(BoundElement bindElem) {
        String value = bindElem.getStringValue();
        TimeField dfTime;
        if (value != null && (value == null ? "" != null : !value.equals(""))) {
            dfTime = new TimeField(value, TimeField.HHMM, ':');
        } else {
            dfTime = new TimeField(TimeField.HHMM);
        }
        //long datelong = Long.parseLong((String) bindElem.getText();
        //dfDate.setDate(new Date(datelong));
        dfTime.setEditable(true);
        //dfDate.addFocusListener(this);
        //dfDate.addDataChangeListener(new HandleInterviewAnswersModified());
        addComponent(dfTime);
    }
}

class XfoilMultipleChoiceFieldUI extends ContainerUI {

    public XfoilMultipleChoiceFieldUI(BoundElement element) {
        super(element);
        addQuestionName();
        addSelectQuestion(element);
    }

    public void commitValue() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEnabled(boolean enabled) {
     //   throw new UnsupportedOperationException("Not supported yet.");
    }

    private void addSelectQuestion(BoundElement bindElem) {
        NodeSet choices = new NodeSet();

        int count = bindElem.getChildCount();
        for (int idx = 0; idx < count; idx++) {
            Node nodeItem = bindElem.getChild(idx);
            if (nodeItem.getLocalName() != null
                    && nodeItem.getLocalName().compareTo("item") == 0) {
                choices.AddNode(nodeItem);
            }
        }

        int length = choices.getLength();
        String[] names = new String[length];
        String[] values = new String[length];
        boolean[] selected = new boolean[length];


        String chosenVal = " " + bindElem.getStringValue() + " ";
        for (int i = 0; i < length; i++) {
            XFormsElement n = (XFormsElement) choices.item(i);
            String label = getLabel(n);
            String value = getValue(n);

            if (!value.equals("") && chosenVal.indexOf(" " + value + " ") >= 0) {
                selected[i] = true;
            } else {
                selected[i] = false;
            }
            names[i] = label;
            values[i] = value;
        }

        for (int chIdx = 0; chIdx < length; chIdx++) {
            CheckBox cb = new CheckBox(names[chIdx]);
            addComponent(cb);
        }
    }
}

class XfoilExclusiveChoiceFieldUI extends ContainerUI {

    public XfoilExclusiveChoiceFieldUI(BoundElement element) {
        super(element);
        addQuestionName();
        addSelect1Question(element);
    }

    public void commitValue() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEnabled(boolean enabled) {
      //  throw new UnsupportedOperationException("Not supported yet.");
    }

    private void addSelect1Question(BoundElement bindElem) {
        NodeSet choices = new NodeSet();

        int count = bindElem.getChildCount();
        for (int idx = 0; idx < count; idx++) {
            Node nodeItem = bindElem.getChild(idx);
            if (nodeItem.getLocalName() != null
                    && nodeItem.getLocalName().compareTo("item") == 0) {
                choices.AddNode(nodeItem);
            }
        }

        int length = choices.getLength();
        String[] names = new String[length];
        String[] values = new String[length];
        boolean[] selected = new boolean[length];

        String chosenVal = " " + bindElem.getStringValue() + " ";
        for (int i = 0; i < length; i++) {
            XFormsElement n = (XFormsElement) choices.item(i);
            String label = getLabel(n);
            String value = getValue(n);

            if (!value.equals("") && chosenVal.indexOf(" " + value + " ") >= 0) {
                selected[i] = true;
            } else {
                selected[i] = false;
            }
            names[i] = label;
            values[i] = value;
        }
        ButtonGroup groupButton = new ButtonGroup();

        int totalChoices = names.length;
        String[] choicesStrings = new String[totalChoices];
        for (int i = 0; i < totalChoices; i++) {
            choicesStrings[i] = (String) names[i];
            RadioButton rb = new RadioButton(choicesStrings[i]);
            rb.setOther(values[i].equals("1"));
            rb.setOtherText(""); // Initializes with empty string
            //rb.addActionListener(new HandleMoreDetails()); // More Details
            //rb.addFocusListener(this); // Controls when changing to a new question
            groupButton.add(rb);
            addComponent(rb);
        }
    }
}
