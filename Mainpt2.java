package program;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import beans.Departamento;
import beans.Departamentos;
import beans.Empleado;
import beans.Empleados;

public class Mainpt2 {
	List<Empleado> empleados = new ArrayList<Empleado>();
	List<Departamento> departamentos = new ArrayList<Departamento>();
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.SEVERE);
		Mainpt2 principal = new Mainpt2();
		int option = 0;
		while (option!=5){
			
			System.out.println("----------Pt2--------");
			System.out.println("1. Crear json a partir de xml");
			System.out.println("2. Ver empleados");
			System.out.println("3. Inserta un departamento");
			System.out.println("4. null");
			System.out.println("5. null");
			System.out.println("6. Exit");
			
			option = Integer.parseInt(returnValue());

			switch (option){

			case 1:
				System.out.println("Dame el fichero de empleados: ");
				principal.convertXMLtoEmpleat(new File(returnValue()));
				System.out.println("Dame el fichero de departamentos: ");
				principal.convertXMLtoDepartamento(new File(returnValue()));
				break;

			case 2:
				principal.verEmple();	
				break;

			case 3:
				if (principal.insertDep())
					System.out.println("todo ok");
				else
					System.out.println("El departamento ya existe");
				break;
				
			case 4:
				
				break;
			case 5:
				
				break;
			default:
				System.out.println("Opció "+option+" no vàlida");
				break;
			}
		}
			
	}

	private boolean insertDep () throws JsonSyntaxException, IOException {
		Departamento dpt = new Departamento();
		System.out.println("Dame un numero de departamento:");
		dpt.setNum(returnValue());
		System.out.println("Dame un nombre de departamento:");
		dpt.setDnombre(returnValue());
		System.out.println("Dame una localidad del departamento");
		dpt.setLoc(returnValue());
		Gson lecturagson = new Gson();
		Departamentos dpts = new Departamentos();
		dpts = lecturagson.fromJson(returnValueJson(new File("departamentos.json")), Departamentos.class);
		for (Departamento departamento : dpts.getDepartaments()) 
			if(departamento.getNum().equalsIgnoreCase(dpt.getNum()))
				return false;
		dpts.getDepartaments().add(dpt);
		departamentos= dpts.getDepartaments();
		for (Departamento departamento : dpts.getDepartaments()) 
			System.out.println(departamento);
		generandoJson(departamentos, "departamentos.json");
		return true;
			
		
	}
	
	private static String returnValue () {
		String s = null;
		
		try{

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		s = in.readLine();

		}catch(IOException e){

		System.out.println("Error al leer");

		e.printStackTrace();

		}

		return s; // convertimos a numérico
		
	}
	private void convertXMLtoEmpleat(File xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
		Document doc = (Document) dbuilder.parse(xml);
		NodeList nlist = (doc).getElementsByTagName("EMP_ROW");
		System.out.println("Numero de empleados: "+nlist.getLength());
		for (int temp = 0; temp < nlist.getLength(); temp++) {
			Node nnode = nlist.item(temp);
			if(nnode.getNodeType() == Node.ELEMENT_NODE) {
				Empleado emp = new Empleado();
				Element eelement = (Element) nnode;
				emp.setEMP_NO(eelement.getElementsByTagName("EMP_NO").item(0).getTextContent());
				emp.setAPELLIDO(eelement.getElementsByTagName("APELLIDO").item(0).getTextContent());
				emp.setOFICIO(eelement.getElementsByTagName("OFICIO").item(0).getTextContent());
				
				if(!emp.getOFICIO().equals("PRESIDENTE")) 
					emp.setDIR(eelement.getElementsByTagName("DIR").item(0).getTextContent());	
				
				emp.setFECHA_ALT(eelement.getElementsByTagName("FECHA_ALT").item(0).getTextContent());
				emp.setSALARIO(eelement.getElementsByTagName("SALARIO").item(0).getTextContent());
				emp.setDEPT_NO(eelement.getElementsByTagName("DEPT_NO").item(0).getTextContent());
				
				empleados.add(emp);
			}
		}
		generandoJson(empleados, "empleados.json");
	}
	private void convertXMLtoDepartamento(File xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
		Document doc = (Document) dbuilder.parse(xml);
		NodeList nlist = (doc).getElementsByTagName("DEP_ROW");
		System.out.println("Numero de departamentos: "+nlist.getLength());
		for (int temp = 0; temp < nlist.getLength(); temp++) {
			Node nnode = nlist.item(temp);
			if(nnode.getNodeType() == Node.ELEMENT_NODE) {
				Departamento dpt = new Departamento();
				Element eelement = (Element) nnode;
				dpt.setNum(eelement.getElementsByTagName("DEPT_NO").item(0).getTextContent());
				dpt.setDnombre(eelement.getElementsByTagName("DNOMBRE").item(0).getTextContent());
				dpt.setLoc(eelement.getElementsByTagName("LOC").item(0).getTextContent());
				departamentos.add(dpt);
			}
		}
		generandoJson(departamentos, "departamentos.json");
	}
	private void generandoJson (List a, String namejson) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String bonico=null;
		if (a.get(0)instanceof Empleado) {
			Empleados obj = new Empleados();
			obj.setEmpleados(a);
			bonico = gson.toJson(obj);
		}else {
			Departamentos obj = new Departamentos();
			obj.setDepartaments(a);
			bonico = gson.toJson(obj);
		}
		FileWriter fichero = null;
		PrintWriter pw = null;
		fichero = new FileWriter(namejson);
		pw = new PrintWriter(fichero);
		pw.println(bonico);
		fichero.close();
	}
	
	private void verEmple() throws JsonSyntaxException, FileNotFoundException {
		File file = new File ("empleados.json");
		Empleados empleats = new Empleados();
		Gson lecturagson = new Gson();
		empleats = lecturagson.fromJson(returnValueJson(file), Empleados.class);
		for (Empleado empleado : empleats.getEmpleados()) {
			System.out.println(empleado);
			
		}
		
	}
	private String returnValueJson(File json) throws FileNotFoundException {
		Scanner s = null;
		String linea = "";
		System.out.println("... leemos el Json ...");
		s= new Scanner(json);
		while (s.hasNextLine()) 
			linea += s.nextLine();
		return linea;
		
	}
}



/*
 * Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Empleado emp = new Empleado("1", "Paco", "Informatico", "", "11/11/2019", "350", "1");
		//System.out.println(gson.toJson(emp));
		List <Empleado> emps = new ArrayList<Empleado>(); 
		emps.add(emp);
		emps.add(emp);
		Empleados emps2 = new Empleados();
		emps2.setEmpleados(emps);
		System.out.println(gson.toJson(emps2));

 * */
