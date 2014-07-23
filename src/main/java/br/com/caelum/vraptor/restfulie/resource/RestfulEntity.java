package br.com.caelum.vraptor.restfulie.resource;

import java.util.Calendar;

public interface RestfulEntity extends Cacheable {

	String getEtag();

	Calendar getLastModified();
}
