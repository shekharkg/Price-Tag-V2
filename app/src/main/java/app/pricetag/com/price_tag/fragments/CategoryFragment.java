package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.pkmmte.view.CircularImageView;
import java.util.ArrayList;
import app.pricetag.com.price_tag.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by shekhar on 24/8/14.
 */
public class CategoryFragment extends Fragment {
  String[] nameArr;
  int[] imageArr = {R.drawable.cameras,R.drawable.computers,R.drawable.electronics,R.drawable.bikes
      ,R.drawable.cars,R.drawable.books,R.drawable.lifestyle,R.drawable.baby_products,R.drawable.appliances
      ,R.drawable.entertainment,R.drawable.flower_gifts,R.drawable.sports,R.drawable.health_beauty
      ,R.drawable.home_decor,R.drawable.handicrafts,R.drawable.furniture};


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    nameArr = container.getResources().getStringArray(R.array.product_list);
    return inflater.inflate(R.layout.main_fragment_list_cointainer, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    initCards();
  }

  private void initCards() {

    ArrayList<Card> cards = new ArrayList<Card>();
    for (int i=0;i<16;i++){
      CardExample card = new CardExample(getActivity(),i);
      cards.add(card);
    }

    CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
    AnimationAdapter animCardArrayAdapter;

    CardListView listView = (CardListView) getActivity().findViewById(R.id.card_list);
    if (listView!=null){
      listView.setAdapter(mCardArrayAdapter);
      animCardArrayAdapter = new ScaleInAnimationAdapter(mCardArrayAdapter);
      animCardArrayAdapter.setAbsListView(listView);
      listView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);
    }

  }

  public class CardExample extends Card{

    protected String mTitleMain;
    protected int mImageMain;

    public CardExample(Context context,int position) {
      super(context, R.layout.main_fragment_single_row);
      this.mTitleMain = nameArr[position];
      this.mImageMain = imageArr[position];
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

      //Retrieve elements
      TextView title = (TextView) parent.findViewById(R.id.card_main_inner_simple_title);
      CircularImageView circularImageView = (CircularImageView) parent.findViewById(R.id.imageCategory);

      if (title != null)
        title.setText(mTitleMain);

      if (circularImageView != null)
        circularImageView.setImageResource(mImageMain);

      setOnClickListener(new OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
          Toast.makeText(getContext(), "Click Listener card=" + mTitleMain, Toast.LENGTH_SHORT).show();
        }
      });
    }
  }
}
