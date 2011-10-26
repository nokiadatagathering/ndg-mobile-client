/*
*  Nokia Data Gathering
*
*  Copyright (C) 2011 Nokia Corporation
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  You should have received a copy of the GNU Lesser General Public License
*  along with this program.  If not, see <http://www.gnu.org/licenses/
*/

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
