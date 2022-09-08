package br.unigran.agenda;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.unigran.bancoDados.ContatoDB;
import br.unigran.bancoDados.DBHelper;

public class MainActivity extends AppCompatActivity {
    EditText nome, telefone;
    List<Contato> dados;
    ListView listagem;
    DBHelper db;
    ContatoDB contatoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //criação de conexão com o banco de dados
        db = new DBHelper(this);

        //mapeamento dos campos da interface
        nome=findViewById(R.id.nomeId);
        telefone=findViewById(R.id.telefoneId);
        listagem=findViewById(R.id.listaId);

        //alocamento de lista
        dados= new ArrayList();

        //vincula adapter
        ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,dados);
        listagem.setAdapter(adapter);

        //listagem inicial
        contatoDB=new ContatoDB(db);
        contatoDB.lista(dados);

        acoes();
    }

    private void acoes() {
        listagem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    dados.get(i).getId();
                    new AlertDialog.Builder(view.getContext())
                    .setMessage("Edicao de contato")
                    .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            Contato contato = new Contato();
                            contato.setNome(nome.getText().toString());
                            contato.setTelefone(telefone.getText().toString());
                            contatoDB.editar(dados.get(i).getId(), contato);
                            contatoDB.lista(dados);
                            contatoDB.atualizar(listagem);
                        }
                    })
                    .setNegativeButton("Fechar", null)
                    .create().show();
            }
        });

        listagem.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Remover contato?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                contatoDB.remover(dados.get(i).getId());
                                contatoDB.lista(dados);
                                contatoDB.atualizar(listagem);
                            }
                        }).setNegativeButton("Cancelar",null)
                        .create().show();
                return false;
            }
        });
    }

    public void salvar(View view){
        Contato contato = new Contato();
        contato.setNome(nome.getText().toString());
        contato.setTelefone(telefone.getText().toString());

        contatoDB.inserir(contato);
        contatoDB.lista(dados);
        contatoDB.atualizar(listagem);

        Toast.makeText(this,"Salvo com sucesso!",Toast.LENGTH_SHORT).show();
    }
}