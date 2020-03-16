package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {
    EditText editPhone, writeCode;
    TextView timer;
    String phoneVerificationId,codeSms;
    View addPhone,view_code;
    FirebaseAuth mAuth;
    Button buttonCode;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        addPhone=findViewById(R.id.add_phone);
        view_code=findViewById(R.id.code);
        editPhone=findViewById(R.id.editPhone);
        timer=findViewById(R.id.timer);
        writeCode=findViewById(R.id.writeCode);

        mAuth = FirebaseAuth.getInstance();
        buttonCode=findViewById(R.id.buttonCode);


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(PhoneActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(PhoneActivity.this, "Something went wrong.Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void verifyVerificationCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
            //signing the user
            signInWithPhoneAuthCredential(credential);

        }catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), "Verification Code is wrong, try again", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

    }



    public void onClickContinue(View view) {
        addPhone.setVisibility(View.GONE);
        view_code.setVisibility(View.VISIBLE);
        sendCode();
        timer();
    }

    private void timer() {
        final int[] time = {30};
        new CountDownTimer(30000, 1000){
            public void onTick(long millisUntilFinished){
                timer.setText("0:"+checkDigit(time[0]));
                time[0]--;
            }
            public  void onFinish(){
                Toast.makeText(PhoneActivity.this, "Try again please", Toast.LENGTH_SHORT).show();
                addPhone.setVisibility(View.VISIBLE);
                view_code.setVisibility(View.GONE);
            }
        }.start();
    }

    private void sendCode() {
        String phone=editPhone.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks);
    }

    public String checkDigit(int number) {
            return number <= 9 ? "0" + number : String.valueOf(number);
        }



    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.e("TAG","onVerificationCompleted");
            codeSms=phoneAuthCredential.getSmsCode();
            if(codeSms!=null){
                writeCode.setText(codeSms);
                verifyVerificationCode(codeSms);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.e("TAG","onVerificationFailed:"+e.getMessage());
        }


        @Override // если смс пришло
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // signIn(phoneAuthCredential);
            Log.d("testing", "onCodeSent: " + phoneVerificationId);
            phoneVerificationId=s;
            mResendToken = forceResendingToken;
        }

        @Override // сработает когда таймер закончится у нас это 60 сек     visible invisible of view with timer
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);

        }
    };

    public void onClickVerify(View view) {
        String code=writeCode.getText().toString().trim();
        verifyVerificationCode(code);
    }
}






