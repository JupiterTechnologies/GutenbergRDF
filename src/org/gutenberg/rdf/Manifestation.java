package org.gutenberg.rdf;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Manifestation {
  private final Node formatNode;

  public Manifestation(Node node) {
    formatNode = node;
  }

  public String url() {
    return Rdf.getNodeAttr("rdf:about", fileNode());
  }

  public Date modifiedDate() {
    java.util.Calendar date = javax.xml.bind.DatatypeConverter.parseDateTime(
        Rdf.getNodeValue("dcterms:modified", fileNode().getChildNodes())
    );
    return date.getTime();
  }

  public int fileSize() {
    return Integer.parseInt(Rdf.getNodeValue("dcterms:extent", fileNode().getChildNodes()));
  }

  public List<String> dataTypes() {
    List<Node> nodes = Rdf.getAllNodes("dcterms:format", fileNode().getChildNodes());
    List<String> entries = new ArrayList<String>();

    for (Node node : nodes) {
      Node descriptionNode = Rdf.getNode("rdf:Description", node.getChildNodes());
      String value = Rdf.getNodeValue("rdf:value", descriptionNode.getChildNodes());

      entries.add(value);
    }
    return entries;
  }

  private final Node fileNode() {
    return Rdf.getNode("pgterms:file", formatNode.getChildNodes());
  }

}
