package finanzdatenbank;

import java.util.Vector;

public class BibEntry {

	private int id;
	private String title;
	private Integer[] months;
	private int year;
	private Integer other;

	public BibEntry(Integer id, int year, String title, Integer[] months, Integer other) {
		this.id = id;
		this.year = year;
		this.title = title;
		this.months = months;
		this.other = other;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMonth(int month, Integer value) {
		this.months[month - 1] = value;
	}

	public void setMonths(Integer[] months) {
		this.months = months;
	}

	public void setOther(Integer other) {
		this.other = other;
	}

	public String getTitle() {
		return this.title;
	}

	public int getYear() {
		return this.year;
	}

	public int getID() {
		return this.id;
	}

	public Integer getOther() {
		return this.other;
	}

	public Integer getMonth(int month) {
		return this.months[month - 1];
	}

	public Integer[] getMonths() {
		return this.months;
	}

	public Vector getVector() {
		Vector v = new Vector();
		v.add(this.title);
		for(int i = 0; i < 12; i++) {
			Integer val = this.months[i];
			v.add(val);
		}
		v.add(this.other);
		v.add(null);
		v.add(this.id);
		return v;
	}
}
