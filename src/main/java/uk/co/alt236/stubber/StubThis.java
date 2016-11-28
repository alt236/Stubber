package uk.co.alt236.stubber;

public final class StubThis {

  private static final String BASE_DIR = "/home/alex/vcs/desktop-mine/";
  private StubThis() {
  }

  public static void main(final String[] args) {
    final Stubber stubber = new Stubber.Builder()
        .setDependencyDirectory(BASE_DIR + "Stubber/input/dependencies/")
        .setOutputDir(BASE_DIR + "Stubber/input/export/")
        .setOutputTemplateDir(BASE_DIR + "Stubber/templates/")
        .setTargetJar(BASE_DIR + "Stubber/input/target.jar")
        .setBlowOnReturn(true)
        .build();

    stubber.stubIt();
  }
}
