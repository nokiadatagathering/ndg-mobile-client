/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

import java.io.PrintStream;

/**
 *
 * @author alexandre martini
 */
public interface Persistable {
    public void save(PrintStream _output, StringBuffer bfSMS);

}
