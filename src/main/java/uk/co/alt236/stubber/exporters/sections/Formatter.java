package uk.co.alt236.stubber.exporters.sections;

public interface Formatter<T> {

  String format(final Class declaringClass, final T input);
}
