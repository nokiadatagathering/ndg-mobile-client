/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.org.indt.ndg.lwuit.model;

import java.util.Vector;

/**
 *
 * @author alexandre martini
 */
public class ImageAnswer extends Answer{

    private Vector images = new Vector();

    public Vector getImages (){
        return images;
    }


    public Object getValue(){
        // TODO notify programmer not to use this method for ImageAnswer
        // use getImages insted
        return null;
    }

}
