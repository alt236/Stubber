package uk.co.alt236.stubber.containers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import uk.co.alt236.stubber.util.ReflectionUtils;
import uk.co.alt236.stubber.util.ReflectionUtils.Exposure;

public class MethodWrapper implements Modifiers{
	private final Method mMember;

	public MethodWrapper(final Method member) {
		mMember = member;
	}

	public Method getEncapsulatedMember(){
		return mMember;
	}

	@Override
	public Exposure getExposure(){
		return ReflectionUtils.getExposure(mMember);
	}

	public String getName(){
		return mMember.getName();
	}

	public Class<?> getReturnType() {
		return mMember.getReturnType();
	}

	public String getReturnTypeAsString() {
		return ReflectionUtils.getSaneType(mMember.getReturnType());
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
