/*
S≥Ûwka
Autor: Miko≥aj Øarnowski
*/

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Color;
import javax.swing.JProgressBar;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.AbstractListModel;

public class Slowka3Win {
	//elementy okna
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu mnPlik;
	private JMenu mnOpcje;
	private JMenu mnPersonalizacja;
	private JMenuItem mntmOtworz;
	private JLabel transOpis;
	private JLabel slowoOpis;
	private JLabel slowoTresc;
	private JTextField transTresc;
	private JLabel wiadomosc;
	private JMenu mnRodzajTranslacji;
	private JLabel postepOpis;
	private JLabel skutecznoscOpis;
	private JButton odpowiedz;
	private JButton sprawdz;
	private JLabel trybOpis;
	private JProgressBar postepPasek;
	private JLabel skutecznoscTresc;
	private JMenuItem trybNatywnyObcy;
	private JMenuItem trybObcyNatywny;
	private JMenuItem motywCiemny;
	private JMenuItem motywJasny;
	private JMenu mnUstawMotyw;
	private JMenuItem mntmResetuj;
	
	//objekt klasy Slowka
	Slowka slowka;
	//poprawna odpowiedz
	String odpowedz;
	String odpPoprawna;
	
	//flagi
	private boolean czyOdpPrawidlowa;
	
	//do opcji
	private int nrOpcji;
	private Object opcjeTN[]= {"Tak", "Nie"};
	
	//formatowanie liczb
	private DecimalFormat dfInt;
	private DecimalFormat dfFloat;
	
	//kolory
	Color kolorTlo;
	Color kolorNapis;
	Color kolorInfo;
	Color kolorDobrze;
	Color kolorZle;
	UIManager ui;
	private JMenu mnTryb;
	private JMenuItem modyfikacjaPrzycisk;
	private JMenuItem testPrzycisk;
	private JPanel testPanel;
	private JPanel modPanel;
	private JLabel mSlowoOpis;
	private JLabel mTransOpis;
	private JTextField slowoPole;
	private JTextField transPole;
	private JButton btnDodaj;
	private JList lista;
	private JScrollPane scrollPane;
	private JButton btnUsun;
	private JMenuItem mntmNowy;
	private JLabel modWiadomosc;
	private JMenu pomoc;
	private JMenuItem oProgramie;
	private JMenuItem plikiZRekordami;
	private JMenuItem rozwiazywanieProblemow;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Slowka3Win window = new Slowka3Win();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Slowka3Win() {
		initialize();
				
		Object opcje[]= {"StwÛrz nowy plik", "OtwÛrz istniejπcy plik", "Zamknij program"};
		nrOpcji=2;
		nrOpcji=JOptionPane.showOptionDialog(null, "Witaj w programie S≥Ûwka\nCo chcesz zrobiÊ?", "Co dalej?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcje, opcje[0]);
		switch(nrOpcji) {
		case 0:
			nowyPlik();
			lista.setModel(slowka.listaSlowek());
			break;
		case 1:
			otworzPlik();
			lista.setModel(slowka.listaSlowek());
			break;
		case 2:
			System.exit(1);
			break;
		default:
			break;
		}
		
		//ustawianie, jak plik poprawnie otworzony i przetworzony
		if(slowka.getCzyJestPlik()) {
			ustawWszystko();
			lista.setModel(slowka.listaSlowek());
		}
		else {
			otworzDobryPlik();
			modUstawEdytowalnosc(false);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int frameWidth=1000;
		int frameHeight=650;
		int sysWidth=Toolkit.getDefaultToolkit().getScreenSize().width;
		int sysHeight=Toolkit.getDefaultToolkit().getScreenSize().height;
		
		slowka=new Slowka();
		
		//inicjalizacja kolorÛw
		kolorTlo=null;
		kolorNapis=null;
		kolorInfo=Color.BLUE;
		kolorDobrze=Color.GREEN;
		kolorZle=Color.RED;
		ui=new UIManager();
		
		//modyfikacja flagi
		
		frame = new JFrame("S≥Ûwka");
		frame.setResizable(false);
		frame.setBounds((sysWidth-frameWidth)/2, (sysHeight-frameHeight)/2, frameWidth, frameHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnPlik = new JMenu("Plik");
		menuBar.add(mnPlik);
		
		mntmOtworz = new JMenuItem("Otw\u00F3rz");
		mntmOtworz.addActionListener(new ActionListener() {
			//otwieranie pliku
			public void actionPerformed(ActionEvent e) {
				otworzPlik();
			}
		});
		
		mntmNowy = new JMenuItem("Nowy");
		mntmNowy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nowyPlik();
			}
		});
		mnPlik.add(mntmNowy);
		mnPlik.add(mntmOtworz);
		
		mnOpcje = new JMenu("Opcje");
		mnOpcje.setEnabled(false);
		menuBar.add(mnOpcje);
		
		mntmResetuj = new JMenuItem("Resetuj");
		mntmResetuj.addActionListener(new ActionListener() {
			//resetowanie statystyk
			public void actionPerformed(ActionEvent e) {
				nrOpcji=JOptionPane.showOptionDialog(null, "Czy na pewno chcesz zresetowaÊ statystyki?", "Pytanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcjeTN, opcjeTN[1]);
				if(nrOpcji==0) {
					resetuj();
					JOptionPane.showMessageDialog(null, "Statystyki zresetowano", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnOpcje.add(mntmResetuj);
		
		mnRodzajTranslacji = new JMenu("Rodzaj translacji");
		mnOpcje.add(mnRodzajTranslacji);
		
		trybNatywnyObcy = new JMenuItem("Natywny -> Obcy");
		trybNatywnyObcy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				zmianaTrybu(false, "z j\u0119zyka natywnego na obcy");
			}
		});
		trybNatywnyObcy.setEnabled(false);
		mnRodzajTranslacji.add(trybNatywnyObcy);
		
		trybObcyNatywny = new JMenuItem("Obcy -> Natywny");
		trybObcyNatywny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zmianaTrybu(true, "z j\u0119zyka obcego na natywny");
			}
		});
		mnRodzajTranslacji.add(trybObcyNatywny);
		
		mnTryb = new JMenu("Tryb");
		menuBar.add(mnTryb);
		
		modyfikacjaPrzycisk = new JMenuItem("Modyfikacja");
		modyfikacjaPrzycisk.setEnabled(false);
		modyfikacjaPrzycisk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nrOpcji=1;
				nrOpcji=JOptionPane.showOptionDialog(null, "Przejúcie do modyfikacji listy oznacza reset statystyk.\nNie prÛbuj wiÍc úciπgaÊ ;)\n\nCzy na pewno chcesz kontynuowaÊ?", "Co dalej?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcjeTN, opcjeTN[1]);
				if(nrOpcji==0) {wyborTrybu(false);}
				
				if(!slowka.getCzyJestPlik()) {otworzDobryPlik();}
				modUstawEdytowalnosc(slowka.getCzyJestPlik());
				
				wyczyscPola();
			}
		});
		mnTryb.add(modyfikacjaPrzycisk);
		
		testPrzycisk = new JMenuItem("Test");
		testPrzycisk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wyborTrybu(true);
				resetuj();
				ustawEdytowalnosc(slowka.getCzySaSlowka());
				if(!slowka.getCzyJestPlik()) {otworzDobryPlik();}
			}
		});
		mnTryb.add(testPrzycisk);
		
		mnPersonalizacja = new JMenu("Personalizacja");
		menuBar.add(mnPersonalizacja);
		
		mnUstawMotyw = new JMenu("Ustaw motyw");
		mnPersonalizacja.add(mnUstawMotyw);
		
		motywJasny = new JMenuItem("Jasny");
		motywJasny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motywJasny.setEnabled(false);
				motywCiemny.setEnabled(true);
				mozZmKol(true);
				kolorki(null, null, Color.BLUE);
			}
		});
		motywJasny.setEnabled(false);
		mnUstawMotyw.add(motywJasny);
		
		motywCiemny = new JMenuItem("Ciemny");
		motywCiemny.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motywJasny.setEnabled(true);
				motywCiemny.setEnabled(false);
				mozZmKol(true);
				kolorki(Color.DARK_GRAY, Color.LIGHT_GRAY, Color.CYAN);
			}
		});
		mnUstawMotyw.add(motywCiemny);
		
		pomoc = new JMenu("Pomoc");
		menuBar.add(pomoc);
		
		oProgramie = new JMenuItem("O programie");
		oProgramie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Program umoøliwia naukÍ s≥Ûw/wyraøeÒ w jÍzyku obcym metodπ wielokrotnych powtÛrzeÒ."
													+"\nFunkcje programu:"
													+ "\n- nauka s≥Ûwek poprzez rÛønorodne po≥πczenia"
													+ "\n- wyúwietlanie danych dot. postÍpu i skutecznoúci w nauce"
													+ "\n- tworzenie nowych plikÛw ze s≥Ûwkami"
													+ "\n- dodawanie i usuwanie s≥Ûwek", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		pomoc.add(oProgramie);
		
		plikiZRekordami = new JMenuItem("Pliki z rekordami");
		plikiZRekordami.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Listy ze s≥Ûwkami sπ w postaci plikÛw TXT."
													+ "\nPojedyncze s≥Ûwko ma nastÍpujπcπ budowÍ:"
													+ "\ns≥owo;t≥umaczenie"
													+ "\nJedna linijka to jedno s≥owo z t≥umaczeniem."
													+ "\nOstatnia linijka jest pusta - miejsce na nowe s≥Ûwko."
													+ "\nProgram umoøliwia dodawanie i usuwanie s≥Ûwek."
													+ "\n\nMoøesz rÛwnieø sprÛbowaÊ samodzielnie utworzyÊ takπ listÍ w edytorze tekstu ;)"
													+ "\nWarunki:"
													+ "\n- Format pliku - TXT"
													+ "\n- Kodowanie - ANSI (inaczej mogπ byÊ problemy z odczytem pliku)"
													+ "\n  Kodowanie ANSI jest domyúlne w systemowym notatniku czy programie Notepad ++",
													"Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		pomoc.add(plikiZRekordami);
		
		rozwiazywanieProblemow = new JMenuItem("Rozwi\u0105zywanie problem\u00F3w");
		rozwiazywanieProblemow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Najprostszy sposÛb na rozwiπzanie problemu z programem:"
													+"\nUruchom ponownie :)"
													+ "\n\nJeúli jednak dalej bÍdzie coú nie tak, to trudno."
													+ "\nNic nie jest idealne i program moøe mieÊ pewne b≥Ídy","Info",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		pomoc.add(rozwiazywanieProblemow);
		frame.getContentPane().setLayout(null);
		
		testPanel = new JPanel();
		testPanel.setVisible(false);
		
		modPanel = new JPanel();
		
		modPanel.setLayout(null);
		modPanel.setBounds(0, 0, 994, 601);
		frame.getContentPane().add(modPanel);
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 11, 974, 413);
		modPanel.add(scrollPane);
		
		lista = new JList();
		lista.setFont(new Font("Arial", Font.PLAIN, 20));
		scrollPane.setViewportView(lista);
		
		mSlowoOpis = new JLabel("S\u0142owo");
		mSlowoOpis.setBounds(10, 472, 105, 25);
		modPanel.add(mSlowoOpis);
		mSlowoOpis.setFont(new Font("Arial", Font.BOLD, 20));
		
		slowoPole = new JTextField();
		slowoPole.setOpaque(false);
		slowoPole.setFont(new Font("Arial", Font.PLAIN, 20));
		slowoPole.setColumns(10);
		slowoPole.setBounds(162, 471, 822, 25);
		modPanel.add(slowoPole);
		
		mTransOpis = new JLabel("T\u0142umaczenie");
		mTransOpis.setFont(new Font("Arial", Font.BOLD, 20));
		mTransOpis.setBounds(10, 508, 120, 25);
		modPanel.add(mTransOpis);
		
		transPole = new JTextField();
		transPole.setOpaque(false);
		transPole.setFont(new Font("Arial", Font.PLAIN, 20));
		transPole.setColumns(10);
		transPole.setBounds(162, 507, 822, 25);
		modPanel.add(transPole);
		
		btnDodaj = new JButton("Dodaj");
		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(slowoPole.getText().equals("") || transPole.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Uzupe≥nij wszystkie pola!", "Info", JOptionPane.INFORMATION_MESSAGE);
				} else {
					nrOpcji=0;
					if(slowka.czySiePowtarzaja(slowoPole.getText(), transPole.getText())) {
						nrOpcji=JOptionPane.showOptionDialog(null, "S≥Ûwko lub t≥umaczenie siÍ powtarza.\nCzy na pewno chcesz dodaÊ?", "Pytanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcjeTN, opcjeTN[1]);
					}
					if(nrOpcji==0) {
						slowka.dodaj(slowoPole.getText(),transPole.getText());
						lista.setModel(slowka.listaSlowek());
						wyczyscPola();
					}
				}
			}
		});
		btnDodaj.setOpaque(false);
		btnDodaj.setFont(new Font("Arial", Font.BOLD, 20));
		btnDodaj.setBounds(764, 543, 105, 25);
		modPanel.add(btnDodaj);
		
		btnUsun = new JButton("Usu\u0144");
		btnUsun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nrOpcji=1;
				nrOpcji=JOptionPane.showOptionDialog(null, "Czy na pewno chcesz usunπÊ zaznaczony rekord?", "Co dalej?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcjeTN, opcjeTN[1]);
				if(nrOpcji==0) {
					slowka.usuwanie(lista.getSelectedIndex());
					lista.setModel(slowka.listaSlowek());
				}
			}
		});
		btnUsun.setOpaque(false);
		btnUsun.setFont(new Font("Arial", Font.BOLD, 20));
		btnUsun.setBounds(879, 543, 105, 25);
		modPanel.add(btnUsun);
		
		modWiadomosc = new JLabel();
		modWiadomosc.setHorizontalAlignment(SwingConstants.CENTER);
		modWiadomosc.setFont(new Font("Arial", Font.BOLD, 20));
		modWiadomosc.setBounds(10, 435, 974, 25);
		modPanel.add(modWiadomosc);
		testPanel.setBounds(0, 0, 994, 601);
		frame.getContentPane().add(testPanel);
		testPanel.setLayout(null);
		
		trybOpis = new JLabel("z j\u0119zyka natywnego na obcy");
		trybOpis.setBounds(10, 10, 974, 25);
		testPanel.add(trybOpis);
		trybOpis.setForeground(kolorInfo);
		trybOpis.setHorizontalAlignment(SwingConstants.CENTER);
		trybOpis.setFont(new Font("Arial", Font.BOLD, 20));
		
		slowoOpis = new JLabel("S\u0142owo");
		slowoOpis.setBounds(10, 46, 105, 25);
		testPanel.add(slowoOpis);
		slowoOpis.setFont(new Font("Arial", Font.BOLD, 20));
		
		slowoTresc = new JLabel("");
		slowoTresc.setBounds(152, 46, 832, 25);
		testPanel.add(slowoTresc);
		slowoTresc.setFont(new Font("Arial", Font.PLAIN, 20));
		
		transOpis = new JLabel("T\u0142umaczenie");
		transOpis.setBounds(10, 85, 120, 25);
		testPanel.add(transOpis);
		transOpis.setFont(new Font("Arial", Font.BOLD, 20));
		
		transTresc = new JTextField();
		transTresc.setBounds(152, 84, 832, 25);
		testPanel.add(transTresc);
		transTresc.setEditable(false);
		transTresc.setFont(new Font("Arial", Font.PLAIN, 20));
		transTresc.setColumns(10);
		
		odpowiedz = new JButton("Twoja odpowied\u017A");
		odpowiedz.setBounds(529, 120, 260, 25);
		testPanel.add(odpowiedz);
		odpowiedz.setEnabled(false);
		odpowiedz.setFont(new Font("Arial", Font.BOLD, 20));
		
		sprawdz = new JButton("Sprawd\u017A");
		sprawdz.setBounds(799, 120, 185, 25);
		testPanel.add(sprawdz);
		sprawdz.setEnabled(false);
		sprawdz.addActionListener(new ActionListener() {
			//nastƒôpne s≈Ç√≥wko LUB koniec gry
			public void actionPerformed(ActionEvent arg0) {
				switch(sprawdz.getText()) {
				case "Sprawd\u017A"://sprawdzanie odpowiedzi
					odpowedz=transTresc.getText();
					transTresc.setEditable(false);
					sprawdz.setText("Nast\u0119pne s\u0142owo");
					if(transTresc.getText().equals(odpPoprawna)) {//prawid≥owa
						czyOdpPrawidlowa=true;
						ustawKolorWyniku(kolorDobrze);
						wiadomosc.setText("Dobrze!");
					} else{//nieprawid≥owa
						czyOdpPrawidlowa=false;
						odpowiedz.setEnabled(true);
						poprawnaOdpowiedzWyglad();
					}
					slowka.dodaniePunktow(czyOdpPrawidlowa);
					ustawPostep();
					break;
				case "Nast\u0119pne s\u0142owo":
					ustawKolorWyniku(kolorNapis);
					transTresc.setText("");
					wiadomosc.setText("");
					ustawEdytowalnosc(true);
					odpowiedz.setEnabled(false);
					odpowiedz.setText("Twoja odpowied\u017A");
					sprawdz.setText("Sprawd\u017A");
					if(!(slowka.przejscieDalej(czyOdpPrawidlowa))) {
						Object opcje[]= {"OtwÛrz inny plik", "Resetuj statystyki", "Nic nie rÛb"};
						ustawEdytowalnosc(false);
						nrOpcji=JOptionPane.showOptionDialog(null, 
								"Koniec s≥Ûwek!\nPoprawne prÛby: "+dfInt.format(slowka.getPoprawne())+
								"\nWszystkie proby: "+dfInt.format(slowka.getProby())+
								"\nSkutecznoúÊ: "+dfFloat.format(slowka.getSkutecznosc())+" %"+
								"\nCo dalej?",
								"Koniec gry", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcje, opcje[0]);
						
						switch(nrOpcji) {
						case 0:
							otworzPlik();
							break;
						case 1:
							resetuj();
							JOptionPane.showMessageDialog(null, "Statystyki zresetowano", "Info", JOptionPane.INFORMATION_MESSAGE);
							break;
						case 2:
							slowka.czyszczenie();
						default:
							break;
						}
					}
					ustawSlowa();
					break;
				}
			}
		});
		sprawdz.setFont(new Font("Arial", Font.BOLD, 20));
		
		wiadomosc = new JLabel();
		wiadomosc.setForeground(Color.RED);
		wiadomosc.setBounds(10, 157, 974, 25);
		testPanel.add(wiadomosc);
		wiadomosc.setHorizontalAlignment(SwingConstants.CENTER);
		wiadomosc.setFont(new Font("Arial", Font.BOLD, 20));
		
		skutecznoscOpis = new JLabel("Skuteczno\u015B\u0107");
		skutecznoscOpis.setBounds(10, 529, 128, 25);
		testPanel.add(skutecznoscOpis);
		skutecznoscOpis.setFont(new Font("Arial", Font.BOLD, 20));
		
		skutecznoscTresc = new JLabel("0,00%");
		skutecznoscTresc.setBounds(152, 529, 832, 25);
		testPanel.add(skutecznoscTresc);
		skutecznoscTresc.setHorizontalAlignment(SwingConstants.RIGHT);
		skutecznoscTresc.setFont(new Font("Arial", Font.PLAIN, 20));
		
		postepOpis = new JLabel("Post\u0119p");
		postepOpis.setBounds(10, 565, 75, 25);
		testPanel.add(postepOpis);
		postepOpis.setFont(new Font("Arial", Font.BOLD, 20));
		
		postepPasek = new JProgressBar();
		postepPasek.setBounds(152, 565, 832, 25);
		testPanel.add(postepPasek);
		postepPasek.setStringPainted(true);
		postepPasek.setForeground(kolorDobrze);
		odpowiedz.addActionListener(new ActionListener() {
			//sprawdzanie odpowiedzi LUB wy≈õwietlanie swojej/poprawnej odpowiedzi
			public void actionPerformed(ActionEvent arg0) {
				switch(odpowiedz.getText()) {
				case "Twoja odpowied\u017A"://pokazanie twojej b≥Ídnej odpowiedzi
					ustawKolorWyniku(kolorZle);
					transTresc.setText(odpowedz);
					odpowiedz.setText("Prawid\u0142owa odpowied\u017A");
					wiadomosc.setText("èle");
					break;
				case "Prawid\u0142owa odpowied\u017A"://pokazanie prawid≥owej odpowiedzi
					//wywal do oddzielnej funkcji
					poprawnaOdpowiedzWyglad();
					break;
				}
			}
		});
		
		dfInt=new DecimalFormat("####0");
		dfFloat=new DecimalFormat("####0.00");
	}
	
	public void mozZmKol(boolean bool) {
		menuBar.setOpaque(bool);
			mnPlik.setOpaque(bool);
				mntmNowy.setOpaque(bool);
				mntmOtworz.setOpaque(bool);
			mnOpcje.setOpaque(bool);
				mntmResetuj.setOpaque(bool);
				mnRodzajTranslacji.setOpaque(bool);
					trybNatywnyObcy.setOpaque(bool);
					trybObcyNatywny.setOpaque(bool);
			mnTryb.setOpaque(bool);
				modyfikacjaPrzycisk.setOpaque(bool);
				testPrzycisk.setOpaque(bool);
			mnPersonalizacja.setOpaque(bool);
				mnUstawMotyw.setOpaque(bool);
					motywJasny.setOpaque(bool);
					motywCiemny.setOpaque(bool);
			pomoc.setOpaque(bool);
				oProgramie.setOpaque(bool);
				plikiZRekordami.setOpaque(bool);
				rozwiazywanieProblemow.setOpaque(bool);
		
		testPanel.setOpaque(bool);
			transTresc.setOpaque(bool);
			odpowiedz.setOpaque(bool);
			sprawdz.setOpaque(bool);
			postepPasek.setOpaque(bool);
			
		modPanel.setOpaque(bool);
			lista.setOpaque(bool);
			btnDodaj.setOpaque(bool);
			btnUsun.setOpaque(bool);
	}
	
	public void kolorki(Color tlo, Color napis, Color info) {
		//zmiana zmiennych klasy
		kolorTlo=tlo;
		kolorNapis=napis;
		kolorInfo=info;
		
		//t≥o
		menuBar.setBackground(tlo);
			mnPlik.setBackground(tlo);
				mntmNowy.setBackground(tlo);
				mntmOtworz.setBackground(tlo);
			mnOpcje.setBackground(tlo);
				mntmResetuj.setBackground(tlo);
				mnRodzajTranslacji.setBackground(tlo);
					trybNatywnyObcy.setBackground(tlo);
					trybObcyNatywny.setBackground(tlo);
			mnTryb.setBackground(tlo);
				modyfikacjaPrzycisk.setBackground(tlo);
				testPrzycisk.setBackground(tlo);
			mnPersonalizacja.setBackground(tlo);
				mnUstawMotyw.setBackground(tlo);
					motywJasny.setBackground(tlo);
					motywCiemny.setBackground(tlo);
			pomoc.setBackground(tlo);
				oProgramie.setBackground(tlo);
				plikiZRekordami.setBackground(tlo);
				rozwiazywanieProblemow.setBackground(tlo);
								
		frame.getContentPane().setBackground(tlo);
			testPanel.setBackground(tlo);
				transTresc.setBackground(tlo);
				odpowiedz.setBackground(tlo);
				sprawdz.setBackground(tlo);
				postepPasek.setBackground(tlo);
			modPanel.setBackground(tlo);
				lista.setBackground(tlo);
				btnDodaj.setBackground(tlo);
				btnUsun.setBackground(tlo);
		
		//napisy
		menuBar.setForeground(napis);
			mnPlik.setForeground(napis);
				mntmNowy.setBackground(napis);
				mntmOtworz.setForeground(napis);
			mnOpcje.setForeground(napis);
				mntmResetuj.setForeground(napis);
				mnRodzajTranslacji.setForeground(napis);
					trybNatywnyObcy.setForeground(napis);
					trybObcyNatywny.setForeground(napis);
			mnTryb.setForeground(napis);
				modyfikacjaPrzycisk.setForeground(napis);
				testPrzycisk.setForeground(napis);
			mnPersonalizacja.setForeground(napis);
				mnUstawMotyw.setForeground(napis);
				motywJasny.setForeground(napis);
				motywCiemny.setForeground(napis);
			pomoc.setForeground(napis);
				oProgramie.setForeground(napis);
				plikiZRekordami.setForeground(napis);
				rozwiazywanieProblemow.setForeground(napis);
		
		slowoOpis.setForeground(napis);
		slowoTresc.setForeground(napis);
		transOpis.setForeground(napis);
		odpowiedz.setForeground(napis);
		sprawdz.setForeground(napis);
		skutecznoscOpis.setForeground(napis);
		skutecznoscTresc.setForeground(napis);
		postepOpis.setForeground(napis);
		
		lista.setForeground(napis);
		mSlowoOpis.setForeground(napis);
		slowoPole.setForeground(napis);
		mTransOpis.setForeground(napis);
		transPole.setForeground(napis);
		btnDodaj.setForeground(napis);
		btnUsun.setForeground(napis);
		
		//odswiezanie napisÛw
		trybOpis.setForeground(info);
		
		if(sprawdz.getText().equals("Sprawd\u017A")) {ustawKolorWyniku(napis);}
		else if(czyOdpPrawidlowa && sprawdz.getText().equals("Nast\u0119pne s\u0142owo")) {ustawKolorWyniku(kolorDobrze);}
		else if(odpowiedz.getText().equals("Twoja odpowied\u017A") && odpowiedz.isEnabled()) {ustawKolorWyniku(info);}
		else if(odpowiedz.getText().equals("Prawid\u0142owa odpowied\u017A")&& odpowiedz.isEnabled()) {ustawKolorWyniku(kolorZle);}
		
		//dla okienek dialogowych i FIleChoosera
		UIManager.put("Panel.opaque",true);
		UIManager.put("OptionPane.background", tlo);
		UIManager.put("OptionPane.messageForeground", napis);
		UIManager.put("Panel.background", tlo);
		UIManager.put("Panel.foreground", napis);
		
		UIManager.put("Button.background", tlo);
		UIManager.put("Button.foreground", napis);
		
		UIManager.put("FileChooser.opaque", true);
		UIManager.put("FileChooser.foreground", napis);
		UIManager.put("FileChooser.cancelButtonText", "Anuluj");//przenieú wyøej
		UIManager.put("FileChooser.openFolderButtonText", "OtwÛrz");//NIE DZIA£A
		
	    UIManager.put("ComboBox.background", tlo);
	    UIManager.put("ComboBox.foreground", napis);

	    UIManager.put("TextField.background", tlo);
	    UIManager.put("TextField.foreground", napis);
	    UIManager.put("ToolBar.background", tlo);

	    UIManager.put("Viewport.background", tlo);
	    UIManager.put("Viewport.foreground", napis);
	    
	    UIManager.put("ScrollPane.background", tlo);
	    UIManager.put("List.background", tlo);
	    UIManager.put("List.foreground", napis);
	}
	
	public void nowyPlik() {
		if(slowka.nowyPlik()) {
			lista.setModel(slowka.listaSlowek());
			resetuj();
			ustawEdytowalnosc(slowka.getCzySaSlowka());
			modUstawEdytowalnosc(slowka.getCzyJestPlik());
		}
	}
	
	public void otworzPlik() {
		boolean czyOtwarty=slowka.otworzPlik();
		if(!slowka.getCzyJestPlik()) {
			ustawSlowa();
			ustawPostep();
			otworzDobryPlik();
		} else if (czyOtwarty && slowka.getCzyJestPlik()){
			lista.setModel(slowka.listaSlowek());
			resetuj();
		}
		ustawEdytowalnosc(slowka.getCzySaSlowka());
		modUstawEdytowalnosc(slowka.getCzyJestPlik());
	}
	
	public void resetuj() {
		slowka.reset();
		transTresc.setText("");
		ustawWszystko();
	}
	
	public void ustawWszystko() {
		ustawSlowa();
		ustawPostep();
		ustawEdytowalnosc(true);
		wiadomosc.setText("");
		modWiadomosc.setText("");
	}
	
	public void otworzDobryPlik() {
		wiadomosc.setForeground(kolorZle);
		wiadomosc.setText("Otw\u00F3rz odpowiedni plik lub stw\u00F3rz nowy!");
		modWiadomosc.setForeground(kolorZle);
		modWiadomosc.setText("Otw\u00F3rz odpowiedni plik lub stw\u00F3rz nowy!");
	}
	
	public void ustawSlowa() {
		slowoTresc.setText(slowka.getNatyw());
		odpPoprawna=slowka.getObcy();
	}
	
	public void ustawPostep() {
		//skutecznoúÊ
		skutecznoscTresc.setText(dfFloat.format(slowka.getSkutecznosc())+"%");
		//pasek postÍpu
		postepPasek.setValue(slowka.getPostep());
	}
	
	public void ustawEdytowalnosc(boolean bool) {
		transTresc.setEditable(bool);
		sprawdz.setEnabled(bool);
		//if(bool) {transTresc.setDropMode(DropMode.INSERT);}
	}
	
	public void modUstawEdytowalnosc(boolean bool) {
		slowoPole.setEditable(bool);
		transPole.setEditable(bool);
		btnDodaj.setEnabled(bool);
		btnUsun.setEnabled(bool);
	}
	
	public void ustawDodawanie(boolean bool) {
		slowoPole.setEnabled(bool);
		transPole.setEnabled(bool);
	}
	
	public void ustawKolorWyniku(Color kolor) {
		transTresc.setForeground(kolor);
		wiadomosc.setForeground(kolor);
	}

	public void poprawnaOdpowiedzWyglad() {
		ustawKolorWyniku(kolorInfo);
		transTresc.setText(odpPoprawna);
		odpowiedz.setText("Twoja odpowied\u017A");
		wiadomosc.setText("Poprawnie");
	}
	
	public void zmianaTrybu(boolean bool, String wiadomosc) {
		nrOpcji=JOptionPane.showOptionDialog(null, "Chcesz zmieniÊ tryb translacji na '"+wiadomosc+"'\nSpowoduje reset statystyk.\nCzy na pewno chcesz kontynuowaÊ?", 
				"Pytanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcjeTN, opcjeTN[1]);
		if(nrOpcji==0) {
			trybNatywnyObcy.setEnabled(bool);
			trybObcyNatywny.setEnabled(!bool);
			slowka.zamianaMiejsc();
			ustawSlowa();
			ustawPostep();
			trybOpis.setText(wiadomosc);
			JOptionPane.showMessageDialog(null, "Obecny tryb translacji\n'"+wiadomosc+"'", "Info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void wyczyscPola() {
		slowoPole.setText("");
		transPole.setText("");
	}
	
	public void wyborTrybu(boolean bool) {
		//false - Modyfikacja
		//true - Test
		
		modyfikacjaPrzycisk.setEnabled(bool);
		testPrzycisk.setEnabled(!bool);
		modPanel.setVisible(!bool);
		testPanel.setVisible(bool);
		mnOpcje.setEnabled(bool);
	}
}
