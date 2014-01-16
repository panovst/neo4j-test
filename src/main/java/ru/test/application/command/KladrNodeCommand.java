package ru.test.application.command;

import ru.test.kladr.NodeLabel;

/**
 * 	Представление узла во внешнем запросе
 */
public class KladrNodeCommand {
	private NodeLabel label;
	private String name;
	private String socr;
	private String kladrCode;

	public NodeLabel getLabel() {
		return label;
	}

	public void setLabel(NodeLabel label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSocr() {
		return socr;
	}

	public void setSocr(String socr) {
		this.socr = socr;
	}

	public String getKladrCode() {
		return kladrCode;
	}

	public void setKladrCode(String kladrCode) {
		this.kladrCode = kladrCode;
	}

	public static class Builder {
		private final KladrNodeCommand command;

		public Builder(NodeLabel label) {
			command = new KladrNodeCommand();
			command.setLabel(label);
		}

		public Builder withName(String name) {
			command.setName(name);
			return this;
		}

		public Builder withSocr(String socr) {
			command.setSocr(socr);
			return this;
		}

		public Builder withKladrCode(String code) {
			command.setKladrCode(code);
			return this;
		}

		public KladrNodeCommand build() {
			return command;
		}
	}
}
