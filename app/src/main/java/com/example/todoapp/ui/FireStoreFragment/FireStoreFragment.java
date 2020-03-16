package com.example.todoapp.ui.FireStoreFragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.FormActivity;
import com.example.todoapp.R;
import com.example.todoapp.models.Work;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FireStoreFragment extends Fragment {
    private FireAdapter adapter;
    private List<Work> list;
    FirebaseFirestore dataBase;


    public FireStoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fire_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView=view.findViewById(R.id.recyclerViewFireStore);
        list= new ArrayList<>();
        adapter=new FireAdapter();
        recyclerView.setAdapter(adapter);
        dataBase= FirebaseFirestore.getInstance();
        dataBase.collection("works").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List <DocumentSnapshot> documentSnapshotList=queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d:documentSnapshotList){
                        Work work=d.toObject(Work.class);
                        list.add(work);
                        adapter.updateList(list);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adapter.onItemClick(new OnClick() {
            @Override
            public void onItemClick(int position) {
                Work work=list.get(position);
                Intent intent=new Intent(getContext(), FormActivity.class);
                intent.putExtra("newWork",work);
                startActivity(intent);
            }
        });






    }
}
