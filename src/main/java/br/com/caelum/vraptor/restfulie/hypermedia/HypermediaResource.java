package br.com.caelum.vraptor.restfulie.hypermedia;

import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

public interface HypermediaResource {
	/**
	 * configures relations using given relation builder
	 * @param builder
	 */
	void configureRelations(RelationBuilder builder);
}
