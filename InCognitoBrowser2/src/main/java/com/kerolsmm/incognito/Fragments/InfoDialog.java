package com.kerolsmm.incognito.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kerolsmm.incognito.R;

import java.util.Date;

public class InfoDialog  extends DialogFragment {


    WebView webView;

    public InfoDialog () {

    }

    public InfoDialog (WebView webView) {
        this.webView = webView;
    }


    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //We have added a title in the custom layout. So let's disable the default title.
        getDialog().getWindow().setLayout( RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        return inflater.inflate(R.layout.info_alter_diloag,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        TextView textView = view.findViewById(R.id.info_site);
        textView.setText(pageInfo(webView));
        ImageView cancel = view.findViewById(R.id.back_info_dialog);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Dialog dialog = new Dialog(getActivity());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setGravity(Gravity.TOP);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen);;
    }


    private String pageInfo(WebView webView) {
        String s = "URL: " + webView.getUrl() + "\n";
        s += "Title: " + webView.getTitle() + "\n\n";
        SslCertificate certificate = webView.getCertificate();
        s += certificate == null ? "Not secure" : "Certificate:\n" + certificateToStr(certificate);

      return s;
    }

    @SuppressLint("DefaultLocale")
    private static String certificateToStr(SslCertificate certificate) {
        if (certificate == null) {
            return null;
        }
        String s = "";
        SslCertificate.DName issuedTo = certificate.getIssuedTo();
        if (issuedTo != null) {
            s += "Issued to: " + issuedTo.getDName() + "\n";
        }
        SslCertificate.DName issuedBy = certificate.getIssuedBy();
        if (issuedBy != null) {
            s += "Issued by: " + issuedBy.getDName() + "\n";
        }
        Date issueDate = certificate.getValidNotBeforeDate();
        if (issueDate != null) {
            s += String.format("Issued on: %tF %tT %tz\n", issueDate, issueDate, issueDate);
        }
        Date expiryDate = certificate.getValidNotAfterDate();
        if (expiryDate != null) {
            s += String.format("Expires on: %tF %tT %tz\n", expiryDate, expiryDate, expiryDate);
        }
        return s;
    }

}
