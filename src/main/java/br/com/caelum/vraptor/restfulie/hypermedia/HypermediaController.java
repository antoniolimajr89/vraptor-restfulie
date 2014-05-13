package br.com.caelum.vraptor.restfulie.hypermedia;

import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

public interface HypermediaController {
	/**
	 * configures relations using given relation builder
	 * @param builder
	 */
	void configureRelations(RelationBuilder builder);
}
