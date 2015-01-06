package org.gutenberg.rdf;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Agent {
  private String agentRole;
  private final Node agentNode;

  public Agent(Node node) {
    setRole(node);
    this.agentNode = Rdf.getNode("pgterms:agent", node.getChildNodes());
  }

  public int id() {
    String[] about = Rdf.getNodeAttr("rdf:about", agentNode).split("/");
    return Integer.parseInt(about[about.length - 1]);
  }

  public String role() {
    return agentRole;
  }

  public String firstName() {
    return (String) names().get(0);
  }

  public String lastName() {
    if (names().size() <= 1) {
      return "";
    } else {
      return (String) names().get(1);
    }
  }

  public int birthDate() {
    return extractYearFrom("pgterms:birthdate");
  }

  public int deathDate() {
    return extractYearFrom("pgterms:deathdate");
  }

  public String webpage() {
    Node node = Rdf.getNode("pgterms:webpage", agentNode.getChildNodes());
    if (node == null) return "";
    return Rdf.getNodeAttr("rdf:resource", node);
  }

  public List aliases() {
    List<Node> nodes = Rdf.getAllNodes("pgterms:alias", agentNode.getChildNodes());
    List<String> entries = new ArrayList<String>();

    for (Node node : nodes) {
      entries.add(node.getTextContent());
    }

    return entries;
  }


  private List names() {
    return reverseNames(Rdf.getNodeValue("pgterms:name", agentNode.getChildNodes()).split(", "));
  }

  private List reverseNames(String[] namesList) {
    List<String> list = Arrays.asList(namesList);
    Collections.reverse(list);
    return list;

  }

  private int extractYearFrom(String nodeName) {
    String node = Rdf.getNodeValue(nodeName, agentNode.getChildNodes());
    if (node.isEmpty()) return 0;
    return Integer.parseInt(node);
  }

  private void setRole(Node node) {
    String nodeName = node.getNodeName();
    if (nodeName.equals("dcterms:creator")) {
      this.agentRole = "aut";
    } else {
      this.agentRole = nodeName.split(":")[1];
    }
  }
}
