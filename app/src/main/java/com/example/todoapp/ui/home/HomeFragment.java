package com.example.todoapp.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.App;
import com.example.todoapp.FormActivity;
import com.example.todoapp.R;
import com.example.todoapp.models.Work;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment  {
    private WorkAdapter adapter;
    private List<Work> list;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        list= new ArrayList<>();
        adapter=new WorkAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.onItemClick(new OnItemClick() {
            @Override
            public void onItemClick(int adapterPosition) {
                Work work=list.get(adapterPosition);
                Intent intent=new Intent(getContext(), FormActivity.class);
                intent.putExtra("newWork",work);
                startActivity(intent);

            }
        });


        App.getDataBase().workDao().getAll().observe(getActivity(), new Observer<List<Work>>() {
            @Override
            public void onChanged(List<Work> works) {
                list.clear();
                list.addAll(works);
                adapter.notifyDataSetChanged();
            }
        });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                animateIntent(view);
//            }
//        });




    }
}