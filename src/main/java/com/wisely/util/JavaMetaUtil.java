package com.wisely.util;

public class JavaMetaUtil {

	public static String getSimpleName(Class<?> clazz) {
		String simpleName = clazz.getSimpleName();
		if (clazz.isMemberClass()) {
			Class<?> enclosingClass = clazz.getEnclosingClass();
			simpleName = enclosingClass.getSimpleName() + "." + simpleName;
		}
		return simpleName;
	}
	
	public static String getPackagePath(Class cls) {
		String path = cls.getPackage().getName();				
		return path;
	}
}
