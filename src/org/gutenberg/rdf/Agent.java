/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Michael Cook
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author: Michael Cook
 * @version: 0.0.1
 */

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
