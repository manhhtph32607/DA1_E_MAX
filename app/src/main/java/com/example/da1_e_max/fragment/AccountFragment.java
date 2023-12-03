package com.example.da1_e_max.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.da1_e_max.R;
import com.example.da1_e_max.activity.ChangePasswordActivity;
import com.example.da1_e_max.activity.MainActivity;
import com.example.da1_e_max.activity.OrderHistoryActivity;
import com.example.da1_e_max.activity.SignInActivity;
import com.example.da1_e_max.activity.SignUpActivity;
import com.example.da1_e_max.constant.GlobalFunction;
import com.example.da1_e_max.databinding.FragmentAccountBinding;
import com.example.da1_e_max.model.User;
import com.example.da1_e_max.prefs.DataStoreManager;
import com.example.da1_e_max.utils.StringUtil;
import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends BaseFragment {
    Button btndk, btndn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAccountBinding fragmentAccountBinding = FragmentAccountBinding.inflate(inflater, container, false);
        fragmentAccountBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());
        String Strcheck = fragmentAccountBinding.tvEmail.getText().toString();
        if ( StringUtil.isEmpty(Strcheck)){
            fragmentAccountBinding.layoutSignIn.setVisibility(View.VISIBLE);
            fragmentAccountBinding.layoutSignOut.setVisibility(View.GONE);
            fragmentAccountBinding.layoutOrderHistory.setVisibility(View.GONE);
            fragmentAccountBinding.layoutChangePassword.setVisibility(View.GONE);
        }else {
            fragmentAccountBinding.layoutSignIn.setVisibility(View.GONE);
            fragmentAccountBinding.layoutSignOut.setVisibility(View.VISIBLE);
        }
        fragmentAccountBinding.layoutSignOut.setOnClickListener(v -> onClickSignOut());
        fragmentAccountBinding.layoutSignIn.setOnClickListener(v ->onClickLogin() );
        fragmentAccountBinding.layoutChangePassword.setOnClickListener(v -> onClickChangePassword());
        fragmentAccountBinding.layoutOrderHistory.setOnClickListener(v -> onClickOrderHistory());


        return fragmentAccountBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.account));
        }
    }

    private void onClickOrderHistory() {
        GlobalFunction.startActivity(getActivity(), OrderHistoryActivity.class);
    }

    private void onClickChangePassword() {
        GlobalFunction.startActivity(getActivity(), ChangePasswordActivity.class);
    }
    private void onClickLogin(){
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);


    }
    private void onClickSignup(){
        Intent intent = new Intent(getActivity(), SignUpActivity.class);

        startActivity(intent);
    }

    private void onClickSignOut() {
        if (getActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Bạn có chắc chắn muốn đăng xuất không?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                DataStoreManager.setUser(null);
                GlobalFunction.startActivity(getActivity(), MainActivity.class);
                getActivity().finishAffinity();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
