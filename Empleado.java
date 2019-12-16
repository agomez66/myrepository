package beans;

public class Empleado {
	String EMP_NO, APELLIDO,OFICIO,DIR,FECHA_ALT,SALARIO,DEPT_NO;

	public Empleado(String eMP_NO, String aPELLIDO, String oFICIO, String dIR, String fECHA_ALT, String sALARIO,
			String dEPT_NO) {
		EMP_NO = eMP_NO;
		APELLIDO = aPELLIDO;
		OFICIO = oFICIO;
		DIR = dIR;
		FECHA_ALT = fECHA_ALT;
		SALARIO = sALARIO;
		DEPT_NO = dEPT_NO;
	}
	public Empleado() {
		
	}

	public String getEMP_NO() {
		return EMP_NO;
	}

	public void setEMP_NO(String eMP_NO) {
		EMP_NO = eMP_NO;
	}

	public String getAPELLIDO() {
		return APELLIDO;
	}

	public void setAPELLIDO(String aPELLIDO) {
		APELLIDO = aPELLIDO;
	}

	public String getOFICIO() {
		return OFICIO;
	}

	public void setOFICIO(String oFICIO) {
		OFICIO = oFICIO;
	}

	public String getDIR() {
		return DIR;
	}

	public void setDIR(String dIR) {
		DIR = dIR;
	}

	public String getFECHA_ALT() {
		return FECHA_ALT;
	}

	public void setFECHA_ALT(String fECHA_ALT) {
		FECHA_ALT = fECHA_ALT;
	}

	public String getSALARIO() {
		return SALARIO;
	}

	public void setSALARIO(String sALARIO) {
		SALARIO = sALARIO;
	}

	public String getDEPT_NO() {
		return DEPT_NO;
	}

	public void setDEPT_NO(String dEPT_NO) {
		DEPT_NO = dEPT_NO;
	}

	@Override
	public String toString() {
		return "Empleado [EMP_NO=" + EMP_NO + ", APELLIDO=" + APELLIDO + ", OFICIO=" + OFICIO + ", DIR=" + DIR
				+ ", FECHA_ALT=" + FECHA_ALT + ", SALARIO=" + SALARIO + ", DEPT_NO=" + DEPT_NO + "]";
	}
	 
}
