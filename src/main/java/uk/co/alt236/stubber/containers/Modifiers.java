package uk.co.alt236.stubber.containers;

import uk.co.alt236.stubber.util.ReflectionUtils.Exposure;


public interface Modifiers {

  public Exposure getExposure();

  public boolean isAbstract();

  public boolean isFinal();

  public boolean isStatic();

  public boolean isStaticFinal();

  public boolean isSynchronized();

  public boolean isTransient();

  public boolean isVolatile();
}
