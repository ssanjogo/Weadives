package com.example.weadives.PantallaDeHorarios;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.ColorUtils;

import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.ViewHolderImpl;
import com.example.weadives.Model.TableDataSource;
import com.example.weadives.R;

public class AdapterTablaHorario extends LinkedAdaptiveTableAdapter<ViewHolderImpl> {
    private static final int[] COLORS = new int[]{
            0xffE4E4E4, 0xff949494};
    /*
            0xffe62a10, 0xffe91e63, 0xff9c27b0, 0xff673ab7, 0xff3f51b5,
            0xff5677fc, 0xff03a9f4, 0xff00bcd4, 0xff009688, 0xff259b24,
            0xff8bc34a, 0xffcddc39, 0xffffeb3b, 0xffffc107, 0xffff9800, 0xffff5722};
    */

    private final LayoutInflater mLayoutInflater;
    private final TableDataSource mTableDataSource;
    private final int mColumnWidth;
    private final int mRowHeight;
    private final int mHeaderHeight;
    private final int mHeaderWidth;

    public AdapterTablaHorario(Context context, TableDataSource mTableDataSource) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mTableDataSource = mTableDataSource;
        Resources res = context.getResources();
        this.mColumnWidth = 120;
        this.mRowHeight = 120;
        this.mHeaderHeight = 100;
        this.mHeaderWidth = 160;
    }

    @Override
    public int getRowCount() {
        return mTableDataSource.getRowsCount();
    }

    @Override
    public int getColumnCount() {
        return mTableDataSource.getColumnsCount();
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent) {
        return new TestViewHolder((View) mLayoutInflater.inflate(R.layout.item_card, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderColumnViewHolder(mLayoutInflater.inflate(R.layout.item_header_column, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderRowViewHolder(mLayoutInflater.inflate(R.layout.item_header_row, parent, false));
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateLeftTopHeaderViewHolder(@NonNull ViewGroup parent) {
        return new TestHeaderLeftTopViewHolder(mLayoutInflater.inflate(R.layout.item_header_left_top, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl viewHolder, int row, int column) {
        final TestViewHolder vh = (TestViewHolder) viewHolder;

        String itemData = mTableDataSource.getItemData(row, column); // skip headers

        if (TextUtils.isEmpty(itemData)) {
            itemData = "";
        }

        itemData = itemData.trim();
        vh.tvText.setVisibility(View.VISIBLE);
        vh.tvText.setText(itemData);
        vh.tvText.setTextColor(0xffffffff);
        //vh.tvText.setBackgroundColor(0x00efefef);
        //vh.tvText.setHighlightColor(0xff00ff00);
        //vh.tvText.setHintTextColor(0xff0000ff);
        //vh.tvText.setLinkTextColor(0xffffff00);
        //vh.tvText.setOutlineAmbientShadowColor(0xffff00ff);
        //vh.tvText.setOutlineSpotShadowColor(0xff00ffff);
        //vh.tvText.setDrawingCacheBackgroundColor(0xffffffff);
        //System.out.println("Starts the print " + row + " " + column);

        /*      IN CASE WE WANT TO IMPLEMENT IMAGES IN THE SQUARES, UN COMMENT THIS

        vh.ivImage.setVisibility(View.INVISIBLE);

        Glide.with(vh.ivImage.getContext())
                .load(itemData)
                .apply(new RequestOptions().transform(new FitCenter()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        System.out.println("IT FAILED");
                        vh.ivImage.setVisibility(View.INVISIBLE);
                        vh.tvText.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        System.out.println("THIS SHOULD WORK");
                        vh.ivImage.setVisibility(View.VISIBLE);
                        vh.tvText.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(vh.ivImage);
         */
    }

    @Override
    public void onBindHeaderColumnViewHolder(@NonNull ViewHolderImpl viewHolder, int column) {
        int color = COLORS[column % COLORS.length];
        TestHeaderColumnViewHolder vh = (TestHeaderColumnViewHolder) viewHolder;
        //System.out.println("Prueba" + mTableDataSource.getColumnHeaderData(column));
        vh.tvText.setText(mTableDataSource.getColumnHeaderData(column));  // skip left top header
        GradientDrawable gd = new GradientDrawable(
                mIsRtl ? GradientDrawable.Orientation.RIGHT_LEFT : GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ColorUtils.setAlphaComponent(color, 50), ColorUtils.setAlphaComponent(color, 50)}); //{ColorUtils.setAlphaComponent(color, 50), 0x00000000}
        gd.setCornerRadius(0f);
        vh.vGradient.setBackground(gd);
        vh.vLine.setBackgroundColor(color);
    }

    @Override
    public void onBindHeaderRowViewHolder(@NonNull ViewHolderImpl viewHolder, int row) {
        TestHeaderRowViewHolder vh = (TestHeaderRowViewHolder) viewHolder;
        vh.tvText.setText(mTableDataSource.getRowHeaderData(row));
    }

    @Override
    public void onBindLeftTopHeaderViewHolder(@NonNull ViewHolderImpl viewHolder) {
        TestHeaderLeftTopViewHolder vh = (TestHeaderLeftTopViewHolder) viewHolder;
        vh.tvText.setText(mTableDataSource.getFirstHeaderData());
    }

    @Override
    public int getColumnWidth(int column) {
        return mColumnWidth;
    }

    @Override
    public int getHeaderColumnHeight() {
        return mHeaderHeight;
    }

    @Override
    public int getRowHeight(int row) {
        return mRowHeight;
    }

    @Override
    public int getHeaderRowWidth() {
        return mHeaderWidth;
    }

    //------------------------------------- view holders ------------------------------------------

    private static class TestViewHolder extends ViewHolderImpl {
        TextView tvText;
        ImageView ivImage;

        private TestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

    private static class TestHeaderColumnViewHolder extends ViewHolderImpl {
        TextView tvText;
        View vGradient;
        View vLine;

        private TestHeaderColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            vGradient = itemView.findViewById(R.id.vGradient);
            vLine = itemView.findViewById(R.id.vLine);
        }
    }

    private static class TestHeaderRowViewHolder extends ViewHolderImpl {
        TextView tvText;

        TestHeaderRowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
        }
    }

    private static class TestHeaderLeftTopViewHolder extends ViewHolderImpl {
        TextView tvText;

        private TestHeaderLeftTopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
        }
    }
}
