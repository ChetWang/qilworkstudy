package com.creaway.inmemdb.util;

import java.io.File;

public abstract class FileWatchDog extends Thread {

	protected FileWatchDog(String filename) {
		delay = 60000L;
		lastModif = new File(filename).lastModified();
		warnedAlready = false;
		interrupted = false;
		this.filename = filename;
		file = new File(filename);
		setDaemon(true);
		checkAndConfigure();
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	protected abstract void doOnChange();

	protected void checkAndConfigure() {
		boolean fileExists;
		try {
			fileExists = file.exists();
		} catch (SecurityException e) {
			DBLogger.log(DBLogger.WARN,
					"Was not allowed to read check file existance, file:["
							+ filename + "].", e);
			interrupted = true;
			return;
		}
		if (fileExists) {
			long l = file.lastModified();
			if (l > lastModif) {
				lastModif = l;
				doOnChange();
				warnedAlready = false;
			}
		} else if (!warnedAlready) {
			warnedAlready = true;
		}
	}

	public void run() {
		while (!interrupted) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
			checkAndConfigure();
		}
	}

	public static final long DEFAULT_DELAY = 60000L;
	protected String filename;
	protected long delay;
	File file;
	long lastModif;
	boolean warnedAlready;
	boolean interrupted;
}