package nz.ac.auckland.se281;

/* You cannot modify this class! */

public enum MessageCli {
  OPERATORS_FOUND("There %s %s matching operator%s found%s"),
  OPERATOR_CREATED("Successfully created operator '%s' ('%s') located in '%s'."),
  OPERATOR_NOT_CREATED_INVALID_OPERATOR_NAME(
      "Operator not created: '%s' is not a valid operator name."),
  OPERATOR_NOT_CREATED_INVALID_LOCATION("Operator not created: '%s' is an invalid location."),
  OPERATOR_NOT_CREATED_ALREADY_EXISTS_SAME_LOCATION(
      "Operator not created: the operator name '%s' already exists same location for '%s'."),
  OPERATOR_NOT_FOUND("Operator not found: '%s' is an invalid operator ID."),
  OPERATOR_ENTRY("  * %s ('%s' located in '%s')"),
  ACTIVITIES_FOUND("There %s %s matching activit%s found%s"),
  ACTIVITY_CREATED("Successfully created activity '%s' ('%s': '%s') for '%s'."),
  ACTIVITY_NOT_CREATED_INVALID_ACTIVITY_NAME(
      "Activity not created: '%s' is not a valid activity name."),
  ACTIVITY_NOT_CREATED_INVALID_OPERATOR_ID("Activity not created: '%s' is an invalid operator ID."),
  ACTIVITY_NOT_FOUND("Activity not found: '%s' is an invalid activity ID."),
  ACTIVITY_ENTRY("  * %s: [%s/%s] offered by %s"),
  REVIEW_ADDED("%s review '%s' added successfully for activity '%s'."),
  REVIEW_NOT_ADDED_INVALID_ACTIVITY_ID("Review not added: '%s' is an invalid activity ID."),
  REVIEWS_FOUND("There %s %s review%s for activity '%s'."),
  REVIEW_NOT_FOUND("Review not found: '%s' is an invalid review ID."),
  REVIEW_ENDORSED("Review '%s' endorsed successfully."),
  REVIEW_NOT_ENDORSED("Review not endorsed: '%s' is not a public review."),
  REVIEW_RESOLVED("Review '%s' resolved successfully."),
  REVIEW_NOT_RESOLVED("Review not resolved: '%s' is not a private review."),
  REVIEW_IMAGE_ADDED("Image '%s' uploaded successfully for review '%s'."),
  REVIEW_IMAGE_NOT_ADDED_NOT_EXPERT("Image not uploaded: '%s' is not an expert review."),
  REVIEW_ENTRY_HEADER("  * [%s/%s] %s review (%s) by '%s'"),
  REVIEW_ENTRY_REVIEW_TEXT("    \"%s\" "),
  REVIEW_ENTRY_ENDORSED("    Endorsed by admin."),
  REVIEW_ENTRY_RESOLVED("    Resolved: \"%s\""),
  REVIEW_ENTRY_FOLLOW_UP("    Need to email '%s' for follow-up."),
  REVIEW_ENTRY_RECOMMENDED("    Recommended by experts."),
  REVIEW_ENTRY_IMAGES("    Images: [%s]"),
  NO_REVIEWED_ACTIVITIES("No reviewed activities found in %s."),
  TOP_ACTIVITY("Top reviewed activity in %s is '%s', with an average rating of %s"),

  CUSTOM("%s"),
  COMMAND_NOT_FOUND(
      "Error! Command not found! (run 'help' for the list of available commands): \"%s\""),
  WRONG_ARGUMENT_COUNT(
      "Error! Incorrect number of arguments provided. Expected %s argument%s for the \"%s\""
          + " command"),

  END("You closed the terminal. Goodbye.");

  private final String msg;

  private MessageCli(final String msg) {
    this.msg = msg;
  }

  public String getMessage(final String... args) {
    String tmpMessage = msg;

    for (final String arg : args) {
      tmpMessage = tmpMessage.replaceFirst("%s", arg);
    }

    return tmpMessage;
  }

  public void printMessage(final String... args) {
    System.out.println(getMessage(args));
  }

  @Override
  public String toString() {
    return msg;
  }
}
