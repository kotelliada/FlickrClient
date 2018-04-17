package io.github.kotelliada.flickrlient.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import io.github.kotelliada.flickrlient.R;
import io.github.kotelliada.flickrlient.model.Photo;

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        this.imageView = itemView.findViewById(R.id.item_image);
    }

    public void bind(Photo photo) {
        Picasso.get()
                .load(photo.getUrl())
                .into(this.imageView);
    }
}