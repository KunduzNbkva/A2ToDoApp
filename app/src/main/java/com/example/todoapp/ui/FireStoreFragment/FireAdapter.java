package com.example.todoapp.ui.FireStoreFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.models.Work;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FireAdapter extends RecyclerView.Adapter<FireAdapter.ViewHolder> {

    private List<Work> list = new ArrayList<>();
    OnClick onItemClickListener;



    public void onItemClick(OnClick onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public FireAdapter() {


    }

    public void updateList(List<Work> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_firestore, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;
        FireAdapter adapter;
        FirebaseFirestore db;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitleFS);
            desc = itemView.findViewById(R.id.textDescFS);
            db=FirebaseFirestore.getInstance();
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder deleteWindow = new AlertDialog.Builder(itemView.getContext());
                    deleteWindow.setTitle("Удаление");
                    deleteWindow.setMessage("Удалить данную заметку?");
                    deleteWindow.setPositiveButton("Да", (new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.collection("users").document("TevV6anAqS2KyFVydzMU")
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });
                            notifyDataSetChanged();
                        }
                    }));
                    deleteWindow.setNegativeButton("Нет", null);
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
            if (work != null) {
                title.setText(work.getTitle());
                desc.setText(work.getDesc());
            }
        }
    }
}
