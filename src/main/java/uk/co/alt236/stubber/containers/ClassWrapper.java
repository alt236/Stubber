package uk.co.alt236.stubber.containers;

import uk.co.alt236.stubber.util.ReflectionUtils;
import uk.co.alt236.stubber.util.ReflectionUtils.ClassType;
import uk.co.alt236.stubber.util.ReflectionUtils.Exposure;

import java.lang.reflect.Modifier;
import java.util.List;


public class ClassWrapper implements Modifiers {

  private final Class<?> mClass;
  private final List<ConstructorWrapper> mConstructors;
  private final List<FieldWrapper> mFields;
  private final List<ClassWrapper> mInnerClasses;
  private final List<ClassWrapper> mInterfaces;
  private final List<MethodWrapper> mMethods;

  private final String mPackageName;

  public ClassWrapper(final Class<?> clazz) {
    System.out.println("\t\t\t Creating '" + clazz + "'");
    mClass = clazz;
    mFields = WrapperFactory.getWrapper(mClass.getFields());
    mMethods = WrapperFactory.getWrapper(mClass.getDeclaredMethods());
    mPackageName = ReflectionUtils.getPackageName(clazz);
    mInterfaces = WrapperFactory.getWrapper(mClass.getInterfaces());
    mConstructors = WrapperFactory.getWrapper(mClass.getDeclaredConstructors());
    mInnerClasses = WrapperFactory.getWrapper(mClass.getDeclaredClasses());
  }

  public String getCanonicalName() {
    return mClass.getCanonicalName();
  }

  public List<ConstructorWrapper> getConstructors() {
    return mConstructors;
  }

  @Override
  public Exposure getExposure() {
    return ReflectionUtils.getExposure(mClass);
  }

  public List<FieldWrapper> getFields() {
    return mFields;
  }

  public List<ClassWrapper> getInnerClasses() {
    return mInnerClasses;
  }

  public List<ClassWrapper> getInterfaces() {
    return mInterfaces;
  }

  public List<MethodWrapper> getMethods() {
    return mMethods;
  }

  public String getPackageName() {
    return mPackageName;
  }

  public String getSimpleName() {
    return mClass.getSimpleName();
  }

  public ClassWrapper getSuperClass() {
    final Class<?> clazz = mClass.getSuperclass();
    if (clazz == null) {
      return null;
    } else {
      return new ClassWrapper(clazz);
    }
  }

  public ClassType getType() {
    return ReflectionUtils.getClassType(mClass);
  }

  @Override
  public boolean isAbstract() {
    return Modifier.isAbstract(mClass.getModifiers());
  }

  public boolean isAnonymousClass() {
    return mClass.isAnonymousClass();
  }

  @Override
  public boolean isFinal() {
    return Modifier.isFinal(mClass.getModifiers());
  }

  public boolean isInnerClass() {
    if (isAnonymousClass()) {
      return true;
    } else {
      return getCanonicalName().contains("$");
    }
  }

  @Override
  public boolean isStatic() {
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
