package ru.test;

import com.google.common.base.Preconditions;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.StringLogger;
import ru.test.kladr.KladrNode;
import ru.test.kladr.KladrRelationship;

import java.util.List;

/**
 *	Обертка над neo4j api
 */
public class Neo4jOperations {

	private final Configuration cfg;
	private final Neo4jRepository nodeRepository;
	private GraphDatabaseService graphRepository;
	private ExecutionEngine engine;

	public Neo4jOperations(Configuration cfg) {
		this.cfg = cfg;
/*
		Preconditions.checkNotNull(cfg.getDbPath(), "Не указан путь к базе данных");
		graphRepository = new GraphDatabaseFactory().newEmbeddedDatabase(cfg.getDbPath() );
		Runtime.getRuntime().addShutdownHook( new Thread()
		{
			@Override
			public void run()
			{
				graphRepository.shutdown();
			}
		} );
		engine = new ExecutionEngine( graphRepository , StringLogger.SYSTEM);
*/
		nodeRepository = new Neo4jRepository(cfg);
	}

	public void createNode(KladrNode command) {
		Transaction tx = graphRepository.beginTx();

		Node firstNode = graphRepository.createNode(command.getLabel());
		if (command.getName() != null) {
			firstNode.setProperty(KladrNode.NAME_PROPERTY, command.getName());
		}
		if (command.getSocr() != null) {
			firstNode.setProperty(KladrNode.SOCR_PROPERTY, command.getSocr());
		}
		if (command.getKladrCode() != null) {
			firstNode.setProperty(KladrNode.KLADR_CODE_PROPERTY, command.getKladrCode());
		}
		tx.success();
		tx.finish();
	}
	/*
		TODO переделать сигнатуру: добавить объект-команду
	 */
	public void createRelationship(KladrNode from, KladrNode to, KladrRelationship relationship) {
		List<KladrNode> found = nodeRepository.find(new Neo4jRepository.Filter()
				.withKladrCode(from.getKladrCode())
		);
		Preconditions.checkArgument(!found.isEmpty(), "По коду КЛАДР узел не найден! Код: " + from.getKladrCode());
		Preconditions.checkArgument(found.size() == 1, "Невалидное количество найденных данных! Expected: " + 1 + ", found: " + found.size());
		System.out.println(found);
	}

}
