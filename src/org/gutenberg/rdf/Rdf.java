package org.gutenberg.rdf;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Rdf {
  private NodeList docRoot;

  public Rdf(String filename) {
    try {
      DOMParser parser = new DOMParser();
      parser.parse(filename);
      Document doc = parser.getDocument();
      docRoot = doc.getChildNodes();
    } catch (Exception e) {
      System.out.println("Error initializing Parser\n\n");
      e.printStackTrace();
    }
  }

  public int ebookID() {
    String[] about = getNodeAttr("rdf:about", ebookNode()).split("/");
    return Integer.parseInt(about[about.length - 1]);
  }

  public String type() {
    Node typeNode = getNode("dcterms:type", ebookNode().getChildNodes());
    Node descNode = getNode("rdf:Description", typeNode.getChildNodes());
    return getNodeValue("rdf:value", descNode.getChildNodes());
  }

  public String title() {
    return titles()[0];
  }

  public String subtitle() {
    if (titles().length == 1) return "";

    List<String> subsArray = new ArrayList<String>();
    for (int i = 1; i < titles().length; i++) {
      subsArray.add(titles()[i]);
    }

    return com.sun.deploy.util.StringUtils.join(subsArray, " - ");
  }

  public List<Agent> authors() {
    List<Node> nodes = getAllNodes("dcterms:creator", ebookNode().getChildNodes());
    List<Agent> creatorsArray = new ArrayList<Agent>();

    for (Node node : nodes) {
      creatorsArray.add(new Agent(node));
    }

    return creatorsArray;
  }

  public List<Agent> contributors() {
    List<Node> nodes = getContributorNodes();
    List<Agent> contributorsArray = new ArrayList<Agent>();

    for (Node node : nodes) {
      contributorsArray.add(new Agent(node));
    }

    return contributorsArray;
  }

  public Date releaseDate() {
    String issued = getNodeValue("dcterms:issued", ebookNode().getChildNodes());

    Date date;
    java.text.DateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US);
    try {
      date = formatter.parse(issued);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    return date;
  }

  public String language() {
    Node typeNode = getNode("dcterms:language", ebookNode().getChildNodes());
    Node descNode = getNode("rdf:Description", typeNode.getChildNodes());
    return getNodeValue("rdf:value", descNode.getChildNodes());
  }

  public String description() {
    String node = getNodeValue("dcterms:description", ebookNode().getChildNodes());
    if (node == null) return "";
    return node.replaceAll("[\r\n]+", " ");
  }

  public String publisher() {
    String node = getNodeValue("dcterms:publisher", ebookNode().getChildNodes());
    if (node == null) return "";
    return node.replaceAll("[\r\n]+", " ");
  }

  public String rights() {
    String node = getNodeValue("dcterms:rights", ebookNode().getChildNodes());
    if (node == null) return "";
    return node.replaceAll("[\r\n]+", " ");
  }

  public int downloads() {
    String node = getNodeValue("pgterms:downloads", ebookNode().getChildNodes());
    if (node == null) return 0;
    return Integer.parseInt(node);
  }

  public List<Book> ebookFormats() {
    List<Node> nodes = getAllNodes("dcterms:hasFormat", ebookNode().getChildNodes());
    List<Book> formatsArray = new ArrayList<Book>();

    for (Node node : nodes) {
      Node formatNode = getNode("pgterms:file", node.getChildNodes());
      formatsArray.add(new Book(formatNode));
    }

    return formatsArray;
  }

  public List<Subject> subjects() {
    List<Node> nodes = getAllNodes("dcterms:subject", ebookNode().getChildNodes());
    List<Subject> subjectsArray = new ArrayList<Subject>();

    for (Node node : nodes) {
      subjectsArray.add(new Subject(node));
    }

    return subjectsArray;
  }


  private String[] titles() {
    return getNode("dcterms:title", ebookNode().getChildNodes()).getTextContent().split("[\r\n]+");
  }

  private List<Node> getContributorNodes() {
    NodeList nodes = ebookNode().getChildNodes();
    List<Node> entries = new ArrayList<Node>();

    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().split(":")[0].equals("marcrel")) {
        entries.add(node);
      }
    }
    return entries;
  }

  Node rdfNode() {
    return getNode("rdf:RDF", docRoot);
  }

  Node ebookNode() {
    return getNode("pgterms:ebook", rdfNode().getChildNodes());
  }

  static List<Node> getAllNodes(String tagName, NodeList nodes) {
    List<Node> entries = new ArrayList<Node>();

    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeName().equalsIgnoreCase(tagName)) {
        entries.add(node);
      }
    }

    return entries;
  }

  // The following helper methods we're taken from:
  // http://www.drdobbs.com/jvm/easy-dom-parsing-in-java/231002580
  static Node getNode(String tagName, NodeList nodes) {
    for (int x = 0; x < nodes.getLength(); x++) {
      Node node = nodes.item(x);
      if (node.getNodeName().equalsIgnoreCase(tagName)) {
        return node;
      }
    }

    return null;
  }

  protected static String getNodeValue(Node node) {
    NodeList childNodes = node.getChildNodes();
    for (int x = 0; x < childNodes.getLength(); x++) {
      Node data = childNodes.item(x);
      if (data.getNodeType() == Node.TEXT_NODE)
        return data.getNodeValue();
    }
    return "";
  }

  static String getNodeValue(String tagName, NodeList nodes) {
    for (int x = 0; x < nodes.getLength(); x++) {
      Node node = nodes.item(x);
      if (node.getNodeName().equalsIgnoreCase(tagName)) {
        NodeList childNodes = node.getChildNodes();
        for (int y = 0; y < childNodes.getLength(); y++) {
          Node data = childNodes.item(y);
          if (data.getNodeType() == Node.TEXT_NODE)
            return data.getNodeValue();
        }
      }
    }
    return "";
  }

  static String getNodeAttr(String attrName, Node node) {
    NamedNodeMap attrs = node.getAttributes();
    for (int y = 0; y < attrs.getLength(); y++) {
      Node attr = attrs.item(y);
      if (attr.getNodeName().equalsIgnoreCase(attrName)) {
        return attr.getNodeValue();
      }
    }
    return "";
  }

  protected static String getNodeAttr(String tagName, String attrName, NodeList nodes) {
    for (int x = 0; x < nodes.getLength(); x++) {
      Node node = nodes.item(x);
      if (node.getNodeName().equalsIgnoreCase(tagName)) {
        NodeList childNodes = node.getChildNodes();
        for (int y = 0; y < childNodes.getLength(); y++) {
          Node data = childNodes.item(y);
          if (data.getNodeType() == Node.ATTRIBUTE_NODE) {
            if (data.getNodeName().equalsIgnoreCase(attrName))
              return data.getNodeValue();
          }
        }
      }
    }

    return "";
  }
}
