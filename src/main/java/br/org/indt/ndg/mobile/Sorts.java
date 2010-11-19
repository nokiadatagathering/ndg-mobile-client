/*
 * Sorts.java
 *
 * Created on November 6, 2007, 2:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package br.org.indt.ndg.mobile;

import java.util.Vector;
/**
 *
 * @author tdickes
 */
public class Sorts {
    
    /** Creates a new instance of Sorts */
    public Sorts() {
    }
    
    //Vector of Strings version
    public void qsort(Vector list) {
        quicksort(list, 0, list.size()-1);
    }
    
    //String Array version
    public void qsort(String[] list) {
        quicksort(list, 0, list.length-1);
    }
    
    //Vector of Strings version
    private void quicksort(Vector list, int p, int r) {
        if (p < r) {
            int q = qpartition(list, p, r);
            if (q == r) {
                q--;
            }
            quicksort(list, p, q);
            quicksort(list, q+1, r);
        }
    }
    
    //String Array version
    private void quicksort(String[] list, int p, int r) {
        if (p < r) {
            int q = qpartition(list, p, r);
            if (q == r) {
                q--;
            }
            quicksort(list, p, q);
            quicksort(list, q+1, r);
        }
    }
    
    //Vector of Strings version
    private int qpartition(Vector list, int p, int r) {
        String pivot = ((XmlResultFile) list.elementAt(p)).getDisplayName();
        int lo = p;
        int hi = r;
        
        while (true) {
            while ( (((XmlResultFile) list.elementAt(hi)).getDisplayName()).compareTo(pivot) >= 0 && lo < hi) {
                hi--;
            }
            while ( (((XmlResultFile) list.elementAt(lo)).getDisplayName()).compareTo(pivot) < 0 && lo < hi) {
                lo++;
            }
            if (lo < hi) {
                XmlResultFile T = (XmlResultFile) list.elementAt(lo);
                list.setElementAt(list.elementAt(hi), lo);
                list.setElementAt(T, hi);
            }
            else return hi;
        }
    }
    
    //String Array version
    private int qpartition(String[] list, int p, int r) {
        String pivot = list[p];
        int lo = p;
        int hi = r;
        
        while (true) {
            while (list[hi].compareTo(pivot) >= 0 && lo < hi) {
                hi--;
            }
            while (list[lo].compareTo(pivot) < 0 && lo < hi) {
                lo++;
            }
            if (lo < hi) {
                String T = list[lo];
                list[lo] = list[hi];
                list[hi] = T;
            }
            else return hi;
        }
    }
}
