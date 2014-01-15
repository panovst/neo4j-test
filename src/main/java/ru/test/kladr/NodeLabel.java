package ru.test.kladr;

import org.neo4j.graphdb.Label;

/**
 *	Классификация узлов по группам
 */
public enum NodeLabel implements Label{
	FEDERAL_REGION,
	REGION,
	CITY,
	TOWNSHIP,
	STREET,
	HOUSE
}
