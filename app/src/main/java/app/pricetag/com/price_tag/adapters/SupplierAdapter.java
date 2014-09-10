package app.pricetag.com.price_tag.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.List;

import app.pricetag.com.price_tag.ProductDetailsActivity;
import app.pricetag.com.price_tag.R;
import app.pricetag.com.price_tag.dao.SupplierObject;

/**
 * Created by shekhar on 9/9/14.
 */
public class SupplierAdapter extends BaseAdapter {


  Context context;
  List<SupplierObject> supplierList;
  public SupplierAdapter(Context context, List<SupplierObject> resource) {
    this.context = context;
    this.supplierList = resource;
    Log.e("SKG", String.valueOf(supplierList));
  }

  @Override
  public int getCount() {
    return supplierList.size();
  }

  @Override
  public Object getItem(int position) {
    return supplierList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent){
    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View row = layoutInflater.inflate(R.layout.supplier_single_row, null);
    ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
    TextView priceTextView = (TextView) row.findViewById(R.id.textView3);
    TextView titleTextView = (TextView) row.findViewById(R.id.textView4);
    TextView stockTextView = (TextView) row.findViewById(R.id.textView6);
    Button buyButton = (Button) row.findViewById(R.id.button);
    Ion.with(imageView).placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).load(supplierList.get(position).getStoreImage());
    titleTextView.setText(supplierList.get(position).getName() + " : ");
    stockTextView.setText(supplierList.get(position).getStockInfo());
    priceTextView.setText("Rs. " + supplierList.get(position).getPrice());
    buyButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String siteUrl = supplierList.get(position).getUrl();
        Toast.makeText(context,siteUrl,Toast.LENGTH_SHORT).show();
      }
    });
    return row;
  }
}
