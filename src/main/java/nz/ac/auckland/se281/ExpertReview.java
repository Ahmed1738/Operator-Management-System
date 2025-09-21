package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an expert review which can include recommended status and image attachments.
 */
public class ExpertReview extends Review {
  private String text;
  private boolean recommended;
  private List<String> images;

  /**
   * Constructs an ExpertReview with details and review content.
   *
   * @param id the review ID
   * @param rating the rating
   * @param author the review author
   * @param text the content of the review
   * @param recommended whether the expert recommends the activity
   */
  public ExpertReview(String id, int rating, String author, String text, boolean recommended) {
    super(id, rating, author);
    this.text = text;
    this.recommended = recommended;
    this.images = new ArrayList<>();
  }

  /**
   * Returns the type of this review.
   *
   * @return "expert"
   */
  @Override
  public String getType() {
    return "Expert";
  }

  /**
   * Returns the review content.
   *
   * @return review text
   */
  @Override
  public String getContent() {
    return text;
  }

  /**
   * Returns whether this review is recommended.
   *
   * @return true if recommended, false otherwise
   */
  public boolean isRecommended() {
    return recommended;
  }

  /**
   * Adds an image name to the list of uploaded images.
   *
   * @param imageName the name of the image
   */
  public void addImage(String imageName) {
    images.add(imageName);
  }

  /**
   * Returns a list of uploaded image names.
   *
   * @return list of images
   */
  public List<String> getImages() {
    return images;
  }
}
