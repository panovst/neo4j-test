package ru.test.kladr;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

/**
 *	Представление элемента КЛАДР в виде объекта-узла графа
 */
public class KladrNode {
	private Long id;

	private final NodeLabel label;
	private String name;
	private String socr;
	private String kladrCode;

	private Node source;

	private KladrNode(NodeLabel label) {
		this.label = label;
	}

	public NodeLabel getLabel() {
		return label;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getSocr() {
		return socr;
	}

	private void setSocr(String socr) {
		this.socr = socr;
	}

	public String getKladrCode() {
		return kladrCode;
	}

	private void setKladrCode(String kladrCode) {
		this.kladrCode = kladrCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	protected Node getSource() {
		return source;
	}

	protected void setSource(Node source) {
		this.source = source;
	}

	public static class Builder {
		private final KladrNode node;

		public Builder(NodeLabel label) {
			node = new KladrNode(label);
		}

		public Builder withName(String name) {
			node.setName(name);
			return this;
		}

		public Builder withSocr(String socr) {
			node.setSocr(socr);
			return this;
		}

		public Builder withKladrCode(String code) {
			node.setKladrCode(code);
			return this;
		}

		public KladrNode build() {
			return node;
		}
	}

	public static KladrNode fromGraphNode(Node node) {
		KladrNode result = null;
		if (node != null) {
			for (Label label : node.getLabels()) {
				result = new KladrNode(NodeLabel.valueOf(label.name()));
				break;
			}
			if (result != null) {
				result.setId(node.getId());
				result.setName(String.valueOf(node.getProperty(KladrNode.NAME_PROPERTY)));
				result.setSocr(String.valueOf(node.getProperty(KladrNode.SOCR_PROPERTY)));
				result.setKladrCode(String.valueOf(node.getProperty(KladrNode.KLADR_CODE_PROPERTY)));
				result.setSource(node);
			}
		}
		return result;
	}
	public static final String NAME_PROPERTY = "name";
	public static final String SOCR_PROPERTY = "socr";
	public static final String KLADR_CODE_PROPERTY = "kladrCode";
}
