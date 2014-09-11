package app.pricetag.com.price_tag.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;

import app.pricetag.com.price_tag.ProductDetailsActivity;
import app.pricetag.com.price_tag.ProductListDetailActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.adapters.DialogAdapter;
import app.pricetag.com.price_tag.asynctask.ProductListHttpAsyncTask;
import app.pricetag.com.price_tag.dao.ConnectedToInternetOrNot;
import app.pricetag.com.price_tag.listners.PLFScrollListener;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by shekhar on 28/8/14.
 */
public class ProductListFragment extends Fragment {
  Context context;
  public int start;
  public int totalProductCount;
  public ArrayList<Card> cards;
  public CardListView listView;
  public CardArrayAdapter mCardArrayAdapter;
  AnimationAdapter animCardArrayAdapter;
  Activity activity;
  ConnectedToInternetOrNot connectedToInternetOrNot;
  int connected;
  ProductListFragment fragment;

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
    listView.setOnScrollListener(new PLFScrollListener(fragment));
    connectedToInternetOrNot = new ConnectedToInternetOrNot();
  }



  protected void sortmenu(){
    final ImageView fabIconNew = new ImageView(activity);
    fabIconNew.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_collections_sort_by_size));
    final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(activity)
        .setContentView(fabIconNew)
        .setBackgroundDrawable(R.drawable.sort_button)
        .build();
    SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(activity);
    rLSubBuilder.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.sort_button));

    fabIconNew.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        final String[] sortName = activity.getResources().getStringArray(R.array.dialog_list);
        final String[] sortOrderName = activity.getResources().getStringArray(R.array.dialog_sort_order);

        // custom dialog
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View viewD = inflater.inflate(R.layout.dialog_listview, null);
        dialogBuilder.setView(viewD);
        ListView listViewDialog = (ListView) viewD.findViewById(R.id.listView);
        DialogAdapter dialogAdapter = new DialogAdapter(context,sortName);
        listViewDialog.setAdapter(dialogAdapter);
        final Dialog dialog = dialogBuilder.show();

        listViewDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            connected = connectedToInternetOrNot.ConnectedToInternetOrNot(activity);
            if (connected == 1) {
              ProductListDetailActivity.sortOrder = "&" + sortOrderName[position] + "&limit=25&start=";
              ProductListDetailActivity.dialogSelectedIndex = position;
              activity.getFragmentManager().beginTransaction().replace(R.id.content_frame, new ProductListFragment()).commit();
              rightLowerButton.detach();
              Crouton.cancelAllCroutons();
            }
            dialog.dismiss();

          }
        });
      }
    });
  }

  public void productListDao(String jsonString) {
    if(start == 25){
      try{
        cards.remove(0);
      }catch (IndexOutOfBoundsException e){
        e.printStackTrace();
      }
      sortmenu();
      animCardArrayAdapter.setAbsListView(listView);
      listView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);
    }
    else{
      cards.remove(start-25);
    }


      JSONObject jsonObject = null;
      try {
        jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("products");
        totalProductCount = Integer.parseInt(jsonObject.getString("product_count"));
        for(int i=0; i<jsonArray.length(); i++) {
          JSONObject productObject = jsonArray.getJSONObject(i);

          int id_category = Integer.parseInt(productObject.getString("id_category"));
          int productId = Integer.parseInt(productObject.getString("id_product"));
          String productName = productObject.getString("name");
          int productPrice = Integer.parseInt(productObject.getString("price"));
          int saveUpTO = Integer.parseInt(productObject.getString("save_upto"));
          String productImage = productObject.getString("big_image");
          float productRating = Float.parseFloat(productObject.getString("average_rating"));
          int productSupplierCount = Integer.parseInt(productObject.getString("supplier_count"));

          if(id_category == ProductListDetailActivity.subCategoryId){
            ProductList card = new ProductList(context, productId, productName, productPrice, saveUpTO, productImage,
                productRating, productSupplierCount);
            mCardArrayAdapter.add(card);
            mCardArrayAdapter.setNotifyOnChange(true);
            mCardArrayAdapter.notifyDataSetChanged();
          }
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
                Fragment fragment = new ProductListFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
              } else {
                cards.remove(start - 25);
                start = start - 25;
                Card cardR = new Card(fragment.getActivity());
                cardR.setInnerLayout(R.layout.loading_view_card);
                fragment.mCardArrayAdapter.add(cardR);
                fragment.mCardArrayAdapter.setNotifyOnChange(true);
                fragment.mCardArrayAdapter.notifyDataSetChanged();
                new ProductListHttpAsyncTask(fragment).execute(ProductListDetailActivity.productListUrl +
                    ProductListDetailActivity.sortOrder + start);
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

    public class ProductList extends Card{

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
      public void setupInnerViewElements(final ViewGroup parent, View view) {

        //Retrieve elements
        ImageView circularImageView = (ImageView) parent.findViewById(R.id.product_image);
        TextView title = (TextView) parent.findViewById(R.id.product_title);
        RatingBar ratingBar = (RatingBar) parent.findViewById(R.id.product_rating);
        TextView seller = (TextView) parent.findViewById(R.id.product_seller);
        TextView price = (TextView) parent.findViewById(R.id.product_price);
        TextView saveupto = (TextView) parent.findViewById(R.id.product_saveupto);

        if (title != null){
          Ion.with(circularImageView).placeholder(R.drawable.loading).error(R.drawable.loading).load(productImage);
          title.setText(productName);
          ratingBar.setRating(productRating);
          seller.setText(productSupplierCount + " seller found");
          price.setText("Rs. " + NumberFormat.getNumberInstance().format(productPrice));
          saveupto.setText("Save upto : Rs. " + NumberFormat.getNumberInstance().format(saveUpTO));
          if(saveUpTO == 0){
            saveupto.setVisibility(View.INVISIBLE);
          }
        }
        setOnClickListener(new OnCardClickListener() {
          @Override
          public void onClick(Card card, View view) {
            connected = connectedToInternetOrNot.ConnectedToInternetOrNot(activity);
            if(connected == 1) {
              Crouton.cancelAllCroutons();
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

