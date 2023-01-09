package com.example.app_sepiem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Cadastrar extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    EditText mDateFormat;
    DatePickerDialog.OnDateSetListener onDateSetListener;


    EditText nomeProprio;
    EditText apelido1 ;
    EditText apelido2 ;
    EditText bilhete ;
    EditText naturalidade ;
    EditText provincia;
    EditText residencia ;
    EditText nascimento ;
    EditText escola ;
    EditText curso ;
    TextView lblDocumentos ;
    StorageReference storageReference;
    DatabaseReference databaseReference, databaseReferenceCursos;

    List<String> nomes, escolas;

    Button  btnFinalizarInscricao, btnCarregarDocumentos;

    String[] itemsProvincias = {"Cuanza Norte","Luanda","Uige"};



    AutoCompleteTextView AutoCompleteProvincia, AutoCompleteEscola,AutoCompleteCurso;
    ArrayAdapter<String>  adapterItemsProvincia;


    @Override
    public void onBackPressed() {


        if(backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        }else {
            backToast = Toast.makeText(getBaseContext(),"Pressione 'Back' novamente para sair !!!" +
                    "", Toast.LENGTH_LONG);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Formulário de Inscrição");
        checkConnection();



        preencherEscola();





        AutoCompleteProvincia = findViewById(R.id.txtAutoCompleteProvincia);



        adapterItemsProvincia = new ArrayAdapter<String>(this, R.layout.combo_item_provincia, itemsProvincias);

        AutoCompleteProvincia.setAdapter(adapterItemsProvincia);
        AutoCompleteProvincia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), ""+item, Toast.LENGTH_SHORT).show();
            }
        });

        AutoCompleteEscola.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {


                String selecionar = parent.getItemAtPosition(position).toString();

                preencherCursosEscola(selecionar);

                Toast.makeText(Cadastrar.this, ""+selecionar, Toast.LENGTH_LONG).show();




            }
        });



        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mDateFormat = findViewById(R.id.dateFormat);

        mDateFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Cadastrar.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                mDateFormat.setText(date);

            }
        };

        nomeProprio = findViewById(R.id.txtNomeProprio);
        apelido1 = findViewById(R.id.txtApelido1);
        apelido2 = findViewById(R.id.txtApelido2);
        bilhete = findViewById(R.id.txtBI);
        naturalidade = findViewById(R.id.txtNaturalidade);
        provincia = findViewById(R.id.txtAutoCompleteProvincia);
        residencia = findViewById(R.id.txtResidencia);
        nascimento = findViewById(R.id.dateFormat);
        escola = findViewById(R.id.txtAutoCompleteEscola);
        curso = findViewById(R.id.txtAutoCompleteCurso);
        lblDocumentos = findViewById(R.id.lblDocumentos);
         btnFinalizarInscricao = findViewById(R.id.btnInscrever);
        btnCarregarDocumentos = findViewById(R.id.btnCarregarDocumentos);

        btnCarregarDocumentos.setVisibility(View.INVISIBLE);
        btnFinalizarInscricao.setVisibility(View.INVISIBLE);
        lblDocumentos.setVisibility(View.INVISIBLE);






        nomeProprio.addTextChangedListener(validar);
        apelido1.addTextChangedListener(validar);
        apelido2.addTextChangedListener(validar);
        bilhete.addTextChangedListener(validar);
        naturalidade.addTextChangedListener(validar);
        provincia.addTextChangedListener(validar);
        residencia.addTextChangedListener(validar);
        nascimento.addTextChangedListener(validar);
        escola.addTextChangedListener(validar);
        curso.addTextChangedListener(validar);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Inscritos");
        databaseReferenceCursos = FirebaseDatabase.getInstance().getReference("Cursos");


        btnCarregarDocumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPdf();

                btnFinalizarInscricao.setVisibility(View.VISIBLE);


            }
        });

    }

    private void preencherEscola() {

        AutoCompleteEscola = findViewById(R.id.txtAutoCompleteEscola);
        nomes = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Escolas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds: snapshot.getChildren()) {
                String nomeEscolas = ds.child("designacao").getValue(String.class);
                nomes.add(nomeEscolas);
            }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Cadastrar.this, R.layout.combo_item_escola, nomes);
                AutoCompleteEscola.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



    private void preencherCursosEscola(String selecionar) {

        AutoCompleteCurso = findViewById(R.id.txtAutoCompleteCurso);
        escolas = new ArrayList<>();
        databaseReferenceCursos = FirebaseDatabase.getInstance().getReference();
        databaseReferenceCursos.child("Cursos").orderByChild("escola").equalTo(""+selecionar+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                escolas.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String nomeCursos = ds.child("designacao").getValue(String.class);

                    escolas.add(nomeCursos);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Cadastrar.this, R.layout.combo_item_escola, escolas);
                AutoCompleteCurso.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==12 && resultCode==RESULT_OK && data != null && data.getData() != null ){
           btnFinalizarInscricao.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   AlertDialog.Builder builder = new AlertDialog.Builder(Cadastrar.this);
                   builder.setTitle("Aviso");
                   builder.setMessage("Confirmas ter inserido corretamente as informações\n" +
                           " e Carregado igualmente o ficheiro contendo o BI e a declaração do I Ciclo?");

                   builder.setCancelable(false);

                   builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                           uploadPdfFileFirebase(data.getData());
                           //finish();
                       }
                   });

                   builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           dialogInterface.dismiss();
                       }
                   });


                   builder.create().show();



               }
           });
        }
    }


    private void limparCampos() {
        nomeProprio.setText("");
        apelido1.setText("");
        apelido2.setText("");
        bilhete.setText("");
        naturalidade.setText("");
        provincia.setText("");
        residencia.setText("");
        nascimento.setText("");
        escola.setText("");
        curso.setText("");

        btnCarregarDocumentos.setVisibility(View.INVISIBLE);
        btnFinalizarInscricao.setVisibility(View.INVISIBLE);
        lblDocumentos.setVisibility(View.INVISIBLE);
    }

    private void uploadPdfFileFirebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Carregando Informações...");
        progressDialog.show();
        String nomeFicheiro = "DOC_"+nomeProprio.getText().toString()+"_"+apelido1.getText().toString()+"_"+apelido2.getText().toString()+"_"+bilhete.getText().toString();

        StorageReference reference = storageReference.child("Inscritos/"+nomeFicheiro+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri url = uriTask.getResult();


                        Inscrito inscrito = new Inscrito(nomeProprio.getText().toString(),apelido1.getText().toString(),apelido2.getText().toString(),
                                bilhete.getText().toString(),naturalidade.getText().toString(), provincia.getText().toString(),
                                residencia.getText().toString(),nascimento.getText().toString(),escola.getText().toString(),curso.getText().toString(),
                                url.toString()
                        );


                        databaseReference.child(databaseReference.push().getKey()).setValue(inscrito);
                        Toast.makeText(Cadastrar.this, "Inscrição feita com Sucesso...", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                        limparCampos();
                        startActivity(new Intent(Cadastrar.this, pesquisarInscricao.class));

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Em progresso... "+(int) progress + "%");
                    }
                });

    }

    private TextWatcher validar = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String nomeInput = nomeProprio.getText().toString().trim();
            String apelido1Input = apelido1.getText().toString().trim();
            String apelido2Input = apelido2.getText().toString().trim();
            String bilheteInput = bilhete.getText().toString().trim();
            String naturalidadeInput = naturalidade.getText().toString().trim();
            String provinciaInput = provincia.getText().toString().trim();
            String residenciaInput = residencia.getText().toString().trim();
            String nascimentoInput = nascimento.getText().toString().trim();
            String escolaInput = escola.getText().toString().trim();
            String cursoInput = curso.getText().toString().trim();

            if (!nomeInput.isEmpty() && !apelido2Input.isEmpty()
                    &&!bilheteInput.isEmpty() && !naturalidadeInput.isEmpty() && !provinciaInput.isEmpty()
                    && !residenciaInput.isEmpty() && !nascimentoInput.isEmpty() && !escolaInput.isEmpty() &&
            !cursoInput.isEmpty()
            ){

                btnCarregarDocumentos.setVisibility(View.VISIBLE);
                lblDocumentos.setVisibility(View.VISIBLE);

            }else{
                btnCarregarDocumentos.setVisibility(View.INVISIBLE);
                btnFinalizarInscricao.setVisibility(View.INVISIBLE);
                lblDocumentos.setVisibility(View.INVISIBLE);
            }







        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private void showDialogInterConnection() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.internetconnection);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void checkConnection(){
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null != activeNetwork){

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
               // Toast.makeText(this, "Wifi Habilitado !!!", Toast.LENGTH_LONG).show();
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
               // Toast.makeText(this, "Dados Móveis Habiltados !!!", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            showDialogInterConnection();
            //Toast.makeText(this, "Sem Ligação à Internet !!!", Toast.LENGTH_LONG).show();
        }
    }

}

