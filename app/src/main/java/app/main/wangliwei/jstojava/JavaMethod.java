package app.main.wangliwei.jstojava;


import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JavaMethod {
    private MainActivity mainActivity;
    private Handler uiHandler;

    public JavaMethod(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        uiHandler = new Handler(Looper.getMainLooper());
    }

    @JavascriptInterface
    public void JsToJavaInterface(final String param) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                mainActivity.setTextShow("from JavaInterface: " + param);
            }
        });
    }

    public WebViewClient getWebViewClient() {
        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                // 一般根据scheme(协议格式) & authority(协议名)判断
                // url = "js://jstojava?arg1=1&arg2=2"
                if(uri.getScheme().equals("js")) {
                    if(uri.getAuthority().equals("jstojava")) {
                        final String param1 = uri.getQueryParameter("arg1");
                        final String param2 = uri.getQueryParameter("arg2");
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.setTextShow("arg1="+param1+" arg2="+param2);
                            }
                        });
                    }
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        };
        return webViewClient;
    }

    public WebChromeClient getWebChromeClient() {
        WebChromeClient webChromeClient = new WebChromeClient(){
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                Uri uri = Uri.parse(message);
                if(uri.getScheme().equals("js")) {
                    if(uri.getAuthority().equals("jstojava")) {
                        final String param3 = uri.getQueryParameter("arg3");
                        final String param4 = uri.getQueryParameter("arg4");
                        uiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.setTextShow("arg3="+param3+" arg4="+param4);
                                result.confirm("我来自onJsPrompt");
                            }
                        });
                    }
                    return true;
                }

                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        };
        return webChromeClient;
    }
}
