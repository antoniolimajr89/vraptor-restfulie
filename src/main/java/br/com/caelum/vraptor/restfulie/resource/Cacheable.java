package br.com.caelum.vraptor.restfulie.resource;

public interface Cacheable {

	/**
	 * Returns the maximum number of seconds this resource can be cached for.
	 *
	 * @return
	 */
	int getMaximumAge();
	
}
