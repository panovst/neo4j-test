package ru.test;

import ru.test.application.command.KladrNodeCommand;
import ru.test.application.command.KladrRelationshipCommand;
import ru.test.kladr.KladrNode;
import ru.test.kladr.KladrRelationship;
import ru.test.kladr.Neo4jRepository;

/**
 *	Обертка над neo4j api
 */
public class Neo4jOperations {

	private final Neo4jRepository nodeRepository;

	public Neo4jOperations(Configuration cfg) {
		nodeRepository = new Neo4jRepository(cfg);
	}

	public KladrNode createNode(KladrNodeCommand command) {
		return nodeRepository.addNode(command);
	}

	public KladrRelationship createRelationship(KladrRelationshipCommand command) {
		return nodeRepository.addRelationship(command);
	}

}
