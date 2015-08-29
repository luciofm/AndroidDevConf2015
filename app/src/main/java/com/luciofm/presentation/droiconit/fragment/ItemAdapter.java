package com.luciofm.presentation.droiconit.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luciofm.presentation.droiconit.R;
import com.luciofm.presentation.droiconit.model.Item;
import com.luciofm.presentation.droiconit.util.Utils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ItemAdapter extends ArrayAdapter<Item> {

    private final Bitmap bitmap;

    public ItemAdapter(Context context, List<Item> objects) {
        super(context, 0, objects);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_image);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if (convertView == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder(v);
        } else
            holder = (ViewHolder) v.getTag();


        Item item = holder.item = getItem(position);
        holder.position = position;

        final int color = getContext().getResources().getColor(item.getColor());

        holder.image.setImageBitmap(bitmap);
        holder.image.setImageTintList(ColorStateList.valueOf(color));
        holder.text1.setTextColor(color);
        holder.text1.setText(item.getTitle());
        Utils.colorRippleBackground(v, color, color);

        return v;
    }

    public class ViewHolder {
        @InjectView(R.id.thumb)
        ImageView image;
        @InjectView(R.id.text1)
        TextView text1;

        int position;
        Item item;

        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
            v.setTag(this);
            v.setOnClickListener(clickListener);
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Clicked(2): " + position, Toast.LENGTH_SHORT).show();
                //handleClick((ViewHolder) v.getTag());
            }
        };
    }
}