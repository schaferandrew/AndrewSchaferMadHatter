package edu.schaf170msu.andrewschafermadhatter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * The view we will draw out hatter in
 */
public class HatterView extends View {

    /**
     * The current parameters
     */
    private Parameters params = new Parameters();

    /**
     * The image bitmap. None initially.
     */
    private Bitmap imageBitmap = null;

    /**
     * Image drawing scale
     */
    private float imageScale = 1;

    /**
     * Image left margin in pixels
     */
    private float marginLeft = 0;

    /**
     * Image top margin in pixels
     */
    private float marginTop = 0;

    /**
     * The ID values for each of the hat types. The values must
     * match the index into the array hats_spinner in arrays.xml.
    */
    public static final int HAT_BLACK = 0;
    public static final int HAT_GRAY = 1;
    public static final int HAT_CUSTOM = 2;

    /**
     * The bitmap to draw the hat
     */
    private Bitmap hatBitmap = null;

    /**
     * The bitmap to draw the hat band. We draw this
     * only when drawing the custom color hat, so we
     * don't color the hat band
     */
    private Bitmap hatbandBitmap = null;

    /**
     * Get the installed image path
     * @return path or null if none
     */
    public String getImagePath() {
        return params.imagePath;
    }

    /**
     * Set an image path
     * @param imagePath path to image file
     */
    public void setImagePath(String imagePath) {
        params.imagePath = imagePath;

        imageBitmap = BitmapFactory.decodeFile(imagePath);
        invalidate();
    }

    public HatterView(Context context) {
        super(context);
        init(null, 0);
    }

    public HatterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HatterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setHat(HAT_BLACK);
    }

    /**
     * Handle a draw event
     * @param canvas canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // If there is no image to draw, we do nothing
        if(imageBitmap == null) {
            return;
        }

        /*
         * Determine the margins and scale to draw the image
         * centered and scaled to maximum size on any display
         */
        // Get the canvas size
        float wid = canvas.getWidth();
        float hit = canvas.getHeight();

        // What would be the scale to draw the where it fits both
        // horizontally and vertically?
        float scaleH = wid / imageBitmap.getWidth();
        float scaleV = hit / imageBitmap.getHeight();

        // Use the lesser of the two
        imageScale = scaleH < scaleV ? scaleH : scaleV;

        // What is the scaled image size?
        float iWid = imageScale * imageBitmap.getWidth();
        float iHit = imageScale * imageBitmap.getHeight();

        // Determine the top and left margins to center
        marginLeft = (wid - iWid) / 2;
        marginTop = (hit - iHit) / 2;

        /*
         * Draw the image bitmap
         */
        canvas.save();
        canvas.translate(marginLeft,  marginTop);
        canvas.scale(imageScale, imageScale);
        canvas.drawBitmap(imageBitmap, 0, 0, null);

        /*
         * Draw the hat
         */
        canvas.translate(params.hatX,  params.hatY);
        canvas.scale(params.hatScale, params.hatScale);
        canvas.rotate(params.hatAngle);

        canvas.drawBitmap(hatBitmap, 0, 0, null);
        if(hatbandBitmap != null) {
            canvas.drawBitmap(hatbandBitmap, 0, 0, null);
        }

        canvas.restore();
    }

    /**
     * Get the current hat type
     * @return one of the hat type values HAT_BLACK, etc.
     */
    public int getHat() {
        return params.hat;
    }

    /**
     * Set the hat type
     * @param hat hat type value, HAT_BLACK, etc.
     */
    public void setHat(int hat) {
        params.hat = hat;
        hatBitmap = null;
        hatbandBitmap = null;

        switch(hat) {
            case HAT_BLACK:
                hatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hat_black);
                break;

            case HAT_GRAY:
                hatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hat_gray);
                break;

            case HAT_CUSTOM:
                hatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hat_white);
                hatbandBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hat_white_band);
                break;
        }

        invalidate();
    }
    private class Parameters {
        /**
         * Path to the image file if one exists
         */
        public String imagePath = null;

        /**
         * The current hat type
         */
        public int hat = HAT_BLACK;

        /**
         * X location of hat relative to the image
         */
        public float hatX = 0;

        /**
         * Y location of hat relative to the image
         */
        public float hatY = 0;

        /**
         * Hat scale, also relative to the image
         */
        public float hatScale = 0.5f;

        /**
         * Hat rotation angle
         */
        public float hatAngle = 0;
    }

}