package com.sjwi.meals.model;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

public class SqlQueryStore {
	private static final String SQL_CLASSPATH = "classpath:mysql.yaml";
	
	private Map<String,String> sqlQueries;

	@SuppressWarnings("unchecked")
	public SqlQueryStore() throws FileNotFoundException {
			sqlQueries = (Map<String,String>) new Yaml().load(new FileReader(ResourceUtils.getFile(SQL_CLASSPATH)));
	}
	
	public Map<String,String> getQueries() {
		return sqlQueries;
	}
}
