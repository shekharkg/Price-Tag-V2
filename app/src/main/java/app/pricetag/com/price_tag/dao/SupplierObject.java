package app.pricetag.com.price_tag.dao;

/**
 * Created by shekhar on 9/9/14.
 */
public class SupplierObject {
  String name;
  String url;
  String price;
  String storeImage;
  String stockInfo;

  public SupplierObject(String name, String url, String price, String storeImage, String stockInfo) {
    this.name = name;
    this.url = url;
    this.price = price;
    this.storeImage = storeImage;
    this.stockInfo = stockInfo;
  }

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public String getPrice() {
    return price;
  }

  public String getStoreImage() {
    return storeImage;
  }

  public String getStockInfo() {
    return stockInfo;
  }
}
