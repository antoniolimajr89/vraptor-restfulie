package br.com.caelum.vraptor.restfulie;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.restfulie.hypermedia.ConfigurableHypermediaResource;
import br.com.caelum.vraptor.restfulie.hypermedia.DefaultConfigurableHypermediaResource;
import br.com.caelum.vraptor.restfulie.relation.DefaultRelationBuilder;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

@ApplicationScoped
public class DefaultRestfulie implements Restfulie {

	private Proxifier proxifier;
	private Router router;

	/**
	 * @deprecated CDI eyes only
	 */
	public DefaultRestfulie() {
	}

	@Inject
	public DefaultRestfulie(Proxifier proxifier, Router router) {
		this.proxifier = proxifier;
		this.router = router;
	}

	public RelationBuilder newRelationBuilder() {
		return new DefaultRelationBuilder(router, proxifier);
	}

	public ConfigurableHypermediaResource enhance(Object object) {
		return new DefaultConfigurableHypermediaResource(newRelationBuilder(),
				object);
	}

}
