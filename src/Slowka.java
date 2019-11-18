/*
S��wka
Autor: Miko�aj �arnowski
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;


public class Slowka {
	//pola g��wne
	private File plik;
	private List<String>natyw;
	private List<String>obcy;
	private int iloscWyjsciowa;
	private int iloscObecna;
	private int index;
	private List<Integer>wylosowane;
	private List<Integer>bledne;
	private double poprawne;
	private double proby;
	
	//flagi
	private boolean czyJestPlik;
	private boolean czySaSlowka;
	
	//pola pomocnicze
	private int nrOpcji;
	private int temp;
	private List<String>tempStrLst;
	
	//konstruktor
	public Slowka() {
		//inicjalizacja zmiennej "plik" i list
		//plik=new File(""); 
		plik=new File(""); //DO TEST�W
		natyw=new ArrayList<String>();
		obcy=new ArrayList<String>();
		wylosowane=new ArrayList<Integer>();
		bledne=new ArrayList<Integer>();
		tempStrLst=new ArrayList<String>();
		
		//otworzPlik();	//otworzenie pliku
		przetworzenie();//przetworzenie s��wek z wczytanego pliku
		reset();//tu inicjalizacja zmiennych zawiera si� w funkcji przetworznie
	}	
	
	//resetowanie statystyk
	public void reset() {
		iloscWyjsciowa=obcy.size();
		iloscObecna=iloscWyjsciowa;
		index=0;
		poprawne=0;
		proby=0;
		czyszczenie();
		losowanie();
	}
	
	public void czyszczenie() {
		wylosowane.clear();
		bledne.clear();
	}
	
	//filtr plik�w
	public class TextFileFilter extends FileFilter {
	    public boolean accept(File f) {
	        return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
	    }
	     
	    public String getDescription() {
	        return "Plik tekstowy (*.txt)";
	    }
	} 
	
	//utworzenie nowego pliku
	public boolean nowyPlik() {
		JFileChooser fc = new JFileChooser();
		String t="";
		fc.setFileFilter(new TextFileFilter());
		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			//czy jest ko�c�wka TXT i dopisanie, je�li taka potrzeba
			if(!fc.getSelectedFile().getName().toLowerCase().endsWith(".txt")) {t=".txt";}
			plik = new File(fc.getSelectedFile()+t);
		    try{
		    	boolean czyJestPlikiem= false;
		    	if(!plik.exists()) {czyJestPlikiem = plik.createNewFile();}
		    	PrintWriter pw = new PrintWriter(plik);
				pw.close();
				
				przetworzenie();
				return true;
		      }
		      catch(IOException e){}
		} 
		return false;
	}
	//otworzenie pliku
	public boolean otworzPlik() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new TextFileFilter());
		
		//potwierdzenie wybrania pliku - przetworzenie, przelosowanie, reset statystyk
		if(fc.showDialog(null, "Wybierz plik")==JFileChooser.APPROVE_OPTION) {
			plik=fc.getSelectedFile();
			przetworzenie();
			return true;
		}
		//anulowanie - zostawienie tak, jak by�o
		return false;
	}
	
	//przetworzenie s��wek z wczytanego pliku
	public void przetworzenie() {
		String linia;//linia - natywny i obcy
		Scanner skaner;//do odczytywania
		
		//poprawny plik
		try {
			//ustawienia pocz�tkowe
			czyJestPlik=false;
			czySaSlowka=false;
			skaner=new Scanner(plik);
			natyw.clear();
			obcy.clear();
			iloscWyjsciowa=0;
			iloscObecna=0;
			//zczytywanie
			while(skaner.hasNext()) {
				String podzial[] = skaner.nextLine().split(";");
				natyw.add(podzial[0]);
				obcy.add(podzial[1]);
				czySaSlowka=true;
			}
			//ustawienia ko�cowe
			iloscWyjsciowa=obcy.size();
			iloscObecna=iloscWyjsciowa;
			czyJestPlik=true;
		}
		//plik nie znaleziony
		catch (FileNotFoundException e) {
		} 
		//plik nie przystosowany do pracy z programem
		catch (IndexOutOfBoundsException e) {
			//nazwy pocji
			Object nazwyOpcji[]= {"Wybierz plik", "Zamknij program","Anuluj"};
			
			//obs�uga b��du
			nrOpcji=JOptionPane.showOptionDialog(null, "Plik nieprzystosowany do pracy z programem!", "B��d!",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, nazwyOpcji, nazwyOpcji[0]);
			przetworzenieObslugaBledu(nrOpcji);
		}
		
	}
	
	//do funkcji przetworzenie
	private void przetworzenieObslugaBledu(int opcja) {
		switch(opcja) {
		case 0:	//ponowne wybranie pliku
			otworzPlik();
			break;
		case 1:	//zamkni�cie programu
			System.exit(1);
			break;
		default://nic
			natyw.clear();
			obcy.clear();
			czyszczenie();
			break;
		}
	}
	
	//losowanie bez powt�rze� indeks�w do list ze s��wkami
	public void losowanie() {
		for(int i=0;i<iloscWyjsciowa;i++) {
			do {
				temp=(int)(Math.random()*iloscWyjsciowa);
			} while(czy_wylosowana(temp));
			wylosowane.add(temp);
		}
	}
	
	//do losowania bez powt�rze�
	public boolean czy_wylosowana(int nr) {
		for(int i=0;i<wylosowane.size();i++) {
			if(nr==wylosowane.get(i)) {return true;}
		}
		return false;
	}
	
	//uzyskanie flagi
	public boolean getCzyJestPlik() {
		return czyJestPlik;
	}
	
	public boolean getCzySaSlowka() {
		return czySaSlowka;
	}
	
	//uzyskanie s��wka natywnego
	public String getNatyw() {
		try {
			return natyw.get(wylosowane.get(index));
		} catch (Exception e) {//niepowodzenie
			return "";
		}
	}
	
	//uzyskanie s��wka obcego
	public String getObcy() {
		try {
			return obcy.get(wylosowane.get(index));
		} catch (Exception e) {//niepowodzenie
			return "";
		}
	}
	
	//uzyskanie skuteczno�ci
	public double getSkutecznosc() {
		//czy nie ma dzielenia przez 0
		if(proby!=0) {return poprawne/proby*100;}
		return 0;
	}
	
	//uzyskanie procentowego post�pu
	public int getPostep() {
		//czy nie ma dzielenia przez 0
		if(iloscWyjsciowa!=0) {return (int)(poprawne/iloscWyjsciowa*100);}
		return 0;
	}
	
	public double getPoprawne() {return poprawne;}
	public double getProby() {return proby;}
	
	//dodanie do poprawnych odpowiedzi i pr�b
	public void dodaniePunktow(boolean czyPrawidlowa) {
		//zawsze pr�ba o 1 wi�cej
		proby++;
		//punkty o 1 wi�cej, gdy odpowied� prawid�owa
		if(czyPrawidlowa) {poprawne++;};
	}
	
	//przej�cie do kolejnego indeksu LUB powt�rzenie b��dnych odpowiedzi LUB koniec s��wek
	public boolean przejscieDalej(boolean czyPrawidlowa) {
		//dodawanie s��wek do powt�rki
		if(!czyPrawidlowa) {bledne.add(wylosowane.get(index));}
		
		//przej�cie do kolejnego indeksu
		if(index<iloscObecna-1) {
			index++;
			return true;
		}
		//koniec s��wek
		else if(bledne.isEmpty()) {
			index=-1;
			//czyszczenie();
			return false;
		}
		//powt�rzenie b��dnych odpowiedzi
		wylosowane.clear();
		wylosowane.addAll(bledne);
		iloscObecna=wylosowane.size();
		bledne.clear();
		index=0;
		return true;
	}
	
	//z natywnego na obcy i z obcego na natywny
	public void zamianaMiejsc() {
		//zamiana miejsc
		tempStrLst.clear();
		tempStrLst.addAll(natyw);
		natyw.clear();
		natyw.addAll(obcy);
		obcy.clear();
		obcy.addAll(tempStrLst);
		tempStrLst.clear();
		
		reset();//reset stat�w
	}
	
	//MODYFIKACJA
	
	public DefaultListModel<Object> listaSlowek() {
		DefaultListModel<Object> dlm=new DefaultListModel<>();
		try {
			for(int i=0;i<natyw.size();i++) {
				dlm.addElement(natyw.get(i)+" - "+obcy.get(i));
			}
		} catch(IndexOutOfBoundsException e) {
			System.out.println(e);
		}
		return dlm;
	}
	
	public void dodaj(String natywny, String obcojez) {
		PrintWriter save;
		try {
			save=new PrintWriter(new FileWriter(plik, true));
			save.println(natywny+";"+obcojez);
			save.close();
			przetworzenie();
		} catch (IOException e) {
			
		}
		
	}
	
	public boolean czySiePowtarzaja(String natywny, String obcojez) {
		for(int i=0;i<natyw.size();i++) {
			if(natywny.equals(natyw.get(i)) || obcojez.equals(obcy.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public void nadpisanie() {
		try {
			PrintWriter pw = new PrintWriter(plik);
			pw.close();
			pw = new PrintWriter(new FileWriter(plik, true));
			
			for(int i=0;i<natyw.size();i++) {
				pw.println(natyw.get(i)+";"+obcy.get(i));
			}
			
			pw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void usuwanie(int index) {
		if(index>-1) {
			try {
				natyw.remove(index);
				obcy.remove(index);
				nadpisanie();
				przetworzenie();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
