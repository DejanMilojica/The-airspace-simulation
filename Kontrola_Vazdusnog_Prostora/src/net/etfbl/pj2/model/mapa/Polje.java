package net.etfbl.pj2.model.mapa;

public class Polje {
	private Element element;
	
	public Polje() {
		super();
	}
	
	public Polje(Element element) {
		this.element = element;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}
	
	@Override
	public String toString() {
		String string = " __ ";
		return string;
	}
}
