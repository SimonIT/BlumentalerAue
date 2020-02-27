package de.szut.simon.BlumentalerAue.data;

import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class Pflanzenmittel {
    private SimpleStringProperty Nr;
    private SimpleStringProperty Mittel;

    public Pflanzenmittel(String Nr, String Mittel) {
        this.Nr = new SimpleStringProperty(Nr);
        this.Mittel = new SimpleStringProperty(Mittel);
    }

    public String getNr() {
        return Nr.get();
    }

    public void setNr(String nr) {
        this.Nr.set(nr);
    }

    public SimpleStringProperty nrProperty() {
        return Nr;
    }

    public String getMittel() {
        return Mittel.get();
    }

    public void setMittel(String mittel) {
        this.Mittel.set(mittel);
    }

    public SimpleStringProperty mittelProperty() {
        return Mittel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pflanzenmittel that = (Pflanzenmittel) o;
        return Nr.equals(that.Nr) &&
                Mittel.equals(that.Mittel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Nr, Mittel);
    }

    @Override
    public String toString() {
        return Mittel.getValue();
    }
}
