package com.example.lwp_lab01.ui.PDF_List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lwp_lab01.R;

public class adapter extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ItemSelected Listener;
    private final Context context;

    public TextView pdfTitle;
    public adapter(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();

        pdfTitle = itemView.findViewById(R.id.pdf_name);
    }

    @Override
    public void onClick(View view){
        Listener.onClick(view, getAdapterPosition(), false);
    }
}
