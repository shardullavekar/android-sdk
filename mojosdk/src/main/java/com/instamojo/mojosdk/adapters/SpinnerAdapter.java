package com.instamojo.mojosdk.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.instamojo.mojosdk.R;

import java.util.ArrayList;

/**
 * Spinner adapter for the Banks other than the Favourite Banks
 * in {@link com.instamojo.mojosdk.fragments.NetBankingForm}.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class SpinnerAdapter extends BaseAdapter implements android.widget.SpinnerAdapter {

    private ArrayList<String> list;
    private Context context;
    private TextView textView;

    /**
     * Initiate the Adapter with activity context and {@link ArrayList} of Bank Names.
     *
     * @param context - {@link Context} Of Activity.
     * @param list    - {@link ArrayList<String>} of Bank Names.
     */
    public SpinnerAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (textView == null) {
            textView = (TextView) View.inflate(context, android.R.layout.simple_spinner_item, null);
        }
        textView.setText(list.get(position));
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dropDownView = inflater.inflate(R.layout.spinner_down_down_item, parent, false);
        TextView label = (TextView) dropDownView.findViewById(R.id.text);
        View line = dropDownView.findViewById(R.id.text_line);

        label.setText(list.get(position));
        if (position == 0) {
            line.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
        }
        return dropDownView;
    }
}
