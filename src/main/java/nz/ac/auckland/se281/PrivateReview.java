package nz.ac.auckland.se281;

/** Represents a private review which can be resolved by the admin with a response. */
public class PrivateReview extends Review {
  private String text;
  private String resolution;
  private String contact;
  private boolean followUp;

  /**
   * Constructs a PrivateReview with review details, content, contact, and follow-up flag.
   *
   * @param id the review ID
   * @param rating the rating
   * @param author the author of the review
   * @param text the content of the review
   * @param contact the contact email for follow-up
   * @param followUp whether follow-up is needed
   */
  public PrivateReview(
      String id, int rating, String author, String text, String contact, boolean followUp) {
    super(id, rating, author);
    this.text = text;
    this.resolution = null;
    this.contact = contact;
    this.followUp = followUp;
  }

  /**
   * Returns the type of this review.
   *
   * @return "Private"
   */
  @Override
  public String getType() {
    return "Private";
  }

  /**
   * Returns the review text.
   *
   * @return review content
   */
  @Override
  public String getContent() {
    return text;
  }

  /**
   * Resolves the review by setting a resolution message.
   *
   * @param resolution the resolution response
   */
  public void resolve(String resolution) {
    this.resolution = resolution;
  }

  /**
   * Checks if the review has been resolved.
   *
   * @return true if resolved, false otherwise
   */
  public boolean isResolved() {
    return resolution != null && !resolution.isBlank();
  }

  /**
   * Returns the resolution response if available.
   *
   * @return resolution string or "-"
   */
  public String getResolution() {
    return (resolution == null || resolution.isBlank()) ? "-" : resolution;
  }

  /**
   * Returns true if follow-up is requested.
   *
   * @return true if follow-up is needed
   */
  public boolean needsFollowUp() {
    return followUp;
  }

  /**
   * Returns the contact email.
   *
   * @return the contact
   */
  public String getContact() {
    return contact;
  }
}
