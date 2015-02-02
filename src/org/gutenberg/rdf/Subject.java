package org.gutenberg.rdf;

import org.w3c.dom.Node;

public class Subject {
  private final Node subjectNode;

  public Subject(Node node) {
    subjectNode = node;
  }

  public String heading() {
    return Rdf.getNodeValue("rdf:value", descriptionNode().getChildNodes());
  }

  public String type() {
    String[] resources = uri().split("/");
    return resources[resources.length - 1].toUpperCase();
  }

  public String uri() {
    Node node = Rdf.getNode("dcam:memberOf", descriptionNode().getChildNodes());
    return Rdf.getNodeAttr("rdf:resource", node);
  }

  private final Node descriptionNode() {
    return Rdf.getNode("rdf:Description", subjectNode.getChildNodes());
  }

}
