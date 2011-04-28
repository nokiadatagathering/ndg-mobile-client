package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.OkEncryptionScreenCommand;
import br.org.indt.ndg.lwuit.extended.DescriptiveField;
import br.org.indt.ndg.lwuit.ui.style.NDGStyleToolbox;
import br.org.indt.ndg.mobile.AppMIDlet;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.TextArea;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;

/**
 *
 * @author roda
 */
public class EncryptionKeyScreen extends Screen implements ActionListener {

    private DescriptiveField tfDesc = null;
    private String password = null;

    protected void loadData() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.ENCRYPTION);
    }

    protected void customize() {
        form.removeAllCommands();
        form.removeAll();
        form.addCommand(OkEncryptionScreenCommand.getInstance().getCommand());
        try{
            form.removeCommandListener(this);
        } catch (NullPointerException npe ) {
            //during first initialisation remove throws exception.
            //this ensure that we have registered listener once
        }
        form.addCommandListener(this);

        TextArea questionName = UIUtils.createTextArea( Resources.ENCRYPTION_PASSWORD,
                                                        NDGStyleToolbox.fontMedium );
        questionName.getStyle().setFgColor( NDGStyleToolbox.getInstance().listStyle.unselectedFontColor );
        form.addComponent(questionName);

        tfDesc = new DescriptiveField(16);
        tfDesc.setInputMode("Abc");
        tfDesc.setEditable(true);
        tfDesc.setFocusable(true);
        form.addComponent(tfDesc);
        tfDesc.requestFocus();
    }

    public void actionPerformed(ActionEvent evt) {
        Object cmd = evt.getSource();
        if (cmd == OkEncryptionScreenCommand.getInstance().getCommand()) {
            password = tfDesc.getText();
            Digest digest = new MD5Digest();

            if(password != null && !password.equals("")) {

                byte[] key = password.getBytes();
                digest.update(key, 0, key.length);
                byte[] md5 = new byte[digest.getDigestSize()];
                digest.doFinal(md5, 0);

                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < md5.length; i++ ) {
                    byte b = md5[i];

                    if(b >= 0 && b <= 15) {
                        sb.append(Integer.toHexString( (int) 0));
                        sb.append(Integer.toHexString( (int) b));
                    }
                    else
                        sb.append(Integer.toHexString((int) (b & 0xff)));
                }

                AppMIDlet.getInstance().setKey(sb.toString().getBytes());
                OkEncryptionScreenCommand.getInstance().execute(null);
            }
            else {
                GeneralAlert.getInstance().addCommand( GeneralAlert.DIALOG_OK, true );
                GeneralAlert.getInstance().showCodedAlert( Resources.FAILED_REASON, Resources.EMPTY_KEY, GeneralAlert.ALARM );
            }
        }
    }
}