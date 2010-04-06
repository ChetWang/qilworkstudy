package fr.itris.glips.svgeditor.io.managers;

import java.awt.Frame;
import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.graphunit.NCIEquipSymbolBean;
import com.nci.svg.logntermtask.LongtermTask;
import com.nci.svg.logntermtask.LongtermTaskManager;
import com.nci.svg.ui.NciRemoteSvgFileDialog;
import com.nci.svg.ui.RemoteFileChooser;
import com.nci.svg.util.Constants;
import com.nci.svg.util.NCIGlobal;
import com.nci.svg.util.ServletActionConstants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.monitor.Monitor;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.io.IOManager;
import fr.itris.glips.svgeditor.io.managers.dialog.FileChooserDialog;
import fr.itris.glips.svgeditor.io.managers.dialog.SVGFileFilter;
import fr.itris.glips.svgeditor.io.managers.monitor.OpenMonitor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class handling the opening of files
 * 
 * @author Jordi SUC
 */
public class FileOpen {

    /**
     * the io manager
     */
    private IOManager ioManager;

    /**
     * the file chooser dialog
     */
    private FileChooserDialog fileChooserDialog;

    /**
     * the labels
     */
    private String warningNotNullMessage = "", warningNullMessage = "",
            warningTitle = "";

    /**
     * the constructor of the class
     * 
     * @param ioManager
     *            the io manager
     */
    public FileOpen(IOManager ioManager) {

        this.ioManager = ioManager;

        // creating the file chooser dialog
        if (ioManager.getEditor().getParent() instanceof Frame) {

            fileChooserDialog = new FileChooserDialog((Frame) ioManager
                    .getEditor().getParent(), FileChooserDialog.OPEN_FILE_MODE,
                    ioManager.getEditor());

        } else if (ioManager.getEditor().getParent() instanceof JDialog) {

            fileChooserDialog = new FileChooserDialog((JDialog) ioManager
                    .getEditor().getParent(), FileChooserDialog.OPEN_FILE_MODE,
                    ioManager.getEditor());
        }

        // setting the file filter
        fileChooserDialog.setFileFilter(new SVGFileFilter());

        // getting the labels from the resources store
        ResourceBundle bundle = ResourcesManager.bundle;

        if (bundle != null) {

            try {
                warningNotNullMessage = bundle
                        .getString("OpenFailedErrorFileNotNullMessage");
                warningNullMessage = bundle
                        .getString("OpenFailedErrorFileNullMessage");
                warningTitle = bundle.getString("OpenFailedErrorTitle");
            } catch (Exception ex) {
            }
        }
    }

    /**
     * shows a file chooser so that the user can choose the files to open
     * 
     * @param relativeComponent
     *            the component relatively to which the dialog will be shown
     */
    public void askUserForFile(JComponent relativeComponent) {

        fileChooserDialog.showDialog(relativeComponent);

        // getting the array of the files selected by the user
        File[] selectedFiles = fileChooserDialog.getSelectedFiles();

        if (selectedFiles != null && selectedFiles.length > 0) {

            Monitor monitor = null;

            for (File file : selectedFiles) {

                // creating the monitor
                monitor = new OpenMonitor(file, ioManager.getEditor()
                        .getParent(), relativeComponent, 0, 100);

                // opening the file
                open(file, monitor);
            }
        }
    }

    public void askUserForGraphUnitFile(JComponent relativeComponent) {
        fileChooserDialog.showDialog(relativeComponent);

        // getting the array of the files selected by the user
        File[] selectedFiles = fileChooserDialog.getSelectedFiles();

        if (selectedFiles != null && selectedFiles.length > 0) {

            Monitor monitor = null;

            // for(File file : selectedFiles){
            //				
            // //creating the monitor
            // monitor=new OpenMonitor(file,
            // Editor.getParent(), relativeComponent, 0, 100);
            //				
            // //opening the file
            // open(file, monitor);
            // }
            openGraphUnitFile(selectedFiles[0], monitor);
        }
    }

    private RemoteFileChooser remoteFileChooser;
    private NciRemoteSvgFileDialog remoteSvgFileDialog = null;

    public void askForPersonalFile() {
        if (remoteFileChooser == null) {
            remoteFileChooser = new RemoteFileChooser(ioManager.getEditor(),
                    true, RemoteFileChooser.FILE_TYPE_PERSONAL);
        }
        remoteFileChooser.initRemote();
        remoteFileChooser.setVisible(true);
    }

    public void askForStandardFile() {

        if (remoteSvgFileDialog == null) {
            remoteSvgFileDialog = new NciRemoteSvgFileDialog(ioManager
                    .getEditor().findParentFrame(), true, ioManager.getEditor());
            remoteSvgFileDialog.setTitle(ResourcesManager.bundle.getString("FileOpenStandardItemLabel"));
        }
        remoteSvgFileDialog.initData();
        remoteSvgFileDialog.setVisible(true);
        remoteSvgFileDialog.setLocationRelativeTo(ioManager.getEditor()
                .findParentFrame());
    }

    /**
     * 打开图元
     * 
     * @param file
     * @param monitor
     */
    public void openGraphUnitFile(final File file, final Monitor monitor) {
        SVGHandle handle = null;

        if (file != null && file.exists()) {

            // checking if a svg file having the same name already exists
            // String path=file.toURI().toASCIIString();
            // changed by wangql,为更好地显示中文
            final String path = file.toURI().toString();
            handle = ioManager.getEditor().getHandlesManager().getHandle(path);
            if (handle != null) {

                ioManager.getEditor().getHandlesManager()
                        .setCurrentHandle(path);

            } else {

                // creating a new handle
                // handle = Editor.getEditor().getHandlesManager()
                // .createSVGHandle(path);
                //
                // // adding the file name to the list of the recent files
                // Editor.getEditor().getResourcesManager().addRecentFile(path);
                // Editor.getEditor().getResourcesManager().notifyListeners();
                handle = ioManager.getEditor().getHandlesManager()
                        .getCurrentHandle();
                handle.disposeGraphUnit();
                handle.close();

                // handle.setName(path);
                SVGHandle newHandle = new SVGHandle(path,
                        SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD, handle
                                .getSVGFrame(), ioManager.getEditor());
                ioManager.getEditor().getHandlesManager()
                        .addHandleAndSetToCurrent(newHandle);
                newHandle.getScrollPane().setSvgHandle(newHandle);
                final SVGCanvas canvas = newHandle.getScrollPane()
                        .getSVGCanvas();
                canvas.setSvgHandle(newHandle);
                Runnable runnable = new Runnable() {

                    public void run() {

                        // setting the uri of the svg file
                        canvas.setURI(path, monitor);
                    }
                };

                ioManager.requestExecution(runnable);
            }

        } else {

            // computing the warning message
            String message = warningNullMessage;

            if (file != null) {

                message = warningNotNullMessage + file.getAbsolutePath()
                        + ".</body></html>";
            }

            // if the file could not be opened, a dialog is
            // displayed to notify that an error occured
            JOptionPane.showMessageDialog(ioManager.getEditor().getParent(),
                    message, warningTitle, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 打开指定文件，并赋予该图形所在的厂站名称
     * 
     * @param substationName
     *            厂站名称
     * @param file
     *            指定的要打开的文件
     * @param monitor
     */
    public SVGHandle open(String substationName, final File file,
            final Monitor monitor, int handleType, NCIEquipSymbolBean graphUnit) {
        SVGHandle handle = null;

        if (file != null && file.exists()) {

            // checking if a svg file having the same name already exists
            // String path=file.toURI().toASCIIString();
            // changed by wangql,为更好地显示中文
            final String path = file.toURI().toString();
            handle = ioManager.getEditor().getHandlesManager().getHandle(path);

            if (handle != null) {

                ioManager.getEditor().getHandlesManager()
                        .setCurrentHandle(path);

            } else {

                // creating a new handle
            	System.out.println("createing a new handle");
                handle = ioManager.getEditor().getHandlesManager()
                        .createSVGHandle(path, handleType);
                // 设置厂站名称
//                if (substationName == null)// for test,wangql
//                    substationName = "110kV袁化变";
//                handle.setSubstationName(substationName);
                // adding the file name to the list of the recent files
                if (handleType == SVGHandle.HANDLE_TYPE_SVG) {
                    ioManager.getEditor().getResourcesManager().addRecentFile(
                            path);
                } else if (handleType == SVGHandle.HANDLE_TYPE_GRAPH_UNIT_NORMAL) {
                    handle.setGraphUnit(graphUnit);
                    handle.setName(path);
                }

                ioManager.getEditor().getResourcesManager().notifyListeners();
                final SVGCanvas canvas = handle.getScrollPane().getSVGCanvas();
                Runnable runnable = new Runnable() {
                    public void run() {
                        // setting the uri of the svg file
                        canvas.setURI(path, monitor);
                    }
                };
                ioManager.requestExecution(runnable);
            }

        } else {

            // computing the warning message
            String message = warningNullMessage;

            if (file != null) {

                message = warningNotNullMessage + file.getAbsolutePath()
                        + ".</body></html>";
            }

            // if the file could not be opened, a dialog is
            // displayed to notify that an error occured
            JOptionPane.showMessageDialog(ioManager.getEditor().getParent(),
                    message, warningTitle, JOptionPane.ERROR_MESSAGE);
        }
        return handle;
    }

    /**
     * open the provided file
     * 
     * @param file
     *            a file
     * @param monitor
     *            the object used to monitor the opening of the file
     */
    public void open(final File file, final Monitor monitor) {

        open(null, file, monitor, SVGHandle.HANDLE_TYPE_SVG, null);
    }

    /**
     * @return the linked list of the recently opened files.
     */
    public LinkedList<File> getRecentFiles() {

        // the list that will be returned
        LinkedList<File> recentFiles = new LinkedList<File>();

        // getting the collection of the path of the last recently opened svg
        // files
        Collection<String> filePaths = ioManager.getEditor()
                .getResourcesManager().getRecentFiles();

        // filling the list
        File file = null;

        for (String path : filePaths) {

            try {
                file = new File(new URI(path));
            } catch (Exception ex) {
                file = null;
            }

            if (file != null) {

                recentFiles.add(file);
            }
        }

        return recentFiles;
    }

}
