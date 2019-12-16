package beans;

import java.util.List;

public class Empleados {
	String Titulo="DATOS DE LA TABLA EMPLE";
	List <Empleado> empleados;
	public List<Empleado> getEmpleados() {
		return empleados;
	}
	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}
}
