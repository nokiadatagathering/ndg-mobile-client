package br.org.indt.ndg.lwuit.model;


public class CategoryConditional extends Category{

    private String mConditionQuestion;
    private int mQuantity;

    public CategoryConditional( String aName, String aId, String aConditionQuestion ) {
        super( aName, aId );
        mConditionQuestion = aConditionQuestion;
        mQuantity = 0;
    }

    public String getConditionQuestion() {
        return mConditionQuestion;
    }

    public void setQuantity( int aQuanity ) {
        mQuantity = aQuanity;
    }

    public int getQuantity() {
        return mQuantity;
    }
}
