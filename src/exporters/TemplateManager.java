package exporters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import util.Constants;
import util.FileIo;

public class TemplateManager {
	private final Map<String, String> mTemplateMap = new HashMap<>();

	public TemplateManager(String basePath){
		final Set<String> templateNames = new TreeSet<>();
		templateNames.add(Constants.TEMPLATE_NAME_CLASS_FILE);
		templateNames.add(Constants.TEMPLATE_NAME_INNER_CLASS);

		for(final String templateName : templateNames){
			try {
				final String template = FileIo.readFileAsString(basePath + templateName);
				mTemplateMap.put(templateName, template);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public String getTemplate(String key){
		return mTemplateMap.get(key);
	}
}