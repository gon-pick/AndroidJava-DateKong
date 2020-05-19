package org.techtown.datekong;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity  extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //ID 연결해서 Listener 등록
        findViewById(R.id.SignupButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //app exit
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        //버튼을 클랙했을 경우
        public void onClick(View v) {
            switch (v.getId()) {
                //회원가입 함수
                case R.id.SignupButton:
                    signUp();
                    break;
                //로그인 함수
                case R.id.gotoLoginButton:
                    myStartLoginActivity(LoginActivity.class);
                    break;
            }
        }
    };

    //회원가입 함수
    private void signUp() {
        //email, password, passwordCheck EditText에 있는것 string으로 받아오기.
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.passwordCheckEditText)).getText().toString();


        if(email.length() > 0 && password.length()>0 && passwordCheck.length()>0){
            if(password.equals(passwordCheck)){
                //firebase 신규 사용자 가입
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원가입에 성공");
                                    myStartLoginActivity(MainActivity.class);
                                    //UI
                                } else {
                                    //6글자 이상이여야 한다.
                                    if(task.getException()!=null) {
                                        startToast(task.getException().toString());
                                        //UI
                                    }
                                }

                                // ...
                            }
                        });
            }else{
                startToast("비밀번호가 일치하지 않습니다.");

            }
        }else{
            startToast("이메일 또는 비밀번호를 입력해 주세요. 비밀번호는 6자 이상 입니다.");
        }
    }

    //Toast표시해주기 함수 내에서 listener 등록 불가.
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    //login page로 이동
    private void myStartLoginActivity(Class c) {
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
