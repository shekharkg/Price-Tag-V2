package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.pricetag.com.price_tag.ProductListDetailActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.asynctask.ProductListHttpAsyncTask;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.listener.SwipeOnScrollListener;

/**
 * Created by shekhar on 28/8/14.
 */
public class ProductListFragment extends Fragment {
  static Context context;
  static int start;
  public static ArrayList<Card> cards;
  static CardListView listView;
  static CardArrayAdapter mCardArrayAdapter;
  static AnimationAdapter animCardArrayAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.context = inflater.getContext();
    start = 0;
    return inflater.inflate(R.layout.main_fragment_list_cointainer, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    Toast.makeText(context, ProductListDetailActivity.productListUrl, Toast.LENGTH_SHORT).show();
    cards  = new ArrayList<Card>();
    mCardArrayAdapter = new CardArrayAdapter(context,cards);
    animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter);
    listView = (CardListView) getActivity().findViewById(R.id.card_list);
    listView.setAdapter(mCardArrayAdapter);
    animCardArrayAdapter.setAbsListView(listView);



    listView.setOnScrollListener(
        new SwipeOnScrollListener() {
          @Override
          public void onScrollStateChanged(AbsListView view, int scrollState) {
            super.onScrollStateChanged(view,scrollState);
          }

          @Override
          public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //what is the bottom iten that is visible
            int lastInScreen = firstVisibleItem + visibleItemCount;
            Log.d("TAG", "lastInScreen." + lastInScreen);
            if(lastInScreen == start){
              new ProductListHttpAsyncTask().execute(ProductListDetailActivity.productListUrl + "&limit=25&start=" + start);
              if(start==0){
                Card card = new Card(getActivity());
                card.setInnerLayout(R.layout.loading_view_starting);
                mCardArrayAdapter.add(card);
              }
              else{
                Card card = new Card(getActivity());
                card.setInnerLayout(R.layout.loading_view_card);
                mCardArrayAdapter.add(card);
              }
              start += 25;
            }
          }
        });


  }


  public static void productListDao(String jsonString) {
    if(start == 25){
      cards.remove(0);
    }
    else{
      cards.remove(start-25);
    }


      JSONObject jsonObject = null;
      try {
        jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("products");
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
          mCardArrayAdapter.add(card);
          mCardArrayAdapter.setNotifyOnChange(true);
          mCardArrayAdapter.notifyDataSetChanged();

          //Log.e("SKG","ID: " + productId + " NAME: " + productName + " PRICE: " + productPrice + " SAVE: "
          //+ saveUpTO + " IMAGE: " + productImage + " RATING: " + productRating + " SUPPLIER: " + productSupplierCount);
        }

      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    public static class ProductList extends Card{

      int productId;
      String productName;
      int productPrice;
      int saveUpTO;
      String productImage;
      float productRating;
      int productSupplierCount;
      Context context;

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
        this.context = context;


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

