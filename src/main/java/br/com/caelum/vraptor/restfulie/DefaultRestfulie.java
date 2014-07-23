package br.com.caelum.vraptor.restfulie;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.restfulie.hypermedia.ConfigurableHypermediaController;
import br.com.caelum.vraptor.restfulie.hypermedia.DefaultConfigurableHypermediaController;
import br.com.caelum.vraptor.restfulie.relation.DefaultRelationBuilder;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

@ApplicationScoped
public class DefaultRestfulie implements Restfulie {

	private final Proxifier proxifier;
	private final Router router;

	/**
	 * @deprecated CDI eyes only
	 */
	protected DefaultRestfulie() {
		this(null, null);
	}

	@Inject
	public DefaultRestfulie(Proxifier proxifier, Router router) {
		this.proxifier = proxifier;
		this.router = router;
	}

	public RelationBuilder newRelationBuilder() {
		return new DefaultRelationBuilder(router, proxifier);
	}

	public ConfigurableHypermediaController enhance(Object object) {
		return new DefaultConfigurableHypermediaController(newRelationBuilder(),
				object);
	}

}
