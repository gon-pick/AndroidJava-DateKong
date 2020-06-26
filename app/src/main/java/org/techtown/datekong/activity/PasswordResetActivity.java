package org.techtown.datekong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.techtown.datekong.R;
import org.techtown.datekong.activity.MainActivity;

import static org.techtown.datekong.Util.showToast;

public class PasswordResetActivity extends BasicActivity{
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        setToolbarTitle("비밀번호 재설정");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.sendButton).setOnClickListener(onClickListener);
    }



    //리스너 연결.
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sendButton:
                    send();
                    break;
            }
        }
    };


    private void send() {
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();

        //기존 사용자 로그인
        if(email.length() > 0){
            final RelativeLayout loaderLayout = findViewById(R.id.loaderLyaout);
            loaderLayout.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                loaderLayout.setVisibility(View.GONE);
                                showToast(PasswordResetActivity.this, "이메일을 보냈습니다.");
                            }
                        }
                    });
        }else{
            showToast(PasswordResetActivity.this, "이메일을 입력해 주세요.");
        }
    }

    //mainactivity로 이동.
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
