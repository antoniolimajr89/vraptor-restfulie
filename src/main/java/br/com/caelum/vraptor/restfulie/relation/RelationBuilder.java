package br.com.caelum.vraptor.restfulie.relation;

import java.util.List;

public interface RelationBuilder {

	/**
	 * Creates a named relation
	 */
	WithName relation(String name);

	interface WithName {
		/**
		 * Uses given controller method as uri
		 */
		<T> T uses(Class<T> controller);

		/**
		 * uses given uri for the relation
		 */
		void at(String uri);
	}

	/**
	 * adds a relation
	 * @param relation
	 */
	void add(Relation relation);

	/**
	 * Creates a relation using default name
	 */
	<T> T relation(Class<T> type);

	List<Relation> getRelations();

}
