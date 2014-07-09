package containers;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import util.ReflectionUtils;
import util.ReflectionUtils.ClassType;
import util.ReflectionUtils.Exposure;


public class ClassWrapper implements Modifiers{
	private final Class<?> mClass;
	private final  List<FieldWrapper> mFields;
	private final  List<ClassWrapper> mInnerClasses;
	private final  List<ClassWrapper> mInterfaces;
	private final  List<MethodWrapper> mMethods;

	private String mPackageName;

	public ClassWrapper(Class<?> clazz){
		mClass = clazz;
		mFields = ReflectionUtils.getWrapper(mClass.getFields());
		mInnerClasses = new ArrayList<>();//.getWrapper(mClass.getDeclaredClasses());
		mInterfaces = ReflectionUtils.getWrapper(mClass.getInterfaces());
		mMethods = ReflectionUtils.getWrapper(mClass.getDeclaredMethods());
		mPackageName = ReflectionUtils.getPackageName(clazz);
	}

	@Override
	public Exposure getExposure(){
		return ReflectionUtils.getExposure(mClass);
	}

	public List<FieldWrapper> getFields() {
		return mFields;
	}

	public List<ClassWrapper> getInnerClasses(){
		return mInnerClasses;
	}

	public List<ClassWrapper> getInterfaces(){
		return mInterfaces;
	}

	public List<MethodWrapper> getMethods(){
		return mMethods;
	}

	public String getName(){
		return mClass.getCanonicalName();
	}

	public String getPackageName(){
		return mPackageName;
	}

	public String getSimplename(){
		return mClass.getSimpleName();
	}

	public ClassWrapper getSuperClass(){
		final Class<?> clazz = mClass.getSuperclass();
		if(clazz == null){
			return null;
		} else {
			return new ClassWrapper(clazz);
		}
	}

	public ClassType getType(){
		return ReflectionUtils.getClassType(mClass);
	}

	@Override
	public boolean isAbstract(){
		return Modifier.isAbstract(mClass.getModifiers());
	}

	@Override
	public boolean isFinal(){
		return Modifier.isFinal(mClass.getModifiers());
	}

	public boolean isInnerClass(){
		return getName().contains("$");
	}

	@Override
	public boolean isStatic(){
		return Modifier.isStatic(mClass.getModifiers());
	}

	@Override
	public boolean isStaticFinal() {
		return isStatic() && isFinal();
	}

	@Override
	public boolean isSynchronized() {
		return Modifier.isSynchronized(mClass.getModifiers());
	}

	@Override
	public boolean isTransient() {
		return Modifier.isTransient(mClass.getModifiers());
	}

	@Override
	public boolean isVolatile() {
		return Modifier.isVolatile(mClass.getModifiers());
	}
}
