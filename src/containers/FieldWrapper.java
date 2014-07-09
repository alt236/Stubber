package containers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import util.ReflectionUtils;
import util.ReflectionUtils.Exposure;

public class FieldWrapper implements Modifiers{
	private final Field mMember;

	public FieldWrapper(Field member) {
		mMember = member;
	}

	@Override
	public Exposure getExposure(){
		return ReflectionUtils.getExposure(mMember);
	}

	public Field getEncapsulatedMember(){
		return mMember;
	}

	public String getName(){
		return mMember.getName();
	}

	public String getTypeAsString(){
		return mMember.getType().getName();
	}

	@Override
	public boolean isAbstract() {
		return Modifier.isAbstract(mMember.getModifiers());
	}

	@Override
	public boolean isFinal(){
		return Modifier.isFinal(mMember.getModifiers());
	}

	@Override
	public boolean isStatic(){
		return Modifier.isStatic(mMember.getModifiers());
	}

	@Override
	public boolean isStaticFinal(){
		return isStatic() && isFinal();
	}

	@Override
	public boolean isSynchronized(){
		return Modifier.isSynchronized(mMember.getModifiers());
	}

	@Override
	public boolean isTransient(){
		return Modifier.isTransient(mMember.getModifiers());
	}

	@Override
	public boolean isVolatile(){
		return Modifier.isVolatile(mMember.getModifiers());
	}
}