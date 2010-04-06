package com.nci.svg.svgeditor.io.managers.monitor;

import java.awt.Component;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

import fr.itris.glips.library.monitor.Monitor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class NCISaveToServerMonitor extends Monitor {

	/**
	 * the labels
	 */
	private String titleLabel = "", messageLabel = "", xmlGenerationLabel = "",
			writingFileLabel = "";

	public NCISaveToServerMonitor(Component parent,
			JComponent relativeComponent, int min, int max) {
		super(parent, relativeComponent, min, max);
		initialize();
	}

	@Override
	protected void initialize() {

		this.waitDialog.getCancelButton().setVisible(false);
		JProgressBar progressBar = new JProgressBar();
		this.waitDialog.setProgressBar(progressBar);
		this.waitDialog.setSize(300, 150);
		this.waitDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		progressBar.setIndeterminate(true);
		// getting the labels
		titleLabel = ResourcesManager.bundle
				.getString("nciSaveToServerMonitorTitle");
		messageLabel = ResourcesManager.bundle
				.getString("nciSaveToServerMonitorMessage");
		// xmlGenerationLabel=ResourcesManager.bundle.getString("SaveMonitorXMLGenerationMessage");
		// writingFileLabel=ResourcesManager.bundle.getString("SaveMonitorWritingFileMessage");

		// setting the labels for the dialog
		waitDialog.setTitleMessage(titleLabel);
		waitDialog.setMessage(messageLabel);
	}

	@Override
	public void setIndeterminate(final boolean indeterminate) {

		setProgressMessage(writingFileLabel);
		super.setIndeterminate(indeterminate);
	}

	@Override
	public void start() {

		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				if (!isStopped) {

					enqueue(new Runnable() {

						public void run() {

							if (!isStopped) {

								waitDialog.showDialog(relativeComponent,
										isInError);
							}
						}
					});
				}
			}
		};

		timer.schedule(task, 10);
	}

}
