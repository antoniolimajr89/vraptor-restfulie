package br.com.caelum.vraptor.restfulie.headers;

import java.util.Calendar;

import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaController;

public interface RestDefaults {

	Calendar getLastModifiedFor(HypermediaController controller);

	String getEtagFor(HypermediaController controller);

}
