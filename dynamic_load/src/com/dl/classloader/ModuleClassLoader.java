package com.dl.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dl.Constant;

/** module class loader ��δָ���Լ���parent class loader������ֱ����Ĭ�ϵ� system class loader */
public class ModuleClassLoader extends ClassLoader {

	@Override
	public Class<?> loadClass(String clazz) throws ClassNotFoundException {
		if(isModuleClass(clazz)) {
			File f = new File(Constant.REPOSITORY, clazz.replace('.', File.separatorChar)+".class");
			if(f.exists() && f.isFile()) {
				try(InputStream is = new FileInputStream(f);) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] tmp = new byte[256];
					int i = -1;
					while((i=is.read(tmp)) != -1) {
						baos.write(tmp, 0, i);
					}
					byte[] data = baos.toByteArray();
					return defineClass(clazz, data, 0, data.length);
				} catch (IOException e) {
					e.printStackTrace(); // Just print it, may be the parent class loader can load it.
				}
			}
		} 
		return super.loadClass(clazz);
	}
	
	private boolean isModuleClass(String clazz) {
		// ʵ�����Ӧ���������ж������������������Ҫ��̬���ص�Module�ŵ�һ��ͳһ��jar���������ж��ܷ��ڸ�jar�����ҵ�clazz
		return clazz.startsWith("com.dl.module.impl");
	}
	
}