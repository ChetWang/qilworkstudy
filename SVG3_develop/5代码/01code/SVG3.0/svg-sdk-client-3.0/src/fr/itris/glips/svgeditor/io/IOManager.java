package fr.itris.glips.svgeditor.io;

import java.util.concurrent.CopyOnWriteArrayList;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.logger.LoggerAdapter;

import fr.itris.glips.svgeditor.io.managers.EditorExit;
import fr.itris.glips.svgeditor.io.managers.FileClose;
import fr.itris.glips.svgeditor.io.managers.FileOpen;
import fr.itris.glips.svgeditor.io.managers.FilePrint;
import fr.itris.glips.svgeditor.io.managers.FileSave;
import fr.itris.glips.svgeditor.io.managers.creation.FileNew;
import fr.itris.glips.svgeditor.io.managers.export.FileExport;

/**
 * the class of the manager enabling to interact with the filesystem
 * 
 * @author Jordi SUC
 */
public class IOManager {

	/**
	 * the queue of the runnables used to execute io actions
	 */
	private CopyOnWriteArrayList<Runnable> runnablesQueue = new CopyOnWriteArrayList<Runnable>();
	/**
	 * the "file new" manager
	 */
	private FileNew fileNewManager;
	/**
	 * the "file open" manager
	 */
	private FileOpen fileOpenManager;
	/**
	 * the "file save" manager
	 */
	private FileSave fileSaveManager;
	/**
	 * the "file close" manager
	 */
	private FileClose fileCloseManager;
	/**
	 * the "file print" manager
	 */
	private FilePrint filePrint;
	/**
	 * the "file export" manager
	 */
	private FileExport fileExport;
	/**
	 * the "editor exit" manager
	 */
	private EditorExit editorExitManager;

	EditorAdapter editor;

	/**
	 * the constructor of the class
	 */
	public IOManager(EditorAdapter editor) {

		this.editor = editor;
		fileNewManager = new FileNew(this);
		fileOpenManager = new FileOpen(this);
		fileSaveManager = new FileSave(this);
		fileCloseManager = new FileClose(this);
		filePrint = new FilePrint(this);
		fileExport = new FileExport(this);
		editorExitManager = new EditorExit(this);
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				Runnable runnable = null;

				while (true) {

					if (!runnablesQueue.isEmpty()) {

						// executing the last entered element of the queue
						runnable = runnablesQueue.get(0);
						runnablesQueue.remove(0);
						runnable.run();
						runnable = null;

					} else {

						// sleeping
						try {
//							getEditor().getLogger().log(getEditor(), LoggerAdapter.DEBUG, "IOManager没有可执行的队列，等待...");
							Thread.currentThread().sleep(100);
						} catch (Exception ex) {
						}
					}
				}
			}
		});
	}

	/**
	 * requests this runnable this be executed
	 * 
	 * @param runnable
	 *            a runnable
	 */
	public void requestExecution(Runnable runnable) {

		runnablesQueue.add(runnable);
	}

	/**
	 * @return the "file close" manager
	 */
	public FileClose getFileCloseManager() {
		return fileCloseManager;
	}

	/**
	 * @return the "file new" manager
	 */
	public FileNew getFileNewManager() {
		return fileNewManager;
	}

	/**
	 * @return the "file open" manager
	 */
	public FileOpen getFileOpenManager() {
		return fileOpenManager;
	}

	/**
	 * @return the "file save" manager
	 */
	public FileSave getFileSaveManager() {
		return fileSaveManager;
	}

	/**
	 * @return the "file print" manager
	 */
	public FilePrint getFilePrint() {
		return filePrint;
	}

	/**
	 * @return the "file export" manager
	 */
	public FileExport getFileExportManager() {
		return fileExport;
	}

	/**
	 * @return the "editor exit" manager
	 */
	public EditorExit getEditorExitManager() {
		return editorExitManager;
	}

	public EditorAdapter getEditor() {
		return editor;
	}
}
