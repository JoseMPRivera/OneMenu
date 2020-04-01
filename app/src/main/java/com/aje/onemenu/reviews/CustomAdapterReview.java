package com.aje.onemenu.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aje.onemenu.R;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomAdapterReview extends BaseAdapter {

    Context context;
    ArrayList<Review> reviews;
    ArrayList<StorageReference> storageReferences;

    public CustomAdapterReview(Context context, ArrayList<Review> review, ArrayList<StorageReference> s) {
        this.context = context;
        this.reviews = review;
        this.storageReferences = s;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.activity_fragment_review, parent, false);
        }

        final Review review = (Review) this.getItem(position);

        TextView reviewText = convertView.findViewById(R.id.review_fragment_textview);
        ImageView reviewImage = convertView.findViewById(R.id.review_fragment_imageview);

        reviewText.setText(review.getReview());

//        reviewImage.setImageURI(review.getStorageReference());

        Glide.with(this.context)
                .load(storageReferences.get(position))
                .into(reviewImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, review.getReview(), Toast.LENGTH_LONG).show();
            }
        });


        return convertView;
    }
}
