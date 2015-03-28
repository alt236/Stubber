package uk.co.alt236.stubber.exporters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import uk.co.alt236.stubber.util.Constants;
import uk.co.alt236.stubber.util.FileIo;

public class TemplateManager {
	private final Map<String, String> mTemplateMap = new HashMap<>();

	public TemplateManager(final String basePath){
		final Set<String> templateNames = new TreeSet<>();
		templateNames.add(Constants.TEMPLATE_NAME_CLASS_FILE);
		templateNames.add(Constants.TEMPLATE_NAME_INNER_CLASS);

		for(final String templateName : templateNames){
			try {
				final String template = FileIo.readFileAsString(basePath + templateName);
				mTemplateMap.put(templateName, template);
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public String getTemplate(final String key){
		return mTemplateMap.get(key);
	}
}
