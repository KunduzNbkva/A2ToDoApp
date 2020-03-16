package com.example.todoapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.App;
import com.example.todoapp.R;
import com.example.todoapp.models.Work;



import java.util.List;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.ViewHolder> {

    private List<Work> list;

    OnItemClick onItemClickListener;


    public void onItemClick(OnItemClick onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }





    public WorkAdapter(List<Work> list) {

        this.list = list;
    }






    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_work,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private TextView textTitle;
        private TextView textDesc;
        WorkAdapter adapter;



        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDesc = itemView.findViewById(R.id.textDesc);

            adapter=new WorkAdapter(list);
             itemView.setOnLongClickListener(new View.OnLongClickListener() {
                 @Override
                 public boolean onLongClick(View v) {
                     AlertDialog.Builder deleteWindow=new AlertDialog.Builder(itemView.getContext());
                     deleteWindow.setTitle("Удаление");
                     deleteWindow.setMessage("Удалить данную заметку?");
                     deleteWindow.setPositiveButton("Да",(new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             list.remove(itemView);  // ask about it
                             App.getDataBase().workDao().delete(list.get(getAdapterPosition()));
                             notifyDataSetChanged();
                         }
                     }));
                     deleteWindow.setNegativeButton("Нет",null);
                     deleteWindow.show();

                     return true;
                 }
             });

             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     onItemClickListener.onItemClick(getAdapterPosition());
                 }
             });

        }

        public void bind(Work work) {
            textTitle.setText(work.getTitle());
            textDesc.setText(work.getDesc());
        }



    }
}
