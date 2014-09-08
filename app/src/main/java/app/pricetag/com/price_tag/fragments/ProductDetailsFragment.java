package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.jorgecastilloprz.pagedheadlistview.PagedHeadListView;
import com.jorgecastilloprz.pagedheadlistview.utils.PageTransformerTypes;
import com.koushikdutta.ion.Ion;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import app.pricetag.com.price_tag.ProductDetailsActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.asynctask.DetailsHttpASyncTask;
import app.pricetag.com.price_tag.asynctask.ImageHttpAsyncTask;
import app.pricetag.com.price_tag.model.FeatureObject;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by shekhar on 5/9/14.
 */
public class ProductDetailsFragment extends Fragment {

  private PagedHeadListView mPagedHeadList;
  private View rootView;
  public ArrayList<Card> cardsDetails;
  public CardListView listViewDetails;
  public CardArrayAdapter mCardArrayAdapterDetails;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView =  inflater.inflate(R.layout.main_fragment_list_cointainer, container, false);
    return rootView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    new ImageHttpAsyncTask(this).execute(ProductDetailsActivity.imageUrl);
    new DetailsHttpASyncTask(this).execute(ProductDetailsActivity.idUrl);
    cardsDetails  = new ArrayList<Card>();
    mCardArrayAdapterDetails = new CardArrayAdapter(getActivity(),cardsDetails);
    listViewDetails = (CardListView) getActivity().findViewById(R.id.card_list);
    listViewDetails.setAdapter(mCardArrayAdapterDetails);
    Card card = new Card(getActivity());
    card.setInnerLayout(R.layout.loading_view_starting);
    card.setShadow(false);
    cardsDetails.add(card);
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
    }catch (Exception e){
      e.printStackTrace();
    }
    Title cardDescriptipon = new Title(getActivity());
    cardsDetails.add(cardDescriptipon);
    cardsDetails.remove(0);
    mCardArrayAdapterDetails.setNotifyOnChange(true);
    mCardArrayAdapterDetails.notifyDataSetChanged();
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

      for (Map.Entry<String, List<FeatureObject>> entry : featureMap.entrySet()) {
        //System.out.println(entry.getKey() + "/" + entry.getValue());
        Log.e("", "main key" + entry.getKey());
        List<FeatureObject> indfe = entry.getValue();
        for(FeatureObject indentry : indfe ) {
          Log.e("", "feature main key" + indentry.getName() + " main value :" + indentry.getValue());
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  public class ProductDetailImage extends Card{

    JSONArray imageArray;
    public ProductDetailImage(Context context, JSONArray imageArray) {
      super(context, R.layout.product_specifications);
      this.imageArray = imageArray;
    }


    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {

      //Retrieve elements
      mPagedHeadList = (PagedHeadListView) parent.findViewById(R.id.pagedHeadListView);

      for(int i=0; i<imageArray.length(); i++){
        JSONObject imageObject = null;
        try {
          imageObject = imageArray.getJSONObject(i);
          String imageStringUrl = imageObject.getString("large_image");
          mPagedHeadList.addFragmentToHeader(new ImageHeaderFragment(imageStringUrl));
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
      mPagedHeadList.setHeaderOffScreenPageLimit(imageArray.length() - 1);
      mPagedHeadList.setHeaderPageTransformer(PageTransformerTypes.ACCORDION);
      mPagedHeadList.setAdapter(null);
      setOnClickListener(new OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {

        }
      });
    }
  }
  public class Title extends Card{
    public Title(Context context) {
      super(context, R.layout.description);
    }
    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {
      TextView textView = (TextView) parent.findViewById(R.id.description);
      textView.setText(ProductDetailsActivity.productName);
    }
  }

}

