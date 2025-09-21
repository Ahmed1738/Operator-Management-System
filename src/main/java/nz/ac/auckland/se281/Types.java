package nz.ac.auckland.se281;

/* You cannot modify this class! */

public class Types {

  // https://www.reomaori.co.nz/no-hea-koe-map
  public enum Location {
    AKL("Auckland", "Tāmaki Makaurau", "AKL"),
    HLZ("Hamilton", "Kirikiriroa", "HLZ"),
    TRG("Tauranga", "Tauranga", "TRG"),
    TUO("Taupo", "Taupō-nui-a-Tia", "TUO"),
    WLG("Wellington", "Te Whanganui-a-Tara", "WLG"),
    NSN("Nelson", "Whakatu", "NSN"),
    CHC("Christchurch", "Ōtautahi", "CHC"),
    DUD("Dunedin", "Ōtepoti", "DUD");

    private final String nameEnglish;
    private final String nameTeReo;
    private final String locationAbbreviation;

    Location(String nameEnglish, String nameTeReo, String locationAbbreviation) {
      this.nameEnglish = nameEnglish;
      this.nameTeReo = nameTeReo;
      this.locationAbbreviation = locationAbbreviation;
    }

    public String getFullName() {
      if (this.nameTeReo.equals(this.nameEnglish)) {
        return this.nameTeReo;
      }
      return this.nameEnglish + " | " + this.nameTeReo;
    }

    public String getNameEnglish() {
      return this.nameEnglish;
    }

    public String getNameTeReo() {
      return this.nameTeReo;
    }

    public String getLocationAbbreviation() {
      return this.locationAbbreviation;
    }

    @Override
    public String toString() {
      return this.getFullName();
    }

    public static Location fromString(String text) {
      for (Location location : Location.values()) {
        if (location.nameEnglish.equalsIgnoreCase(text)
            || location.nameTeReo.equalsIgnoreCase(text)
            || location.locationAbbreviation.equalsIgnoreCase(text)) {
          return location;
        }
      }
      return null;
    }
  }

  public enum ActivityType {
    ADVENTURE("Adventure"),
    CULTURE("Culture"),
    FOOD("Food"),
    SCENIC("Scenic"),
    WILDLIFE("Wildlife"),
    OTHER("Other");

    private final String name;

    ActivityType(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.name;
    }

    public static ActivityType fromString(String text) {
      for (ActivityType type : ActivityType.values()) {
        if (type.name.equalsIgnoreCase(text)) {
          return type;
        }
      }
      return OTHER;
    }
  }

  public enum ReviewType {
    PUBLIC("Public"),
    PRIVATE("Private"),
    EXPERT("Expert");

    private final String name;

    ReviewType(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }
}
