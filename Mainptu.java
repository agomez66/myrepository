package ptu;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.xqj.exist.ExistXQDataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.exist.util.UTF8;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

public class Mainptu {
	static String driver = "org.exist.xmldb.DatabaseImpl";
	static String usu="admin",server="localhost",namecollection,
			usuPwd="admin",
			URI="xmldb:exist://"+server+":8080/exist/xmlrpc/db/";
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XQException, XMLDBException {
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
			System.out.println("2. Ver jugadores por nacionalidad");
			System.out.println("3. Insertar un Equipo");
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
				try {
					verJugadores();
				} catch (XQException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				break;

			case 3:
				insertaEquip();
				break;
				
			case 4:
				insertaJugador();
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
	private static boolean existEquip(String nom) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
		Collection col=conectarBBDD();
		XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
		ResourceSet result = servicio.query("for $eq in /equipos_de_futbol/equipo[nombre='"+nom+"'] "
				+ "return $eq");
		ResourceIterator i;
		i = result.getIterator();
		return !i.hasMoreResources();
	}
	private static void insertaEquip() throws XQException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
		String [] value = new String[3];
		System.out.println("Teclea nombre del equipo:"); // se lee un dato de tipo cadena
		value[0]=returnValue (); 
		while (!existEquip(value[0])) {
			System.out.println("Teclea un equipo que no exista:"); // se lee un dato de tipo cadena
			value[0]=returnValue (); 
		}
		System.out.println("Teclea ciudad del equipo:"); // se lee un dato de tipo cadena
		value[1]=returnValue ();
		System.out.println("Teclea entrenador del equipo:"); // se lee un dato de tipo cadena
		value[2]=returnValue ();
		String cad = "update insert <equipo> <nombre>"+value[0]+"</nombre><ciudad>"+value[1]+"</ciudad>"
				+ "<entrenador>"+value[2]+"</entrenador></equipo> into doc('equipos.xml')//equipos_de_futbol";
		
		Collection col = conectarBBDD();
		XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
		ResourceSet result = servicio.query(cad);
		col.close();	
	}
	private static void insertaJugador() throws XQException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
		String [] value = new String[4];
		System.out.println("Teclea nombre del jugador:"); // se lee un dato de tipo cadena
		value[0]=returnValue (); 
		System.out.println("Teclea posicion del jugador:"); // se lee un dato de tipo cadena
		value[1]=returnValue ();
		System.out.println("Teclea nacionalidad del jugador:"); // se lee un dato de tipo cadena
		value[2]=returnValue ();
		System.out.println("Teclea equipo del jugador:"); // se lee un dato de tipo cadena
		value[3]=returnValue ();
		String cad = "update insert <jugador> <posicion>"+value[1]+"</posicion><nombre>"+value[0]+"</nombre>"
				+ "<nacionalidad>"+value[2]+"</nacionalidad>"
				+ "<equipo>"+value[3]+"</equipo></jugador> into doc('jugadores.xml')//jugadores";
		
		Collection col = conectarBBDD();
		XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
		ResourceSet result = servicio.query(cad);
		col.close();	
	}
	private static void verJugadores () throws XQException {
		System.out.println("Teclea Nacionalidad");
		String nac = returnValue();
		org.w3c.dom.Element departament;
		XQDataSource serverxqj = new ExistXQDataSource();
		serverxqj.setProperty("serverName", server);	
		XQPreparedExpression consulta;
		XQResultSequence resultado;
		XQConnection conn = serverxqj.getConnection();
		String cad = "for $jug in /jugadores/jugador[nacionalidad='"+nac+"'] return $jug";
		consulta =conn.prepareExpression(cad);
		resultado = consulta.executeQuery();
		resultado.next(); 
		do {
			departament = (org.w3c.dom.Element)resultado.getObject();
			NodeList e = departament.getChildNodes();
			for (int i = 0; i < e.getLength(); i++) {              
				  Node n= e.item(i);                     
				  if (!n.getNodeName().equalsIgnoreCase("#text")) {
					  System.out.println(n.getNodeName() +" "+ n.getTextContent());
					  if(n.getNodeName().equalsIgnoreCase("equipo"))
						  System.out.println(new String(new char[20]).replace("\0", "="));
				  }
			}
		}
		while(resultado.next());
	}
	
	
	
	private static Collection conectarBBDD() throws ClassNotFoundException, InstantiationException, 
	IllegalAccessException, XMLDBException {
		Class cl = Class.forName(driver); //Cargar del driver
		Database database = (Database) cl.newInstance(); //Instancia de la BD
		DatabaseManager.registerDatabase(database); //Registro del driver
		Collection col = null; // Colección
		col = DatabaseManager.getCollection(URI, usu, usuPwd);
		if (col==null) {
			col=DatabaseManager.getCollection("xmldb:exist://"+server+":8080/exist/xmlrpc/db/",usu,usuPwd);
			CollectionManagementService mgt = (CollectionManagementService) 
					col.getService("CollectionManagementService", "1.0");
			Collection newcol=null;
			newcol=mgt.createCollection(new String(UTF8.encode(namecollection)));
			return newcol;
		}
		return col;
	}
	private static void subirFichero() throws ClassNotFoundException, InstantiationException, 
	IllegalAccessException, XMLDBException {
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
