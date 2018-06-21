package up.edu.br.contatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContatoDao {

    static ArrayList<Contato> contatos = new ArrayList<Contato>();
    static Integer id = 0;

    public void salvar (Contato contato){
        SQLiteDatabase conn = Conexao.getInstance().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", contato.getNome());
        values.put("tipo", contato.getTipo());
        values.put("telefone", contato.getNumero());
        values.put("email", contato.getEmail());
        values.put("imagem", contato.getImagem());

        if(contato.getId() == null){
            conn.insert("contato", null,values);
        } else {
            conn.update("contato", values,"id = ?", new String [] {contato.getId().toString()});
        }

    }

    public List<Contato> listar(){
        SQLiteDatabase conn = Conexao.getInstance().getReadableDatabase();

        Cursor c = conn.query("contato",new String[] {"id","nome","tipo","telefone", "email", "imagem"},
                null, null, null, null,"nome");

        ArrayList<Contato> contatos = new ArrayList<Contato>();

        if (c.moveToFirst()){
            do{
                Contato contato = new Contato();
                contato.setId(c.getInt(0));
                contato.setNome(c.getString(1));
                contato.setTipo(c.getString(2));
                contato.setNumero(c.getString(3));
                contato.setEmail(c.getString(4));
                contato.setImagem(c.getBlob(5));

                contatos.add(contato);
            } while (c.moveToNext());
        }

        return contatos;
    }

    public void excluir(Contato contato) {

        SQLiteDatabase conn = Conexao.getInstance().getWritableDatabase();

        conn.delete("contato","id = ?", new String [] {contato.getId().toString()});

    }
}
