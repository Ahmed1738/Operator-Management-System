package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se281.Types.ActivityType;
import nz.ac.auckland.se281.Types.Location;

/**
 * The OperatorManagementSystem class handles all core logic for managing operators and their
 * associated activities in the system. This includes creating, viewing, and searching both
 * operators and activities.
 */
public class OperatorManagementSystem {

  /** List of all operators managed in the system. */
  private List<Operator> operators = new ArrayList<>();

  /** Constructs the operator management system with an empty operator list. */
  public OperatorManagementSystem() {}

  /**
   * Searches for operators using a keyword. Matches against name, location name, and location
   * abbreviation. The wildcard "*" returns all operators.
   *
   * @param keyword the search term provided by the user
   */
  public void searchOperators(String keyword) {
    // Validate the keyword input
    if (keyword == null || keyword.isBlank()) {
      System.out.println("There are no matching operators found.");
      return;
    }

    // Normalize the keyword for case-insensitive matching
    keyword = keyword.trim().toLowerCase();
    List<Operator> matches = new ArrayList<>();

    // Handle wildcard search
    if (keyword.equals("*")) {
      matches.addAll(operators);
    } else {
      // Iterate through all operators to find matches
      for (Operator op : operators) {
        String name = op.getName().toLowerCase();
        String location = op.getLocation().getFullName().toLowerCase();
        String locationAbbrev = op.getLocation().getLocationAbbreviation().toLowerCase();

        // Skip invalid keywords
        if (keyword.equals("|")) {
          continue;
        }

        // Check if the keyword matches any operator fields
        if (name.contains(keyword)
            || location.contains(keyword)
            || locationAbbrev.contains(keyword)) {
          matches.add(op);
        }
      }
    }

    // If no matches are found, print a message and return
    if (matches.isEmpty()) {
      System.out.println("There are no matching operators found.");
      return;
    }

    // Prepare variables for constructing the output message
    String verb;
    String countWord;
    String pluralSuffix;
    String colon = ":";

    // Determine singular/plural forms based on the number of matches
    if (matches.size() == 1) {
      verb = "is";
      countWord = "1";
      pluralSuffix = "";
    } else {
      verb = "are";
      countWord = String.valueOf(matches.size());
      pluralSuffix = "s";
    }

    // Print the summary message for the found operators
    MessageCli.OPERATORS_FOUND.printMessage(verb, countWord, pluralSuffix, colon);

    // Print details of each matching operator
    for (Operator op : matches) {
      MessageCli.OPERATOR_ENTRY.printMessage(
          op.getName(), op.getId(), op.getLocation().getFullName());
    }
  }

  /**
   * Creates a new operator after validating the name and location. Ensures uniqueness within the
   * same location.
   *
   * @param operatorName name of the operator
   * @param locationStr location in string format
   */
  public void createOperator(String operatorName, String locationStr) {
    // Trim and validate the operator name
    operatorName = operatorName.trim();
    if (operatorName == null || operatorName.isBlank() || operatorName.length() < 3) {
      MessageCli.OPERATOR_NOT_CREATED_INVALID_OPERATOR_NAME.printMessage(operatorName);
      return;
    }

    // Parse and validate the location
    Location loc = Location.fromString(locationStr);
    if (loc == null) {
      MessageCli.OPERATOR_NOT_CREATED_INVALID_LOCATION.printMessage(locationStr);
      return;
    }

    // Check for duplicate operators in the same location
    for (Operator op : operators) {
      if (op.getName().equalsIgnoreCase(operatorName)
          && op.getLocation().getLocationAbbreviation().equals(loc.getLocationAbbreviation())) {
        MessageCli.OPERATOR_NOT_CREATED_ALREADY_EXISTS_SAME_LOCATION.printMessage(
            operatorName, loc.getFullName());
        return;
      }
    }

    // Generate the operator's unique ID
    StringBuilder initials = new StringBuilder();
    for (String word : operatorName.split(" ")) {
      if (!word.isBlank()) {
        initials.append(Character.toUpperCase(word.charAt(0)));
      }
    }

    int count = 0;
    for (Operator op : operators) {
      if (op.getLocation().getLocationAbbreviation().equals(loc.getLocationAbbreviation())) {
        count++;
      }
    }

    String operatorId =
        String.format("%s-%s-%03d", initials, loc.getLocationAbbreviation(), count + 1);

    // Create and add the new operator
    Operator newOp = new Operator(operatorName, loc, operatorId);
    operators.add(newOp);

    // Print confirmation message
    MessageCli.OPERATOR_CREATED.printMessage(operatorName, operatorId, loc.getFullName());
  }

  /**
   * Displays all activities for a given operator if the ID is valid.
   *
   * @param operatorId the ID of the operator
   */
  public void viewActivities(String operatorId) {
    // Search for the operator by ID
    Operator foundOperator = null;
    for (Operator op : operators) {
      if (op.getId().equalsIgnoreCase(operatorId)) {
        foundOperator = op;
        break;
      }
    }

    // If operator is not found, print an error message
    if (foundOperator == null) {
      MessageCli.OPERATOR_NOT_FOUND.printMessage(operatorId);
      return;
    }

    // Retrieve the operator's activities
    List<Activity> activities = foundOperator.getActivities();

    // If no activities are found, print a message and return
    if (activities.isEmpty()) {
      System.out.println("There are no matching activities found.");
      return;
    }

    // Prepare variables for constructing the output message
    String verb;
    String countWord;
    String pluralSuffix;
    String colon = ":";

    // Determine singular/plural forms based on the number of activities
    int numActivities = activities.size();
    countWord = String.valueOf(numActivities);

    if (numActivities == 1) {
      verb = "is";
      pluralSuffix = "y";
    } else {
      verb = "are";
      pluralSuffix = "ies";
    }

    // Print the summary message for the found activities
    MessageCli.ACTIVITIES_FOUND.printMessage(verb, countWord, pluralSuffix, colon);

    // Print details of each activity
    for (Activity activity : activities) {
      StandardActivity sa = (StandardActivity) activity;
      MessageCli.ACTIVITY_ENTRY.printMessage(
          activity.getName(),
          activity.getId(),
          activity.getType().toString(),
          sa.getOperator().getName());
    }
  }

  /**
   * Creates a new activity for a given operator if both name and operator are valid.
   *
   * @param activityName the activity name
   * @param activityType the activity type (as string)
   * @param operatorId the operator offering the activity
   */
  public void createActivity(String activityName, String activityType, String operatorId) {
    // Trim and validate the activity name
    activityName = activityName.trim();
    if (activityName.length() < 3) {
      MessageCli.ACTIVITY_NOT_CREATED_INVALID_ACTIVITY_NAME.printMessage(activityName);
      return;
    }

    // Parse and validate the activity type
    ActivityType type = ActivityType.fromString(activityType);
    if (type == null) {
      type = ActivityType.OTHER;
    }

    // Search for the operator by ID
    Operator foundOperator = null;
    for (Operator op : operators) {
      if (op.getId().equalsIgnoreCase(operatorId)) {
        foundOperator = op;
        break;
      }
    }

    // If operator is not found, print an error message
    if (foundOperator == null) {
      MessageCli.ACTIVITY_NOT_CREATED_INVALID_OPERATOR_ID.printMessage(operatorId);
      return;
    }

    // Generate the activity's unique ID
    int activityCount = foundOperator.getActivities().size() + 1;
    String activityId = String.format("%s-%03d", operatorId, activityCount);

    // Create and add the new activity
    StandardActivity activity = new StandardActivity(activityName, type, activityId, foundOperator);
    foundOperator.addActivity(activity);

    // Print confirmation message
    MessageCli.ACTIVITY_CREATED.printMessage(
        activityName, activityId, type.toString(), foundOperator.getName());
  }

  /**
   * Searches across all operators' activities based on keyword. Keyword may match activity name,
   * type, location, or abbreviation. Wildcard "*" matches all activities.
   *
   * @param keyword search term
   */
  public void searchActivities(String keyword) {
    // Trim and convert the keyword to lowercase for case-insensitive matching
    keyword = keyword.trim().toLowerCase();
    List<StandardActivity> matches = new ArrayList<>();

    // Iterate through all operators and their activities
    for (Operator op : operators) {
      for (Activity act : op.getActivities()) {
        // Cast the activity to StandardActivity for access to specific methods
        StandardActivity sa = (StandardActivity) act;

        // Retrieve and normalize searchable fields
        String name = sa.getName().toLowerCase();
        String type = sa.getType().toString().toLowerCase();
        String location = sa.getOperator().getLocation().getFullName().toLowerCase();
        String abbrev = sa.getOperator().getLocation().getLocationAbbreviation().toLowerCase();

        // Check if the keyword matches any of the fields or is a wildcard
        if (keyword.equals("*")
            || name.contains(keyword)
            || type.contains(keyword)
            || location.contains(keyword)
            || abbrev.contains(keyword)) {
          matches.add(sa); // Add matching activity to the results
        }
      }
    }

    // If no matches are found, print a message and return
    if (matches.isEmpty()) {
      System.out.println("There are no matching activities found.");
      return;
    }

    // Prepare variables for constructing the output message
    String verb;
    String countWord;
    String pluralSuffix;
    String colon = ":";

    // Determine singular/plural forms based on the number of matches
    int count = matches.size();
    countWord = String.valueOf(count);

    if (count == 1) {
      verb = "is";
      pluralSuffix = "y";
    } else {
      verb = "are";
      pluralSuffix = "ies";
    }

    // Print the summary message for the found activities
    MessageCli.ACTIVITIES_FOUND.printMessage(verb, countWord, pluralSuffix, colon);

    // Print details of each matching activity
    for (StandardActivity sa : matches) {
      MessageCli.ACTIVITY_ENTRY.printMessage(
          sa.getName(), sa.getId(), sa.getType().toString(), sa.getOperator().getName());
    }
  }

  /**
   * Adds a public review to the specified activity if the activity ID is valid.
   *
   * @param activityId the ID of the activity to review
   * @param options the review options array (expected: [author, rating, review text])
   */
  public void addPublicReview(String activityId, String[] options) {
    // Check that the correct number of parameters is provided
    if (options.length != 4) { // expected: author, anonymous (ignored), rating, review text
      return;
    }

    // Extract and clean inputs
    String author = options[0].trim();
    int rating = Integer.parseInt(options[2].trim());
    String text = options[3].trim();
    String anonymousOption = options[1].trim().toLowerCase();

    // Adjust rating if it is out of bounds
    if (rating < 1) {
      rating = 1;
    } else if (rating > 5) {
      rating = 5;
    }

    // If anonymous option is "y" or "yes", override author
    if (anonymousOption.equals("y") || anonymousOption.equals("yes")) {
      author = "Anonymous";
    }

    // Search for the activity across all operators
    for (Operator op : operators) {
      for (Activity act : op.getActivities()) {
        if (act.getId().equalsIgnoreCase(activityId)) {
          // Cast to StandardActivity since only those have reviews
          StandardActivity activity = (StandardActivity) act;

          // Generate a new review ID
          String reviewId = String.format("%s-R%d", activityId, activity.getReviews().size() + 1);

          // Create and add the PublicReview
          PublicReview review = new PublicReview(reviewId, rating, author, text);
          activity.addReview(review);

          // Show success message
          MessageCli.REVIEW_ADDED.printMessage("Public", reviewId, activity.getName());
          return;
        }
      }
    }

    // If activity not found, show error
    MessageCli.REVIEW_NOT_ADDED_INVALID_ACTIVITY_ID.printMessage(activityId);
  }

  /**
   * Adds a private review to the specified activity. Validates the input and creates the review if
   * valid.
   *
   * @param activityId the ID of the activity being reviewed
   * @param options the options including rating, author, and text
   */
  public void addPrivateReview(String activityId, String[] options) {
    // Validate input parameters
    if (options == null
        || options.length
            != 5) { // expected: author, contact (ignored), rating, text, resolved (ignored)
      MessageCli.REVIEW_NOT_ADDED_INVALID_ACTIVITY_ID.printMessage(activityId);
      return;
    }

    // Extract and clean inputs
    String author = options[0].trim();
    String contact = options[1].trim();
    String ratingStr = options[2].trim();
    String text = options[3].trim();
    String followupOption = options[4].trim().toLowerCase();

    // Validate rating is a valid integer
    int rating;
    try {
      rating = Integer.parseInt(ratingStr);
    } catch (NumberFormatException e) {
      MessageCli.REVIEW_NOT_ADDED_INVALID_ACTIVITY_ID.printMessage(activityId);
      return;
    }

    // Adjust rating if it is out of bounds
    if (rating < 1) {
      rating = 1;
    } else if (rating > 5) {
      rating = 5;
    }

    // Find the activity
    StandardActivity targetActivity = null;
    for (Operator op : operators) {
      for (Activity act : op.getActivities()) {
        if (act.getId().equalsIgnoreCase(activityId) && act instanceof StandardActivity) {
          targetActivity = (StandardActivity) act;
          break;
        }
      }
      if (targetActivity != null) {
        break;
      }
    }

    // If activity not found, show error
    if (targetActivity == null) {
      MessageCli.REVIEW_NOT_ADDED_INVALID_ACTIVITY_ID.printMessage(activityId);
      return;
    }

    // Generate a unique ID for the review
    String reviewId = String.format("%s-R%d", activityId, targetActivity.getReviews().size() + 1);

    // Create and add the review
    PrivateReview review =
        new PrivateReview(
            reviewId,
            rating,
            author,
            text,
            contact,
            followupOption.equals("y") || followupOption.equals("yes"));
    targetActivity.addReview(review);

    // Output confirmation
    MessageCli.REVIEW_ADDED.printMessage("Private", reviewId, targetActivity.getName());
  }

  /**
   * Adds an expert review to a specified activity if all input values are valid. Validates the
   * rating range (1–5), review text length (minimum 3), and parses the recommended flag (yes/y or
   * anything else as no).
   *
   * @param activityId the ID of the activity being reviewed
   * @param options an array with [author, rating, text, recommended]
   */
  public void addExpertReview(String activityId, String[] options) {
    // Validate that 4 fields are provided
    if (options.length != 4) { // expected: author, rating, text, recommended
      return;
    }

    // Extract and clean the inputs
    String author = options[0].trim();
    int rating = Integer.parseInt(options[1].trim());
    String text = options[2].trim();
    String recInput = options[3].trim().toLowerCase();
    boolean recommended = recInput.equals("y") || recInput.equals("yes");

    // Adjust rating if it is out of bounds
    if (rating < 1) {
      rating = 1;
    } else if (rating > 5) {
      rating = 5;
    }

    // Search for the activity
    for (Operator op : operators) {
      for (Activity act : op.getActivities()) {
        if (act.getId().equalsIgnoreCase(activityId)) {
          StandardActivity activity = (StandardActivity) act;

          // Generate the review ID
          String reviewId = String.format("%s-R%d", activityId, activity.getReviews().size() + 1);

          // Create and add the expert review
          ExpertReview review = new ExpertReview(reviewId, rating, author, text, recommended);
          activity.addReview(review);

          // Show confirmation
          MessageCli.REVIEW_ADDED.printMessage("Expert", reviewId, activity.getName());
          return;
        }
      }
    }

    // If the activity isn't found, print an error
    MessageCli.REVIEW_NOT_ADDED_INVALID_ACTIVITY_ID.printMessage(activityId);
  }

  /**
   * Displays all reviews associated with a specific activity ID. Handles formatting of different
   * review types and their unique properties.
   *
   * @param activityId the ID of the activity whose reviews will be shown
   */
  public void displayReviews(String activityId) {
    StandardActivity targetActivity = null;

    // Find the target activity by ID
    for (Operator operator : operators) {
      for (Activity activity : operator.getActivities()) {
        if (activity.getId().equalsIgnoreCase(activityId) && activity instanceof StandardActivity) {
          targetActivity = (StandardActivity) activity;
          break;
        }
      }
      if (targetActivity != null) {
        break;
      }
    }

    // If no such activity is found, print error
    if (targetActivity == null) {
      MessageCli.ACTIVITY_NOT_FOUND.printMessage(activityId);
      return;
    }

    List<Review> reviews = targetActivity.getReviews();

    // If there are no reviews
    if (reviews.isEmpty()) {
      System.out.println("There are no reviews for activity '" + targetActivity.getName() + "'.");
      return;
    }

    // Display header message about review count
    String verb;
    String count = String.valueOf(reviews.size());
    String pluralSuffix;

    if (reviews.size() == 1) {
      verb = "is";
      pluralSuffix = "";
    } else {
      verb = "are";
      pluralSuffix = "s";
    }

    MessageCli.REVIEWS_FOUND.printMessage(verb, count, pluralSuffix, targetActivity.getName());

    // Display each review
    for (Review review : reviews) {
      MessageCli.REVIEW_ENTRY_HEADER.printMessage(
          String.valueOf(review.getRating()),
          "5",
          review.getType(),
          review.getId(),
          review.getAuthor());

      MessageCli.REVIEW_ENTRY_REVIEW_TEXT.printMessage(review.getContent());

      if (review instanceof PublicReview) {
        PublicReview pub = (PublicReview) review;
        if (pub.isEndorsed()) {
          MessageCli.REVIEW_ENTRY_ENDORSED.printMessage();
        }
      } else if (review instanceof PrivateReview) {
        PrivateReview pr = (PrivateReview) review;

        // Always print the resolution, even if it is "-"
        if (pr.isResolved()) {
          MessageCli.REVIEW_ENTRY_RESOLVED.printMessage(pr.getResolution());
        }

        // Still print follow-up message if needed
        if (!pr.isResolved() && pr.needsFollowUp()) {
          MessageCli.REVIEW_ENTRY_FOLLOW_UP.printMessage(pr.getContact());
        } else {
          MessageCli.REVIEW_ENTRY_RESOLVED.printMessage(pr.getResolution());
        }
      } else if (review instanceof ExpertReview) {
        ExpertReview er = (ExpertReview) review;
        if (er.isRecommended()) {
          MessageCli.REVIEW_ENTRY_RECOMMENDED.printMessage();
        }
        if (!er.getImages().isEmpty()) {
          String imageList = String.join(",", er.getImages());
          MessageCli.REVIEW_ENTRY_IMAGES.printMessage(imageList);
        }
      }
    }
  }

  /**
   * Endorses a public review by its ID, if valid and of correct type.
   *
   * @param reviewId the ID of the review to endorse
   */
  public void endorseReview(String reviewId) {
    Review targetReview = null;

    // Look through all reviews in all activities across all operators
    for (Operator operator : operators) {
      for (Activity activity : operator.getActivities()) {
        if (activity instanceof StandardActivity) {
          List<Review> reviews = ((StandardActivity) activity).getReviews();
          for (Review review : reviews) {
            if (review.getId().equalsIgnoreCase(reviewId)) {
              targetReview = review;
              break;
            }
          }
        }
        if (targetReview != null) {
          break;
        }
      }
      if (targetReview != null) {
        break;
      }
    }

    // If not found, show error
    if (targetReview == null) {
      MessageCli.REVIEW_NOT_FOUND.printMessage(reviewId);
      return;
    }

    // Check if it's a PublicReview
    if (!(targetReview instanceof PublicReview)) {
      MessageCli.REVIEW_NOT_ENDORSED.printMessage(reviewId);
      return;
    }

    // Endorse it and show success message
    PublicReview publicReview = (PublicReview) targetReview;
    publicReview.endorse();
    MessageCli.REVIEW_ENDORSED.printMessage(reviewId);
  }

  /**
   * Resolves a private review by its ID if valid and of correct type.
   *
   * @param reviewId the ID of the private review to resolve
   * @param response the resolution message to attach
   */
  public void resolveReview(String reviewId, String response) {
    Review targetReview = null;

    // Search for the review across all operators and their activities
    for (Operator operator : operators) {
      for (Activity activity : operator.getActivities()) {
        if (activity instanceof StandardActivity) {
          List<Review> reviews = ((StandardActivity) activity).getReviews();
          for (Review review : reviews) {
            if (review.getId().equalsIgnoreCase(reviewId)) {
              targetReview = review;
              break;
            }
          }
        }
        if (targetReview != null) {
          break;
        }
      }
      if (targetReview != null) {
        break;
      }
    }

    // If review not found
    if (targetReview == null) {
      MessageCli.REVIEW_NOT_FOUND.printMessage(reviewId);
      return;
    }

    // If the review is not a private review
    if (!(targetReview instanceof PrivateReview)) {
      MessageCli.REVIEW_NOT_RESOLVED.printMessage(reviewId);
      return;
    }

    // Handle null or blank response by replacing it with "-"
    String finalResponse = (response == null || response.trim().isEmpty()) ? "-" : response.trim();

    // Set resolution and print confirmation
    PrivateReview privateReview = (PrivateReview) targetReview;
    privateReview.resolve(finalResponse);

    // Print the exact expected message
    MessageCli.REVIEW_RESOLVED.printMessage(reviewId, finalResponse);
  }

  /**
   * Uploads an image to an expert review specified by its ID. Validates the existence of the review
   * and ensures it is of type ExpertReview.
   *
   * @param reviewId the ID of the review to upload the image to
   * @param imageName the name of the image to upload
   */
  public void uploadReviewImage(String reviewId, String imageName) {
    Review targetReview = null;

    // Search for the review by ID across all operators and their activities
    for (Operator operator : operators) {
      for (Activity activity : operator.getActivities()) {
        if (activity instanceof StandardActivity) {
          List<Review> reviews = ((StandardActivity) activity).getReviews();
          for (Review review : reviews) {
            if (review.getId().equalsIgnoreCase(reviewId)) {
              targetReview = review;
              break;
            }
          }
        }
        // Break inner loop if review is found
        if (targetReview != null) {
          break;
        }
      }
      // Break outer loop if review is found
      if (targetReview != null) {
        break;
      }
    }

    // Handle review not found
    if (targetReview == null) {
      MessageCli.REVIEW_NOT_FOUND.printMessage(reviewId);
      return;
    }

    // Ensure the review is of type ExpertReview
    if (!(targetReview instanceof ExpertReview)) {
      MessageCli.REVIEW_IMAGE_NOT_ADDED_NOT_EXPERT.printMessage(reviewId);
      return;
    }

    // Upload the image to the expert review
    ExpertReview expertReview = (ExpertReview) targetReview;
    expertReview.addImage(imageName);

    //  Confirm upload to the user
    MessageCli.REVIEW_IMAGE_ADDED.printMessage(imageName, reviewId);
  }

  /**
   * Displays the top reviewed activity in each location based on average rating. If no reviewed
   * activities exist in a location, a message is printed for that location.
   */
  public void displayTopActivities() {
    // Go through each unique location in the system
    for (Location loc : Location.values()) {
      List<StandardActivity> reviewedInLoc = new ArrayList<>();

      // Find reviewed activities for this location
      for (Operator op : operators) {
        if (op.getLocation().equals(loc)) {
          for (Activity act : op.getActivities()) {
            StandardActivity sa = (StandardActivity) act;
            for (Review review : sa.getReviews()) {
              if (review instanceof PublicReview || review instanceof ExpertReview) {
                reviewedInLoc.add(sa);
                break;
              }
            }
          }
        }
      }

      // If no reviewed activities in this location
      if (reviewedInLoc.isEmpty()) {
        MessageCli.NO_REVIEWED_ACTIVITIES.printMessage(loc.getFullName());
        continue;
      }

      // Find the highest-rated activity in this location
      StandardActivity topActivity = reviewedInLoc.get(0);
      for (StandardActivity sa : reviewedInLoc) {
        if (sa.getAverageRating() > topActivity.getAverageRating()) {
          topActivity = sa;
        }
      }

      // Print the top activity message
      String locationName = loc.getFullName();
      String activityName = topActivity.getName();
      String avg = String.format("%.2f", topActivity.getAverageRating());
      MessageCli.TOP_ACTIVITY.printMessage(locationName, activityName, avg);
    }
  }
}
