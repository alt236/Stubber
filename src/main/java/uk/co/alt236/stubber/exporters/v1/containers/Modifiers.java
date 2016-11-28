package uk.co.alt236.stubber.exporters.v1.containers;

import uk.co.alt236.stubber.util.ReflectionUtils.Exposure;


public interface Modifiers {

  Exposure getExposure();

  boolean isAbstract();

  boolean isFinal();

  boolean isStatic();

  boolean isStaticFinal();

  boolean isSynchronized();

  boolean isTransient();

  boolean isVolatile();
}
