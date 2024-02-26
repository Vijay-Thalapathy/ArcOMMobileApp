package com.example.arcomdriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcomdriver.R;
import com.example.arcomdriver.model.Model_NotesList;

import java.util.ArrayList;
import java.util.Locale;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 12 Oct 2022*/
public class Adapter_NotesList extends RecyclerView.Adapter<Adapter_NotesList.QrViewHolder> {
    private ArrayList<Model_NotesList> itemList;
    private Context context;
    public Adapter_NotesList(ArrayList<Model_NotesList> list) {
        this.itemList = list;
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_notes,viewGroup,false);
        context=viewGroup.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, final int i) {

        String s=itemList.get(i).getDisplayname().substring(0,1);

        holder.first_tv.setText(s.toUpperCase(Locale.ROOT));
        holder.notesName_tv.setText(itemList.get(i).getNotes_name());
        holder.notesComments_tv.setText(itemList.get(i).getNotes_comments());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView notesName_tv,notesComments_tv,first_tv;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            notesName_tv = itemView.findViewById(R.id.notesName_tv);
            notesComments_tv = itemView.findViewById(R.id.notesComments_tv);
            first_tv = itemView.findViewById(R.id.first_tv);
        }
    }


}
