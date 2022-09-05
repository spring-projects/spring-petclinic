package org.springframework.samples.petclinic.api;


public class CatFact {
	String fact;
	Integer length;

	public CatFact(String fact, Integer length) {
		this.fact = fact;
		this.length = length;
	}

	public String getFact() {
		return fact;
	}

	public void setFact(String fact) {
		this.fact = fact;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CatFact catFact = (CatFact) o;

		if (!fact.equals(catFact.fact)) return false;
		return length.equals(catFact.length);
	}

	@Override
	public int hashCode() {
		int result = fact.hashCode();
		result = 31 * result + length.hashCode();
		return result;
	}


}
