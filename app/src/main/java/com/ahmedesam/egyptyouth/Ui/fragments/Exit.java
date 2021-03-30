package com.ahmedesam.egyptyouth.Ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ahmedesam.egyptyouth.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Exit extends DialogFragment {
    View view;
    Unbinder mUnbinder;
    @BindView(R.id.mOk)
    Button mOk;
    @BindView(R.id.mNo)
    Button mNo;

    public Exit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_exit, container, false);
        mUnbinder = ButterKnife.bind( this,view);
        return view;
    }

    @OnClick({R.id.mOk, R.id.mNo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mOk:
                System.exit(0);
                break;
            case R.id.mNo:
                dismiss();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(getDialog().getWindow().getAttributes().MATCH_PARENT, getDialog().getWindow().getAttributes().WRAP_CONTENT);

    }
}
