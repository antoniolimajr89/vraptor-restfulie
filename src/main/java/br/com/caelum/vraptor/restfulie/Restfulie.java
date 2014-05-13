package br.com.caelum.vraptor.restfulie;

import br.com.caelum.vraptor.restfulie.hypermedia.ConfigurableHypermediaController;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

public interface Restfulie {

	/**
	 * Creates a new relation builder
	 */
	RelationBuilder newRelationBuilder();

	ConfigurableHypermediaController enhance(Object object);
}
