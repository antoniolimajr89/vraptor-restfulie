package br.com.caelum.vraptor.restfulie;

import br.com.caelum.vraptor.restfulie.hypermedia.ConfigurableHypermediaResource;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

public interface Restfulie {

	/**
	 * Creates a new relation builder
	 */
	RelationBuilder newRelationBuilder();

	ConfigurableHypermediaResource enhance(Object object);
}
