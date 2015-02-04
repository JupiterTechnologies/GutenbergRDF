package org.gutenberg.rdf;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.w3c.dom.*;

public class ManifestationTest {
  private Node formatNode;

  @Before
  public void generateFormatNode() {
    try {
      formatNode = javax.xml.parsers.DocumentBuilderFactory
          .newInstance()
          .newDocumentBuilder()
          .parse(new java.io.ByteArrayInputStream((
              "<dcterms:hasFormat>\n" +
              "  <pgterms:file rdf:about=\"http://www.gutenberg.org/files/12345/12345.txt\">\n" +
              "    <dcterms:isFormatOf rdf:resource=\"ebooks/12345\"/>\n" +
              "    <dcterms:format>\n" +
              "      <rdf:Description rdf:nodeID=\"N2a7661877b964cd8b6672997b42599a4\">\n" +
              "        <rdf:value rdf:datatype=\"http://purl.org/dc/terms/IMT\">text/plain; charset=us-ascii</rdf:value>\n" +
              "        <dcam:memberOf rdf:resource=\"http://purl.org/dc/terms/IMT\"/>\n" +
              "      </rdf:Description>\n" +
              "    </dcterms:format>\n" +
              "    <dcterms:modified rdf:datatype=\"http://www.w3.org/2001/XMLSchema#dateTime\">2004-05-20T09:56:30</dcterms:modified>\n" +
              "    <dcterms:extent rdf:datatype=\"http://www.w3.org/2001/XMLSchema#integer\">281779</dcterms:extent>\n" +
              "  </pgterms:file>\n" +
              "</dcterms:hasFormat>"
          ).getBytes()))
          .getDocumentElement();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void urlIsReturnedAsString() {
    String url = new Manifestation(formatNode).url();

    assertThat(url, equalTo("http://www.gutenberg.org/files/12345/12345.txt"));
  }

  @Test
  public void extractsModifiedDate() {
    java.util.Date date = new Manifestation(formatNode).modifiedDate();

    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

    assertThat(sdf.format(date), equalTo("2004-05-20"));
  }

  @Test
  public void fileSizeReturnedAsInteger() {
    int fileSize = new Manifestation(formatNode).fileSize();

    assertThat(fileSize, is(281779));
  }

  @Test
  public void hasCorrectNumberOfDataTypes() {
    java.util.List types = new Manifestation(formatNode).dataTypes();

    assertThat(types.size(), is(1));
  }
  @Test
  public void hasDataTypeWithCorrectValue() {
    Manifestation manifestation = new Manifestation(formatNode);

    String firstType = manifestation.dataTypes().get(0).toString();

    assertThat(firstType, equalTo("text/plain; charset=us-ascii"));
  }

}

