package de.szut.simon.BlumentalerAue.data;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.Objects;

public class Umweldatum {
	private SimpleLongProperty index;
	private SimpleDoubleProperty one;
	private SimpleDoubleProperty two;
	private SimpleDoubleProperty three;

	public Umweldatum(long index, double one, double two, double drei) {
		this.index = new SimpleLongProperty(index);
		this.one = new SimpleDoubleProperty(one);
		this.two = new SimpleDoubleProperty(two);
		this.three = new SimpleDoubleProperty(drei);
	}

	public long getIndex() {
		return index.get();
	}

	public void setIndex(long index) {
		this.index.set(index);
	}

	public SimpleLongProperty indexProperty() {
		return index;
	}

	public double getOne() {
		return one.get();
	}

	public void setOne(double one) {
		this.one.set(one);
	}

	public SimpleDoubleProperty oneProperty() {
		return one;
	}

	public double getTwo() {
		return two.get();
	}

	public void setTwo(double two) {
		this.two.set(two);
	}

	public SimpleDoubleProperty twoProperty() {
		return two;
	}

	public double getThree() {
		return three.get();
	}

	public void setThree(double three) {
		this.three.set(three);
	}

	public SimpleDoubleProperty threeProperty() {
		return three;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Umweldatum that = (Umweldatum) o;
		return index.equals(that.index) &&
			one.equals(that.one) &&
			two.equals(that.two) &&
			three.equals(that.three);
	}

	@Override
	public int hashCode() {
		return Objects.hash(index, one, two, three);
	}

	@Override
	public String toString() {
		return "Umweldatum{" +
			"index=" + index +
			", one=" + one +
			", two=" + two +
			", three=" + three +
			'}';
	}
}
