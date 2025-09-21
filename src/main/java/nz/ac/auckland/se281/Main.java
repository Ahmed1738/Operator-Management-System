package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** You cannot modify this class! */
public class Main {

  public enum Command {
    // Task 1 commands
    SEARCH_OPERATORS(1, "Search for operators by keyword <KEYWORD> in their name or location"),
    CREATE_OPERATOR(2, "Create a new operator with <OPERATOR_NAME> and <LOCATION>"),

    // Task 2 commands
    VIEW_ACTIVITIES(1, "View all activities for <OPERATOR_ID>"),
    CREATE_ACTIVITY(
        3, "Create activity with <ACTIVITY_NAME> and <ACTIVITY_TYPE> for <OPERATOR_ID>"),
    SEARCH_ACTIVITIES(
        1, "Search activities by <KEYWORD> in activity name or type or operator location"),

    // Task 3 commands
    ADD_PUBLIC_REVIEW(
        1,
        "Add a public review for an activity identified by <ACTIVITY_ID>",
        "Reviewer name",
        "Anonymous? (y/n)",
        "Rating (1-5)",
        "Review text"),
    ADD_PRIVATE_REVIEW(
        1,
        "Add a private review for an activity identified by <ACTIVITY_ID>",
        "Reviewer name",
        "Email",
        "Rating (1-5)",
        "Review text",
        "Follow-up requested? (y/n)"),
    ADD_EXPERT_REVIEW(
        1,
        "Add an expert review for an activity identified by <ACTIVITY_ID>",
        "Reviewer name",
        "Rating (1-5)",
        "Review text",
        "Would recommend? (y/n)"),
    DISPLAY_REVIEWS(1, "Display all reviews for an activity identified by <ACTIVITY_ID>"),
    ENDORSE_REVIEW(1, "Endorse a public review identified by <REVIEW_ID>"),
    RESOLVE_REVIEW(2, "Resolve a private review identified by <REVIEW_ID> with <RESPONSE>"),
    UPLOAD_REVIEW_IMAGE(2, "Upload an <IMAGE_NAME> for an expert review identified by <REVIEW_ID>"),
    DISPLAY_TOP_ACTIVITIES(
        0, "Display the top activity in each location based on public and expert reviews"),

    // System commands
    HELP(0, "Print usage"),
    EXIT(0, "Exit the application");

    private final int numArgs;
    private final String message;
    private final String[] optionPrompts;

    private Command(final int numArgs, final String message) {
      this(numArgs, message, new String[] {});
    }

    private Command(final int numArgs, final String message, final String... optionPrompts) {
      this.numArgs = numArgs;
      this.message = message;
      this.optionPrompts = optionPrompts;
    }

    public boolean hasArguments() {
      return numArgs > 0;
    }

    public int getNumArgs() {
      return numArgs;
    }

    public boolean hasOptions() {
      return optionPrompts.length > 0;
    }

    public int getNumOptions() {
      return optionPrompts.length;
    }

    public String getOptionPrompt(final int index) {
      return optionPrompts[index];
    }
  }

  private static final String COMMAND_PREFIX = "281-activity-operator-system> ";

  public static void main(final String[] args) {
    new Main(new Scanner(System.in), new OperatorManagementSystem()).start(false);
  }

  public static String help() {
    final StringBuilder sb = new StringBuilder();

    for (final Command command : Command.values()) {
      sb.append(command).append("\t");

      // Add extra padding to align the argument counts
      // if the command name is less than the tab width.
      if (command.toString().length() < 16) {
        sb.append("\t");
      }

      // Add more for even shorter command names.
      if (command.toString().length() < 8) {
        sb.append("\t");
      }

      if (command.numArgs > 0) {
        sb.append("[")
            .append(command.numArgs)
            .append(" argument" + (command.numArgs > 1 ? "s" : "") + "]");
      } else {
        sb.append("[no args]");
      }

      sb.append("\t").append(command.message).append(System.lineSeparator());
    }

    return sb.toString();
  }

  public static void printBanner() {
    // https://patorjk.com/software/taag/
    // https://www.freeformatter.com/java-dotnet-escape.html#before-output

    StringBuilder buf = new StringBuilder();
    buf.append(
        "\r\n"
            + " _______   _____   __      _______  _______ __________________        "
            + " __________________            _______  _______  _______  _______  _______ _________"
            + " _______  _______ \r\n"
            + "/ ___   ) / ___ \\ /  \\    (  ___  )(  ____ \\__   __/\\__   __/|\\     /|\\__  "
            + " __/\\__   __/|\\     /|  (  ___  )(  ____ )(  ____ \\(  ____ )(  ___  )\\__   __/( "
            + " ___  )(  ____ )\r\n"
            + "\\/   )  |( (___) )\\/) )   | (   ) || (    \\/   ) (      ) (   | )   ( |   ) (    "
            + "  ) (   ( \\   / )  | (   ) || (    )|| (    \\/| (    )|| (   ) |   ) (   | (   )"
            + " || (    )|\r\n"
            + "    /   ) \\     /   | |   | (___) || |         | |      | |   | |   | |   | |     "
            + " | |    \\ (_) /   | |   | || (____)|| (__    | (____)|| (___) |   | |   | |   | ||"
            + " (____)|\r\n"
            + "  _/   /  / ___ \\   | |   |  ___  || |         | |      | |   ( (   ) )   | |     "
            + " | |     \\   /    | |   | ||  _____)|  __)   |     __)|  ___  |   | |   | |   | || "
            + "    __)\r\n"
            + " /   _/  ( (   ) )  | |   | (   ) || |         | |      | |    \\ \\_/ /    | |     "
            + " | |      ) (     | |   | || (      | (      | (\\ (   | (   ) |   | |   | |   | ||"
            + " (\\ (   \r\n"
            + "(   (__/\\( (___) )__) (_  | )   ( || (____/\\   | |   ___) (___  \\   /  ___) (___ "
            + "  | |      | |     | (___) || )      | (____/\\| ) \\ \\__| )   ( |   | |   | (___)"
            + " || ) \\ \\__\r\n"
            + "\\_______/ \\_____/ \\____/  |/     \\|(_______/   )_(   \\_______/   \\_/  "
            + " \\_______/   )_(      \\_/     (_______)|/       (_______/|/   \\__/|/     \\|  "
            + " )_(   (_______)|/   \\__/\r\n"
            + "                                                                                    "
            + "                                                                                    "
            + "    \r\n");
    System.out.println(buf.toString());
  }

  private final Scanner scanner;

  private final OperatorManagementSystem system;

  public Main(final Scanner scanner, final OperatorManagementSystem system) {
    this.scanner = scanner;
    this.system = system;
  }

  public void start() {
    start(true);
  }

  public void start(boolean debug) {
    printBanner();
    System.out.println(help());

    // Prompt and process commands until the exit command.
    String command;
    do {
      System.out.print(COMMAND_PREFIX);
      command = scanner.nextLine().trim();
      if (debug) {
        System.out.println(command);
      }
    } while (processCommand(command, debug));
  }

  private static String[] splitWithQuotes(String input) {
    List<String> items = new ArrayList<>();

    // Match based on spaces, while respecting words surrounded by single quotes
    Matcher matcher = Pattern.compile("('(?:[^']+|'')*'\\S*|\\S+)").matcher(input);

    while (matcher.find()) {
      String matched = matcher.group(1);

      // Remove the surrounding quotes
      if (matched.startsWith("'") && matched.endsWith("'")) {
        matched = matched.substring(1, matched.length() - 1);
      }

      items.add(matched);
    }

    return items.toArray(new String[0]);
  }

  private boolean processCommand(String input, boolean debug) {
    // Remove whitespace at the beginning and end of the input.
    input = input.trim();

    final String[] args = splitWithQuotes(input);

    // In case the user pressed "Enter" without typing anything
    if (args.length == 0) {
      return true;
    }

    // Allow any case, and dashes to be used instead of underscores.
    final String commandStr = args[0].toUpperCase().replaceAll("-", "_");

    final Command command;

    try {
      // Command names correspond to the enum names.
      command = Command.valueOf(commandStr);
    } catch (final Exception e) {
      MessageCli.COMMAND_NOT_FOUND.printMessage(commandStr);
      return true;
    }

    // Check if the number of arguments provided don't match up as required for that command
    if (!checkArgs(command, args)) {
      MessageCli.WRONG_ARGUMENT_COUNT.printMessage(
          String.valueOf(command.getNumArgs()), command.getNumArgs() != 1 ? "s" : "", commandStr);

      return true;
    }

    switch (command) {
      case SEARCH_OPERATORS:
        system.searchOperators(args[1]);
        break;
      case CREATE_OPERATOR:
        system.createOperator(args[1], args[2]);
        break;
      case CREATE_ACTIVITY:
        system.createActivity(args[1], args[2], args[3]);
        break;
      case VIEW_ACTIVITIES:
        system.viewActivities(args[1]);
        break;
      case SEARCH_ACTIVITIES:
        system.searchActivities(args[1]);
        break;
      case ADD_PUBLIC_REVIEW:
        system.addPublicReview(args[1], processOptions(command, debug));
        break;
      case ADD_PRIVATE_REVIEW:
        system.addPrivateReview(args[1], processOptions(command, debug));
        break;
      case ADD_EXPERT_REVIEW:
        system.addExpertReview(args[1], processOptions(command, debug));
        break;
      case DISPLAY_REVIEWS:
        system.displayReviews(args[1]);
        break;
      case ENDORSE_REVIEW:
        system.endorseReview(args[1]);
        break;
      case RESOLVE_REVIEW:
        system.resolveReview(args[1], args[2]);
        break;
      case UPLOAD_REVIEW_IMAGE:
        system.uploadReviewImage(args[1], args[2]);
        break;
      case DISPLAY_TOP_ACTIVITIES:
        system.displayTopActivities();
        break;
      case EXIT:
        MessageCli.END.printMessage();
        // Signal that the program should exit.
        return false;
      case HELP:
        System.out.println(help());
        break;
    }

    // Signal that another command is expected.
    return true;
  }

  private String[] processOptions(final Command command, boolean debug) {
    final String[] options = new String[command.getNumOptions()];

    // Prompt the user for each option
    for (int i = 0; i < command.getNumOptions(); i++) {
      System.out.print("\t" + command.getOptionPrompt(i) + ": ");
      options[i] = scanner.nextLine().trim();

      // Print out the response in case we are in automated testing mode
      if (debug) {
        System.out.println(options[i]);
      }
    }
    return options;
  }

  private boolean checkArgs(final Command command, final String[] args) {
    return command.getNumArgs() == args.length - 1;
  }
}
