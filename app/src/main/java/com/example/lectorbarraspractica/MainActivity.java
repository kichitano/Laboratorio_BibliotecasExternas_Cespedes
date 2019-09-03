package com.example.lectorbarraspractica;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    Button buttonCapturar;
    TextView txtResultado;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCapturar = findViewById(R.id.btnIr);
        txtResultado = findViewById(R.id.txtTexto);
        imageView = findViewById(R.id.sView);

        buttonCapturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setOrientationLocked(false);
                integrator.setPrompt("Camara a la inversa");
                integrator.initiateScan();
            }
        });

        registerForContextMenu(txtResultado);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        final IntentResult intentResult =
                IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        manipularResultado(intentResult);
    }

    private void actualiarUITextViews(String resultadoScaneo){
        ((TextView)findViewById(R.id.txtTexto)).setText(resultadoScaneo);
    }

    private void manipularResultado(IntentResult intentResult){
        if(intentResult != null){
            actualiarUITextViews(intentResult.getContents());
        }else{
            Toast.makeText(getApplicationContext(),"No se leyó nada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Elija una opcion");
        menu.add(0, v.getId(), 0, "Copiar texto");
        menu.add(0, v.getId(), 0, "Ir a direccion");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Copiar texto") {
            try{
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(txtResultado.getText(), txtResultado.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this,"Texto copiado!", Toast.LENGTH_SHORT);
            }catch (Exception e){
                Toast.makeText(this,"Error al copiar texto", Toast.LENGTH_SHORT);
            }
        }
        else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(txtResultado.getText().toString()));
            startActivity(browserIntent);
        }
        return true;
    }

}
