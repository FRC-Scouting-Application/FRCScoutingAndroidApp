package ca.tnoah.frc.scouting.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImage extends ViewModel {

    private MutableLiveData<Bitmap> bitmap;
    private Bitmap mBitmap;

    private String url;

    public DownloadImage(String url) {
        this.url = url;

        DownloadImageFromThread down = new DownloadImageFromThread();
        down.execute();
    }

    public MutableLiveData<Bitmap> getBitmap() {
        if (bitmap == null) bitmap = new MutableLiveData<>();
        return bitmap;
    }

    private void setBitmap(Bitmap bitmap) {
        this.bitmap.setValue(bitmap);
    }

    private class DownloadImageFromThread extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            mBitmap = getBitmapFromURL(url);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateBitmap();
        }
    }

    private Bitmap getBitmapFromURL(String src) {
        try {

            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateBitmap() {
        setBitmap(mBitmap);
    }

}
