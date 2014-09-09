package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.jorgecastilloprz.pagedheadlistview.PagedHeadListView;
import com.jorgecastilloprz.pagedheadlistview.utils.PageTransformerTypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import app.pricetag.com.price_tag.ProductDetailsActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.asynctask.DetailsHttpASyncTask;
import app.pricetag.com.price_tag.asynctask.ImageHttpAsyncTask;
import app.pricetag.com.price_tag.dao.ConnectedToInternetOrNot;
import app.pricetag.com.price_tag.dao.FeatureObject;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardGridView;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by shekhar on 5/9/14.
 */
public class ProductDetailsFragment extends Fragment {


  private View rootView;
  public ArrayList<Card> cardsDetails;
  public CardListView listViewDetails;
  public CardArrayAdapter mCardArrayAdapterDetails;
  ConnectedToInternetOrNot connectedToInternetOrNot;
  int connected;
  public static String imageStringUrl;
  int imageLoop, featureLoop;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView =  inflater.inflate(R.layout.main_fragment_list_cointainer, container, false);
    return rootView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    new ImageHttpAsyncTask(this).execute(ProductDetailsActivity.imageUrl);
    cardsDetails  = new ArrayList<Card>();
    mCardArrayAdapterDetails = new CardArrayAdapter(getActivity(),cardsDetails);
    listViewDetails = (CardListView) getActivity().findViewById(R.id.card_list);
    listViewDetails.setAdapter(mCardArrayAdapterDetails);
    Card card = new Card(getActivity());
    card.setInnerLayout(R.layout.loading_view_starting);
    card.setShadow(false);
    cardsDetails.add(card);
    connectedToInternetOrNot = new ConnectedToInternetOrNot();
    mCardArrayAdapterDetails.setNotifyOnChange(true);
    imageLoop = 0;
    featureLoop = 0;
  }

  public void imageDao(String jsonResult) {

    JSONObject jsonObject = null;
    try{
      jsonObject = new JSONObject(jsonResult);
      Log.e("IMAGE", jsonResult);
      JSONArray imageArray = jsonObject.getJSONArray("images");
      ProductDetailImage imageCard = new ProductDetailImage(getActivity(), imageArray);
      imageCard.setShadow(false);
      cardsDetails.add(imageCard);
      Title cardDescription = new Title(getActivity());
      cardsDetails.add(cardDescription);
      Card card = new Card(getActivity());
      card.setInnerLayout(R.layout.loading_view_card);
      cardsDetails.remove(0);
      cardsDetails.add(card);
      mCardArrayAdapterDetails.notifyDataSetChanged();
      new DetailsHttpASyncTask(this).execute(ProductDetailsActivity.idUrl);
    }catch (Exception e){
      Card card = new Card(getActivity());
      card.setInnerLayout(R.layout.retry_loading);
      card.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
          connected = connectedToInternetOrNot.ConnectedToInternetOrNot(getActivity());
          if (connected == 1) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new ProductDetailsFragment()).commit();
            Crouton.cancelAllCroutons();
          }
        }
      });
      cardsDetails.remove(0);
      cardsDetails.add(card);
      mCardArrayAdapterDetails.notifyDataSetChanged();
      e.printStackTrace();
    }

  }

  public void detailsDao(String productDetails) {
    try {
      JSONObject productJson  = new JSONObject(productDetails);
      JSONObject features = productJson.getJSONObject("features").getJSONObject("group_features");
      Map<String, List<FeatureObject>> featureMap = new TreeMap<String, List<FeatureObject>>();
      Iterator keys = features.keys();
      while(keys.hasNext()) {
        String key = keys.next().toString();
        JSONArray featureArray = features.getJSONArray(key);
        List<FeatureObject> indFeatureMap = new ArrayList<FeatureObject>();
        for(int i = 0; i < featureArray.length(); i++) {
          JSONObject jsonObject = featureArray.getJSONObject(i);
          String name = jsonObject.getString("name");
          String value = jsonObject.getString("value");
          FeatureObject featureObject = new FeatureObject(name, value);
          indFeatureMap.add(featureObject);
        }
        String keyValue = key.replace("_features", "").replace("_", " ");
        featureMap.put(keyValue, indFeatureMap);
      }
      ProductDetailsSpecifications cardProductDetailsSpecifications = new ProductDetailsSpecifications(getActivity(), featureMap);
      CardHeader header = new CardHeader(getActivity(), R.layout.description);
      cardProductDetailsSpecifications.addCardHeader(header);
      cardsDetails.add(cardProductDetailsSpecifications);
      cardsDetails.remove(2);
      mCardArrayAdapterDetails.notifyDataSetChanged();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  protected class ProductDetailImage extends Card{
    JSONArray imageArray;
    public ProductDetailImage(Context context, JSONArray imageArray) {
      super(context, R.layout.product_images);
      this.imageArray = imageArray;
    }
    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {
      //Retrieve elements
      PagedHeadListView mPagedHeadList = (PagedHeadListView) parent.findViewById(R.id.pagedHeadListView);
      if (imageLoop == 0) {
      for(int i=0; i<imageArray.length(); i++) {
          JSONObject imageObject = null;
          try {
            imageObject = imageArray.getJSONObject(i);
            imageStringUrl = imageObject.getString("large_image");
            mPagedHeadList.addFragmentToHeader(new ImageHeaderFragment(this.getContext()));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
        mPagedHeadList.setHeaderOffScreenPageLimit(imageArray.length() - 1);
        mPagedHeadList.setHeaderPageTransformer(PageTransformerTypes.ACCORDION);
        mPagedHeadList.setAdapter(null);
        imageLoop = 99;
      }
    }
  }
  protected class Title extends Card{
    public Title(Context context) {
      super(context, R.layout.description);
    }
    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {
      TextView textView = (TextView) parent.findViewById(R.id.description);
      textView.setText(ProductDetailsActivity.productName);
    }
  }

  protected class ProductDetailsSpecifications extends Card{
    Map<String, List<FeatureObject>> featureMap;
    public ProductDetailsSpecifications(Context context, Map<String, List<FeatureObject>> featureMap) {
      super(context, R.layout.product_specifications);
      this.featureMap = featureMap;
    }
    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {
      final LinearLayout mySpecs = (LinearLayout) parent.findViewById(R.id.mySpecificationLayout);



      for (Map.Entry<String, List<FeatureObject>> entry : featureMap.entrySet()) {
        //System.out.println(entry.getKey() + "/" + entry.getValue());
        Log.e("", "main key" + entry.getKey());

        LayoutInflater layoutInflater = ((ProductDetailsActivity) getActivity()).getLayoutInflater();
        View tableView = layoutInflater.inflate(R.layout.table_view, null);
        TextView tableTitle = (TextView) tableView.findViewById(R.id.tableTitle);
        LinearLayout tableRow = (LinearLayout) tableView.findViewById(R.id.tableRow);
        tableTitle.setText(entry.getKey());

        List<FeatureObject> indfe = entry.getValue();
        for(FeatureObject indentry : indfe ) {
          //Log.e("", "feature main key" + indentry.getName() + " main value :" + indentry.getValue());
          View tableRowView = layoutInflater.inflate(R.layout.table_row, null);
          TextView columnName = (TextView) tableRowView.findViewById(R.id.textName);
          TextView columnValue = (TextView) tableRowView.findViewById(R.id.textValue);
          columnName.setText(indentry.getName());
          columnValue.setText(indentry.getValue());
          tableRow.addView(tableRowView);
        }
        mySpecs.addView(tableView);
      }
    }
  }

}

