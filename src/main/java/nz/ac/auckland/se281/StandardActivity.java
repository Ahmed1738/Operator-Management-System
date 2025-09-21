package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se281.Types.ActivityType;

/**
 * Represents a standard activity created by an operator. Contains references to the operator and
 * inherits basic activity properties. Also stores all reviews related to this activity.
 */
public class StandardActivity extends Activity {
  private Operator operator;
  private List<Review> reviews;

  /**
   * Constructs a StandardActivity with the specified name, type, ID, and operator.
   *
   * @param name the name of the activity
   * @param type the type of the activity
   * @param id the identifier of the activity
   * @param operator the operator who offers the activity
   */
  public StandardActivity(String name, ActivityType type, String id, Operator operator) {
    super(name, type, id);
    this.operator = operator;
    this.reviews = new ArrayList<>();
  }

  /**
   * Returns the operator who offers this activity.
   *
   * @return the operator
   */
  public Operator getOperator() {
    return operator;
  }

  /**
   * Returns a description of the activity including name, ID, type, and operator's name.
   *
   * @return formatted description string
   */
  @Override
  public String getDescription() {
    return String.format("%s: [%s/%s] offered by %s", name, id, type, operator.getName());
  }

  /**
   * Adds a review to this activity.
   *
   * @param review the review to add
   */
  public void addReview(Review review) {
    reviews.add(review);
  }

  /**
   * Returns the list of reviews associated with this activity.
   *
   * @return list of reviews
   */
  public List<Review> getReviews() {
    return reviews;
  }

  /**
   * Calculates and returns the average rating of all reviews for this activity. Only considers the
   * numeric rating values from the reviews.
   *
   * @return the average rating as a double, or 0.0 if there are no reviews
   */
  public double getAverageRating() {

    // Get all reviews for this activity
    List<Review> reviews = getReviews();

    // If there are no reviews, return 0.0
    if (reviews.isEmpty()) {
      return 0.0;
    }

    // Sum all the ratings
    int total = 0;
    for (Review review : reviews) {
      total += review.getRating();
    }

    // Compute and return the average
    return (double) total / reviews.size();
  }
}
