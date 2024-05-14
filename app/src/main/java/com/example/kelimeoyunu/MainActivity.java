package com.example.kelimeoyunu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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
    }

    public void mainBtn(View v) {
        int itemId = v.getId();
        if (itemId == R.id.basla_btn) {
            Intent playIntent = new Intent(this, PlayActivity.class);
            finish();
            startActivity(playIntent);
            overrideActivityTransition(R.anim.slide_out_up, R.anim.slide_in_down);
        } else if (itemId == R.id.nasil_btn) {

        } else if (itemId == R.id.cikis_btn) {
            cikisBildirim();

        }
    }

    @Override
    public void onBackPressed() {
        cikisBildirim();
    }

    public void cikisBildirim() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Çıkış");
        alertDialog.setMessage("Uygulamadan Çıkmak İstediğinize Emin Misiniz?");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setPositiveButton("Evet",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
        alertDialog.setNegativeButton("Hayır",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void overrideActivityTransition(int slideOutUp, int slideInDown) {
    }
}