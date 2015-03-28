package uk.co.alt236.stubber.exporters;

import java.io.File;
import java.util.List;

import uk.co.alt236.stubber.template.ClassTemplate;
import uk.co.alt236.stubber.util.FileIo;
import uk.co.alt236.stubber.util.ReflectionUtils.Exposure;
import uk.co.alt236.stubber.containers.ClassWrapper;

public class ClassExporter {
	private final File mExportDirectory;
	private final ClassTemplate classTemplate;

	public ClassExporter (final String exportDir, final String templateDirectory){
		mExportDirectory = new File(exportDir);
		classTemplate = new ClassTemplate(templateDirectory);
	}

	private void cleanup(){
		if(mExportDirectory.exists()){
			try{
				FileIo.delete(mExportDirectory);
			} catch (final Exception e){
				System.out.println(e.getMessage());
			}
		}
		mExportDirectory.mkdirs();

		if(!mExportDirectory.isDirectory()){
			mExportDirectory.delete();
			throw new IllegalStateException(mExportDirectory.getAbsolutePath() + " is not a directory!");
		}
	}

	public void export(final List<ClassWrapper> classes){
		cleanup();
		long count = 0;
		for(final ClassWrapper clazz : classes){
			if(writeToFile(clazz)){
				count++;
			}
		}
		System.out.println("Files Written: " + count);
	}

	private boolean writeToFile(final ClassWrapper clazz) {
		final boolean isPublic = clazz.getExposure() == Exposure.PUBLIC;
		if(!isPublic){return false;}

		final boolean isInner = clazz.isInnerClass();
		final boolean isAnon = clazz.isAnonymousClass();

		System.out.println("About to export: Public: " + isPublic + ", Inner: " + isInner + ", Anon: " + isAnon + " -- '" + clazz.getCanonicalName() + "'");

		if(!isInner && !isAnon && isPublic){
			System.out.println("\tExporting: " + clazz.getCanonicalName());

			final File packagePath = new File(
					mExportDirectory,
					convertPackageToPath(clazz.getPackageName()));

			packagePath.mkdirs();

			final File classFile = new File(
					packagePath,
					clazz.getSimplename() + ".java");


			final String classContent = classTemplate.build(clazz);
			FileIo.writeToFile(
					classFile,
					classContent,
					false,
					true);
			return true;
		} else {
			return false;
		}
	}

	private static String convertPackageToPath(final String packageName){
		return packageName.replace('.', File.separatorChar) + File.separatorChar;
	}

}
