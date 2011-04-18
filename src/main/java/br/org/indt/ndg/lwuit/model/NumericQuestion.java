package br.org.indt.ndg.lwuit.model;

public abstract class NumericQuestion extends NDGQuestion {
    private int mLength;

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public boolean passConstraints( String input ) {
        if ( passLowConstraint( input ) )
            if ( passHighConstraint( input ) )
                return true;
            else
                return false;
        else
            return false;
    }

    abstract public void setHighConstraint( String _high );
    abstract public void setLowConstraint( String _low );
    abstract public String getType();

    abstract protected boolean passLowConstraint( String aAnswer );
    abstract protected boolean passHighConstraint( String aAnswer );
}
