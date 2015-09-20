package uk.co.alt236.stubber;

public final class StubThis {

    private StubThis() {
    }

    public static void main(final String[] args) {
        final Stubber stubber = new Stubber.Builder()
                .setDependencyDirectory("/home/alex/vcs/git/Stubber/input/dependencies/")
                .setOutputDir("/home/alex/vcs/git/Stubber/input/export/")
                .setOutputTemplateDir("/home/alex/vcs/git/Stubber/templates/")
                .setTargetJar("/home/alex/vcs/git/Stubber/input/target.jar")
                .setBlowOnReturn(true)
                .build();

        stubber.stubIt();
    }
}
