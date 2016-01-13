package com.dl;

import com.dl.module.IModule;
import com.dl.module.ModuleManager;
import com.dl.module.impl.PrintModule;
import com.dl.watch.ClassChangeDetector;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		// ����IModule������ModuleClassLoader��parent class loader ���أ�
		IModule module = new PrintModule();
		ModuleManager mgr = new ModuleManager();
		mgr.put(module);
		
		String clazz = PrintModule.class.getName();
		ClassChangeDetector detector = new ClassChangeDetector("ClassChangeDetectThread", clazz);
		detector.addListener(mgr);
		detector.start();
		
		while(true) {
			mgr.get(clazz).process();
			
			Thread.sleep(1000);
		}
	}
}
