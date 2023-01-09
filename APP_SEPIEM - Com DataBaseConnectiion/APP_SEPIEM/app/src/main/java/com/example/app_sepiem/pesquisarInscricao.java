package com.example.app_sepiem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.service.autofill.Dataset;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.common.net.InetAddresses;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;


public class pesquisarInscricao extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;

    DatabaseReference ref;
    private AutoCompleteTextView pesquisar;
    private RecyclerView listaInscritos;
    private Button btnImprimirComprovativo;

    TextView nome,apelido1, apelido2,BI, naturalidade, residencia, nascimento, provincia,escola, curso;

    String[] arrayInformacao = new String[]{"Nome Completo","Bilhete","Naturalidade", "Residência", "Nascimento", "Província", "Escola", "Curso"};
    public static String[] arryDadosCandidatos;

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


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_inscricao);
//        getSupportActionBar().setTitle("Pesquisar...");
        checkConnection();
        nome = findViewById(R.id.lblNome);
        apelido1 = findViewById(R.id.lblApelido1);
        apelido2 = findViewById(R.id.lblApelido2);
        BI = findViewById(R.id.lblBI);
        naturalidade = findViewById(R.id.lblNaturalidade);
        residencia = findViewById(R.id.lblResidencia);
        nascimento = findViewById(R.id.lblNascimento);
        provincia = findViewById(R.id.lblProvincia);
        escola = findViewById(R.id.lblEscola);
        curso = findViewById(R.id.lblCurso);

        btnImprimirComprovativo = findViewById(R.id.btnImprimirComprovativo);
        btnImprimirComprovativo.setVisibility(View.INVISIBLE);


    /*    menu = findViewById(R.id.txtMenu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(pesquisarInscricao.this, Cadastrar.class));

                showDialog();
            }
        });*/

        ref = FirebaseDatabase.getInstance().getReference("Inscritos");
        pesquisar = (AutoCompleteTextView) findViewById(R.id.txtPesquisar);
        listaInscritos = (RecyclerView) findViewById(R.id.tb_Inscrito);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listaInscritos.setLayoutManager(layoutManager);



        preencherPesquisa();

        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED
        );
        createPdf();

    }

    public void createPdf() {
        btnImprimirComprovativo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PdfDocument myPdfDocument = new PdfDocument();
                Paint myPaint = new Paint();

                PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(250, 400, 1).create();
                PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);

                Canvas canvas = myPage1.getCanvas();

                myPaint.setTextAlign(Paint.Align.CENTER);
                myPaint.setTextSize(12.0f);
                myPaint.setColor(Color.BLUE);
                canvas.drawText("SEPIEM - Inscrições Online", myPageInfo1.getPageWidth()/2, 30, myPaint);
                myPaint.setTextSize(8.0f);
                //myPaint.setColor(Color.rgb(122, 119, 119));
                myPaint.setColor(Color.BLUE);
                canvas.drawText("Comprovativo de Inscrição", myPageInfo1.getPageWidth()/2, 40, myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(10.0f);
                myPaint.setColor(Color.rgb(122, 119, 119));

                canvas.drawText("Informações do Candidato:", 10, 70, myPaint);



                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(9.0f);
                myPaint.setColor(Color.BLACK);


                int startXPosition = 10;
                int endXPosition = myPageInfo1.getPageWidth()-10;
                int startYPosition = 100;



                for (int i = 0; i<8 ;i++){
                    canvas.drawText(arrayInformacao[i], startXPosition, startYPosition, myPaint);
                    canvas.drawText(arryDadosCandidatos[i], 100, startYPosition, myPaint);
                    canvas.drawLine(startXPosition, startYPosition, endXPosition, startYPosition, myPaint);

                    startYPosition += 20;
                }
            /*
                canvas.drawLine(100, 92, 80, 190, myPaint);
                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(2);



                canvas.drawRect(10, 200, myPageInfo1.getPageWidth()-10, 300, myPaint);
                canvas.drawLine(85, 200, 85, 300, myPaint);
                canvas.drawLine(163, 200, 163, 300, myPaint);
                myPaint.setStrokeWidth(0);
                myPaint.setStyle(Paint.Style.FILL);

                canvas.drawText("Foto", 35, 250, myPaint);
                canvas.drawText("Foto", 110, 250, myPaint);
                canvas.drawText("Foto", 190, 250, myPaint);

                 */

                canvas.drawText("Nota: ", 10, 320, myPaint);
                canvas.drawLine(35, 325, myPageInfo1.getPageWidth()-10, 325, myPaint);
                canvas.drawLine(10, 345, myPageInfo1.getPageWidth()-10, 345, myPaint);
                canvas.drawLine(10, 365, myPageInfo1.getPageWidth()-10, 365, myPaint);






                myPdfDocument.finishPage(myPage1);

                File file = new File(Environment.getExternalStorageDirectory(), "Download/SEPIEM_Comprovativo.pdf");

                try {
                    myPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myPdfDocument.close();
                Toast.makeText(pesquisarInscricao.this, "Salvo em Transferências", Toast.LENGTH_LONG).show();

                startActivity(new Intent(pesquisarInscricao.this, pesquisarInscricao.class));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_escolas:
                startActivity(new Intent(pesquisarInscricao.this, activityEscola.class));
                return true;
            case R.id.item_formInscricao:
                startActivity(new Intent(pesquisarInscricao.this, Cadastrar.class));
                return true;
            case R.id.item_SobreNos:

                startActivity(new Intent(pesquisarInscricao.this, perfilEscola.class));
                return true;

                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(pesquisarInscricao.this);
                builder.setTitle("Aviso");
                builder.setMessage("Confirmas ter inserido corretamente as informações\n" +
                        " e Carregado igualmente o ficheiro contendo o BI e a declaração do I Ciclo?");
                builder.setIcon(R.drawable.ic_baseline_warning_24);


                builder.setCancelable(false);

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.create().show();


                Toast.makeText(this, "Sobre Nós", Toast.LENGTH_SHORT).show();
                */

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        LinearLayout escolasLayout = dialog.findViewById(R.id.layoutEscolas);
        LinearLayout inscricaoLayout = dialog.findViewById(R.id.layoutInscricao);
        LinearLayout infoLayout = dialog.findViewById(R.id.layoutInfo);

        escolasLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(pesquisarInscricao.this, "Clicou Escolas", Toast.LENGTH_SHORT).show();
            }
        });
        inscricaoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(pesquisarInscricao.this, Cadastrar.class));
            }
        });
        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(pesquisarInscricao.this, "Clicou Info", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }



    private void preencherPesquisa() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    ArrayList<String> BI = new ArrayList<>();

                    for (DataSnapshot ds:snapshot.getChildren()){
                        String n = ds.child("bilhete").getValue(String.class);
                        BI.add(n);

                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, BI);
                    pesquisar.setAdapter(adapter);
                    pesquisar.setThreshold(9);
                    pesquisar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {

                            String selecionar = parent.getItemAtPosition(postion).toString();

                            getCandidatos(selecionar);

                            btnImprimirComprovativo.setVisibility(View.VISIBLE);

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ref.addListenerForSingleValueEvent(eventListener);
    }

    private void getCandidatos(String selecionar) {
        Query query = ref.orderByChild("bilhete").equalTo(selecionar);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ArrayList<candidatosInfo> candidatosInfos = new ArrayList<>();

                    for(DataSnapshot ds:snapshot.getChildren()){
                        candidatosInfo candidatosInfo = new candidatosInfo(ds.child("nomeProprio").getValue(String.class),
                                ds.child("apelido1").getValue(String.class), ds.child("apelido2").getValue(String.class),
                                ds.child("bilhete").getValue(String.class), ds.child("naturalidade").getValue(String.class),
                        ds.child("residencia").getValue(String.class),ds.child("nascimento").getValue(String.class),
                                ds.child("provincia").getValue(String.class), ds.child("escola").getValue(String.class),
                                ds.child("curso").getValue(String.class));
                        candidatosInfos.add(candidatosInfo);


                    }
                    MyAdapter adapter = new MyAdapter(candidatosInfos, pesquisarInscricao.this);
                    listaInscritos.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        query.addListenerForSingleValueEvent(eventListener);
    }

    class candidatosInfo{

        public String nome;
        public String apelido1;
        public String apelido2;
        public String BI;
        public String naturalidade;
        public String residencia;
        public String nascimento;
        public String provincia;
        public String escola;
        public String curso;

        public String getApelido1() {
            return apelido1;
        }

        public String getNaturalidade() {
            return naturalidade;
        }

        public String getResidencia() {
            return residencia;
        }

        public String getNascimento() {
            return nascimento;
        }

        public String getProvincia() {
            return provincia;
        }

        public String getCurso() {
            return curso;
        }


        public String getBI() {
            return BI;
        }

        public String getNome() {
            return nome;
        }

        public String getApelido2() {
            return apelido2;
        }

        public String getEscola() {
            return escola;
        }

        public candidatosInfo(String nome, String apelido1, String apelido2, String BI, String naturalidade, String residencia, String nascimento, String provincia, String escola, String curso) {
            this.nome = nome;
            this.apelido1 = apelido1;
            this.apelido2 = apelido2;
            this.BI = BI;
            this.naturalidade = naturalidade;
            this.residencia = residencia;
            this.nascimento = nascimento;
            this.provincia = provincia;
            this.escola = escola;
            this.curso = curso;
        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<candidatosInfo> mDataSet;


        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
          TextView nome, apelido1, apelido2, BI,naturalidade, residencia, nascimento, provincia, escola,curso;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                nome = (TextView) view.findViewById(R.id.lblNome);
                apelido1 = (TextView) view.findViewById(R.id.lblApelido1);
                apelido2 = (TextView) view.findViewById(R.id.lblApelido2);
                BI = (TextView) view.findViewById(R.id.lblBI);
                naturalidade = (TextView) view.findViewById(R.id.lblNaturalidade);
                residencia = (TextView) view.findViewById(R.id.lblResidencia);
                nascimento = (TextView) view.findViewById(R.id.lblNascimento);
                provincia = (TextView) view.findViewById(R.id.lblProvincia);
                escola = (TextView) view.findViewById(R.id.lblEscola);
                curso = (TextView) view.findViewById(R.id.lblCurso);
            }


        }


        public MyAdapter(ArrayList<candidatosInfo> myDataSet, Context mContext) {
            this.mDataSet = myDataSet;

        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_layout, parent, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
                candidatosInfo thiscandidatoInfo = mDataSet.get(position);

                viewHolder.nome.setText(thiscandidatoInfo.getNome());
                viewHolder.apelido1.setText(thiscandidatoInfo.getApelido1());
                viewHolder.apelido2.setText(thiscandidatoInfo.getApelido2());
                viewHolder.BI.setText(thiscandidatoInfo.getBI());
                viewHolder.naturalidade.setText(thiscandidatoInfo.getNaturalidade());
                viewHolder.residencia.setText(thiscandidatoInfo.getResidencia());
                viewHolder.nascimento.setText(thiscandidatoInfo.getNascimento());
                viewHolder.provincia.setText(thiscandidatoInfo.getProvincia());
                viewHolder.escola.setText(thiscandidatoInfo.getEscola());
                viewHolder.curso.setText(thiscandidatoInfo.getCurso());



            arryDadosCandidatos = new String[]{""+thiscandidatoInfo.getNome()+" "+thiscandidatoInfo.getApelido1()+" "+thiscandidatoInfo.getApelido2(),""+thiscandidatoInfo.getBI(),
                    ""+thiscandidatoInfo.getNaturalidade(),""+thiscandidatoInfo.getResidencia(),
                    ""+thiscandidatoInfo.getNascimento(),""+thiscandidatoInfo.getProvincia(),
                    ""+thiscandidatoInfo.getEscola(),""+thiscandidatoInfo.getCurso()
            };


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }


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
              //  Toast.makeText(this, "Wifi Habilitado !!!", Toast.LENGTH_LONG).show();
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