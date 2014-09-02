package app.pricetag.com.price_tag.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;
import com.pkmmte.view.CircularImageView;
import java.util.ArrayList;

import app.pricetag.com.price_tag.MyActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.dao.ConnectedToInternetOrNot;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by shekhar on 24/8/14.
 */
public class CategoryFragment extends Fragment {
  String[] nameArr;
  ArrayList<Card> cards = new ArrayList<Card>();
  ConnectedToInternetOrNot connectedToInternetOrNot;
  int connected;

  int[] imageArr = {R.drawable.mobiles,R.drawable.cameras,R.drawable.computers,R.drawable.electronics,R.drawable.bikes
      ,R.drawable.cars,R.drawable.books,R.drawable.lifestyle,R.drawable.baby_products,R.drawable.appliances
      ,R.drawable.entertainment,R.drawable.flower_gifts,R.drawable.sports,R.drawable.health_beauty
      ,R.drawable.home_decor,R.drawable.handicrafts,R.drawable.furniture};


  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
  }
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    nameArr = container.getResources().getStringArray(R.array.product_list);
    connectedToInternetOrNot = new ConnectedToInternetOrNot();
    return inflater.inflate(R.layout.main_fragment_list_cointainer, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    initCards();
  }

  private void initCards() {


    for (int i=0;i<17;i++){
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
    protected int mImageMain, selectedCategoryIndex;

    public CardExample(Context context,int position) {
      super(context, R.layout.main_fragment_single_row);
      this.mTitleMain = nameArr[position];
      this.mImageMain = imageArr[position];
      this.selectedCategoryIndex = position;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

      //Retrieve elements
      TextView title = (TextView) parent.findViewById(R.id.titleCategory);
      CircularImageView circularImageView = (CircularImageView) parent.findViewById(R.id.imageCategory);

      if (title != null)
        title.setText(mTitleMain);

      if (circularImageView != null)
        circularImageView.setImageResource(mImageMain);

      setOnClickListener(new OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
          connected = connectedToInternetOrNot.ConnectedToInternetOrNot((MyActivity) getActivity());
          if(connected == 1){
            ((MyActivity) getActivity()).getActionBar().setTitle(mTitleMain);
            MyActivity.index = selectedCategoryIndex;
            Fragment fragment = new SubCategoryListFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            MyActivity.mTitle = mTitleMain;
            MyActivity.fragmentCount =1;
            Crouton.cancelAllCroutons();
          }
        }
      });
    }
  }
}
