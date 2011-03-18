package br.org.indt.ndg.lwuit.model;

/**
 *
 * @author alexandre martini
 */
public class ImageQuestion extends NDGQuestion{

    private int maxCount = 1;
    
    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxImages) {
        this.maxCount = maxImages;
    }

    public boolean passConstraints( NDGAnswer aAnswer ) {
        return true;
    }

    public NDGAnswer getAnswerModel() {
        return new ImageAnswer();
    }
}
