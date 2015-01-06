package org.gutenberg.rdf;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Book {
  private final Node mediaNode;

  public Book(Node node) {
    mediaNode = node;
  }

  public String url() {
    return Rdf.getNodeAttr("rdf:about", mediaNode);
  }

  public Date modifiedDate() {
    java.util.Calendar date = javax.xml.bind.DatatypeConverter.parseDateTime(
        Rdf.getNodeValue("dcterms:modified", mediaNode.getChildNodes())
    );
    return date.getTime();
  }

  public int fileSize() {
    return Integer.parseInt(Rdf.getNodeValue("dcterms:extent", mediaNode.getChildNodes()));
  }

  public List<String> dataTypes() {
    List<Node> nodes = Rdf.getAllNodes("dcterms:format", mediaNode.getChildNodes());
    List<String> entries = new ArrayList<String>();

    for (Node node : nodes) {
      Node descriptionNode = Rdf.getNode("rdf:Description", node.getChildNodes());
      String value = Rdf.getNodeValue("rdf:value", descriptionNode.getChildNodes());

      entries.add(value);
    }
    return entries;
  }
}
