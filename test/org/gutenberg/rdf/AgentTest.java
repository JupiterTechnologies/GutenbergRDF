package org.gutenberg.rdf;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.List;

public class AgentTest {
  private Node agentNode;

  @Before
  public void generateAgentNode() {
    try {
      agentNode = DocumentBuilderFactory
          .newInstance()
          .newDocumentBuilder()
          .parse(new ByteArrayInputStream((
              "<dcterms:creator>" +
              "  <pgterms:agent rdf:about=\"2009/agents/4328\">" +
              "    <pgterms:name>Lawson, Thomas William</pgterms:name>" +
              "    <pgterms:webpage rdf:resource=\"http://en.wikipedia.org/wiki/Thomas_W._Lawson_(businessman)\"/>" +
              "    <pgterms:birthdate rdf:datatype=\"http://www.w3.org/2001/XMLSchema#integer\">1857</pgterms:birthdate>" +
              "    <pgterms:deathdate rdf:datatype=\"http://www.w3.org/2001/XMLSchema#integer\">1925</pgterms:deathdate>" +
              "    <pgterms:alias>Lawson, Thomas W.</pgterms:alias>" +
              "  </pgterms:agent>" +
              "</dcterms:creator>"
          ).getBytes()))
          .getDocumentElement();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void extractsId() {
    int id = new Agent(agentNode).id();

    assertThat(id, is(4328));
  }

  @Test
  public void extractsContributorsRole() {
    String role = new Agent(agentNode).role();

    assertThat(role, equalTo("aut"));
  }

  @Test
  public void extractsFirstName() {
    String name = new Agent(agentNode).firstName();

    assertThat(name, equalTo("Thomas William"));
  }

  @Test
  public void extractsLastName() {
    String name = new Agent(agentNode).lastName();

    assertThat(name, equalTo("Lawson"));
  }

  @Test
  public void extractsDateOfBirth() {
    int dob = new Agent(agentNode).birthDate();

    assertThat(dob, is(1857));
  }

  @Test
  public void extractsDateOfDeath() {
    int dod = new Agent(agentNode).deathDate();

    assertThat(dod, is(1925));
  }

  @Test
  public void extractsWebpage() {
    String url = new Agent(agentNode).webpage();

    assertThat(url, equalTo("http://en.wikipedia.org/wiki/Thomas_W._Lawson_(businessman)"));
  }

  @Test
  public void expectsCorrectNumberOfAliases() {
    List aliases = new Agent(agentNode).aliases();

    assertThat(aliases.size(), is(1));
  }
  @Test
  public void expectsAliasWithCorrectName() {
    Agent agent = new Agent(agentNode);

    String firstAlias = agent.aliases().get(0).toString();

    assertThat(firstAlias, equalTo("Lawson, Thomas W."));
  }

}