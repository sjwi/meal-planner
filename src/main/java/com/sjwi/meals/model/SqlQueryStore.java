package com.sjwi.meals.model;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

@Component
public class SqlQueryStore {
	private static final String SQL_CLASSPATH = "classpath:mysql.yaml";
	
	private Map<String,String> sqlQueries;

	@SuppressWarnings("unchecked")
	public SqlQueryStore() throws FileNotFoundException {
			sqlQueries = (Map<String,String>) new Yaml().load(new FileReader(ResourceUtils.getFile(SQL_CLASSPATH)));
	}
	
	public String get(String key, Map<String, String> preferences) {
		String sort = Boolean.parseBoolean(preferences.get("pinFavorites"))?
			"FAVORITE DESC," + preferences.get("sort"):
			preferences.get("sort");

		return sqlQueries.get(key)
			.replace("{{SORT}}",sort)
			.replace("{{SORT_DIRECTION}}",preferences.get("sortDirection"));
	}
	public String get(String key){
		return sqlQueries.get(key);
	}
	public Map<String,String> getQueries() {
		return sqlQueries;
	}
}
