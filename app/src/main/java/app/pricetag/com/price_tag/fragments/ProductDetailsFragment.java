package app.pricetag.com.price_tag.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jorgecastilloprz.pagedheadlistview.PagedHeadListView;
import com.jorgecastilloprz.pagedheadlistview.utils.PageTransformerTypes;
import com.koushikdutta.ion.Ion;

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
import app.pricetag.com.price_tag.dao.CityWisePriceObject;
import app.pricetag.com.price_tag.dao.ConnectedToInternetOrNot;
import app.pricetag.com.price_tag.dao.FeatureObject;
import app.pricetag.com.price_tag.dao.SupplierObject;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
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
  int imageLoop, featureLoop, sellerLoop;
  ProductDetailsFragment fragment;

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
    fragment = this;
    Card card = new Card(getActivity());
    card.setInnerLayout(R.layout.loading_view_starting);
    card.setShadow(false);
    cardsDetails.add(card);
    connectedToInternetOrNot = new ConnectedToInternetOrNot();
    mCardArrayAdapterDetails.setNotifyOnChange(true);
    imageLoop = 0;
    featureLoop = 0;
    sellerLoop = 0;
  }

  public void imageDao(String jsonResult) {

    JSONObject jsonObject = null;
    try{
      jsonObject = new JSONObject(jsonResult);
//      Log.e("IMAGE", jsonResult);
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

      try{
        String prodCategory = productJson.getJSONObject("products").getString("category");
        prodCategory = prodCategory.replace(":","").replace(" ","");
        String bikes = "Bikes";
        String cars = "Cars";
        if(!prodCategory.equals(bikes) && !prodCategory.equals(cars)) {
          Log.e("Category If:", "+" + prodCategory + "+");
          JSONArray supplierDetails = productJson.getJSONObject("suppliers").getJSONArray("supplier_details");
          List<SupplierObject> supplierList = new ArrayList<SupplierObject>();
          if(supplierDetails.length() > 0){
            for(int i=0; i<supplierDetails.length(); i++){
              JSONObject supplierDetailsJSONObject = supplierDetails.getJSONObject(i);
              String name = supplierDetailsJSONObject.getString("name");
              String url = supplierDetailsJSONObject.getString("url");
              String price = supplierDetailsJSONObject.getString("price");
              String storeImage = supplierDetailsJSONObject.getString("store_image");
              String stockInfo = supplierDetailsJSONObject.getString("stock_information");
              SupplierObject supplierObject = new SupplierObject(name, url, price, storeImage, stockInfo);
              supplierList.add(supplierObject);
              //Log.e("Suppliers", "\nname :" + name + "\nurl :" + url + "\nprice :" + price + "\nimage :" + storeImage + "\nStock : " +stockInfo);
            }
            ProductSuppliers productSuppliersCard = new ProductSuppliers(getActivity(),supplierList);
            CardHeader header = new CardHeader(getActivity(), R.layout.seller);
            productSuppliersCard.addCardHeader(header);
            cardsDetails.add(productSuppliersCard);
            cardsDetails.remove(2);
            mCardArrayAdapterDetails.notifyDataSetChanged();
          } else {
            cardsDetails.remove(2);
            mCardArrayAdapterDetails.notifyDataSetChanged();
          }
        } else {
          Log.e("Category else:", "+" + prodCategory + "+");
          JSONArray cityWisePrice = productJson.getJSONArray("city_wise_price_details");
          List<CityWisePriceObject> cityWisePriceObjectList = new ArrayList<CityWisePriceObject>();
          if(cityWisePrice.length() > 0){
            for(int i=0; i<cityWisePrice.length(); i++){
              JSONObject cityWiseJSJsonObject = cityWisePrice.getJSONObject(i);
              String city = cityWiseJSJsonObject.getString("city");
              String price = cityWiseJSJsonObject.getString("price");
              String storeUrl = cityWiseJSJsonObject.getString("store_url");
              CityWisePriceObject cityWisePriceObject = new CityWisePriceObject(city, price, storeUrl);
              cityWisePriceObjectList.add(cityWisePriceObject);
              //Log.e("Suppliers", "\ncity :" + city + "\nprice :" + price + "\nstoreUrl :" + storeUrl);
            }
            ProductCity productCityCard = new ProductCity(getActivity(),cityWisePriceObjectList);
            CardHeader header = new CardHeader(getActivity(), R.layout.city_wise);
            productCityCard.addCardHeader(header);
            cardsDetails.add(productCityCard);
            cardsDetails.remove(2);
            mCardArrayAdapterDetails.notifyDataSetChanged();
          } else {
            cardsDetails.remove(2);
            mCardArrayAdapterDetails.notifyDataSetChanged();
          }
        }
      }catch (Exception e){
        e.printStackTrace();
      }


      //================================================================================================
      //****************************************************************************************************

      try{
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
        ProductDetailsSpecifications cardProductDetailsSpecifications =
            new ProductDetailsSpecifications(getActivity(), featureMap);
        CardHeader header = new CardHeader(getActivity(), R.layout.description);
        cardProductDetailsSpecifications.addCardHeader(header);
        cardsDetails.add(cardProductDetailsSpecifications);
        mCardArrayAdapterDetails.notifyDataSetChanged();
      } catch (Exception e){
        e.printStackTrace();
      }

    } catch (JSONException e) {
      Card card = new Card(getActivity());
      card.setInnerLayout(R.layout.retry_loading);
      card.setOnClickListener(new Card.OnCardClickListener() {
        @Override
        public void onClick(Card card, View view) {
          connected = connectedToInternetOrNot.ConnectedToInternetOrNot(getActivity());
          if (connected == 1) {
            new DetailsHttpASyncTask(fragment).execute(ProductDetailsActivity.idUrl);
            Crouton.cancelAllCroutons();
            Card cardR = new Card(getActivity());
            cardR.setInnerLayout(R.layout.loading_view_card);
            cardsDetails.remove(2);
            cardsDetails.add(cardR);
            mCardArrayAdapterDetails.notifyDataSetChanged();
          }
        }
      });
      cardsDetails.remove(2);
      cardsDetails.add(card);
      mCardArrayAdapterDetails.notifyDataSetChanged();
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
            mPagedHeadList.addFragmentToHeader(new ImageHeaderFragment());
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
      int child = mySpecs.getChildCount();
      if (child == 0)
        featureLoop = 0;
      if(featureLoop == 0){
        for (Map.Entry<String, List<FeatureObject>> entry : featureMap.entrySet()) {
          //System.out.println(entry.getKey() + "/" + entry.getValue());
          //Log.e("", "main key" + entry.getKey());
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
        featureLoop = 99;
      }
    }
  }

  protected class ProductSuppliers extends Card{

    Context context;
    List<SupplierObject> supplierList;

    public ProductSuppliers(Context context, List<SupplierObject> supplierList) {
      super(context,R.layout.card_list_view);
      this.context = context;
      this.supplierList = supplierList;
    }
    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {
      final LinearLayout mySeller = (LinearLayout) parent.findViewById(R.id.sellerDetails);
      if(sellerLoop == 0){
        LayoutInflater layoutInflater = ((ProductDetailsActivity) getActivity()).getLayoutInflater();

        for(int position=0; position<supplierList.size(); position++){
          View sellerView = layoutInflater.inflate(R.layout.supplier_single_row,null);
          ImageView imageView = (ImageView) sellerView.findViewById(R.id.imageView);
          TextView priceTextView = (TextView) sellerView.findViewById(R.id.textView3);
          TextView titleTextView = (TextView) sellerView.findViewById(R.id.textView4);
          TextView stockTextView = (TextView) sellerView.findViewById(R.id.textView6);
          Button buyButton = (Button) sellerView.findViewById(R.id.button);
          Ion.with(imageView).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).load(supplierList.get(position).getStoreImage());
          titleTextView.setText(supplierList.get(position).getName() + " :");
          stockTextView.setText(supplierList.get(position).getStockInfo());
          double pri = Double.parseDouble(supplierList.get(position).getPrice());
          priceTextView.setText("Rs. " + NumberFormat.getNumberInstance().format(pri));
          final int finalPosition = position;
          buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String sellerSiteUrl = supplierList.get(finalPosition).getUrl();
              connected = connectedToInternetOrNot.ConnectedToInternetOrNot(getActivity());
              if (connected == 1) {
                Intent externalActivity = new Intent(Intent.ACTION_VIEW);
                externalActivity.setData(Uri.parse(sellerSiteUrl));
                startActivity(externalActivity);
                Crouton.cancelAllCroutons();
              }
            }
          });
          mySeller.addView(sellerView);
        }
        sellerLoop = 99;
      }
    }

  }

  protected class ProductCity extends Card{

    Context context;
    List<CityWisePriceObject> cityWisePriceObjectList;

    public ProductCity(Context context, List<CityWisePriceObject> cityWisePriceObjectList) {
      super(context, R.layout.card_list_view);
      this.context = context;
      this.cityWisePriceObjectList = cityWisePriceObjectList;
    }

    @Override
    public void setupInnerViewElements(final ViewGroup parent, View view) {
      final LinearLayout mySellerCity = (LinearLayout) parent.findViewById(R.id.sellerDetails);
      if(sellerLoop == 0){
        LayoutInflater layoutInflater = ((ProductDetailsActivity) getActivity()).getLayoutInflater();
        for(int position=0; position<cityWisePriceObjectList.size(); position++){
          View sellerCityView = layoutInflater.inflate(R.layout.supplier_city_single_row,null);
          TextView cityTextView = (TextView) sellerCityView.findViewById(R.id.textView3);
          TextView priceTextView = (TextView) sellerCityView.findViewById(R.id.textView4);
          Button buyButton = (Button) sellerCityView.findViewById(R.id.button);
          cityTextView.setText(cityWisePriceObjectList.get(position).getCity());
          double pri = Double.parseDouble(cityWisePriceObjectList.get(position).getPrice());
          priceTextView.setText("Rs. " + NumberFormat.getNumberInstance().format(pri));
          final int finalPosition = position;
          buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String siteUrl = cityWisePriceObjectList.get(finalPosition).getStoreUrl();
              Intent externalActivity = new Intent(Intent.ACTION_VIEW);
              externalActivity.setData(Uri.parse(siteUrl));
              startActivity(externalActivity);
            }
          });
          mySellerCity.addView(sellerCityView);
        }
        sellerLoop = 99;
      }
    }

  }

}

