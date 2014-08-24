package app.pricetag.com.price_tag.dao;

/**
 * Created by shekhar on 24/8/14.
 */
public class CategoryDao {
  public String productName;
  public int productImage;

  public CategoryDao(String productName, int productImage) {
    this.productName = productName;
    this.productImage = productImage;
  }
}
