package br.org.indt.ndg.mobile.error;

/*
 * Used to emphasise that OutOfMmeoryError is likely to appear in function and should be handled
 */
public class OutOfMemoryErrorExtended extends Exception {

    public OutOfMemoryErrorExtended( String reason ) {
        super("OutOfMemoryError that requires handling occured. Reason: " + reason);
    }

}
