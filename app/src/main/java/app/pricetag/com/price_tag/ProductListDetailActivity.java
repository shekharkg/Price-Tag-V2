package app.pricetag.com.price_tag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import app.pricetag.com.price_tag.fragments.ProductListFragment;

/**
 * Created by shekhar on 28/8/14.
 */
public class ProductListDetailActivity extends Activity {
  int subCategoryId;
  public static String productListUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_list_detail);

    Intent intent = getIntent();
    if(intent!=null){
      subCategoryId = intent.getIntExtra("subCategoryId", 1);
    }
    productListUrl = getResources().getString(R.string.productListUrl) + subCategoryId;
    getFragmentManager().beginTransaction().add(R.id.content_frame_product_list,new ProductListFragment()).commit();
    Toast.makeText(this,productListUrl,Toast.LENGTH_SHORT).show();


  }

}
