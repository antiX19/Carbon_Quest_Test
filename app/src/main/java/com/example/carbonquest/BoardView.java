package com.example.carbonquest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class BoardView extends View {
    private Bitmap boardBitmap;
    private Paint paint;
    private int[] playerPositions;
    private int player1Position = 0;
    private int player2Position = 0;
    private Bitmap tokenPlayer1;
    private Bitmap tokenPlayer2;

    public BoardView(Context context) {
        super(context);
        init();
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        boardBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.plateau);
        paint = new Paint();
        tokenPlayer1 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.zorro);
        tokenPlayer2 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.luffy);
        tokenPlayer1 = Bitmap.createScaledBitmap(tokenPlayer1, tokenPlayer1.getWidth() / 2, tokenPlayer1.getHeight() / 2, true);
        tokenPlayer2 = Bitmap.createScaledBitmap(tokenPlayer2, tokenPlayer2.getWidth() / 2, tokenPlayer2.getHeight() / 2, true);
        playerPositions = new int[]{50, 700};
        setPlayerPositions(playerPositions );

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int left = canvas.getWidth();
        int top = canvas.getHeight();
        Rect dest = new Rect(0, 0, left, top);
        canvas.drawBitmap(boardBitmap, null, dest, paint);
        drawPlayerToken(canvas, player1Position, tokenPlayer1, dest);
        drawPlayerToken(canvas, player2Position, tokenPlayer2, dest);
        int[] coords1 = getScaledCenterCoordinates(player1Position, canvas.getWidth(), canvas.getHeight());
        int[] coords2 = getScaledCenterCoordinates(player2Position, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(tokenPlayer1, coords1[0] - tokenPlayer1.getWidth()/2, coords1[1] - tokenPlayer1.getHeight()/2, paint);
        canvas.drawBitmap(tokenPlayer2, coords2[0] - tokenPlayer2.getWidth()/2, coords2[1] - tokenPlayer2.getHeight()/2, paint);
    }

    public void setPlayerPositions(int[] positions) {
        playerPositions = positions;
        invalidate();
    }

    private void drawPlayerToken(Canvas canvas, int position, Bitmap token, Rect dest) {
        int[] coords = getCenterCoordinates(position);
        if (coords != null) {
            canvas.drawBitmap(token, coords[0] - token.getWidth() / 2, coords[1] - token.getHeight() / 2, paint);
        }
    }

    private int[] getScaledCenterCoordinates(int position, int viewWidth, int viewHeight) {
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

    private int[] getCenterCoordinates(int position) {
        switch (position) {
            default : return new int[]{50, 700};
            // Bottom side
            case 1: return new int[]{50, 700};
            case 2: return new int[]{165, 700};
            case 3: return new int[]{236, 700};
            case 4: return new int[]{310, 700};
            case 5: return new int[]{386, 700};
            case 6: return new int[]{458, 700};
            case 7: return new int[]{530, 700};
            case 8: return new int[]{605, 700};
            // Right side
            case 9: return new int[]{700, 700};
            case 10: return new int[]{700, 600};
            case 11: return new int[]{708, 523};
            case 12: return new int[]{708, 450};
            case 13: return new int[]{708, 375};
            case 14: return new int[]{708, 310};
            case 15: return new int[]{708, 233};
            case 16: return new int[]{708, 151};
            // Top side
            case 17: return new int[]{722, 50};
            case 18: return new int[]{605, 50};
            case 19: return new int[]{530, 50};
            case 20: return new int[]{458, 50};
            case 21: return new int[]{386, 58};
            case 22: return new int[]{310, 53};
            case 23: return new int[]{236, 53};
            case 24: return new int[]{165, 53};
            // Left side
            case 25: return new int[]{56, 65};
            case 26: return new int[]{56, 158};
            case 27: return new int[]{56, 229};
            case 28: return new int[]{56, 309};
            case 29: return new int[]{56, 375};
            case 30: return new int[]{56, 450};
            case 31: return new int[]{56, 528};
            case 32: return new int[]{56, 600};
        }
    }

}
