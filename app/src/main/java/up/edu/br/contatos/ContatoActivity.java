package up.edu.br.contatos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ContatoActivity extends AppCompatActivity {

    Contato contato;
    int CAMERA_REQUEST = 1888;

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
        ImageView image = (ImageView) findViewById(R.id.image);

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

            //pegar imagem do banco
            ByteArrayInputStream imagemStream = new ByteArrayInputStream(contato.getImagem());
            Bitmap bitmap = BitmapFactory.decodeStream(imagemStream);
            image.setImageBitmap(bitmap);

        }
        //adicionando acesso a camera.
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
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

            //image view
            ImageView image = (ImageView)findViewById(R.id.image);

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

            //salvar foto
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,baos);
            byte[] imagemInByte = baos.toByteArray();
            contato.setImagem(imagemInByte);

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView image = (ImageView)findViewById(R.id.image);
            image.setImageBitmap(photo);
        }
    }

}
