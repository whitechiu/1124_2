package test.project.a1124_2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.R.attr.bitmap;

public class MainActivity extends AppCompatActivity {
    private Bitmap bitmap = null;
    private InputStream inputStream = null;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageView img;
    TextView tv;
    double sum = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.imageView);
        tv = (TextView) findViewById(R.id.textView);

        new Thread(){
            @Override
            public void run() {
                super.run();
                try
                {
                    URL url = new URL("http://i.dongtw.com/2014/12/Danny-Green-2013-NBA-Finals-1280x800-BasketWallpapers.com-.jpg");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    inputStream = conn.getInputStream();

                    double fullSize = conn.getContentLength(); // 總長度
                    byte[] buffer = new byte[64]; // buffer ( 每次讀取長度 )
                    int readSize = 0; // 當下讀取長度
                    int readAllSize = 0;


                    while ((readSize = inputStream.read(buffer)) != -1)
                    {
                        outputStream.write(buffer, 0, readSize);
                        readAllSize += readSize;
                        sum = (readAllSize / fullSize) * 100; // 累計讀取進度
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(String.valueOf(sum) + "%");
                            }
                        });
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] result = outputStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(bitmap);
                    }
                });
            }
        }.start();

    }
}
