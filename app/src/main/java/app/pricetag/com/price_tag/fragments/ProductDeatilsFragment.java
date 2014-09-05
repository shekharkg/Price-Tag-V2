package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.text.NumberFormat;
import java.util.ArrayList;

import app.pricetag.com.price_tag.ProductDetails;
import app.pricetag.com.price_tag.R;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by shekhar on 5/9/14.
 */
public class ProductDeatilsFragment extends Fragment {

  CardListView cardListView;
  CardArrayAdapter mCardArrayAdapter;
  ArrayList<Card> cards;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.product_specifications, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    cardListView = (CardListView) getActivity().findViewById(R.id.card_list);
    cards  = new ArrayList<Card>();
    initCard();
  }

  /**
   * This method builds a simple card
   */
  private void initCard() {

    for(int i=0; i<20; i++){
      ProductDetail card = new ProductDetail(getActivity(),12345,"XYZ",99999,25,"xyz",2.5f,7);
      cards.add(card);
    }
    mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
    if (cardListView != null) {
      cardListView.setAdapter(mCardArrayAdapter);
    }
  }


  public class ProductDetail extends Card{

    int productId;
    String productName;
    int productPrice;
    int saveUpTO;
    String productImage;
    float productRating;
    int productSupplierCount;
    Context context;

    public ProductDetail(Context context, int productId, String productName, int productPrice, int saveUpTO,
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
        Ion.with(circularImageView).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).load(productImage);
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

        }
      });
    }
  }

}

