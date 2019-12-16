package program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import beans.Departamento;
import beans.Departamentos;
import beans.Empleado;
import beans.Empleados;

public class Mainpt2mongo {
	MongoClient mongo = new MongoClient();//sino pongo nada por defecto coge localhost
	DB db = mongo.getDB("Abraham");
	public static void main(String[] args) throws FileNotFoundException {
		Mainpt2mongo mainmongo = new Mainpt2mongo();
		int option = 0;
		while (option!=6){
			
			System.out.println("----------Pt2 by mongo--------");
			System.out.println("1. Crear mongo a partir de xml");
			System.out.println("2. Ver empleados");
			System.out.println("3. Inserta un departamento");
			System.out.println("4. Modifcar un departamento");
			System.out.println("5. Borrar un departamento");
			System.out.println("6. Exit");
			
			option = Integer.parseInt(returnValue());

			switch (option){

			case 1:
				System.out.println("Dame el fichero de empleados: ");
				mainmongo.generandoMongo(new File(returnValue()), mainmongo.crearConexion("Empleados"));
				System.out.println("Dame el fichero de departamentos: ");
				mainmongo.generandoMongo(new File(returnValue()), mainmongo.crearConexion("Departamentos"));
				break;

			case 2:
				mainmongo.verEmple(mainmongo.crearConexion("Empleados"));	
				break;

			case 3:
				if (mainmongo.insertaDep(mainmongo.crearConexion("Departamentos")))
					System.out.println("todo ok");
				else
					System.out.println("El departamento ya existe");
				break;
				
			case 4:
				if (mainmongo.modificaDep(mainmongo.crearConexion("Departamentos")))
					System.out.println("todo ok");
				else
					System.out.println("El departamento no se ha modificado");
				break;
			case 5:
				
				break;
			default:
				System.out.println("Opció "+option+" no vàlida");
				break;
			}
		}
			

	}
	
	private boolean modificaDep(DBCollection collection) {
		Departamento dpt = new Departamento();
		System.out.println("Dame un numero de departamento a buscar:");
		dpt.setNum(returnValue());
		BasicDBObject searchquery = new BasicDBObject().append("DEPT_NO", dpt.getNum());
		System.out.println("Dime el nuevo valor que quieres asignar:");
		dpt.setNum(returnValue());
		BasicDBObject newdocument = new BasicDBObject();
		newdocument.append("$set", new BasicDBObject().append("DEPT_NO", dpt.getNum()));
		collection.update(searchquery, newdocument);
		BasicDBObject wherequery = new BasicDBObject();
		wherequery.put("DEPT_NO", dpt.getNum());
		DBCursor cursor = collection.find(wherequery);
		if(!cursor.hasNext()) 
			return true;
		return false;
	}
	
	private boolean borrarDep(DBCollection collection) {
		
		Departamento dpt = new Departamento();
		System.out.println("Dame un numero de departamento borrar:");
		dpt.setNum(returnValue());
		BasicDBObject wherequery = new BasicDBObject();
		wherequery.put("DEPT_NO", dpt.getNum());
		DBCursor cursor = collection.find(wherequery);
		if(cursor.hasNext()) {
			collection.remove(wherequery);
			return true;
		}
		return false;
	}
	
	private boolean insertaDep(DBCollection collection) {
		
		Departamento dpt = new Departamento();
		System.out.println("Dame un numero de departamento:");
		dpt.setNum(returnValue());
		System.out.println("Dame un nombre de departamento:");
		dpt.setDnombre(returnValue());
		System.out.println("Dame una localidad del departamento");
		dpt.setLoc(returnValue());
		Gson lecturagson = new Gson();

		BasicDBObject wherequery = new BasicDBObject();
		wherequery.put("DEPT_NO", dpt.getNum());
		DBCursor cursor = collection.find(wherequery);
		if(!cursor.hasNext()) {
			DBObject dbobject = (DBObject)JSON.parse(lecturagson.toJson(dpt));
			collection.insert(dbobject);
			return true;
		}
		return false;
	}
	
	private void verEmple(DBCollection collection) {
		BasicDBObject wherequery = new BasicDBObject();
		System.out.println("Dime un numero de departamento");
		wherequery.put("DEPT_NO",returnValue());
		DBCursor cursor= collection.find(wherequery);//si lo dejo en blanco salen todos, wherequery=""
		while (cursor.hasNext()) 
			System.out.println(cursor.next());
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
	private DBCollection crearConexion(String nomcollection) {
		
		DBCollection collection = db.getCollection(nomcollection);
		return collection;
		
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
	private void generandoMongo (File file, DBCollection collection) throws FileNotFoundException {
		if (file.getName().equalsIgnoreCase("empleados.json")) {
			Empleados empleats = new Empleados();
			Gson lecturagson = new Gson();
			empleats = lecturagson.fromJson(returnValueJson(file), Empleados.class);
			for (Empleado empleado : empleats.getEmpleados()) {
				DBObject dbobject = (DBObject) JSON.parse(lecturagson.toJson(empleado));
				collection.insert(dbobject);
			}
		}
		else {
			Departamentos departamentos = new Departamentos();
			Gson lecturagson = new Gson();
			departamentos = lecturagson.fromJson(returnValueJson(file), Departamentos.class);
			for (Departamento departamento : departamentos.getDepartaments()) {
				DBObject dbobject = (DBObject) JSON.parse(lecturagson.toJson(departamento));
				collection.insert(dbobject);
			}
		}
		
		
			
	}
}
