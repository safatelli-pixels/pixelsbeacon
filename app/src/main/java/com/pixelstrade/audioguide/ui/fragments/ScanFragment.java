package com.pixelstrade.audioguide.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.zxing.Result;
import com.pixelstrade.audioguide.R;
import com.pixelstrade.audioguide.ui.activitys.DetailsArticleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanFragment extends BaseFragment implements ZXingScannerView.ResultHandler{


    private ZXingScannerView mScannerView;

    @BindView(R.id.scannerView)
    ViewGroup scannerView;
    @BindView(R.id.articleNumber)
    EditText articleNumber;

    @BindView(R.id.fab)
    FloatingActionButton validateButton;

    public ScanFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_scan, container, false);

        ButterKnife.bind(this,rootView);

        mScannerView = new ZXingScannerView(getActivity());
        scannerView.addView(mScannerView);

        //create listner on editTextView
        articleNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length()>0)
                    validateButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.colorPrimary)));
                else
                    validateButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.grayItemMenu)));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            //start scan code
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
          requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @OnClick(R.id.fab)
    void showDetails()
    {
        if (articleNumber.getText().toString().equals(""))
            Toast.makeText(getActivity(), "Veuillez saisir ou scanner le numéro de l'article", Toast.LENGTH_SHORT).show();

        else
        {
            Intent intent = new Intent(getActivity(), DetailsArticleActivity.class);
            //put code article
            intent.putExtra("code",articleNumber.getText().toString());
            startActivity(intent);
        }
    }

    @Override
    public void handleResult(Result result) {


        //get code from QR code and set it in editText
        articleNumber.setText(result.getText());

        Toast.makeText(getActivity(), "Code detecté", Toast.LENGTH_SHORT).show();

        //change color button validate
        validateButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.colorPrimary)));        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanFragment.this);
            }
        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                // No need to start camera here; it is handled by onResume
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
