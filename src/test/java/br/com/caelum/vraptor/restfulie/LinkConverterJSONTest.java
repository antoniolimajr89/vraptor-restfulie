package br.com.caelum.vraptor.restfulie;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.caelum.vraptor.config.Configuration;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaController;
import br.com.caelum.vraptor.restfulie.relation.Relation;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;
import br.com.caelum.vraptor.restfulie.serialization.LinkConverterJSON;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

public class LinkConverterJSONTest {

	private @Mock Restfulie restfulie;
	private @Mock RelationBuilder builder;
	private @Mock HypermediaController controller;


	private XStream xstream;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.restfulie = mock(Restfulie.class);
		when(restfulie.newRelationBuilder()).thenReturn(builder);

		Configuration config = mock(Configuration.class);
		when(config.getApplicationPath()).thenReturn("http://www.caelum.com.br");

		xstream = new XStream(new JsonHierarchicalStreamDriver());
		ReflectionConverter base = new ReflectionConverter(xstream.getMapper(), xstream.getReflectionProvider());

		xstream.registerConverter(new LinkConverterJSON(base, restfulie, config));
	}

	@Test
	public void shouldSerializeNoLinksIfThereIsNoTransition() {
		String json = xstream.toXML(controller);
		assertThat(json, not(containsString("links")));
	}

	@Test
	public void shouldSerializeOneLinkIfThereIsATransition() {
		Relation kill = mock(Relation.class);
		when(kill.getName()).thenReturn("kill");
		when(kill.getUri()).thenReturn("/kill");

		when(builder.getRelations()).thenReturn(Arrays.asList(kill));
		String json = xstream.toXML(controller);
		String expectedLinks = "\"links\": [\n    {\n      \"rel\": \"kill\",\n      \"href\": \"http://www.caelum.com.br/kill\"\n    }\n  ]";
		assertThat(json, containsString(expectedLinks));
	}

	@Test
	public void shouldSerializeAllLinksIfThereAreTransitions() {
		Relation kill = mock(Relation.class);
		when(kill.getName()).thenReturn("kill");
		when(kill.getUri()).thenReturn("/kill");

		Relation ressurect = mock(Relation.class);
		when(ressurect.getName()).thenReturn("ressurect");
		when(ressurect.getUri()).thenReturn("/ressurect");

		when(builder.getRelations()).thenReturn(Arrays.asList(kill, ressurect));
		String json = xstream.toXML(controller);
		String expectedLinks = "\"links\": [\n    {\n      \"rel\": \"kill\",\n      \"href\": \"http://www.caelum.com.br/kill\"\n    },\n    {\n      \"rel\": \"ressurect\",\n      \"href\": \"http://www.caelum.com.br/ressurect\"\n    }\n  ]";
		assertThat(json, containsString(expectedLinks));
	}
}
