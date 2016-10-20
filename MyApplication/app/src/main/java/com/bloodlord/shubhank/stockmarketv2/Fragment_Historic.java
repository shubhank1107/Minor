package com.bloodlord.shubhank.stockmarketv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.Fragment;

/**
 * Created by Shubhank on 16-10-2016.
 */

public class Fragment_Historic extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.frag_hist,container,false);
        final String symbol = getArguments().getString("symbol");
        final String history_data = getArguments().getString("historic");

        View view = inflater.inflate(R.layout.fragment_history,container,false);

        final WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/renderHighcharts.html");
        Log.i("Progress","html loaded");
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                Log.i("Progress","You reached here");
                webView.loadUrl("javascript:render_chart('"+symbol+"')");
            }

        });
        return view;
    }
}
