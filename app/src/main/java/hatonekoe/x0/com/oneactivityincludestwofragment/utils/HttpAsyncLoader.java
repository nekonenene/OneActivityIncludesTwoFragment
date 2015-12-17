package hatonekoe.x0.com.oneactivityincludestwofragment.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rei_m, Mocel, Yokoe on 2014/07/24, 2011/11/02, 2015/12/11.
 *
 * 以下のサイトを大変参考にさせていただきました
 * rei_m : http://rei19.hatenablog.com/entry/2015/01/25/225041
 * Mocel : http://archive.guma.jp/2011/11/-asynctask-asynctaskloader.html
 * http://msdev.sakura.ne.jp/know-how/?p=8
 */
public class HttpAsyncLoader extends AsyncTaskLoader<JSONObject>
{
    
    private static final String TAG = "HttpAsyncLoader";

    private String mUrlString;
    private JSONObject mSendJson;
    private JSONObject mResultJson;

    private static String mRequestMethodName;
    private static final String REQUEST_POST = "POST";
    private static final String REQUEST_GET  = "GET";

    /** Constructor
     * @param context   <a href="http://mitoroid.com/category/android/android_context.php">
     *                  アプリケーション情報に関するグローバル情報（何から起動されたか、現在の状態、何にアクセスしょうとしているか等の情報）を管理</a>
     * @param urlString アクセス先
     * @param sendJson  リクエストを要求するために送信する JSON
     */
    public HttpAsyncLoader(Context context, String urlString, JSONObject sendJson)
    {
        super(context);
        commonConstructMethod( context, urlString, sendJson );
    }
    /** Constructor Overload
     * @param context   <a href="http://mitoroid.com/category/android/android_context.php">
     *                  アプリケーション情報に関するグローバル情報（何から起動されたか、現在の状態、何にアクセスしょうとしているか等の情報）を管理</a>
     * @param urlString アクセス先
     * @param sendJson  リクエストを要求するために送信する JSON
     * @param isPost true なら POST, false なら GET メソッドを送る
     */
    public HttpAsyncLoader(Context context, String urlString, JSONObject sendJson, Boolean isPost)
    {
        super(context);
        commonConstructMethod( context, urlString, sendJson );
        if( !isPost )
        {
            mRequestMethodName = REQUEST_GET;
        }
    }
    /** Common Constructor Method : ただし super だけはコンストラクタで呼び出す */
    private void commonConstructMethod(Context context, String urlString, JSONObject sendJson)
    {
        mUrlString = urlString;
        mSendJson  = sendJson;
        mRequestMethodName = REQUEST_POST;
    }

    /** 指定 URL に非同期通信でアクセスし、JSON を取得する / access to the URL by Asynchrony communication, and get a JSON */
    @Override
    public JSONObject loadInBackground()
    {
        // mUrlString が URL として正しい文字列かチェックし、url に代入 : check if the URL is legal
        URL url;
        try
        {
            url = new URL(mUrlString);
        }
        catch(MalformedURLException e)
        {
            LogDebug.E(TAG, "invalid URL : " + mUrlString + " : " + e );
            return null;
        }

        // HTTP Connection を開始 : start the HTTP connection
        HttpURLConnection httpConnection = null;
        try
        {
            String sendJsonString = mSendJson.toString();
            LogDebug.D(TAG, "http connection を開始します, url : " + url );
            long connectStart = System.nanoTime() ;

            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod(mRequestMethodName);
            // HTTPヘッダーの設定, 参考 : http://www.atmarkit.co.jp/fnetwork/rensai/netpro01/header-fields.html
            httpConnection.setRequestProperty("Connection", "close"); // TODO: Keep-Alive と close どっちがいい？
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestProperty("Content-Length", String.valueOf(sendJsonString.length())); // 不要かも

            httpConnection.setUseCaches(true); // キャッシュを使用
            httpConnection.setFixedLengthStreamingMode(sendJsonString.getBytes().length); // 指定したバイト数、HTTP Request をストリーミング

            long connectProcess = System.nanoTime();
            LogDebug.I(TAG, "before .connect " + (double) ( connectProcess - connectStart ) / Math.pow(10, 6) + " ms");

            httpConnection.connect(); // 不要ではあるが、計測時間をわかりやすくするためここに置く

            connectProcess = System.nanoTime();
            LogDebug.I(TAG, "before outputStream " + (double) ( connectProcess - connectStart ) / Math.pow(10, 6) + " ms");

            DataOutputStream outputStream = new DataOutputStream(httpConnection.getOutputStream());
            outputStream.writeBytes(sendJsonString);
            LogDebug.D(TAG, "送るJSON : " + sendJsonString);

            connectProcess = System.nanoTime();
            LogDebug.I(TAG, "before getResponse " + (double) ( connectProcess - connectStart ) / Math.pow(10, 6) + " ms");

            int responseCode = httpConnection.getResponseCode();
            LogDebug.D(TAG, "Response code = " + responseCode);

            String content;
            if(responseCode == 200)
            {
                content = readContent(httpConnection);
                long connectEnd = System.nanoTime();
                LogDebug.I(TAG, "接続に " + (double)(connectEnd - connectStart) / Math.pow(10, 6) + " ms かかった" );
            }else
            {
                String errorMessage;
                if(responseCode >= 300)
                {
                    errorMessage = "Redirect Error : " + responseCode;
                }else if(responseCode >= 400)
                {
                    errorMessage = "Client-connection Error : " + responseCode;
                }else if(responseCode >= 500)
                {
                    errorMessage = "Server-connection Error : " + responseCode;
                }else
                {
                    errorMessage = "some connection Error : " + responseCode;
                }
                LogDebug.E(TAG, errorMessage );
                return null;
            }

            if( TextUtils.isEmpty(content) )
            {
                return null;
            }else
            {
                return new JSONObject(content);
            }
        }
        catch(IOException e)
        {
            LogDebug.E(TAG, "content を取得できません , url=" + url + " : " + e );
            return null;
        }
        catch(JSONException e)
        {
            LogDebug.E(TAG, "content を JSON に変換できません : " + e );
            return null;
        }
        finally
        {
            if (httpConnection != null)
            {
                try
                {
                    httpConnection.disconnect();
                }
                catch (Exception e)
                {
                    LogDebug.E(TAG, "HttpURLConnection を切断できませんでした : " + e );
                }
            }
        }
    }
    
    /** 受け取ったデータを文字列にする / convert to the string from the received data */
    private String readContent(HttpURLConnection httpConnection) throws IOException
    {
        // 文字エンコード形式を、指定されていれば取得 : get the character encoding style, and set that, or UTF-8
        String charsetName;
        String contentType = httpConnection.getContentType();
        LogDebug.D(TAG, "contentType = " + contentType) ;
        if (! TextUtils.isEmpty(contentType))
        {
            int charsetKeyStart = contentType.indexOf("charset=");
            if(charsetKeyStart != -1)
            {
                charsetName = contentType.substring( charsetKeyStart + "charset=".length() );
            }else
            {
                charsetName = "UTF-8";
            }
        }else
        {
            charsetName = "UTF-8";
        }

        /** メモリアロケート（メモリ不足によるエラー）を防ぐため、バッファーを通して一行ずつサーバの出力を読み込んでいく :
         *  read the server's message through the buffer for prevent the memory allocate error
         *  TODO: 一行ごとでなく特定の Byte 数を指定して読み込むのが安全かな。一行に大量の文字があった場合つらいので
         */
        StringBuilder stringBuilder = new StringBuilder();
        String tmpALineString;
        InputStream inputStream = new BufferedInputStream(httpConnection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charsetName));
        while((tmpALineString = reader.readLine()) != null)
        {
            stringBuilder.append(tmpALineString);
            LogDebug.D(TAG, "A Line : " + tmpALineString );
        }
        try
        {
            inputStream.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    @Override
    public void deliverResult(JSONObject data)
    {
        if( isReset() )
        {
            if(mResultJson != null)
            {
                mResultJson = null;
            }
            return;
        }
        
        mResultJson = data;
        
        if( isStarted() )
        {
            super.deliverResult(data);
        }
    }
    
    @Override
    protected void onStartLoading()
    {
        if(mResultJson != null)
        {
            deliverResult(mResultJson);
        }
        if(takeContentChanged() || mResultJson == null)
        {
            forceLoad();
        }
    }
    
    @Override
    protected void onStopLoading()
    {
        super.onStopLoading();
        cancelLoad();
    }
    
    @Override
    protected void onReset()
    {
        super.onReset();
        onStopLoading();
    }
    
    public String getUrlString()
    {
        return mUrlString;
    }
    
    @Override
    public void dump(String prefix, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] args)
    {
        super.dump(prefix, fileDescriptor, printWriter, args);
        printWriter.print(prefix);  printWriter.print("urlString=");   printWriter.println(mUrlString);
        printWriter.print(prefix);  printWriter.print("resultJson=");  printWriter.println(mResultJson);
    }
}
