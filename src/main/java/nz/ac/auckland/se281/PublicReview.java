package nz.ac.auckland.se281;

/** Represents a public review which can be endorsed by the admin. */
public class PublicReview extends Review {
  private String text;
  private boolean endorsed;

  /**
   * Constructs a PublicReview with basic fields and the review text.
   *
   * @param id the review ID
   * @param rating the rating
   * @param author the author's name
   * @param text the review content
   */
  public PublicReview(String id, int rating, String author, String text) {
    super(id, rating, author);
    this.text = text;
    this.endorsed = false;
  }

  /**
   * Returns the type of this review.
   *
   * @return "public"
   */
  @Override
  public String getType() {
    return "Public";
  }

  /**
   * Returns the textual content of the review.
   *
   * @return review text
   */
  @Override
  public String getContent() {
    return text;
  }

  /** Marks the review as endorsed. */
  public void endorse() {
    endorsed = true;
  }

  /**
   * Checks if the review is endorsed.
   *
   * @return true if endorsed, false otherwise
   */
  public boolean isEndorsed() {
    return endorsed;
  }
}
