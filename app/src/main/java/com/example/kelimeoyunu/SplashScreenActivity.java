package com.example.kelimeoyunu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {

    private String[] sorularList = {"Mutfakta iş yaparken veya yemek yerken kullanılan aletler nelerdir ?", "Arabanın parçaları nelerdir ?"};
    private String[] sorularKodList = {"m1", "a1"};
    private String[] kelimelerList = {"çatal", "bıçak", "kaşık", "tencere", "motor", "krank mili", "egzoz", "triger kayışı"};
    private String[] kelimeKodList = {"m1", "m1", "m1", "m1", "a1", "a1", "a1", "a1"};
    private ProgressBar mProgress;
    private TextView mTextView;
    private SQLiteDatabase database;
    private Cursor cursor;
    private float maksProg = 100f, artacakProg, progMiktari = 0;
    static public HashMap<String, String> sorularHash;
    private String sqlSorgusu;
    private SQLiteStatement statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mProgress = (ProgressBar) findViewById(R.id.splash_progressBar);
        mTextView = (TextView) findViewById(R.id.splash_TextView);
        sorularHash = new HashMap<>();

        try {
            database = this.openOrCreateDatabase("KelimeBilmece", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS Sorular (id INTEGER PRIMARY KEY, sKod VARCHAR UNIQUE, soru VARCHAR)");
            database.execSQL("DELETE FROM Sorular");
            sqlSorulariEkle();

            database.execSQL("CREATE TABLE IF NOT EXISTS Kelimeler (kKod VARCHAR, kelime VARCHAR, FOREIGN KEY(kKod) REFERENCES Sorular(sKod)) ");
            database.execSQL("DELETE FROM Kelimeler");
            sqlKelimeleriEkle();

            cursor = database.rawQuery("SELECT * FROM Sorular", null);
            artacakProg = maksProg / cursor.getCount();

            int sKodIndex = cursor.getColumnIndex("sKod");
            int soruIndex = cursor.getColumnIndex("soru");

            mTextView.setText("Sorular yükleniyor...");

            while (cursor.moveToNext()) {
                sorularHash.put(cursor.getString(sKodIndex), cursor.getString(soruIndex));
                progMiktari += artacakProg;
                mProgress.setProgress((int) progMiktari);
            }

            mTextView.setText("Sorular Başarıyla Yüklendi, Başlatılıyor..");
            cursor.close();

            new CountDownTimer(1100, 1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    finish();
                    startActivity(mainIntent);
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sqlSorulariEkle() {

        try {
            for (int s = 0; s < sorularList.length; s++) {
                sqlSorgusu = "INSERT INTO Sorular(sKod,soru)VALUES(?, ?)";
                statement = database.compileStatement(sqlSorgusu);
                statement.bindString(1, sorularKodList[s]);
                statement.bindString(2, sorularList[s]);
                statement.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sqlKelimeleriEkle() {
        try {
            for (int k = 0; k <= kelimelerList.length; k++) {
                sqlSorgusu = "INSERT INTO Kelimeler(kKod,kelime)VALUES(?, ?)";
                statement = database.compileStatement(sqlSorgusu);
                statement.bindString(1, kelimeKodList[k]);
                statement.bindString(2, kelimelerList[k]);
                statement.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}