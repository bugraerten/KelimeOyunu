package com.example.kelimeoyunu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;


public class PlayActivity extends AppCompatActivity {

    private TextView soruTextView, kelimeTextView;
    private EditText tahminEditText;
    private SQLiteDatabase database;
    private Cursor cursor;
    private ArrayList<String> sorularList;
    private ArrayList<String> sorularKodList;
    private ArrayList<String> kelimeList;
    private ArrayList<Character> kelimeHarfleri;

    private Random rndSoru, rndKelime, rndHarf;
    private int rndSoruNumber, rndKelimeNumber, rndHarfNumber;
    private String rastgeleSoru, rastgeleSoruKodu, rastgeleKelime, kelimeBilgisi = "", editTahmin;
    private int rastgeleBelirlenecekHarfSayisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        kelimeTextView = (TextView) findViewById(R.id.text_bosluk);
        soruTextView = (TextView) findViewById(R.id.text_sorular);
        tahminEditText = (EditText) findViewById(R.id.edit_tahmin);
        sorularList = new ArrayList<>();
        sorularKodList = new ArrayList<>();
        kelimeList = new ArrayList<>();
        rndSoru = new Random();
        rndKelime = new Random();
        rndHarf = new Random();


        for (Map.Entry soru : SplashScreenActivity.sorularHash.entrySet()) {
            sorularList.add(String.valueOf(soru.getValue()));
            sorularKodList.add(String.valueOf(soru.getKey()));
        }
        rndSoruNumber = rndSoru.nextInt(sorularList.size());
        rastgeleSoru = sorularList.get(rndSoruNumber);
        rastgeleSoruKodu = sorularKodList.get(rndSoruNumber);

        soruTextView.setText(rastgeleSoru);

        try {
            database = this.openOrCreateDatabase("KelimeBilmece", MODE_PRIVATE, null);
            cursor = database.rawQuery("SELECT * FROM kelimeler WHERE kKod = ?", new String[]{sorularKodList.get(rndSoruNumber)});
            int kelimeIndex = cursor.getColumnIndex("kelime");
            while (cursor.moveToNext()) {
                kelimeList.add(cursor.getString(kelimeIndex));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        rndKelimeNumber = rndKelime.nextInt(kelimeList.size());
        rastgeleKelime = kelimeList.get(rndKelimeNumber);
        kelimeList.remove(rndKelimeNumber);

        for (int i = 0; i < rastgeleKelime.length(); i++) {
            if (i < rastgeleKelime.length() - 1)
                kelimeBilgisi += "_ ";
            else
                kelimeBilgisi += "_";
        }
        kelimeTextView.setText(kelimeBilgisi);
        System.out.println("Gelen kelime = " + rastgeleKelime);
        System.out.println("gelen Harf sayısı = " + rastgeleKelime.length());
        kelimeHarfleri = new ArrayList<>();

        for (char harf : rastgeleKelime.toCharArray()) {
            kelimeHarfleri.add(harf);
        }
        if (rastgeleKelime.length() >= 5 && rastgeleKelime.length() <= 7) {
            rastgeleBelirlenecekHarfSayisi = 1;
        } else if (rastgeleKelime.length() > 7 && rastgeleKelime.length() <= 10) {
            rastgeleBelirlenecekHarfSayisi = 2;
        } else if (rastgeleKelime.length() > 10 && rastgeleKelime.length() <= 14) {
            rastgeleBelirlenecekHarfSayisi = 3;
        } else if (rastgeleKelime.length() >= 15) {
            rastgeleBelirlenecekHarfSayisi = 4;
        } else {
            rastgeleBelirlenecekHarfSayisi = 0;
        }
        for (int i = 0; i < rastgeleBelirlenecekHarfSayisi; i++) {
            rastgeleHarfAl();
        }
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        finish();
        startActivity(mainIntent);
        overrideActivityTransition(R.anim.slide_in_down, R.anim.slide_out_up);
    }

    private void overrideActivityTransition(int slideInDown, int slideOutUp) {
    }

    public void btnHarfAl(View v) {
        rastgeleHarfAl();
    }

    public void btnTahminEt(View v) {
        editTahmin = tahminEditText.getText().toString();
        if (!TextUtils.isEmpty(editTahmin)) {
            if (editTahmin.matches(rastgeleKelime)) {
                Toast.makeText(getApplicationContext(), "Doğru Tahmin! ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Yanlış Tahmin! ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Tahmin Değeri Boş Olamaz! ", Toast.LENGTH_SHORT).show();
        }
    }

    private void rastgeleHarfAl() {
        if (kelimeHarfleri.size() > 0) {
            rndHarfNumber = rndHarf.nextInt(kelimeHarfleri.size());
            String[] txtHarfler = kelimeTextView.getText().toString().split(" ");
            char[] gelenKelimeHarfler = rastgeleKelime.toCharArray();

            for (int i = 0; i < rastgeleKelime.length(); i++) {
                if (txtHarfler[i].equals("_") && gelenKelimeHarfler[i] == kelimeHarfleri.get(rndHarfNumber)) {
                    txtHarfler[i] = String.valueOf(kelimeHarfleri.get(rndHarfNumber));
                    kelimeBilgisi = "";

                    for (int j = 0; j < txtHarfler.length; j++) {
                        if (j < txtHarfler.length - 1)
                            kelimeBilgisi += txtHarfler[j] + " ";
                        else
                            kelimeBilgisi += txtHarfler[j];
                    }
                    break;
                }
            }
            kelimeTextView.setText(kelimeBilgisi);
            kelimeHarfleri.remove(rndHarfNumber);
        }
    }
}


