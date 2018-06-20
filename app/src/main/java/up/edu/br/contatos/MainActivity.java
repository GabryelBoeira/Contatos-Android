package up.edu.br.contatos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        new Conexao(getApplicationContext(), "contatos.db", null ,2);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(MainActivity.this,
                        ContatoActivity.class);
                startActivity(it);


                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        ListView listaContatos = (ListView) findViewById(R.id.listaContatos);

//        ArrayAdapter<Contato> arrayAdapterContatos = new ArrayAdapter<Contato>(getApplicationContext(),
//                android.R.layout.simple_list_item_1);
//
//        arrayAdapterContatos.addAll(new ContatoDao().listar());

//        listaContatos.setAdapter(arrayAdapterContatos);

        ContatoAdapter contatoAdapter = new ContatoAdapter(new ContatoDao().listar(), this);

        listaContatos.setAdapter(contatoAdapter);

        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contato c = (Contato) parent.getItemAtPosition(position);

//                Toast.makeText(getApplicationContext(),"Contato: " + c.getNome(), Toast.LENGTH_LONG).show();

                Intent it = new Intent(MainActivity.this, ContatoActivity.class);

                it.putExtra("contato", c);

                startActivity(it);

            }
        });

        listaContatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alert =
                        new AlertDialog.Builder
                                (MainActivity.this);
                alert.setMessage("Deseja realmente excluir?");
                alert.setCancelable(false);
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Contato contato = (Contato) parent.getItemAtPosition(position);

                        new ContatoDao().excluir(contato);

                        ((ContatoAdapter) parent.getAdapter()).remove(contato);
                        ((ContatoAdapter) parent.getAdapter()).notifyDataSetChanged();

                    }
                });
                alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

                alert.show();

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
