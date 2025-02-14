package com.example.carbonquest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int[] diceValues = new int[]{-1, -1};
    private int[] playerPositions = new int[2];
    private Random random = new Random(System.currentTimeMillis());
    private int playerTurn = 0;
    private Handler handler;
    private ImageButton fullLogButtonPlayer1;
    private ImageButton fullLogButtonPlayer2;
    private BoardView boardView;
    private TextView player1Role, player2Role;
    private ImageView diceImage11, diceImage12, diceImage21, diceImage22;
    private ImageView player1TurnIndicator, player2TurnIndicator;
    private Button rollDiceButtonPlayer1, rollDiceButtonPlayer2;

    @SuppressLint("SetTextI18n")
    private void init(){
        boardView = findViewById(R.id.boardview);
        rollDiceButtonPlayer1 = findViewById(R.id.rollDiceButtonPlayer1);
        rollDiceButtonPlayer2 = findViewById(R.id.rollDiceButtonPlayer2);
        diceImage11 = findViewById(R.id.diceImage11);
        diceImage12 = findViewById(R.id.diceImage12);
        diceImage21 = findViewById(R.id.diceImage21);
        diceImage22 = findViewById(R.id.diceImage22);
        player1TurnIndicator = findViewById(R.id.player1_turn_indicator);
        player2TurnIndicator = findViewById(R.id.player2_turn_indicator);
        player1Role = findViewById(R.id.player1Role);
        player2Role = findViewById(R.id.player2Role);
        fullLogButtonPlayer1 = findViewById(R.id.fullLogButtonPlayer1);
        fullLogButtonPlayer2 = findViewById(R.id.fullLogButtonPlayer2);
        player1Role.setText("Player 1: Directrice");
        player2Role.setText("Player 2: Directrice");
        diceImage11.setImageResource(R.drawable.dice3d160);
        diceImage12.setImageResource(R.drawable.dice3d160);
        diceImage21.setImageResource(R.drawable.dice3d160);
        diceImage22.setImageResource(R.drawable.dice3d160);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        init();
        updateTurnUI();
        playerTurn = random.nextInt();
        fullLogButtonPlayer1.setOnClickListener(view -> onFullLogClickPlayer(0));
        fullLogButtonPlayer2.setOnClickListener(view -> onFullLogClickPlayer(1));
        rollDiceButtonPlayer1.setOnClickListener(view -> throwDices(0));
        rollDiceButtonPlayer2.setOnClickListener(view -> throwDices(1));
    }
    public void onFullLogClickPlayer(int playerIndex) {
        displayPlayerLog(playerIndex);
    }

    private void displayPlayerLog(int playerIndex, int dice1, int dice2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.infopopup, null);

        int playerPositionBefore = playerPositions[playerIndex];
        String playerName = "Joueur " + (playerIndex + 1);

        String logMessageBefore = playerName + " - Avant l'action :\n" +
                "Position : " + playerPositionBefore;

        int diceSum = dice1 + dice2;

        int newPosition = (playerPositions[playerIndex] + diceSum) % 32;

        String logMessageAfter = playerName + " - Après l'action :\n" +
                "Lancer de dés : " + dice1 + " et " + dice2 + "\n" +
                "Nouvelle position : " + newPosition;

        new AlertDialog.Builder(this)
                .setView(popupView)
                .setTitle("Log complet de " + playerName)
                .setMessage(logMessageBefore + "\n\n" + logMessageAfter)
                .setPositiveButton("OK", null)
                .show();
    }
    private void throwDices(int currentPlayer) {
        if (currentPlayer != playerTurn) return;

        for (int i=0; i < 2; i++)
            diceValues[i] = random.nextInt(6) + 1;
        int dicesum = diceValues[0] + diceValues[1];
        Toast.makeText(this, "Joueur " + (playerTurn + 1) + " a lancé : " + dicesum, Toast.LENGTH_SHORT).show();

        int newPosition = (playerPositions[playerTurn] + dicesum) % 40;
        gotoDestination(playerPositions[playerTurn], newPosition,false);
        boardView.setPlayerPositions(playerPositions);
        updateTurnUI();
    }
    private void gotoDestination(int currentPosition, int finalDestination, boolean fast) {
        playerPositions[playerTurn] = currentPosition;
        BoardView boardView = findViewById(R.id.boardview);
        boardView.setPlayerPositions(playerPositions);

        if (currentPosition != finalDestination) {
            int intermediateDestination = (currentPosition % 40) + 1; // Move forward by 1 (Monopoly-style)

            if (intermediateDestination > 40) {
                intermediateDestination = intermediateDestination - 40;
            }

            Log.d("BoardView", "intermediateDestination: " + intermediateDestination + ", finalDestination: " + finalDestination);

            final int nextDestination = intermediateDestination;

            handler.postDelayed(
                    () -> gotoDestination(nextDestination, finalDestination, fast),
                    fast ? 250 : 500
            );
        } else {
            playerTurn = (playerTurn + 1) % playerPositions.length;
        }


    }
    private void updateTurnUI() {
        if (playerTurn == 0) {
            rollDiceButtonPlayer1.setVisibility(View.VISIBLE);
            rollDiceButtonPlayer2.setVisibility(View.INVISIBLE);
            player1TurnIndicator.setVisibility(View.VISIBLE);
            player2TurnIndicator.setVisibility(View.INVISIBLE);
        } else {
            rollDiceButtonPlayer1.setVisibility(View.INVISIBLE);
            rollDiceButtonPlayer2.setVisibility(View.VISIBLE);
            player1TurnIndicator.setVisibility(View.INVISIBLE);
            player2TurnIndicator.setVisibility(View.VISIBLE);
        }
    }
    private void updateDiceImage(ImageView diceImage, int diceResult) {
        int diceDrawable;
        switch (diceResult) {
            case 1:
                diceDrawable = R.drawable.dice1;
                break;
            case 2:
                diceDrawable = R.drawable.dice2;
                break;
            case 3:
                diceDrawable = R.drawable.dice3;
                break;
            case 4:
                diceDrawable = R.drawable.dice4;
                break;
            case 5:
                diceDrawable = R.drawable.dice5;
                break;
            case 6:
                diceDrawable = R.drawable.dice6;
                break;
            default:
                diceDrawable = R.drawable.dice3d160;
        }
        diceImage.setImageResource(diceDrawable);
    }
}
