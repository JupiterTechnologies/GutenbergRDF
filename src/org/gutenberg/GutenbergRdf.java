package org.gutenberg;

import org.gutenberg.rdf.Agent;
import org.gutenberg.rdf.Book;
import org.gutenberg.rdf.Rdf;
import org.gutenberg.rdf.Subject;

class GutenbergRdf {
  private final Rdf rdf;

  private GutenbergRdf(String filename) {
    rdf = new Rdf(filename);
  }

  public static void main(String[] args) {
    GutenbergRdf parser = new GutenbergRdf(args[args.length - 1]);
    parser.outputMetadata();
    parser.outputAuthors();
    parser.outputContributors();
    parser.outputSubjects();
    parser.outputFormats();
  }

  void outputMetadata() {
    System.out.println("eBook ID:    " + rdf.ebookID());
    System.out.println("Type:        " + rdf.type());
    System.out.println("Title:       " + rdf.title());
    System.out.println("Subtitle:    " + rdf.subtitle());
    System.out.println("Released:    " + rdf.releaseDate());
    System.out.println("Language:    " + rdf.language());
    System.out.println("Description: " + rdf.description());
    System.out.println("Publisher:   " + rdf.publisher());
    System.out.println("Rights:      " + rdf.rights());
    System.out.println("Downloads:   " + rdf.downloads());
    System.out.println("\r");
  }

  void outputAuthors() {
    System.out.println("Authors:");
    for (int i = 0; i < rdf.authors().size(); i++) {
      outputAgent(rdf.authors().get(i));
    }
    System.out.println("\r");
  }

  void outputContributors() {
    System.out.println("Contributors:");
    for (int i = 0; i < rdf.contributors().size(); i++) {
      outputAgent(rdf.contributors().get(i));
    }
    System.out.println("\r");
  }

  void outputFormats() {
    System.out.println("eBook Formats:");
    for (int i = 0; i < rdf.ebookFormats().size(); i++) {
      Book book = rdf.ebookFormats().get(i);
      System.out.println("         URL: " + book.url());
      System.out.println("    Released: " + book.modifiedDate());
      System.out.println("    Filesize: " + book.fileSize());
      System.out.println("  Data Types: " + book.dataTypes());
      System.out.println("\r");
    }
  }

  void outputSubjects() {
    System.out.println("Subjects:");
    for (int i = 0; i < rdf.subjects().size(); i++) {
      Subject subject = rdf.subjects().get(i);
      System.out.println("  Heading: " + subject.heading());
      System.out.println("     Type: " + subject.type());
      System.out.println("      URI: " + subject.uri());
      System.out.println("\r");
    }
  }

  private void outputAgent(Agent agent) {
    System.out.println("ID: " + agent.id());
    System.out.println("   Role: " + agent.role());
    System.out.println("   Born: " + agent.birthDate());
    System.out.println("   Died: " + agent.deathDate());
    System.out.println("  First: " + agent.firstName());
    System.out.println("   Last: " + agent.lastName());
    System.out.println("    Web: " + agent.webpage());
    System.out.println("  Alias: " + agent.aliases());
    System.out.println("\r");
  }
}
