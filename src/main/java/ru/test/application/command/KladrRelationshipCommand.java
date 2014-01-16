package ru.test.application.command;

import ru.test.kladr.KladrRelationshipType;

/**
 * 	Представление объекта-связи двух узлов во внешнем запросе
 */
public class KladrRelationshipCommand {
	private String kladrCodeFrom;
	private String kladrCodeTo;
	private KladrRelationshipType relationshipType;

	public KladrRelationshipCommand() {
	}

	public KladrRelationshipCommand(String kladrCodeFrom, String kladrCodeTo, KladrRelationshipType relationshipType) {
		this.kladrCodeFrom = kladrCodeFrom;
		this.kladrCodeTo = kladrCodeTo;
		this.relationshipType = relationshipType;
	}

	public String getKladrCodeFrom() {
		return kladrCodeFrom;
	}

	public void setKladrCodeFrom(String kladrCodeFrom) {
		this.kladrCodeFrom = kladrCodeFrom;
	}

	public String getKladrCodeTo() {
		return kladrCodeTo;
	}

	public void setKladrCodeTo(String kladrCodeTo) {
		this.kladrCodeTo = kladrCodeTo;
	}

	public KladrRelationshipType getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(KladrRelationshipType relationshipType) {
		this.relationshipType = relationshipType;
	}

}
