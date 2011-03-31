package br.org.indt.ndg.lwuit.ui;

import br.org.indt.ndg.lwuit.control.CancelPickPhotoFormCommand;
import br.org.indt.ndg.lwuit.ui.camera.NDGCameraManager;
import br.org.indt.ndg.lwuit.ui.camera.LoadedPhotoForm;
import br.org.indt.ndg.lwuit.ui.renderers.FileBrowserCellRenderer;
import br.org.indt.ndg.mobile.Resources;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListModel;
import java.io.IOException;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import com.sun.lwuit.Command;
import com.sun.lwuit.Display;
import com.sun.lwuit.List;
import com.sun.lwuit.layouts.GridLayout;
import java.io.DataInputStream;
import java.util.Stack;
import java.util.Vector;
import javax.microedition.io.file.FileSystemRegistry;

public class FileBrowserView extends Screen implements ActionListener {

    private List myList;
    private byte[] fileRead;
    private static String initialDir = "file:///";
    private static String photoDir = Resources.DEFAULT_PHOTO_DIR;
    private Stack lastDirs;
    private Stack lastSelections;
    private Command localBackCommand = new Command(Resources.NEWUI_BACK);

    protected void loadData() {
        form.removeAll();
        form.removeAllCommands();
        myList = new List();
        GridLayout layout = new GridLayout(1, 1);
        lastDirs = new Stack();
        lastSelections = new Stack();
        form.setLayout(layout);
        form.addComponent(myList);
        myList.removeActionListener(this);
        myList.addActionListener(this);
        myList.setListCellRenderer(new FileBrowserCellRenderer());
        form.addCommand(localBackCommand);
        try {
            form.removeCommandListener(this);
        } catch ( Exception ex ) {
            //nothing;
        }
        form.addCommandListener(this);
        form.addGameKeyListener(Display.GAME_RIGHT, this);
        form.addGameKeyListener(Display.GAME_LEFT, this);

    }

    protected void customize() {
        setTitle(Resources.NEWUI_NOKIA_DATA_GATHERING, Resources.LOAD_FROM_FILE);
        initialRoots();
    }

    protected void initialRoots() {
        Enumeration e = FileSystemRegistry.listRoots();
        Vector v = new Vector();
        //e.nextElement(); // pop out c:
        v.addElement( photoDir );
        while (e.hasMoreElements()) {
            v.addElement(e.nextElement().toString());
        }
        ListModel listModel = new DefaultListModel(v);
        myList.setModel(listModel);
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == myList && myList.getSelectedItem() != null ||
                evt.getKeyEvent() == Display.GAME_RIGHT) {
            handleEnterDirectory();
        } else if (evt.getSource() == localBackCommand ||
                evt.getKeyEvent() == Display.GAME_LEFT) {
            handleBackDirectory();
        }
    }

    private void handleEnterDirectory() {
        String path = null;
        Object selectedItem = myList.getSelectedItem();
        if (lastDirs.isEmpty()) {
            if (selectedItem.toString().indexOf(photoDir) == -1) {
                path = initialDir + selectedItem.toString();
            } else {
                path = System.getProperty("fileconn.dir.photos"); // for initial photo dir
            }
        } else {
            path = lastDirs.peek()  + selectedItem.toString();
        }
        if(handlePath(path)) {
            lastDirs.push(path);
            lastSelections.push(selectedItem);
        }
    }

    private void handleBackDirectory() {
        if (!lastDirs.isEmpty()) {
            lastDirs.pop();
            if (lastDirs.size() == 0) {
                initialRoots();
            } else {
                handlePath(lastDirs.peek().toString());
            }
            myList.setSelectedItem(lastSelections.pop());
        } else {
            CancelPickPhotoFormCommand.getInstance().execute(null);
        }
    }

    protected boolean handlePath(String path) {
        boolean result = true;
        String fileName = null;
        try {
            FileConnection connection = (FileConnection) Connector.open(path, Connector.READ);
            if (connection.exists()) {
                if (path.endsWith("/")) {  // isDirctory does not work
                    Enumeration e = connection.list();
                    connection.close();
                    Vector v = new Vector();
                    FileConnection file;
                    while (e.hasMoreElements()) {
                        try {
                            fileName = e.nextElement().toString();
                            file = (FileConnection) Connector.open(path + fileName, Connector.READ);
                            if (file.exists() && (fileName.endsWith("/") || fileName.toLowerCase().endsWith("jpg")
                                    || fileName.toLowerCase().endsWith("jpeg")
                                    || fileName.toLowerCase().endsWith("png")) ) {
                                v.addElement(fileName);
                            }
                            file.close();
                        } catch (SecurityException ex) {
                            // not accessible file or dir is not displayed
                        } catch (Exception ex) {
                            // bad formatted file or dir is not displayed
                        }
                    }
                ListModel listModel = new DefaultListModel(v);
                myList.setModel(listModel);
            } else {
                DataInputStream input = connection.openDataInputStream();
                    if (connection.fileSize() > Integer.MAX_VALUE) {
                        // hope this wont happen in 21 century
                        throw new IOException("file is to large?!?");
                    }
                fileRead = new byte[(int) connection.fileSize()];
                input.read(fileRead);
                input.close();
                NDGCameraManager.getInstance().updatePhotoForm(fileRead);
                Screen.show(LoadedPhotoForm.class, true);
                }
            } else {
                result = false;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            result = false;
        }  catch (SecurityException ex) {
            //acces denied
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(Resources.WARNING, Resources.ACCESS_DENIED , GeneralAlert.WARNING);
            result = false;
        }  catch (OutOfMemoryError ex) {
            GeneralAlert.getInstance().addCommand(GeneralAlert.DIALOG_OK, true);
            GeneralAlert.getInstance().show(Resources.WARNING, Resources.MEMORY_OUT , GeneralAlert.WARNING);
            result = false;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }
}
