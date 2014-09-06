package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jorgecastilloprz.pagedheadlistview.PagedHeadListView;
import com.jorgecastilloprz.pagedheadlistview.utils.PageTransformerTypes;
import org.json.JSONArray;
import org.json.JSONObject;
import app.pricetag.com.price_tag.ProductDetailsActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.asynctask.DetailsHttpASyncTask;
import app.pricetag.com.price_tag.asynctask.ImageHttpAsyncTask;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by shekhar on 5/9/14.
 */
public class ProductDetailsFragment extends Fragment {

  private PagedHeadListView mPagedHeadList;
  private View rootView;
  View footer;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView =  inflater.inflate(R.layout.product_specifications, container, false);
    return rootView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    new ImageHttpAsyncTask(this).execute(ProductDetailsActivity.imageUrl);
    new DetailsHttpASyncTask(this).execute(ProductDetailsActivity.idUrl);
    mPagedHeadList = (PagedHeadListView) rootView.findViewById(R.id.pagedHeadListView);
    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
    footer = layoutInflater.inflate(R.layout.product_details, null);
    footer.setClickable(false);
    mPagedHeadList.addFooterView(footer);
  }

  public void imageDao(String jsonResult) {

    JSONObject jsonObject = null;
    try{
      jsonObject = new JSONObject(jsonResult);
      Log.e("IMAGE", jsonResult);
      JSONArray imageArray = jsonObject.getJSONArray("images");
      for(int i=0; i<imageArray.length(); i++){
        JSONObject imageObject = imageArray.getJSONObject(i);
        String imageStringUrl = imageObject.getString("large_image");
        mPagedHeadList.addFragmentToHeader(new ImageHeaderFragment(imageStringUrl));
      }
      mPagedHeadList.setHeaderOffScreenPageLimit(imageArray.length()-1);
      mPagedHeadList.setHeaderPageTransformer(PageTransformerTypes.ACCORDION);
      mPagedHeadList.setAdapter(null);
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  public void detailsDao(String jsonResult) {

    JSONObject jsonObject = null;
    Title cardT = new Title(getActivity());
    CardView cardTitle = (CardView) footer.findViewById(R.id.card_title);
    cardTitle.setCard(cardT);
    cardTitle.setVisibility(View.VISIBLE);

    try{
      jsonObject = new JSONObject(jsonResult);
      String stringDesc = jsonObject.getJSONObject("products").getString("desc");
      String check = stringDesc.replaceAll(" ", "");
      if(!check.equals("") || !check.equals(null)){
        Desc cardD = new Desc(getActivity(), stringDesc);
        CardView cardDesc = (CardView) footer.findViewById(R.id.card_desc);
        cardDesc.setCard(cardD);
        cardDesc.setVisibility(View.VISIBLE);
      }
    }catch (Exception e){
      e.printStackTrace();
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
  public class Desc extends Card{
    String desc;
    public Desc(Context context, String desc) {
      super(context, R.layout.description2);
      this.desc = desc;
    }
    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {
      TextView textView = (TextView) parent.findViewById(R.id.description);
      textView.setText(Html.fromHtml(desc));
    }
  }
}

