package up.edu.br.contatos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ContatoActivity extends AppCompatActivity {

    Contato contato;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        EditText txtNome = (EditText) findViewById(R.id.txtNome);
        Spinner spTipo = (Spinner) findViewById(R.id.spTipo);
        EditText txtTelefone = (EditText) findViewById(R.id.txtTelefone);
        EditText txtEmail = (EditText) findViewById(R.id.txtEmail);


        EditText txtCidade = (EditText) findViewById(R.id.txtCidade);
        Spinner spEstado = (Spinner) findViewById(R.id.spEstado);

        CheckBox checkAtivo = (CheckBox) findViewById(R.id.checkAtivo);


        Intent it = getIntent();
        if (it != null && it.hasExtra("contato")) {

            contato = (Contato) it.getSerializableExtra("contato");

            txtNome.setText(contato.getNome());
            spTipo.setSelection(((ArrayAdapter) spTipo.getAdapter()).getPosition(contato.getTipo()));
            txtTelefone.setText(contato.getNumero());
            txtEmail.setText(contato.getEmail());

            txtCidade.setText(contato.getCidade());
            spEstado.setSelection(((ArrayAdapter) spEstado.getAdapter()).getPosition(contato.getEstado()));

            checkAtivo.setChecked(contato.isAtivo());


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {
            EditText txtNome = (EditText) findViewById(R.id.txtNome);
            Spinner spTipo = (Spinner) findViewById(R.id.spTipo);
            EditText txtTelefone = (EditText) findViewById(R.id.txtTelefone);
            EditText txtEmail = (EditText) findViewById(R.id.txtEmail);

            EditText txtCidade = (EditText) findViewById(R.id.txtCidade);
            Spinner spEstado = (Spinner) findViewById(R.id.spEstado);

            CheckBox checkAtivo = (CheckBox) findViewById(R.id.checkAtivo);


            if (contato == null) {
                contato = new Contato();
            }

            contato.setNome(txtNome.getText().toString());
            contato.setTipo(spTipo.getSelectedItem().toString());
            contato.setNumero(txtTelefone.getText().toString());
            contato.setEmail(txtEmail.getText().toString());

            contato.setCidade(txtCidade.getText().toString());
            contato.setEstado(spEstado.getSelectedItem().toString());

            contato.setAtivo(checkAtivo.isChecked());

            new ContatoDao().salvar(contato);
            contato = null;


            Toast.makeText(getApplicationContext(),
                    "Salvo com sucesso!",
                    Toast.LENGTH_LONG).show();

            Intent it = new Intent(ContatoActivity.this, MainActivity.class);
            startActivity(it);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void ligar(View view) {

        EditText txtTelefone = (EditText) findViewById(R.id.txtTelefone);

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + txtTelefone.getText()));

        ActivityCompat.requestPermissions(ContatoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

    }

    public void email(View view) {

        EditText txtEmail = (EditText) findViewById(R.id.txtEmail);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + txtEmail.getText().toString()));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Testando envio por de email");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Enviado email por intent");

        startActivity(Intent.createChooser(emailIntent, "Email do contato"));
    }
}
