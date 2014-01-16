package ru.test.kladr;

import org.neo4j.graphdb.RelationshipType;

/**
 *
 */
public enum KladrRelationshipType implements RelationshipType {
	/*
		Вышестоящий, по подчиненности, объект территориально-административного деления
	 */
	PARENT,
	/*
		Нижестоящий, по подчиненности, объект территориально-административного деления
	 */
	CHILD
}
