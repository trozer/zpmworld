package zpmworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class Skeleton {
	private static int deepness = 0;
	private static boolean outputEnabled = true;
	private static HashMap<Integer, String> objectName  = new HashMap<Integer, String>();
	private static String callDir = "->";
	private static  Player player;
	private static Direction currDir;
	private static Object empty = new Object();
	

	
	public static void consoleMenu(){
		BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
		try{
			System.out.println("�dv�z�llek kedves felhaszn�l�! Men�pont v�laszt�shoz �rj be egy sz�mot a felsorolt lehet�s�gek k�z�l, majd nyomj entert!\n");
			System.out.println("A men�: ");
			System.out.println("1. J�t�kos l�p");
			System.out.println("2. J�t�kos fordul");
			System.out.println("3. J�t�kos l�");
			System.out.println("4. J�t�kos felvesz egy egys�get");
			System.out.println("5. J�t�kos lerak egy dobozt");
			System.out.println("6. L�ved�k mozog");
			System.out.println("7. Kil�p�s\n");
			String in=buffer.readLine(); //sz�m beolvas�sa
			//int chosen = Integer.parseInt(in); //intt� alak�tjuk
			char cases = in.charAt(0);
			switch (cases){
			case '1':
				System.out.println("Milyen t�pus� az ir�nyodba es� szomsz�dos mez�?");
				System.out.println("[f]al | [s]zakad�k | [k]apu | [m]�rleg | [u]t | [p]ort�lfal");
				in=buffer.readLine();
				cases = in.charAt(0);
				switch (cases){
				case 'k':
					System.out.println("Nyitva van a kapu ? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						System.out.println("Van egys�g elhelyezve a nyitott kapuban? (I/N)");
						in=buffer.readLine();
						cases = in.charAt(0);
						if(cases == 'I' || cases == 'i'){
							System.out.println("Milyen egys�g van a nyitott kapuban?");
							System.out.println("([z]pm | [d]oboz)");
							in=buffer.readLine();
							cases = in.charAt(0);
							if(cases == 'z'){
								char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','-'},{'b','0','2'},{'z','0','1'}};
								start(table,1);
							}
							else if(cases == 'd'){
								char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','-'},{'b','0','1'},{'b','0','2'}};
								start(table,1);
							}
							else{
								System.out.println("Helytelen v�lasz.. :(");
							} 
						}else if(cases == 'N' || cases == 'n'){
							char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','-'},{'b','0','2'}};
							start(table,1);
						}else{ System.out.println("Helytelen v�lasz.. :(");}
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','-'}};
						start(table,1);
					}
					else{ System.out.println("Helytelen v�lasz.. :(");}
				break;
				case 'm':
					System.out.println("Van egys�g elhelyezve rajta? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						System.out.println("Milyen egys�g van rajta elhelyezve?");
						System.out.println("([z]pm | [d]oboz)");
						in=buffer.readLine();
						cases = in.charAt(0);
						if(cases == 'z'){
							char[][] table= {{'r','s','g'},{'>'},{'c','0','2','0','1'},{'P','0','0','e','-'},{'z','0','1'}};
							start(table,1);
						}
						else if(cases == 'd'){
							char[][] table= {{'r','s','g'},{'>'},{'c','0','2','0','1'},{'P','0','0','e','-'},{'b','0','1'}};
							start(table,1);
						}
						else{
							System.out.println("Helytelen v�lasz.. :(");
						} 
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','s','g'},{'>'},{'c','0','2','0','1'},{'P','0','0','e','-'}};
						start(table,1);
					}else{ System.out.println("Helytelen v�lasz.. :(");}
				break;
				case 'u':
					System.out.println("Van egys�g elhelyezve rajta? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						System.out.println("Milyen egys�g van rajta elhelyezve?");
						System.out.println("([z]pm | [d]oboz)");
						in=buffer.readLine();
						cases = in.charAt(0);
						if(cases == 'z'){
							char[][] table= {{'r','r'},{'>'},{'P','0','0','e','-'},{'z','0','1'}};
							start(table,1);
						}
						else if(cases == 'd'){
							char[][] table= {{'r','r'},{'>'},{'P','0','0','e','-'},{'b','0','1'}};
							start(table,1);
						}
						else{
							System.out.println("Helytelen v�lasz.. :(");
						} 
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','r'},{'>'},{'P','0','0','e','-'}};
						start(table,1);
					}else{ System.out.println("Helytelen v�lasz.. :(");}
				break;
				case 'p':
					System.out.println("Van rajta port�lkapu? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						System.out.println("Van kij�rata? (I/N)");
						in=buffer.readLine();
						cases = in.charAt(0);
						if(cases == 'I' || cases == 'i'){
							char[][] table= {{'r','p','p'},{'>'},{'p','0','1','0','2'},{'P','0','0','e','-'}};
							start(table,1);
						}
						else if(cases == 'N' || cases == 'n'){
							char[][] table= {{'r','p'},{'>'},{'p','0','1'},{'P','0','0','e','-'}};
							start(table,1);
						}
						else{
							System.out.println("Helytelen v�lasz.. :(");
						} 
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','p'},{'>'},{'P','0','0','e','-'}};
						start(table,1);
					}else{ System.out.println("Helytelen v�lasz.. :(");}
				case 'f':
				{
					char[][] table= {{'r','w'},{'>'},{'P','0','0','e','-'}};
					start(table,1);
				}

				break;
				case 's':
				{		
					char[][] table= {{'r','a'},{'>'},{'P','0','0','e','-'}};
					start(table,1);
				}
				break;
				default:
					System.out.println("Nincs ilyen t�pus");
				break;
				}
			break;
			case '2':
			{
				System.out.println("Melyik ir�nyba forduljon?");
				System.out.println("([e]szak | [d]�l | [k]elet | [n]yugat)");
				in=buffer.readLine();
				cases = in.charAt(0);
				switch(cases){
				case 'e':
				{
					char[][] table = {{'r'},{'>'},{'P','0','0','s','-'}};
					currDir = Direction.SOUTH;
					start(table,2);
				}
				break;
				case 'd':
				{
					char[][] table = {{'r'},{'>'},{'P','0','0','n','-'}};
					currDir = Direction.NORTH;
					start(table,2);
				}
				break;
				case 'k':
				{
					char[][] table = {{'r'},{'>'},{'P','0','0','w','-'}};
					currDir = Direction.WEST;
					start(table,2);
				}
				break;
				case 'n':
				{
					char[][] table = {{'r'},{'>'},{'P','0','0','e','-'}};
					currDir = Direction.EAST;
					start(table,2);
				}
				break;
				default:
					System.out.println("Helytelen v�lasz.. :(");
				break;
				}
			}
			break;
			case '3':
			{	
				System.out.println("Milyen sz�n� l�ved�ket l�j�n?");
				System.out.println("([k]�k | [s]�rga)");
				in=buffer.readLine();
				cases = in.charAt(0);
				if(cases == 'k'){
					char[][] table = {{'r', 'r', 'r'},{'>'},{'P','0','0','e','-'}};
					start(table,3);
				}else if(cases == 's'){
					char[][] table = {{'r', 'r', 'r'},{'>'},{'P','0','0','e','-'}};
					start(table,3);
				}else{
					System.out.println("Helytelen v�lasz.. :(");
				}
			}
			break;
			case '4':
				System.out.println("Milyen t�pus� az ir�nyodba es� szomsz�dos mez�?");
				System.out.println("[f]al | [s]zakad�k | [k]apu | [m]�rleg | [u]t | [p]ort�lfal");
				in=buffer.readLine();
				cases = in.charAt(0);
				switch (cases){
				case 'k':
					System.out.println("Nyitva van a kapu ? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						System.out.println("Van egys�g elhelyezve a nyitott kapuban? (I/N)");
						in=buffer.readLine();
						cases = in.charAt(0);
						if(cases == 'I' || cases == 'i'){
							System.out.println("Milyen egys�g van a nyitott kapuban?");
							System.out.println("([z]pm | [d]oboz)");
							in=buffer.readLine();
							cases = in.charAt(0);
							if(cases == 'z'){
								char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','-'},{'b','0','2'},{'z','0','1'}};
								start(table,4);
							}
							else if(cases == 'd'){
								char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','-'},{'b','0','2'},{'b','0','1'}};
								start(table,4);
							}
							else{
								System.out.println("Helytelen v�lasz.. :(");
							} 
						}else if(cases == 'N' || cases == 'n'){
							char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','-'},{'b','0','2'}};
							start(table,4);
						}else{ System.out.println("Helytelen v�lasz.. :(");}
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','-'}};
						start(table,4);
					}
					else{ System.out.println("Helytelen v�lasz.. :(");}
				break;
				case 'm':
					System.out.println("Van egys�g elhelyezve rajta? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						System.out.println("Milyen egys�g van rajta elhelyezve?");
						System.out.println("([z]pm | [d]oboz)");
						in=buffer.readLine();
						cases = in.charAt(0);
						if(cases == 'z'){
							char[][] table= {{'r','s','g'},{'>'},{'c','0','2','0','1'},{'P','0','0','e','-'},{'z','0','1'}};
							start(table,4);
						}
						else if(cases == 'd'){
							char[][] table= {{'r','s','g'},{'>'},{'c','0','2','0','1'},{'P','0','0','e','-'},{'b','0','1'}};
							start(table,4);
						}
						else{
							System.out.println("Helytelen v�lasz.. :(");
						} 
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','s','g'},{'>'},{'c','0','2','0','1'},{'P','0','0','e','-'}};
						start(table,4);
					}else{ System.out.println("Helytelen v�lasz.. :(");}
				break;
				case 'u':
					System.out.println("Van egys�g elhelyezve rajta? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						System.out.println("Milyen egys�g van rajta elhelyezve?");
						System.out.println("([z]pm | [d]oboz)");
						in=buffer.readLine();
						cases = in.charAt(0);
						if(cases == 'z'){
							char[][] table= {{'r','r'},{'>'},{'P','0','0','e','-'},{'z','0','1'}};
							start(table,4);
						}
						else if(cases == 'd'){
							System.out.println("Van a j�t�kosna�l kor�bban felvett doboz? (I/N)");
							in=buffer.readLine();
							cases = in.charAt(0);
							if(cases == 'I' || cases == 'i'){
								char[][] table= {{'r','r'},{'>'},{'P','0','0','e','b'},{'b','0','1'}};
								start(table,4);
							}else if(cases == 'N' || cases == 'n'){
								char[][] table= {{'r','r'},{'>'},{'P','0','0','e','-'},{'b','0','1'}};
								start(table,4);
							}else{ System.out.println("Helytelen v�lasz.. :(");}
						}
						else{
							System.out.println("Helytelen v�lasz.. :(");
						} 
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','r'},{'>'},{'P','0','0','e','-'}};
						start(table,4);
					}else{ System.out.println("Helytelen v�lasz.. :(");}
				break;
				case 'p':
				{
					char[][] table= {{'r','p'},{'>'},{'p','0','1'},{'P','0','0','e','-'}};
					start(table,4);
				}
				break;
				case 'f':
				{
					char[][] table= {{'r','w'},{'>'},{'P','0','0','e','-'}};
					start(table,4);
				}

				break;
				case 's':
				{		
					char[][] table= {{'r','a'},{'>'},{'P','0','0','e','-'}};
					start(table,4);
				}
				break;
				default:
					System.out.println("Nincs ilyen t�pus");
				break;
				}
			break;
			case '5':
				System.out.println("Milyen t�pus� az ir�nyodba es� szomsz�dos mez�?");
				System.out.println("[f]al | [s]zakad�k | [k]apu | [m]�rleg | [u]t | [p]ort�lfal");
				in=buffer.readLine();
				cases = in.charAt(0);
				switch (cases){
				case 'k':
					System.out.println("Nyitva van a kapu ? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						System.out.println("Van egys�g elhelyezve a nyitott kapuban? (I/N)");
						in=buffer.readLine();
						cases = in.charAt(0);
						if(cases == 'I' || cases == 'i'){
							char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','b'},{'b','0','2'},{'b','0','1'}};
							start(table,5);
						}else if(cases == 'N' || cases == 'n'){
							char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','b'},{'b','0','2'}};
							start(table,5);
						}else{ System.out.println("Helytelen v�lasz.. :(");}
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'P','0','0','e','b'}};
						start(table,5);
					}
					else{ System.out.println("Helytelen v�lasz.. :(");}
				break;
				case 'm':
					System.out.println("Van egys�g elhelyezve rajta? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						char[][] table= {{'r','s','g'},{'>'},{'c','0','2','0','1'},{'P','0','0','e','b'},{'b','0','1'},{'b','0','2'}};
						start(table,5);
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','s','g'},{'>'},{'c','0','2','0','1'},{'P','0','0','e','b'}};
						start(table,5);
					}else{ System.out.println("Helytelen v�lasz.. :(");}
				break;
				case 'u':
					System.out.println("Van egys�g elhelyezve rajta? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						char[][] table= {{'r','r'},{'>'},{'P','0','0','e','b'},{'b','0','1'}};
						start(table,5);
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','r'},{'>'},{'P','0','0','e','b'}};
						start(table,5);
					}else{ System.out.println("Helytelen v�lasz.. :(");}
				break;
				case 'p':
				{
					char[][] table= {{'r','p'},{'>'},{'p','0','1'},{'P','0','0','e','b'}};
					start(table,5);
				}
				break;
				case 'f':
				{
					char[][] table= {{'r','w'},{'>'},{'P','0','0','e','b'}};
					start(table,5);
				}

				break;
				case 's':
				{		
					char[][] table= {{'r','a'},{'>'},{'P','0','0','e','b'}};
					start(table,5);
				}
				break;
				default:
					System.out.println("Nincs ilyen t�pus");
				break;
				}
			break;
			case '6':
				System.out.println("Milyen t�pus� a l�ved�k ir�ny�ba es� szomsz�dos mez�");
				System.out.println("[f]al | [s]zakad�k | [k]apu | [m]�rleg | [u]t | [p]ort�lfal");
				in=buffer.readLine();
				cases = in.charAt(0);
				switch (cases){
				case 'k':
					System.out.println("Nyitva van a kapu ? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'B','0','0','e'},{'b','0','2'}};
						start(table,6);
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','g','s'},{'>'},{'c','0','1','0','2'},{'B','0','0','e','-'}};
						start(table,6);
					}
					else{ System.out.println("Helytelen v�lasz.. :(");}
				break;
				case 'm':
				{
					char[][] table= {{'r','s','g'},{'>'},{'c','0','2','0','1'},{'B','0','0','e'},{'b','0','2'}};
					start(table,6);
				}
				break;
				case 'u':
				{
					char[][] table= {{'r','r'},{'>'},{'B','0','0','e'}};
					start(table,6);
				}
				break;
				case 'p':
					System.out.println("Van rajta port�lkapu? (I/N)");
					in=buffer.readLine();
					cases = in.charAt(0);
					if(cases == 'I' || cases == 'i'){
						System.out.println("Van olyan sz�n� port�lbakpu a p�ly�n, mint a l�ved�k? (I/N)");
						in=buffer.readLine();
						cases = in.charAt(0);
						if(cases == 'I' || cases == 'i'){
							char[][] table= {{'r','p','p'},{'>'},{'p','0','2','0','1'},{'B','0','0','e'}};
							start(table,6);
						}
						else if(cases == 'N' || cases == 'n'){
							char[][] table= {{'r','p'},{'>'},{'p','0','1'},{'B','0','0','e'}};
							start(table,6);
						}
						else{
							System.out.println("Helytelen v�lasz.. :(");
						} 
					}else if(cases == 'N' || cases == 'n'){
						char[][] table= {{'r','p'},{'>'},{'B','0','0','e'}};
						start(table,6);
					}else{ System.out.println("Helytelen v�lasz.. :(");}
				case 'f':
				{
					char[][] table= {{'r','w'},{'>'},{'B','0','0','e',}};
					start(table,6);
				}

				break;
				case 's':
				{		
					char[][] table= {{'r','a'},{'>'},{'B','0','0','e'}};
					start(table,6);
				}
				break;
				default:
					System.out.println("Nincs ilyen t�pus");
				break;
				}
			break;
			case 7:
				System.out.println("kil�p�s");
			break;
			default:
				System.out.println("Nincs ilyen men�pont!");
			break;			
			}
		} catch (IOException e){
			System.out.println(e.getMessage());
		} 
	}
	
	public static void start(char[][] table, int control){

		
	}
	
	public static void returnMethod(String methodName, Object called, List<Object> parameters){
		callDir = "<-";
		printMethod(methodName, called, parameters);
		deepness--;	
	}
	
	public static void callMethod(String methodName, Object called, List<Object> parameters) {
		callDir = "->";
		deepness++;
		printMethod(methodName, called, parameters);
	}
	
	public static void printMethod(String methodName, Object called, List<Object> parameters){

	}
	
	public static void registerHashCode(Integer objectHash, String name){
		objectName.put(objectHash, name);
	}
	
	public static void clearObjectName(){
		objectName = new HashMap<Integer, String>();
		objectName.put(empty.hashCode(), "void");
	}
	
	public static Object getEmpty(){
		return empty;
	}
	
	public static void disableOutput(){
		outputEnabled = false;
	}
	
	public static void enableOutput(){
		outputEnabled = true;
	}
	
	public static void addPlayer(Player gplayer) { player = gplayer; }
	
	
}

