package nz.ac.auckland.se281;

/**
 * Abstract base class for all types of reviews. Stores common fields such as ID, rating, and
 * author.
 */
public abstract class Review {
  protected String id;
  protected int rating;
  protected String author;

  /**
   * Constructs a Review with the given ID, rating, and author.
   *
   * @param id the unique review ID
   * @param rating the rating given
   * @param author the author of the review
   */
  public Review(String id, int rating, String author) {
    this.id = id;
    this.rating = rating;
    this.author = author;
  }

  /**
   * Returns the review's ID.
   *
   * @return the ID
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the review's rating.
   *
   * @return the rating
   */
  public int getRating() {
    return rating;
  }

  /**
   * Returns the review's author.
   *
   * @return the author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Returns the type of review (e.g., public, private, expert).
   *
   * @return the review type
   */
  public abstract String getType();

  /**
   * Returns the main review content.
   *
   * @return the content
   */
  public abstract String getContent();
}
