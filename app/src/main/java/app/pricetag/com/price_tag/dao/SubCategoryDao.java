package app.pricetag.com.price_tag.dao;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.pricetag.com.price_tag.MyActivity;
import app.pricetag.com.price_tag.ProductListDetailActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.fragments.SubCategoryListFragment;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by shekhar on 27/8/14.
 */
public class SubCategoryDao {

  Context context;

  public SubCategoryDao(String jsonString, Context context) {
    this.context = context;
    ArrayList<Card> cards = new ArrayList<Card>();
    try {
      JSONObject jsonObject = new JSONObject(jsonString);
      JSONArray jsonArray = jsonObject.getJSONArray("categories");

      for(int i=0; i<jsonArray.length(); i++) {
        JSONObject productObject = jsonArray.getJSONObject(i);
        String subCategoryListName = productObject.getString("name");
        int subCategoryProductCount = Integer.parseInt(productObject.getString("product_count"));
        int subCategoryProductId = Integer.parseInt(productObject.getString("id_category"));
        SubCategoryList card = new SubCategoryList(context, subCategoryListName,subCategoryProductCount,subCategoryProductId);
        cards.add(card);
        Log.e("skg", subCategoryListName + "-----" + subCategoryProductId + "-----" + subCategoryProductCount);
      }
      CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(context,cards);
      AnimationAdapter animCardArrayAdapter;

      CardListView listView = (CardListView) SubCategoryListFragment.c.findViewById(R.id.card_list);
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

  public class SubCategoryList extends Card{

    String subCategoryListName;
    int subCategoryProductCount;
    int subCategoryProductId;

    public SubCategoryList(Context context, String subCategoryListName, int subCategoryProductCount, int subCategoryProductId) {
      super(context, R.layout.sub_category_single_row);
      this.subCategoryListName = subCategoryListName;
      this.subCategoryProductCount = subCategoryProductCount;
      this.subCategoryProductId = subCategoryProductId;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

      //Retrieve elements
      TextView title = (TextView) parent.findViewById(R.id.titleSubCategory);

      if (title != null)
        title.setText(subCategoryListName + " (" + subCategoryProductCount + ")");

      setOnClickListener(new OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
          Intent intent = new Intent(SubCategoryListFragment.c,ProductListDetailActivity.class);
          intent.putExtra("subCategoryId", subCategoryProductId);
          getContext().startActivity(intent);
          Toast.makeText(context,"subCategoryProductId is : " + subCategoryProductId,Toast.LENGTH_SHORT).show();
        }
      });
    }
  }
}



