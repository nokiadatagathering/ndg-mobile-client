/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.extended;

import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.list.ListModel;

import java.util.Vector;

/**
 *
 * @author SRM
 */
public class FilterProxyListModel implements ListModel, DataChangedListener {
   private ListModel underlying;
   private Vector filter;
   private Vector listeners = new Vector();
   private int maxDisplay = 4;

   public FilterProxyListModel(ListModel underlying) {
        this.underlying = underlying;
        underlying.addDataChangedListener(this);
   }

   public int getFilterOffset(int index) {
       if(filter == null) {
           return index;
       }
       if(filter.size() > index && index >=0) {
           return ((Integer)filter.elementAt(index)).intValue();
       }
       return -1;
   }

   private int getUnderlyingOffset(int index) {
       if(filter == null) {
           return index;
       }
       return filter.indexOf(new Integer(index));
   }

   public void filter(String str) {
       filter = new Vector();
       str = str.toUpperCase();
       for(int iter = 0 ; iter < underlying.getSize() ; iter++) {
           //String element = (String)underlying.getItemAt(iter);
           String element = underlying.getItemAt(iter).toString();
           if(element.toUpperCase().indexOf(str) > -1) {
               filter.addElement(new Integer(iter));
           }
       }
       dataChanged(DataChangedListener.CHANGED, -1);
   }

   public Object getItemAt(int index) {
      
       return underlying.getItemAt(getFilterOffset(index));
   }

   public int getSize() {

       int modelSize = 0;
       if(filter == null) {
           modelSize = underlying.getSize();           
       }else{
           modelSize = filter.size();
       }
       
       if(maxDisplay < 0){
           return modelSize;
       }

       return (modelSize <maxDisplay)? modelSize:maxDisplay;
   }

   public int getSelectedIndex() {
       return Math.max(0, getUnderlyingOffset(underlying.getSelectedIndex()));
   }

   public void setSelectedIndex(int index) {
       underlying.setSelectedIndex(getFilterOffset(index));
   }

   public void addDataChangedListener(DataChangedListener l) {
       listeners.addElement(l);
   }

   public void removeDataChangedListener(DataChangedListener l) {
       listeners.removeElement(l);
   }

   public void addSelectionListener(SelectionListener l) {
       underlying.addSelectionListener(l);
   }

   public void removeSelectionListener(SelectionListener l) {
       underlying.removeSelectionListener(l);
   }

   public void addItem(Object item) {
       underlying.addItem(item);
   }

   public void removeItem(int index) {
       underlying.removeItem(index);
   }

   public void dataChanged(int type, int index) {
       if(index > -1) {
           index = getUnderlyingOffset(index);
           if(index < 0) {
               return;
           }
       }
       for(int iter = 0 ; iter < listeners.size() ; iter++) {

           ((DataChangedListener)listeners.elementAt(iter)).dataChanged(type, index);
       }
   }

    /**
     * @return the maxDisplay
     */
    public int getMaxDisplay() {
        return maxDisplay;
    }

    /**
     * @param maxDisplay the maxDisplay to set
     */
    public void setMaxDisplay(int maxDisplay) {
        this.maxDisplay = maxDisplay;
    }
}