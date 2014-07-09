package exporters;

import java.io.File;
import java.util.List;

import util.Constants;
import util.FileIo;
import util.ReflectionUtils.Exposure;
import containers.ClassWrapper;

public class ClassExporter {
	private final File mExportDirectory;
	private final TemplateManager mTemplateManager;

	public ClassExporter (String exportDir, String templateDirectory){
		mExportDirectory = new File(exportDir);
		mTemplateManager = new TemplateManager(templateDirectory);
	}

	private void cleanup(){
		if(mExportDirectory.exists()){
			try{
				FileIo.delete(mExportDirectory);
			} catch (Exception e){
				System.out.println(e.getMessage());
			}
		}
		mExportDirectory.mkdirs();

		if(!mExportDirectory.isDirectory()){
			mExportDirectory.delete();
			throw new IllegalStateException(mExportDirectory.getAbsolutePath() + " is not a directory!");
		}
	}

	public void export(List<ClassWrapper> classes){
		cleanup();
		for(final ClassWrapper clazz : classes){
			System.out.println(clazz.getName());
			writeToFile(clazz);
		}
	}

	private void writeToFile(ClassWrapper clazz) {
		if(!clazz.isInnerClass() && clazz.getExposure() == Exposure.PUBLIC){
			final File packagePath = new File(
					mExportDirectory,
					convertPackageToPath(clazz.getPackageName()));
			packagePath.mkdirs();

			final File classFile = new File(
					packagePath,
					clazz.getSimplename() + ".java");

			FileIo.writeToFile(
					classFile,
					getClassContent(
							mTemplateManager.getTemplate(Constants.TEMPLATE_NAME_CLASS_FILE),
							clazz),
							false,
							true);
		}
	}


	private String getClassContent(String template, ClassWrapper clazz){
		String result = template.replace(
				Constants.REP_TOKEN_PACKAGE,
				clazz.getPackageName() + ";");

		result = result.replace(
				Constants.REP_TOKEN_CLASS_DEFINITION,
				ClassExporterUtils.getClassDefinition(clazz));

		result = result.replace(
				Constants.REP_TOKEN_FIELDS,
				ClassExporterUtils.getFieldDefinition(clazz));

		result = result.replace(
				Constants.REP_TOKEN_METHODS,
				ClassExporterUtils.getMethods(clazz));

		if(clazz.getInnerClasses().size() > 0){
			for(final ClassWrapper inner : clazz.getInnerClasses()){
				if(inner.getExposure() == Exposure.PUBLIC){
					result = result.replace(
							Constants.REP_TOKEN_INNER_CLASSES,
							getClassContent(
									mTemplateManager.getTemplate(Constants.TEMPLATE_NAME_INNER_CLASS),
									inner));
				}
			}
		} else {
			result = result.replace(
					Constants.REP_TOKEN_INNER_CLASSES,
					"\t// NO INNER CLASSES!");
		}


		return result;
	}



	private static String convertPackageToPath(String packageName){
		return packageName.replace('.', File.separatorChar) + File.separatorChar;
	}

}
