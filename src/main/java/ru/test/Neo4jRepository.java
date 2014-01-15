package ru.test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.StringLogger;
import ru.test.kladr.KladrNode;
import scala.collection.Iterator;

import java.util.List;

/**
 *  Низкоуровневые операции с базой данных
 */
public class Neo4jRepository {

	private final Configuration cfg;
	private GraphDatabaseService graphOperations;
	private ExecutionEngine engine;


	public Neo4jRepository(Configuration cfg) {
		this.cfg = cfg;
		Preconditions.checkNotNull(cfg.getDbPath(), "Не указан путь к базе данных");
		graphOperations = new GraphDatabaseFactory().newEmbeddedDatabase(cfg.getDbPath() );
		Runtime.getRuntime().addShutdownHook( new Thread()
		{
			@Override
			public void run()
			{
				graphOperations.shutdown();
			}
		} );
		engine = new ExecutionEngine( graphOperations , StringLogger.SYSTEM);
	}

	public void addNode(KladrNode command) {
		Transaction tx = graphOperations.beginTx();

		Node firstNode = graphOperations.createNode(command.getLabel());
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

	public List<KladrNode> find(Filter filter) {
		List<KladrNode> result = Lists.newArrayList();

		Transaction tx = graphOperations.beginTx();
		ExecutionResult found = engine.execute(createCypherQuery(filter));
		Iterator<Object> iterator = found.columnAs("nodes");
		while(iterator.hasNext())
		{
			result.add(
					KladrNode.fromGraphNode((Node)iterator.next()));
		}
		tx.success();
		tx.finish();
		return result;
	}

	protected String createCypherQuery(Filter filter) {
		StringBuilder result = new StringBuilder("MATCH (nodes{ ");
		if (filter.getName() != null) {
			result.append(KladrNode.NAME_PROPERTY);
			result.append(":'");
			result.append(filter.getName());
			result.append("' ");
		}
		if (filter.getSocr() != null) {
			result.append(KladrNode.SOCR_PROPERTY);
			result.append(":'");
			result.append(filter.getSocr());
			result.append("' ");
		}
		if (filter.getKladrCode() != null) {
			result.append(KladrNode.KLADR_CODE_PROPERTY);
			result.append(":'");
			result.append(filter.getKladrCode());
			result.append("' ");
		}
		result.append("}) RETURN nodes");

		if (filter.getLimit() != null) {
			result.append(" LIMIT ");
			result.append(filter.getLimit());
		}
		return result.toString();
	}
	/*
		Критерии поиска узлов
	 */
	public static class Filter {
		private String name;
		private String socr;
		private String kladrCode;
		private Integer limit;

		public Filter withName(String name) {
			this.name = name;
			return this;
		}

		public Filter withSocr(String socr) {
			this.socr = socr;
			return this;
		}

		public Filter withKladrCode(String code) {
			this.kladrCode = code;
			return this;
		}

		public Filter withLimit(Integer limit) {
			this.limit = limit;
			return this;
		}

		public String getName() {
			return name;
		}

		public String getSocr() {
			return socr;
		}

		public String getKladrCode() {
			return kladrCode;
		}

		public Integer getLimit() {
			return limit;
		}
	}
}
