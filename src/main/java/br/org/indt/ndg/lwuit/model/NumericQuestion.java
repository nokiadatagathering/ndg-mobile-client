package br.org.indt.ndg.lwuit.model;

public abstract class NumericQuestion extends NDGQuestion {
    private int mLength;

    public int getLength() {
        return mLength;
    }

    public void setLength(int length) {
        mLength = length;
    }

    public boolean passConstraints( NDGAnswer aAnswer ) {
        NumericAnswer numericAnswer = (NumericAnswer)aAnswer;
        if ( passLowConstraint( numericAnswer ) )
            if ( passHighConstraint( numericAnswer ) )
                return true;
            else
                return false;
        else
            return false;
    }

    abstract public void setHighConstraint( String _high );
    abstract public void setLowConstraint( String _low );
    abstract public String getType();

    abstract protected boolean passLowConstraint( NumericAnswer aAnswer );
    abstract protected boolean passHighConstraint( NumericAnswer aAnswer );
}
