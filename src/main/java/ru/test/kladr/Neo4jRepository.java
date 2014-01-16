package ru.test.kladr;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.StringLogger;
import ru.test.Configuration;
import ru.test.application.command.KladrNodeCommand;
import ru.test.application.command.KladrRelationshipCommand;
import scala.collection.Iterator;

import java.util.List;

/**
 *  Низкоуровневые операции с базой данных
 */
public class Neo4jRepository {

	private GraphDatabaseService graphOperations;
	private ExecutionEngine engine;


	public Neo4jRepository(Configuration cfg) {
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

	public KladrNode addNode(KladrNodeCommand command) {
		KladrNode result;

		Transaction tx = graphOperations.beginTx();

		Node node = graphOperations.createNode(command.getLabel());
		if (command.getName() != null) {
			node.setProperty(KladrNode.NAME_PROPERTY, command.getName());
		}
		if (command.getSocr() != null) {
			node.setProperty(KladrNode.SOCR_PROPERTY, command.getSocr());
		}
		if (command.getKladrCode() != null) {
			node.setProperty(KladrNode.KLADR_CODE_PROPERTY, command.getKladrCode());
		}

		result = KladrNode.fromGraphNode(node);

		tx.success();
		tx.finish();

		return result;
	}

	public KladrRelationship addRelationship(KladrRelationshipCommand command) {
		KladrRelationship result;

		Transaction tx = graphOperations.beginTx();

		Preconditions.checkNotNull(command.getKladrCodeFrom(), "Не указан начальный узел!");
		Preconditions.checkNotNull(command.getKladrCodeTo(), "Не указан конечный узел!");
		Preconditions.checkNotNull(command.getRelationshipType(), "Не указан тип связи!");

		KladrNode from = findSingleNode(new Neo4jRepository.Filter()
				.withKladrCode(command.getKladrCodeFrom()));
		KladrNode to = findSingleNode(new Neo4jRepository.Filter()
				.withKladrCode(command.getKladrCodeTo()));

		Preconditions.checkNotNull(from, "Не удалось найти узел по КЛАДР коду! Код: " + command.getKladrCodeFrom());
		Preconditions.checkNotNull(to, "Не удалось найти узел по КЛАДР коду! Код: " + command.getKladrCodeTo());

		result = KladrRelationship.fromGraphRelationship(
				from.getSource().createRelationshipTo(to.getSource(), command.getRelationshipType()));

		tx.success();
		tx.finish();

		return result;
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

	/**
	 *	Поиск единственного узла по указанному критерию.
	 *  Если результатов несколько, то выбрасывается исключение
	 *
	 * @param filter - критерии поиска
	 * @return найденный узел
	 */
	public KladrNode findSingleNode(Neo4jRepository.Filter filter) {
		List<KladrNode> found = find(filter);
		Preconditions.checkArgument(found.size() <= 1, "Невалидное количество найденных данных! Expected: 0 or 1; found: " + found.size());
		if (!found.isEmpty()) {
			return found.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Создает запрос в синтаксисе языка Chypher
	 * @see <a>http://docs.neo4j.org/chunked/milestone/cypher-query-lang.html</a>
	 *
	 * @param filter - критерии поиска
	 * @return строковое представление запроса
	 */
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
