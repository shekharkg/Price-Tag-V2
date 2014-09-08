package app.pricetag.com.price_tag.model;

/**
 * Created by shekhar on 8/9/14.
 */
public class FeatureObject {
  String name;
  String value;

  public FeatureObject(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }
}
