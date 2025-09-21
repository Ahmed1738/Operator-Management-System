package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.List;
import nz.ac.auckland.se281.Types.Location;

/**
 * Represents an operator in the system. Each operator has a name, location, unique ID, and a list
 * of activities they offer.
 */
public class Operator {

  private String name;
  private Location location;
  private String id;
  private List<Activity> activities;

  /**
   * Constructs a new Operator instance.
   *
   * @param name the name of the operator
   * @param location the location of the operator
   * @param id the unique identifier of the operator
   */
  public Operator(String name, Location location, String id) {
    this.name = name;
    this.location = location;
    this.id = id;
    this.activities = new ArrayList<>();
  }

  /**
   * Returns the name of the operator.
   *
   * @return operator name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the location of the operator.
   *
   * @return operator location
   */
  public Location getLocation() {
    return location;
  }

  /**
   * Returns the unique identifier of the operator.
   *
   * @return operator ID
   */
  public String getId() {
    return id;
  }

  /**
   * Returns a list of activities offered by the operator.
   *
   * @return list of activities
   */
  public List<Activity> getActivities() {
    return activities;
  }

  /**
   * Adds an activity to the operator's list of activities.
   *
   * @param activity the activity to add
   */
  public void addActivity(Activity activity) {
    activities.add(activity);
  }
}
