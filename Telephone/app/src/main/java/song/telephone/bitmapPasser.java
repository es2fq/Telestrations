package song.telephone;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by 2015esong on 1/20/2015.
 */
public class bitmapPasser implements Serializable{
    Bitmap bitmap;

    public bitmapPasser( Bitmap map ) {
        bitmap = map;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
