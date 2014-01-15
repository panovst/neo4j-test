package ru.test;

import ru.test.kladr.KladrNode;
import ru.test.kladr.NodeLabel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		Main main = new Main();
		try {
			Configuration cfg = main.loadConfiguration();

			Neo4jOperations operations = new Neo4jOperations(cfg);
/*
			operations.createNode(new KladrNode.Builder(NodeLabel.FEDERAL_REGION)
					.withName("Адыгея")
					.withSocr("Респ")
					.withKladrCode("0100000000000")
					.build());
			operations.createNode(new KladrNode.Builder(NodeLabel.FEDERAL_REGION)
					.withName("Башкортостан")
					.withSocr("Респ")
					.withKladrCode("0200000000000")
					.build());
			operations.createNode(new KladrNode.Builder(NodeLabel.FEDERAL_REGION)
					.withName("Бурятия")
					.withSocr("Респ")
					.withKladrCode("0300000000000")
					.build());
*/

			operations.createRelationship(new KladrNode.Builder(NodeLabel.FEDERAL_REGION)
					.withName("Адыгея")
					.withSocr("Респ")
					.withKladrCode("0100000000000")
					.build(), null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Configuration loadConfiguration() throws Exception {
		File settingsFile = new File(CONFIG_FILE_NAME);
		if ( !settingsFile.exists() ) {
			throw new Exception("The settings file not found: " + settingsFile.getAbsolutePath());
		}

		InputStream input = new BufferedInputStream(new FileInputStream(settingsFile));
		Properties settings = new Properties();
		settings.load(input);
		input.close();

		Configuration result = new Configuration();
		result.setDbPath(settings.getProperty("db.path"));
		return result;
	}

	private static final String CONFIG_FILE_NAME = "neo4j-test.properties";
}
