package beans;

import com.google.gson.annotations.SerializedName;

public class Departamento {
	@SerializedName("DEPT_NO")
	private String num;
	private String dnombre,loc;
	public Departamento() {
	}
	public Departamento(String num, String dnombre, String loc) {
		this.num = num;
		this.dnombre = dnombre;
		this.loc = loc;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getDnombre() {
		return dnombre;
	}
	public void setDnombre(String dnombre) {
		this.dnombre = dnombre;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	@Override
	public String toString() {
		return "Departamento [num=" + num + ", dnombre=" + dnombre + ", loc=" + loc + "]";
	}
	
}
