package com.common.utils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * 类反射工具
 * @author jianghaoming
 * @date 2016/10/12  11:25
 */
public final class ReflectionUtils {

	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 * @param object
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static final Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException {
		Field field = getDeclaredField(object, fieldName);
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 * @param object
	 * @param field field
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static final Object getFieldValue(Object object, Field field) throws NoSuchFieldException {
		//Assert.notNull(field);
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
	 * @param object
	 * @param fieldName
	 * @param value
	 * @throws NoSuchFieldException
	 */
	public static final void setFieldValue(Object object, String fieldName, Object value) throws NoSuchFieldException {
		Field field = getDeclaredField(object, fieldName);
		if (!field.isAccessible()) {
			field.setAccessible(true);
		}
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * @param object
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	public static final Field getDeclaredField(Object object, String fieldName) throws NoSuchFieldException {
		//Assert.notNull(object);
		return getDeclaredField(object.getClass(), fieldName);
	}

	/**
	 * 循环向上转型,获取类的DeclaredField.
	 * @param clazz
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public static final Field getDeclaredField(Class clazz, String fieldName) throws NoSuchFieldException {
		//Assert.notNull(clazz);
		//Assert.hasText(fieldName);
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上寻找
			}
		}
		throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + fieldName);
	}


	/**
	 * 获取所有的属性定义，包括父类的属性
	 * @param object
	 * @return
	 */
	public static Field[] getDeclaredField(Object object){
		List<Field> list = new ArrayList<Field>();
		Class<?> clazz = object.getClass() ;
		for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
			try {
				Field[] filed = clazz.getDeclaredFields();
				if( filed==null || filed.length==0 ) continue;
				for(int i=0;i<filed.length;i++){
					list.add(filed[i]);
				}
			} catch (Exception e) {
			}
		}
		Field[] result = new Field[list.size()];
		return list.toArray(result);
	}
	private ReflectionUtils(){}


}
