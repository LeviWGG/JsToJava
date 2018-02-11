package app.main.wangliwei.jstojava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private Unbinder unbinder;
    private JavaMethod javaMethod;

    @BindView(R.id.webview)
    FrameLayout frameLayout;

    @BindView(R.id.text_show)
    TextView textShow;

    @BindView(R.id.text_java_js)
    TextView textJavaToJs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        javaMethod = new JavaMethod(this);
        webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        webView.setWebViewClient(javaMethod.getWebViewClient());
        webView.setWebChromeClient(javaMethod.getWebChromeClient());
        webView.addJavascriptInterface(javaMethod,"android");
        frameLayout.addView(webView);
        webView.loadUrl("file:///android_asset/JsMethod.html");
    }

    public void setTextShow(String str) {
        textShow.setText(str);
    }

    @OnClick({R.id.text_java_js,R.id.text_java_evaJs})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_java_js:
                webView.loadUrl("javascript:javatojscallback('我来自Java')");
                break;
            case R.id.text_java_evaJs:
                webView.evaluateJavascript("javascript:javatojswith('我来自Java')",
                        new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        textShow.setText(s);
                    }
                });
                break;
        }

    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.setTag(null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
        unbinder.unbind();
    }
}
