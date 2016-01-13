package com.dl.module;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dl.classloader.ModuleClassLoader;
import com.dl.watch.IClassChangeListener;

public class ModuleManager implements IClassChangeListener {

	private Map<String, IModule> modules = new ConcurrentHashMap<String, IModule>();
	
	@Override
	public void onClassChange(String clazz) {
		ModuleClassLoader mcl = new ModuleClassLoader(); // 新建命名空间
		try {
			IModule module = (IModule) mcl.loadClass(clazz).newInstance();
			put(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public IModule get(String clazz) {
		return modules.get(clazz);
	}
	
	public void put(IModule module) {
		modules.put(module.getClass().getName(), module);
	}
}
