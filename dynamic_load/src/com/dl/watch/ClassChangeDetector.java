package com.dl.watch;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.dl.Constant;

public class ClassChangeDetector extends Thread {

	private final String clazz;
	
	public ClassChangeDetector(String daemonThreadName, String clazz) {
		super(daemonThreadName);
		this.clazz = clazz;
		setDaemon(true);
	}
	
	List<IClassChangeListener> listeners = new LinkedList<IClassChangeListener>();
	
	public void addListener(IClassChangeListener listener) {
		listeners.add(listener);
	}
	
	public void fireEvent() {
		for(IClassChangeListener l : listeners) {
			l.onClassChange(clazz);
		}
	}
	
	@Override
	public void run() {
		File f = new File(Constant.REPOSITORY, clazz.replace('.', File.separatorChar)+".class");
		if(!f.exists() || !f.isFile()) {
			return;
		}
		
		long lastChangeTime = -1;
		while(true) {
			long changeTime = f.lastModified();
			
			if(lastChangeTime == -1) {
				lastChangeTime = changeTime;
			}
			
			if(changeTime > lastChangeTime) {
				fireEvent();
				lastChangeTime = changeTime;
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
