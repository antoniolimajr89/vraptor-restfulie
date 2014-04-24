package br.com.caelum.vraptor.restfulie.headers;

import java.util.Calendar;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.restfulie.RestHeadersHandler;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaResource;
import br.com.caelum.vraptor.restfulie.resource.Cacheable;
import br.com.caelum.vraptor.restfulie.resource.RestfulEntity;

public class DefaultRestHeadersHandler implements RestHeadersHandler {

	private HttpServletResponse response;
	private RestDefaults defaults;

	/**
	 * @deprecated CDI eyes only
	 */
	public DefaultRestHeadersHandler() {
	}

	@Inject
	public DefaultRestHeadersHandler(HttpServletResponse response,
			RestDefaults defaults) {
		this.defaults = defaults;
		this.response = response;
	}

	@Override
	public void handle(HypermediaResource resource) {
		// TODO implement link headers
		if (Cacheable.class.isAssignableFrom(resource.getClass())) {
			Cacheable cache = (Cacheable) resource;
			response.addHeader("Cache-control",
					"max-age=" + cache.getMaximumAge());
		}
		if (RestfulEntity.class.isInstance(resource)) {
			RestfulEntity entity = (RestfulEntity) resource;
			restfulHeadersFor(entity.getEtag(), entity.getLastModified());
		} else {
			restfulHeadersFor(defaults.getEtagFor(resource),
					defaults.getLastModifiedFor(resource));
		}

	}

	private void restfulHeadersFor(String etag, Calendar lastModified) {
		response.addHeader("ETag", etag);
		if (lastModified != null) {
			response.setDateHeader("Last-modified",
					lastModified.getTimeInMillis());
		}
	}
}
