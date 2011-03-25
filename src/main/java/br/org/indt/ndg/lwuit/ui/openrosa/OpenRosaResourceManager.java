package br.org.indt.ndg.lwuit.ui.openrosa;

import com.nokia.xfolite.xml.dom.Element;
import com.nokia.xfolite.xml.dom.Node;
import java.util.Hashtable;

/**
 *
 * @author damian.janicki
 */
public class OpenRosaResourceManager {
    protected Hashtable resources = null;


    public OpenRosaResourceManager(){
        resources = new  Hashtable();
    }

    public void clear(){
        resources.clear();
    }

    public void put(String key, String value){
        resources.put(key, value);
    }

    public void tryAddLabelValue(Element el){
        Element parent = (Element)el.getParentNode();
        if(parent.getNodeName() != "text"){
            return;
        }

        String id = parent.getAttribute("id");
        String value = el.getText();

        put(id, value);
    }

    public String tryGetLabelForElement(Element el){
        for(int idx = 0; idx < el.getChildCount(); idx++){
            Node child = el.getChild(idx);
            if(child.getNodeName() == "label"){
                Element childElem = (Element)child;
                String reference = childElem.getAttribute("ref");
                if(reference != ""){
                    return parseRefItextElement(reference);
                }
            }
        }
        return "";
    }

    private  String parseRefItextElement(String refValue){
        if(!refValue.startsWith("jr:itext('")){
            return "";
        }

        String strPath = refValue.substring(10, refValue.length()-2);
        if(resources.containsKey(strPath)){
            return (String)resources.get(strPath);
        }else{
            return "";
        }
    }
}
