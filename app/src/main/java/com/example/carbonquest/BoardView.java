package com.example.carbonquest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BoardView extends View {
    public BoardView(Context context) {
        super(context);
        init(null, 0);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public static final int[] PLAYER_COLORS = new int[] {
            Color.BLACK,
            Color.GREEN,
            Color.RED
    };

    private Bitmap bitmap;
    private Paint paint;

    private int[] playerPositions = new int[0];

    private void init(AttributeSet attrs, int defStyle) {
        bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.plateau);
        paint = new Paint();
        setPlayerPositions(4, 49, 66);
    }

    public void setPlayerPositions(int... positions) {
        playerPositions = positions;
        invalidate(); // refresh the display
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect dest = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bitmap, null, dest, paint);

        paint.setStyle(Paint.Style.FILL);
        int i = 0;
        for (int position : playerPositions) {
            // Get the scaled center coordinates for the given position
            int[] centerCoordinates = getScaledCenterCoordinates(position, canvas.getWidth(), canvas.getHeight());
            int x = centerCoordinates[0];
            int y = centerCoordinates[1];

            // Log the values of x and y
            Log.d("BoardView", "Player " + i + " - Position: " + position + ", X: " + x + ", Y: " + y);

            paint.setColor(PLAYER_COLORS[i]);
            canvas.drawCircle(x, y, canvas.getWidth() / 40, paint); // Adjust the pawn size
            i++;
        }
    }

    // Helper method to get the scaled center coordinates for a given position
    private int[] getScaledCenterCoordinates(int position, int viewWidth, int viewHeight) {
        // Reference dimensions (from Photoshop)
        int referenceWidth = 762 ; // Width of the Photoshop image
        int referenceHeight = 759; // Height of the Photoshop image

        // Scaling factors
        float scaleX = (float) viewWidth / referenceWidth;
        float scaleY = (float) viewHeight / referenceHeight;

        // Get the reference coordinates for the given position
        int[] referenceCoordinates = getCenterCoordinates(position);
        int refX = referenceCoordinates[0];
        int refY = referenceCoordinates[1];

        // Scale the coordinates
        int scaledX = (int) (refX * scaleX);
        int scaledY = (int) (refY * scaleY);

        return new int[]{scaledX, scaledY};
    }


    // Helper method to get the center coordinates for a given position
    private int[] getCenterCoordinates(int position) {
        // Map each position to its center coordinates

        //position = position %36;
        switch (position) {
            // Top side
            default : return new int[]{65, 57};
            case 2: return new int[]{165, 57};
            case 3: return new int[]{236, 57};
            case 4: return new int[]{310, 57};
            case 5: return new int[]{386, 57};
            case 6: return new int[]{458, 57};
            case 7: return new int[]{530, 57};
            case 8: return new int[]{605, 57};
            case 9: return new int[]{708, 57};
            case 10: return new int[]{708, 156};
            // Right side
            case 11: return new int[]{708, 228};
            case 12: return new int[]{708, 306};
            case 13: return new int[]{708, 375};
            case 14: return new int[]{708, 450};
            case 15: return new int[]{708, 523};
            case 16: return new int[]{708, 595};
            case 17: return new int[]{708, 700};
            //case 18: return new int[]{605, 700};
            case 18: return new int[]{605, 700}; // Bottom side, position 17
            case 19: return new int[]{530, 700}; // Bottom side, position 18
            case 20: return new int[]{458, 700}; // Bottom side, position 19
            case 21: return new int[]{386, 700}; // Bottom side, position 20
            case 22: return new int[]{310, 700}; // Bottom side, position 21
            case 23: return new int[]{236, 700}; // Bottom side, position 22
            case 24: return new int[]{165, 700}; // Bottom side, position 23
            //case 24: return new int[]{65, 750};  // Bottom side, position 24

            case 25: return new int[]{65, 700};   // Left side, position 25
            case 26: return new int[]{65, 595};   // Left side, position 26
            case 27: return new int[]{65, 523};   // Left side, position 27
            case 28: return new int[]{65, 450};   // Left side, position 28
            case 29: return new int[]{65, 375};   // Left side, position 29
            case 30: return new int[]{65, 306};   // Left side, position 30
            case 31: return new int[]{65, 228};   // Left side, position 31
            case 32: return new int[]{65, 157};   // Left side, position 32

            // Default case (if position is out of range)
            //default: return new int[]{0, 0};
        }
    }

    // Helper methods for calculating coordinates

}