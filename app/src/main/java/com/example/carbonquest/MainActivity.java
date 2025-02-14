package com.example.carbonquest;

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
    private int playerTurn = 0; // 0 pour joueur 1, 1 pour joueur 2
    private Random random = new Random();
    private Handler handler;
    private ImageButton fullLogButtonPlayer1;
    private ImageButton fullLogButtonPlayer2;
    private BoardView boardView;
    private TextView player1Role, player2Role;
    private ImageView diceImage11, diceImage12, diceImage21, diceImage22;
    private ImageView player1TurnIndicator, player2TurnIndicator;
    private Button rollDiceButtonPlayer1, rollDiceButtonPlayer2;

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

        // Vérification pour éviter les crashes si les boutons ne sont pas trouvés
        if (fullLogButtonPlayer1 != null) {
            fullLogButtonPlayer1.setOnClickListener(view -> onFullLogClickPlayer(0));
        } else {
            Log.e("MainActivity", "fullLogButtonPlayer1 est NULL");
        }

        if (fullLogButtonPlayer2 != null) {
            fullLogButtonPlayer2.setOnClickListener(view -> onFullLogClickPlayer(1));
        } else {
            Log.e("MainActivity", "fullLogButtonPlayer2 est NULL");
        }

        player1Role.setText("Player 1: Eco-Warrior");
        player2Role.setText("Player 2: Carbon Tycoon");
        diceImage11.setImageResource(R.drawable.eco_dice);
        diceImage12.setImageResource(R.drawable.eco_dice);
        diceImage21.setImageResource(R.drawable.eco_dice);
        diceImage22.setImageResource(R.drawable.eco_dice);

        rollDiceButtonPlayer1.setOnClickListener(view -> throwDices(0));
        rollDiceButtonPlayer2.setOnClickListener(view -> throwDices(1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        init();
        updateTurnUI();

    }
    public void onFullLogClickPlayer(int playerIndex) {
        displayPlayerLog(playerIndex);
    }
    private void displayPlayerLog(int playerIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.infopopup, null);

        int playerPositionBefore = playerPositions[playerIndex];
        String playerName = "Joueur " + (playerIndex + 1);

        String logMessageBefore = playerName + " - Before the action:\\\\\n" +
                "Eco-Zone : " + playerPositionBefore;

        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;
        int diceSum = dice1 + dice2;

        int newPosition = (playerPositions[playerIndex] + diceSum) % 32; // Ensure board size is eco-themed
        playerPositions[playerIndex] = newPosition;

        String logMessageAfter = playerName + " - After the action:\\\\\n" +
                "Dice Roll : " + dice1 + " and " + dice2 + "\\\\\n" +
                "New Eco-Zone : " + newPosition;

        new AlertDialog.Builder(this)
                .setView(popupView)
                .setTitle("Log complet de " + playerName)
                .setMessage(logMessageBefore + "\\\\\n\\\\\n" + logMessageAfter)
                .setPositiveButton("OK", null)
                .show();
    }
    private void throwDices(int currentPlayer) {
        if (currentPlayer != playerTurn) return;

        rollDiceButtonPlayer1.setVisibility(View.INVISIBLE);
        rollDiceButtonPlayer2.setVisibility(View.INVISIBLE);

        for (int i=0; i < 2; i++)
            diceValues[i] = random.nextInt(6) + 1;
        int dicesum = diceValues[0] + diceValues[1];
        Toast.makeText(this, "Joueur " + (playerTurn + 1) + " a lancé : " + dicesum, Toast.LENGTH_SHORT).show();

        int newPosition = (playerPositions[playerTurn] + dicesum) % 32; // Align with eco-themed board size
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

            if (intermediateDestination > 32) {
                intermediateDestination = intermediateDestination - 32;
            }

            Log.d("BoardView", "intermediateDestination: " + intermediateDestination + ", finalDestination: " + finalDestination);

            final int nextDestination = intermediateDestination;

            handler.postDelayed(
                    () -> gotoDestination(nextDestination, finalDestination, fast),
                    fast ? 250 : 500
            );
        } else {
            // Do not switch turns here; allow the player to roll again.
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
                diceDrawable = R.drawable.eco_dice;
        }
        diceImage.setImageResource(diceDrawable);
    }
    private void endTurn(int currentPlayer) {
        if (currentPlayer != playerTurn) return;

        // Switch to the next player's turn
        playerTurn = (playerTurn + 1) % playerPositions.length;
        updateTurnUI();
    }
}