package com.example.carbonquest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int[] diceValues = new int[]{-1, -1};
    private int[] playerPositions = new int[]{};
    private int playerTurn = 0;
    private Handler handler;

    private int nombreDeJoueurs = 0;
    private ArrayList<String> nomsJoueurs = new ArrayList<>();

    private ArrayList<Integer> imagesAttribuees = new ArrayList<>();
    private final int[] imagesJoueurs = {
            R.drawable.directrice, R.drawable.directrice
    };


    public static final String diceCharacters = "⚀⚁⚂⚃⚄⚅";

    private static final Random random = new Random();
    private void throwDices() {
        for (int i=0; i < 2; i++)
            diceValues[i] = random.nextInt(6) + 1;
        //diceValues[0] = 6;
        //diceValues[1] = 1;
    }
    private int computePosition(int initialPosition, int diceSum) {
        if ((initialPosition + diceSum) <= 100)
            return initialPosition + diceSum;
        else
            return 100 - (initialPosition + diceSum - 100);
    }

    private void demanderNomsJoueurs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Entrez le noms des joueurs");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final ArrayList<EditText> inputs = new ArrayList<>();
        for (int i = 0; i < nombreDeJoueurs; i++) {
            EditText input = new EditText(this);
            input.setHint("Joueur " + (i + 1));
            layout.addView(input);
            inputs.add(input);
        }

        builder.setView(layout);

        // Bouton de validation
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nomsJoueurs.clear();
                for (EditText input : inputs) {
                    String nom = input.getText().toString().trim();
                    if (!nom.isEmpty()) {
                        nomsJoueurs.add(nom);
                    }
                }

                if (nomsJoueurs.size() == nombreDeJoueurs) {
                    attribuerImagesAleatoires();
                    Toast.makeText(MainActivity.this, "Noms enregistrés", Toast.LENGTH_SHORT).show();
                    afficherPopupsJoueurs();
                } else {
                    Toast.makeText(MainActivity.this, "Veuillez entrer tous les noms", Toast.LENGTH_SHORT).show();
                    demanderNomsJoueurs();
                }
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(MainActivity.this, "Entrez les noms des joueurs", Toast.LENGTH_SHORT).show();
                demanderNomsJoueurs();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void attribuerImagesAleatoires() {
        List<Integer> imagesDisponibles = new ArrayList<>();
        for (int img : imagesJoueurs) {
            imagesDisponibles.add(img);
        }

        Collections.shuffle(imagesDisponibles); // Mélange les images
        imagesAttribuees.clear();

        Random random = new Random();
        for (int i = 0; i < nomsJoueurs.size(); i++) {
            if (i < imagesDisponibles.size()) {
                imagesAttribuees.add(imagesDisponibles.get(i));
            } else {
                imagesAttribuees.add(imagesJoueurs[random.nextInt(imagesJoueurs.length)]);
            }
        }
    }
    private void afficherPopupsJoueurs() {
        for (int i = 0; i < nomsJoueurs.size(); i++) {
            afficherPopup(nomsJoueurs.get(i), imagesAttribuees.get(i));
        }
    }

    private void afficherPopup(String nomJoueur, int imageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.infopopup, null);

        ImageView imageView = popupView.findViewById(R.id.imageJoueur);
        TextView textView = popupView.findViewById(R.id.nomJoueur);

        imageView.setImageResource(imageId);
        textView.setText(nomJoueur);

        builder.setView(popupView);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void demanderNombreDeJoueurs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Joueurs");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String valeur = input.getText().toString();
                if (!valeur.isEmpty()) {
                    nombreDeJoueurs = Integer.parseInt(valeur);
                    playerPositions = new int[nombreDeJoueurs]; // Initialize the array with the correct size
                    for (int i = 0; i < nombreDeJoueurs; i++) {
                        playerPositions[i] = 1; // Set each player's starting position to 1
                    }
                    demanderNomsJoueurs();
                } else {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un nombre valide", Toast.LENGTH_SHORT).show();
                    demanderNombreDeJoueurs(); // Réouvrir si invalide
                }
            }
        });

        // Bouton Annuler
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(MainActivity.this, "Vous devez entrer un nombre de jeux", Toast.LENGTH_SHORT).show();
                demanderNombreDeJoueurs();
            }
        });

        builder.setCancelable(false); // Empêche de fermer sans choisir
        builder.show();
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

            // Create a final variable for use in the lambda
            final int nextDestination = intermediateDestination;

            handler.postDelayed(
                    () -> gotoDestination(nextDestination, finalDestination, fast),
                    fast ? 250 : 500
            );
        } else {
            playerTurn = (playerTurn + 1) % playerPositions.length;
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        handler = new Handler();
        demanderNombreDeJoueurs();

        BoardView boardView = findViewById(R.id.boardview);

        Button throwButton = findViewById(R.id.throwButton);
        throwButton.setOnClickListener(view -> {
            throwDices();
            int diceSum = diceValues[0] + diceValues[1];
            int destination = (playerPositions[playerTurn] + diceSum) % 40; // Move forward

            if (destination > 32){
                destination = destination-32;
            }
            TextView diceView = findViewById(R.id.diceView);
            diceView.setText(diceCharacters.charAt(diceValues[0] - 1) + " " + diceCharacters.charAt(diceValues[1] - 1));
            Log.d("BoardView", "destination before calling gotoDestination: " + destination  );
            gotoDestination(playerPositions[playerTurn], destination, false);
        });

        boardView.setPlayerPositions(playerPositions);
    }
}