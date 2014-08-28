package app.pricetag.com.price_tag.dao;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import app.pricetag.com.price_tag.ProductListDetailActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.fragments.ProductListFragment;
import app.pricetag.com.price_tag.fragments.SubCategoryListFragment;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by shekhar on 28/8/14.
 */
public class ProductListDao {

  Context context;
  public ProductListDao(String jsonString, Context context) {
    this.context = context;
    ArrayList<Card> cards = new ArrayList<Card>();

    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject(jsonString);
      JSONArray jsonArray = jsonObject.getJSONArray("products");
      ProductListFragment.totalProductCount = Integer.parseInt(jsonObject.getString("product_count"));
      for(int i=0; i<jsonArray.length(); i++) {
        JSONObject productObject = jsonArray.getJSONObject(i);

        int productId = Integer.parseInt(productObject.getString("id_product"));
        String productName = productObject.getString("name");
        int productPrice = Integer.parseInt(productObject.getString("price"));
        int saveUpTO = Integer.parseInt(productObject.getString("save_upto"));
        String productImage = productObject.getString("big_image");
        float productRating = Float.parseFloat(productObject.getString("average_rating"));
        int productSupplierCount = Integer.parseInt(productObject.getString("supplier_count"));

        ProductList card = new ProductList(context, productId, productName, productPrice, saveUpTO, productImage,
            productRating, productSupplierCount);
        cards.add(card);

        //Log.e("SKG","ID: " + productId + " NAME: " + productName + " PRICE: " + productPrice + " SAVE: "
        //+ saveUpTO + " IMAGE: " + productImage + " RATING: " + productRating + " SUPPLIER: " + productSupplierCount);
      }

      CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(context,cards);
      AnimationAdapter animCardArrayAdapter;

      CardListView listView = (CardListView) ProductListFragment.c.findViewById(R.id.card_list);
      if (listView!=null){
        listView.setAdapter(mCardArrayAdapter);
        animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter);
        animCardArrayAdapter.setAbsListView(listView);
        listView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);
      }

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public class ProductList extends Card{

    int productId;
    String productName;
    int productPrice;
    int saveUpTO;
    String productImage;
    float productRating;
    int productSupplierCount;

    public ProductList(Context context, int productId, String productName, int productPrice, int saveUpTO,
                       String productImage, float productRating, int productSupplierCount) {
      super(context, R.layout.product_list_single_row);
      this.productId = productId;
      this.productName = productName;
      this.productPrice = productPrice;
      this.saveUpTO = saveUpTO;
      this.productImage = productImage;
      this.productRating = productRating;
      this.productSupplierCount = productSupplierCount;


    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

      //Retrieve elements
      ImageView circularImageView = (ImageView) parent.findViewById(R.id.product_image);
      TextView title = (TextView) parent.findViewById(R.id.product_title);
      RatingBar ratingBar = (RatingBar) parent.findViewById(R.id.product_rating);
      TextView seller = (TextView) parent.findViewById(R.id.product_seller);
      TextView price = (TextView) parent.findViewById(R.id.product_price);
      TextView saveupto = (TextView) parent.findViewById(R.id.product_saveupto);

      if (title != null){
        Ion.with(circularImageView).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).load(productImage);
        title.setText(productName);
        ratingBar.setRating(productRating);
        seller.setText(productSupplierCount + " seller found");
        price.setText("Rs. " + productPrice);
        saveupto.setText("Save upto : Rs. " + saveUpTO);
        if(saveUpTO == 0){
        }
      }


      setOnClickListener(new OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
          Toast.makeText(context, "productId is : " + productId + " Name: " + productName, Toast.LENGTH_SHORT).show();
        }
      });
    }
  }
}
