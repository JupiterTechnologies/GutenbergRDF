package org.gutenberg.rdf;

import org.junit.*;
import static org.junit.Assert.*;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

public class SubjectTest {
  private Node subjectNode;

  @Before
  public void generateSubjectNode() {
    try {
      subjectNode = DocumentBuilderFactory
          .newInstance()
          .newDocumentBuilder()
          .parse(new ByteArrayInputStream((
              "<dcterms:subject>" +
              "  <rdf:Description rdf:nodeID=\"Ncc082a3debfb4dfca3afc4c48c74bdae\">" +
              "    <dcam:memberOf rdf:resource=\"http://purl.org/dc/terms/LCSH\"/>" +
              "    <rdf:value>Fiction</rdf:value>" +
              "  </rdf:Description>" +
              "</dcterms:subject>"
          ).getBytes()))
          .getDocumentElement();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void subjectHeadingIsReturnedAsString() {
    String heading = new Subject(subjectNode).heading();

    assertEquals(heading, "Fiction");
  }

  @Test
  public void subjectTypeIsReturnedAsString() {
    String type = new Subject(subjectNode).type();

    assertEquals(type, "LCSH");
  }

  @Test
  public void subjectUriIsReturnedAsString() {
    String uri = new Subject(subjectNode).uri();

    assertEquals(uri, "http://purl.org/dc/terms/LCSH");
  }

}