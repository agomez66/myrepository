package program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.BSONObject;
import org.bson.BasicBSONEncoder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class Main_Mongo {
	static MongoClient mongoClient = new MongoClient();
	static DB db = mongoClient.getDB("paco");
	static DBCollection collection = db.getCollection("Zonas");
	public static void main(String[] args) throws IOException {
		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.OFF);
		int option = 0;
		while (option!=6){
			
			System.out.println("----------Pt2 by mongo--------");
			System.out.println("1. Subir contenido Json a mongo");
			System.out.println("2. Crear zona20");
			System.out.println("6. Exit");
			
			option = Integer.parseInt(returnValue());

			switch (option){

			case 1:
				System.out.println("Dame el fichero de zonas: ");
				subirJson(returnValueJson(new File(returnValue())),crearConexion("Zonas"));
				System.out.println("Dame el fichero de productos: ");
				subirJson(returnValueJson(new File(returnValue())),crearConexion("Productos"));
				break;

			case 2:
				break;

			default:
				System.out.println("Opció "+option+" no vàlida");
				break;
			}
		}
		
	}
	private static DBCollection crearConexion(String nombCollection) {
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("Pt2");
		DBCollection collection = db.getCollection(nombCollection);
		return collection;
	}
	public static String consultaBBDD(String consulta, DBCollection collection){
		BasicDBObject query = new BasicDBObject(consulta+".cod_zona","20");
		BasicDBObject fields = new BasicDBObject(consulta,1);
		DBCursor cursor = collection.find(query,fields.append("_id",0));
		String result="";
		for (DBObject dbObject : cursor)
			result= dbObject.toString();
		return result;
	}
	private static String returnValueJson(File JSON) {
		Scanner s = null;
		String linea="";
		try {
			// Leemos el contenido del fichero
			System.out.println("... Leemos el contenido del fichero ...");
			s = new Scanner(JSON);
			// Leemos linea a linea el fichero
			while (s.hasNextLine()) 
				linea += s.nextLine();
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         try{                    
	            if( null != s ){   
	               s.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	      return linea;
	}
	public static void subirJson(String lecturajson, DBCollection collection) {
		JsonParser parser = new JsonParser();
		JsonElement datos = parser.parse(lecturajson);
		recorrerJson(datos,collection);
	}
	public static void recorrerJson(JsonElement elemento,DBCollection collection) {
	    if (elemento.isJsonObject()) {
	        JsonObject obj = elemento.getAsJsonObject();
	        java.util.Set<java.util.Map.Entry<String,JsonElement>> entradas = obj.entrySet();
	        java.util.Iterator<java.util.Map.Entry<String,JsonElement>> iter = entradas.iterator();
	        while (iter.hasNext()) {
	            java.util.Map.Entry<String,JsonElement> entrada = iter.next();
	            recorrerJson(entrada.getValue(),collection);
	        }
	 
	    } else if (elemento.isJsonArray()) {
	        JsonArray array = elemento.getAsJsonArray();
	        for (int i = 0; i < array.size(); i++) { 
                DBObject object = (DBObject) JSON.parse(array.get(i).toString());
                collection.insert(object);
	        }
	        java.util.Iterator<JsonElement> iter = array.iterator();
	        while (iter.hasNext()) {
	            JsonElement entrada = iter.next();
	            recorrerJson(entrada,collection);
	        }
	    } else {
	        ;
	    }
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
	
	
}
