package uk.co.alt236.stubber.containers;

import uk.co.alt236.stubber.util.ReflectionUtils;
import uk.co.alt236.stubber.util.ReflectionUtils.Exposure;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class ConstructorWrapper implements Modifiers {

  private final Constructor<?> mMember;

  /* package */ ConstructorWrapper(final Constructor<?> member) {
    mMember = member;
  }

  public Constructor<?> getEncapsulatedMember() {
    return mMember;
  }

  @Override
  public Exposure getExposure() {
    return ReflectionUtils.getExposure(mMember);
  }

  public String getName() {
    return mMember.getName();
  }

  @Override
  public boolean isAbstract() {
    return Modifier.isAbstract(mMember.getModifiers());
  }

  @Override
  public boolean isFinal() {
    return Modifier.isFinal(mMember.getModifiers());
  }

  @Override
  public boolean isStatic() {
    return Modifier.isStatic(mMember.getModifiers());
  }

  @Override
  public boolean isStaticFinal() {
    return isStatic() && isFinal();
  }

  @Override
  public boolean isSynchronized() {
    return Modifier.isSynchronized(mMember.getModifiers());
  }

  @Override
  public boolean isTransient() {
    return Modifier.isTransient(mMember.getModifiers());
  }

  @Override
  public boolean isVolatile() {
    return Modifier.isVolatile(mMember.getModifiers());
  }
}
