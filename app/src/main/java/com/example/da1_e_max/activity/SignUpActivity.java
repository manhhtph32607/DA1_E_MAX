package com.example.da1_e_max.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.da1_e_max.R;
import com.example.da1_e_max.constant.Constant;
import com.example.da1_e_max.constant.GlobalFunction;
import com.example.da1_e_max.databinding.ActivitySignUpBinding;
import com.example.da1_e_max.model.User;
import com.example.da1_e_max.prefs.DataStoreManager;
import com.example.da1_e_max.utils.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding mActivitySignUpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignUpBinding.getRoot());

//        mActivitySignUpBinding.rdbUser.setChecked(true);
//        mActivitySignUpBinding.rdbUser.setVisibility(View.GONE);
        mActivitySignUpBinding.imgBack.setOnClickListener(v -> onBackPressed());
        mActivitySignUpBinding.layoutSignIn.setOnClickListener(v -> finish());
        mActivitySignUpBinding.btnSignUp.setOnClickListener(v -> onClickValidateSignUp());
    }

    private void onClickValidateSignUp() {
        String strEmail = mActivitySignUpBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignUpBinding.edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            if (strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show();
            } else {
                signUpUser(strEmail, strPassword);
            }
        }
    }

    private void signUpUser(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            if (user.getEmail() != null && user.getEmail().contains(Constant.ADMIN_EMAIL_FORMAT)) {
                                userObject.setAdmin(true);
                            }
                            DataStoreManager.setUser(userObject);
                            GlobalFunction.gotoMainActivity(this);
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, getString(R.string.msg_sign_up_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}