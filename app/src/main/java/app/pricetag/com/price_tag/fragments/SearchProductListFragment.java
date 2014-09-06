package app.pricetag.com.price_tag.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.NumberFormat;
import java.util.ArrayList;

import app.pricetag.com.price_tag.ProductDetailsActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.SearchActivity;
import app.pricetag.com.price_tag.asynctask.SearchProductListHttpAsyncTask;
import app.pricetag.com.price_tag.dao.ConnectedToInternetOrNot;
import app.pricetag.com.price_tag.listners.SPLFScrollListener;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by shekhar on 4/9/14.
 */
public class SearchProductListFragment extends Fragment {
  Context context;
  public int start;
  public ArrayList<Card> cards;
  public CardListView listView;
  public CardArrayAdapter mCardArrayAdapter;
  AnimationAdapter animCardArrayAdapter;
  Activity activity;
  ConnectedToInternetOrNot connectedToInternetOrNot;
  int connected;
  SearchProductListFragment fragment;
  String baseUrl;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.context = inflater.getContext();
    start = 0;
    return inflater.inflate(R.layout.main_fragment_list_cointainer, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    activity = getActivity();

    cards  = new ArrayList<Card>();
    mCardArrayAdapter = new CardArrayAdapter(context,cards);
    animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter);
    listView = (CardListView) getActivity().findViewById(R.id.card_list);
    listView.setAdapter(mCardArrayAdapter);
    fragment = this;
    listView.setOnScrollListener(new SPLFScrollListener(fragment));
    connectedToInternetOrNot = new ConnectedToInternetOrNot();
    baseUrl = fragment.getResources().getString(R.string.search_product_list);
  }


  public void searchProductListDao(String jsonString) {
    if(start == 25){
      try{
        cards.remove(0);
      }catch (IndexOutOfBoundsException e){
        e.printStackTrace();
      }
      animCardArrayAdapter.setAbsListView(listView);
      listView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);
    }
    else{
      cards.remove(start-25);
    }


    JSONObject jsonObject = null;
    if(jsonString == ""){
      Toast.makeText(context,"No item found!!!" + "\n" + "Try something else.",Toast.LENGTH_SHORT).show();
      mCardArrayAdapter.clear();
    } else {
      try {
        jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("products");
        //totalProductCount = Integer.parseInt(jsonObject.getString("count"));
        for(int i=0; i<jsonArray.length(); i++) {
          JSONObject productObject = jsonArray.getJSONObject(i);
          int productId = Integer.parseInt(productObject.getString("id_product"));
          String productName = productObject.getString("name");
          int productPrice = Integer.parseInt(productObject.getString("price"));
          String productImage = productObject.getString("image");
          float productRating = Float.parseFloat(productObject.getString("average_rating"));
          int productSupplierCount = Integer.parseInt(productObject.getString("supplier_count"));

          ProductList card = new ProductList(context, productId, productName, productPrice, productImage,
              productRating, productSupplierCount);
          mCardArrayAdapter.add(card);
          mCardArrayAdapter.setNotifyOnChange(true);
          mCardArrayAdapter.notifyDataSetChanged();
        }

      } catch (JSONException e) {
        Card card = new Card(activity);
        card.setInnerLayout(R.layout.retry_loading);
        card.setOnClickListener(new Card.OnCardClickListener() {
          @Override
          public void onClick(Card card, View view) {
            connected = connectedToInternetOrNot.ConnectedToInternetOrNot(activity);
            if (connected == 1) {
              if (cards.size() < 25) {
                SearchCategoryFragment searchCategoryFragment = new SearchCategoryFragment();
                FragmentTransaction transaction = SearchActivity.manager.beginTransaction();
                transaction.replace(R.id.content_frame_product_list, searchCategoryFragment,"searchCategoryFragment");
                transaction.commit();
              } else {
                cards.remove(start - 25);
                start = start - 25;
                Card cardR = new Card(fragment.getActivity());
                cardR.setInnerLayout(R.layout.loading_view_card);
                fragment.mCardArrayAdapter.add(cardR);
                fragment.mCardArrayAdapter.setNotifyOnChange(true);
                fragment.mCardArrayAdapter.notifyDataSetChanged();
                new SearchProductListHttpAsyncTask(fragment).execute(SearchActivity.searchListUrl +
                    SearchActivity.searchKey + "&limit=25&start=" + start);
                start += 25;
              }
              Crouton.cancelAllCroutons();
              mCardArrayAdapter.setNotifyOnChange(true);
              mCardArrayAdapter.notifyDataSetChanged();
            }
          }
        });
        mCardArrayAdapter.add(card);
        e.printStackTrace();
      }
    }
  }

  public class ProductList extends Card{

    int productId;
    String productName;
    int productPrice;
    String productImage;
    float productRating;
    int productSupplierCount;
    Context context;

    public ProductList(Context context, int productId, String productName, int productPrice,
                       String productImage, float productRating, int productSupplierCount) {
      super(context, R.layout.search_product_list_single_row);
      this.productId = productId;
      this.productName = productName;
      this.productPrice = productPrice;
      this.productImage = productImage;
      this.productRating = productRating;
      this.productSupplierCount = productSupplierCount;
      this.context = context;


    }


    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {

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
        price.setText("Rs. " + NumberFormat.getNumberInstance().format(productPrice));
      }
      setOnClickListener(new OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
          connected = connectedToInternetOrNot.ConnectedToInternetOrNot(activity);
          if(connected == 1) {
            Crouton.cancelAllCroutons();
            SearchActivity.imm.hideSoftInputFromWindow(SearchActivity.mSearchView.getWindowToken(), 0);
            Intent intent = new Intent(context,ProductDetailsActivity.class);
            intent.putExtra("productId", productId);
            intent.putExtra("productName",productName);
            Crouton.cancelAllCroutons();
            context.startActivity(intent);
          }
        }
      });
    }
  }
}

