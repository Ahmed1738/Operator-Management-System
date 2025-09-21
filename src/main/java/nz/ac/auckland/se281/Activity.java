package nz.ac.auckland.se281;

import nz.ac.auckland.se281.Types.ActivityType;

/**
 * Abstract base class for activities in the system.
 * Defines common properties and behavior for all activity types.
 */
public abstract class Activity {

  protected String name;
  protected ActivityType type;
  protected String id;

  /**
   * Constructs a new Activity.
   *
   * @param name the name of the activity
   * @param type the type of the activity
   * @param id the unique identifier for the activity
   */
  public Activity(String name, ActivityType type, String id) {
    this.name = name;
    this.type = type;
    this.id = id;
  }

  /**
   * Returns the name of the activity.
   *
   * @return activity name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the type of the activity.
   *
   * @return activity type
   */
  public ActivityType getType() {
    return type;
  }

  /**
   * Returns the unique identifier of the activity.
   *
   * @return activity ID
   */
  public String getId() {
    return id;
  }

  /**
   * Returns a string description of the activity.
   * Must be implemented by all subclasses.
   *
   * @return formatted activity description
   */
  public abstract String getDescription();
}
