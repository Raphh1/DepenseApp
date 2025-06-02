package com.example.demo1.model;

public class    Expense {
    private String periode;
    private double total;
    private double logement;
    private double nourriture;
    private double sorties;
    private double transport; // voiture/transport
    private double voyage;
    private double impots;
    private double autres;

    public Expense(String periode, double total, double logement, double nourriture,
                   double sorties, double transport, double voyage, double impots, double autres) {
        this.periode = periode;
        this.total = total;
        this.logement = logement;
        this.nourriture = nourriture;
        this.sorties = sorties;
        this.transport = transport;
        this.voyage = voyage;
        this.impots = impots;
        this.autres = autres;
    }

    // Getters
    public String getPeriode() { return periode; }
    public double getTotal() { return total; }
    public double getLogement() { return logement; }
    public double getNourriture() { return nourriture; }
    public double getSorties() { return sorties; }
    public double getTransport() { return transport; }
    public double getVoyage() { return voyage; }
    public double getImpots() { return impots; }
    public double getAutres() { return autres; }

    // Setters
    public void setPeriode(String periode) { this.periode = periode; }
    public void setTotal(double total) { this.total = total; }
    public void setLogement(double logement) { this.logement = logement; }
    public void setNourriture(double nourriture) { this.nourriture = nourriture; }
    public void setSorties(double sorties) { this.sorties = sorties; }
    public void setTransport(double transport) { this.transport = transport; }
    public void setVoyage(double voyage) { this.voyage = voyage; }
    public void setImpots(double impots) { this.impots = impots; }
    public void setAutres(double autres) { this.autres = autres; }
}
