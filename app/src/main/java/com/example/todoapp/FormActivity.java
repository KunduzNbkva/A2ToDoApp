package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.models.Work;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FormActivity extends AppCompatActivity {
    private EditText editTitle;
    private EditText editDesc;
    Work editingWork;

    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form3);
        editTitle=findViewById(R.id.editTitle);
        editDesc=findViewById(R.id.editDesc);
        editingTask();
    }

    public void onClick(View view) {
        String title=editTitle.getText().toString().trim();
        String desc=editDesc.getText().toString().trim();
        Work work=new Work();
        if(editTitle.getText().toString().equals("")||editDesc.getText().toString().equals("")){
            Toast.makeText(FormActivity.this,"Заполните поля",Toast.LENGTH_LONG).show();
        }else if(editingWork!=null){
            editingWork.setTitle(title);
            editingWork.setDesc(desc);
            App.getDataBase().workDao().update(editingWork);
            finish();
        } else{
        work.setTitle(title);
        work.setDesc(desc);
        App.getDataBase().workDao().insert(work);
        saveToFireStore(work);
        finish();
        }
    }

    private void saveToFireStore(Work work) {
        FirebaseFirestore.getInstance().collection("works").add(work)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful())
                    Toast.makeText(FormActivity.this, "Успешно", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void editingTask(){
     editingWork=(Work)getIntent().getSerializableExtra("newWork");
     if (editingWork!=null){
         editTitle.setText(editingWork.getTitle());
         editDesc.setText(editingWork.getDesc());
     }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == 200){
            editTitle.setText(data.getStringExtra("title"));
            editDesc.setText(data.getStringExtra("desc"));
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


}
