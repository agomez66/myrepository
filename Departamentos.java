package beans;

import java.util.ArrayList;
import java.util.List;

public class Departamentos {
	String TITULO="DATOS DE LA TABLA DEPART";
	List<Departamento> departaments = new ArrayList<Departamento>();
	public List<Departamento> getDepartaments() {
		return departaments;
	}
	public void setDepartaments(List<Departamento> departaments) {
		this.departaments = departaments;
	}	
}
