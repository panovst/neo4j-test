package ru.test.kladr;

import org.neo4j.graphdb.Relationship;

/**
 * Представляет собой связь между узлами
 */
public class KladrRelationship {

	private Long id;
	private KladrRelationshipType type;
	private KladrNode from;
	private KladrNode to;

	public KladrRelationshipType getType() {
		return type;
	}

	public void setType(KladrRelationshipType type) {
		this.type = type;
	}

	public KladrNode getFrom() {
		return from;
	}

	public void setFrom(KladrNode from) {
		this.from = from;
	}

	public KladrNode getTo() {
		return to;
	}

	public void setTo(KladrNode to) {
		this.to = to;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static KladrRelationship fromGraphRelationship(Relationship relationship) {
		KladrRelationship result = null;
		if (relationship != null) {
			result = new KladrRelationship();
			result.setId(relationship.getId());
			result.setType(KladrRelationshipType.valueOf(relationship.getType().name()));
			result.setFrom(KladrNode.fromGraphNode(relationship.getStartNode()));
			result.setTo(KladrNode.fromGraphNode(relationship.getEndNode()));
		}
		return result;
	}
}
