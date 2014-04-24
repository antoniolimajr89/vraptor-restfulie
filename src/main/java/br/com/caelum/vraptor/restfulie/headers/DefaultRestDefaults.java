package br.com.caelum.vraptor.restfulie.headers;

import java.util.Calendar;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaResource;

@ApplicationScoped
public class DefaultRestDefaults implements RestDefaults {

	public String getEtagFor(HypermediaResource resource) {
		return resource.hashCode() + "";
	}

	public Calendar getLastModifiedFor(HypermediaResource resource) {
		return null;
	}

}
