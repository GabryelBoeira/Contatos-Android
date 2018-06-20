package up.edu.br.contatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper{

    private static Conexao conexao;

    public static Conexao getInstance(){
        return conexao;
    }


    public Conexao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        conexao = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String statement  = " create table contato (" +
                " id integer primary key autoincrement," +
                " nome varchar (255)," +
                " tipo varchar (50)," +
                " telefone varchar (20), " +
                " email varchar (255) " +
                ")";

        db.execSQL(statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion == 1){
//            if (newVersion > oldVersion){
                db.execSQL(" alter table contato add email varchar(255)");
//            }
        }
    }
}
