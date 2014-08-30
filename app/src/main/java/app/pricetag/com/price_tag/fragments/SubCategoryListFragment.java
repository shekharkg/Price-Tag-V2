package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.pricetag.com.price_tag.MyActivity;
import app.pricetag.com.price_tag.ProductListDetailActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.asynctask.SubCategoryHttpAsyncTask;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by shekhar on 26/8/14.
 */
public class SubCategoryListFragment extends Fragment {
  String categoryUrl;
  String[] categoryIndex;
  static Context context;
  static CardListView listView;
  static CardArrayAdapter mCardArrayAdapter;
  static ArrayList<Card> cards;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    categoryUrl = container.getResources().getString(R.string.subCategoryUrl);
    categoryIndex = container.getResources().getStringArray(R.array.category_list);
    this.context = inflater.getContext();
    return inflater.inflate(R.layout.main_fragment_list_cointainer, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    categoryUrl = categoryUrl + categoryIndex[MyActivity.index];

    listView = (CardListView) getActivity().findViewById(R.id.card_list);
    cards  = new ArrayList<Card>();
    mCardArrayAdapter = new CardArrayAdapter(context, cards);
    Card card = new Card(getActivity());
    card.setInnerLayout(R.layout.loading_view_starting);
    mCardArrayAdapter.add(card);
    listView.setAdapter(mCardArrayAdapter);

    new SubCategoryHttpAsyncTask().execute(categoryUrl);
  }

  public static void subCategoryDao(String jsonString) {
    cards.remove(0);

      try {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("categories");

        for(int i=0; i<jsonArray.length(); i++) {
          JSONObject productObject = jsonArray.getJSONObject(i);
          String subCategoryListName = productObject.getString("name");
          int subCategoryProductCount = Integer.parseInt(productObject.getString("product_count"));
          int subCategoryProductId = Integer.parseInt(productObject.getString("id_category"));
          SubCategoryList card = new SubCategoryList(subCategoryListName,subCategoryProductCount,subCategoryProductId);
          cards.add(card);
          Log.e("skg", subCategoryListName + "-----" + subCategoryProductId + "-----" + subCategoryProductCount);
        }
        AnimationAdapter animCardArrayAdapter;


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

    public static class SubCategoryList extends Card{

      String subCategoryListName;
      int subCategoryProductCount;
      int subCategoryProductId;

      public SubCategoryList(String subCategoryListName, int subCategoryProductCount, int subCategoryProductId) {
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
            Intent intent = new Intent(context,ProductListDetailActivity.class);
            intent.putExtra("subCategoryId", subCategoryProductId);
            intent.putExtra("title", subCategoryListName);
            getContext().startActivity(intent);
          }
        });
      }
    }
  }



