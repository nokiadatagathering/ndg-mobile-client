package br.org.indt.ndg.lwuit.model;

/*
 * NOTE:
 * Usage as extension to Survey is optional here. It's merely a suggestion that
 * there should be a commmon part for NDG and OpenRosa protocol
 */
public class XFormSurvey extends Survey {

    private String m_downloadUrl = null;
    private String  m_formId = null;
    private String m_majorVersion = null;

    public String getDownloadUrl() {
        return m_downloadUrl;
    }

    public void setDownloadUrl( String downloadUrl ) {
        this.m_downloadUrl = downloadUrl;
    }

    public String getFormId() {
        return m_formId;
    }

    public void setFormId( String formId ) {
        this.m_formId = formId;
    }

    public String getMajorVersion() {
        return m_majorVersion;
    }

    public void setMajorVersion( String version ) {
        this.m_majorVersion = version;
    }

}
