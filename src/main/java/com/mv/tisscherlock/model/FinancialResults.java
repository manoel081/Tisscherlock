package com.mv.tisscherlock.model;

public class FinancialResults {

    private double totalLiberado;
    private double totalGlosa;
    private double discrepancia;

    public FinancialResults(double totalLiberado, double totalGlosa, double discrepancia) {
        this.totalLiberado = totalLiberado;
        this.totalGlosa = totalGlosa;
        this.discrepancia = discrepancia;
    }

    // Getters and Setters
    public double getTotalLiberado() {
        return totalLiberado;
    }

    public void setTotalLiberado(double totalLiberado) {
        this.totalLiberado = totalLiberado;
    }

    public double getTotalGlosa() {
        return totalGlosa;
    }

    public void setTotalGlosa(double totalGlosa) {
        this.totalGlosa = totalGlosa;
    }

    public double getDiscrepancia() {
        return discrepancia;
    }

    public void setDiscrepancia(double discrepancia) {
        this.discrepancia = discrepancia;
    }
}
