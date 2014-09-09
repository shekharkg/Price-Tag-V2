package app.pricetag.com.price_tag.dao;

/**
 * Created by shekhar on 9/9/14.
 */
public class CityWisePriceObject {
  String city;
  String price;
  String storeUrl;

  public CityWisePriceObject(String city, String price, String storeUrl) {
    this.city = city;
    this.price = price;
    this.storeUrl = storeUrl;
  }

  public String getCity() {
    return city;
  }

  public String getPrice() {
    return price;
  }

  public String getStoreUrl() {
    return storeUrl;
  }
}
