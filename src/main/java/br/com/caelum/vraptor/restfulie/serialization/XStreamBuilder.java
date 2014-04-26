package br.com.caelum.vraptor.restfulie.serialization;

import com.thoughtworks.xstream.XStream;

public interface XStreamBuilder {

	public XStream xmlInstance();

	public XStream jsonInstance();

	public XStream configure(XStream xstream);

	public XStreamBuilder indented();

	public XStreamBuilder withoutRoot();

}
