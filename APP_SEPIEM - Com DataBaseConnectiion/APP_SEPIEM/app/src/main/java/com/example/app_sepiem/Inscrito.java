package com.example.app_sepiem;

public class Inscrito {

    private String nomeProprio, apelido1, apelido2, bilhete, naturalidade, provincia, residencia, nascimento, escola, curso, urlDocumentos;


    public Inscrito(){}

    public Inscrito(String nomeProprio, String apelido1, String apelido2, String bilhete, String naturalidade, String provincia, String residencia, String nascimento, String escola, String curso,String urlDocumentos) {
        this.nomeProprio = nomeProprio;
        this.apelido1 = apelido1;
        this.apelido2 = apelido2;
        this.bilhete = bilhete;
        this.naturalidade = naturalidade;
        this.provincia = provincia;
        this.residencia = residencia;
        this.nascimento = nascimento;
        this.escola = escola;
        this.curso = curso;
        this.urlDocumentos = urlDocumentos;

    }




    public String getNomeProprio() {
        return nomeProprio;
    }

    public void setNomeProprio(String nomeProprio) {
        this.nomeProprio = nomeProprio;
    }

    public String getApelido1() {
        return apelido1;
    }

    public void setApelido1(String apelido1) {
        this.apelido1 = apelido1;
    }

    public String getApelido2() {
        return apelido2;
    }

    public void setApelido2(String apelido2) {
        this.apelido2 = apelido2;
    }

    public String getBilhete() {
        return bilhete;
    }

    public void setBilhete(String bilhete) {
        this.bilhete = bilhete;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getResidencia() {
        return residencia;
    }

    public void setResidencia(String residencia) {
        this.residencia = residencia;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getEscola() {
        return escola;
    }

    public void setEscola(String escola) {
        this.escola = escola;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getUrlDocumentos() {
        return urlDocumentos;
    }

    public void setUrlDocumentos(String urlDocumentos) {
        this.urlDocumentos = urlDocumentos;
    }


}
