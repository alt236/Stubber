package uk.co.alt236.stubber.containers;
import java.lang.reflect.Modifier;
import java.util.List;

import uk.co.alt236.stubber.util.ReflectionUtils;
import uk.co.alt236.stubber.util.ReflectionUtils.ClassType;
import uk.co.alt236.stubber.util.ReflectionUtils.Exposure;


public class ClassWrapper implements Modifiers{
	private final Class<?> mClass;
	private final List<ConstructorWrapper> mConstructors;
	private final List<FieldWrapper> mFields;
	private final List<ClassWrapper> mInnerClasses;
	private final List<ClassWrapper> mInterfaces;
	private final List<MethodWrapper> mMethods;

	private final String mPackageName;

	public ClassWrapper(final Class<?> clazz){
		mClass = clazz;
		mFields = ReflectionUtils.getWrapper(mClass.getFields());
		mMethods = ReflectionUtils.getWrapper(mClass.getDeclaredMethods());
		mPackageName = ReflectionUtils.getPackageName(clazz);
		mInterfaces = ReflectionUtils.getWrapper(mClass.getInterfaces());
		mConstructors = ReflectionUtils.getWrapper(mClass.getDeclaredConstructors());

		//if(getInnerClasses){
			mInnerClasses = ReflectionUtils.getWrapper(mClass.getDeclaredClasses());
		//} else {
		//	mInnerClasses = new ArrayList<>();
		//}
	}

	public String getCanonicalName(){
		return mClass.getCanonicalName();
	}

	public List<ConstructorWrapper> getConstructors(){
		return mConstructors;
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

	public boolean isAnonymousClass(){
		return mClass.isAnonymousClass();
	}

	@Override
	public boolean isFinal(){
		return Modifier.isFinal(mClass.getModifiers());
	}

	public boolean isInnerClass(){
		if(isAnonymousClass()){
			return true;
		} else {
			return getCanonicalName().contains("$");
		}
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
