package de.szut.simon.BlumentalerAue.data;

import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class PlantProtectant {
	private SimpleStringProperty no;
	private SimpleStringProperty name;

	public PlantProtectant(String no, String name) {
		this.no = new SimpleStringProperty(no);
		this.name = new SimpleStringProperty(name);
	}

	public String getNo() {
		return no.get();
	}

	public void setNo(String no) {
		this.no.set(no);
	}

	public SimpleStringProperty noProperty() {
		return no;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public SimpleStringProperty nameProperty() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PlantProtectant that = (PlantProtectant) o;
		return no.equals(that.no) &&
			name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(no, name);
	}

	@Override
	public String toString() {
		return name.getValue();
	}
}
