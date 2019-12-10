package ptu;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.exist.util.UTF8;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

public class Mainptu {
	static String driver = "org.exist.xmldb.DatabaseImpl";
	static String usu="admin",server="localhost",namecollection,
			usuPwd="admin",
			URI="xmldb:exist://"+server+":8080/exist/xmlrpc/db/";
	
	public static void main(String[] args) {
		int option = 0;
		System.out.println("Cogemos valores por defecto, (Y)es or (N)o?");
		if(!returnValue().equalsIgnoreCase("y")) {
			System.out.println("Dame el nombre de la coleccion:");
			namecollection=returnValue();
			System.out.println("Dame la ip/nombre del server:");
			server=returnValue();
			System.out.println("Dame el nombre de usuario");
			usu=returnValue();
			System.out.println("Dame la contraseña del usuario");
			usuPwd=returnValue();
			URI+=namecollection;
		}
		while (option!=6){
			
			System.out.println("----------Pt1--------");
			System.out.println("1. Subir ficheros");
			System.out.println("2. null");
			System.out.println("3. null");
			System.out.println("4. null");
			System.out.println("5. null");
			System.out.println("6. Exit");
			
			option = Integer.parseInt(returnValue());

			switch (option){

			case 1:
				try {
					subirFichero();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XMLDBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case 2:
				//insertaDep();	
				break;

			case 3:
				//deleteDep();
				break;
				
			case 4:
				//modifyDep();
				break;
			case 5:
				//insertObject();
				break;
			default:
				System.out.println("Opció "+option+" no vàlida");
				break;
			}
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
	private static Collection conectarBBDD() throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
		Class cl = Class.forName(driver); //Cargar del driver
		Database database = (Database) cl.newInstance(); //Instancia de la BD
		DatabaseManager.registerDatabase(database); //Registro del driver
		Collection col = null; // Colección
		col = DatabaseManager.getCollection(URI, usu, usuPwd);
		if (col==null) {
			col=DatabaseManager.getCollection("xmldb:exist://"+server+":8080/exist/xmlrpc/db/",usu,usuPwd);
			CollectionManagementService mgt = (CollectionManagementService) col.getService("CollectionManagementService", "1.0");
			Collection newcol=null;
			newcol=mgt.createCollection(new String(UTF8.encode(namecollection)));
			return newcol;
		}
		return col;
	}
	private static void subirFichero() throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
		System.out.println("Dame un nombre de fichero:");
		File fichero = new File(returnValue());
		Collection col=null;
		col=conectarBBDD();
		XMLResource document = (XMLResource) col.createResource(fichero.getName(),"XMLResource");
		document.setContent(fichero);
		col.storeResource(document);
		col.close();
	}

}
