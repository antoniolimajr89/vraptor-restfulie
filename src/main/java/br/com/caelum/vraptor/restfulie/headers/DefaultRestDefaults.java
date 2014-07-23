package br.com.caelum.vraptor.restfulie.headers;

import java.util.Calendar;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaController;

@ApplicationScoped
public class DefaultRestDefaults implements RestDefaults {

	public String getEtagFor(HypermediaController controller) {
		return controller.hashCode() + "";
	}

	public Calendar getLastModifiedFor(HypermediaController controller) {
		return null;
	}

}
