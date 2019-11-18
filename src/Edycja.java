import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Edycja extends JDialog {	
	private JTextField obcyPole;
	private JTextField natywPole;
	private JLabel natywOpis;
	private JLabel obcyOpis;
	private JPanel panel;
	private JButton btnPotwierdz;
	private JButton btnAnuluj;
	
	//swoje zmienne
	private Slowka slowka;
	private boolean czyPotwierdzonie;
	private boolean czyWalidacjaUdana;
	private String natywTemp;
	private String obcyTemp;
	//kolory
	private Color tlo;
	private Color napis;
	
	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	
	//Dodaj
	public Edycja(Slowka slowka, Color tlo, Color napis) {
		initComp();
		
		czyPotwierdzonie = false;
		natywTemp = "";
		obcyTemp = "";
		
		this.slowka = slowka;
		this.tlo = tlo;
		this.napis = napis;
		
		wygladInterfejsu();
		setTitle("Dodaj");
		
		initFinal();
	}
	//Zmieñ
	public Edycja(Slowka slowka, String natyw, String obcy, Color tlo, Color napis) {
		initComp();
		
		czyPotwierdzonie = false;
		natywTemp = natyw;
		obcyTemp = obcy;
		
		this.slowka = slowka;
		this.tlo = tlo;
		this.napis = napis;
		
		wygladInterfejsu();
		setTitle("Zmieñ");
		btnPotwierdz.setText("Zmieñ");
		
		natywPole.setText(natyw);
		obcyPole.setText(obcy);
		
		initFinal();
	}
	
	public void initComp() {
		int frameWidth=1000;
		int frameHeight=150;
		int sysWidth=Toolkit.getDefaultToolkit().getScreenSize().width;
		int sysHeight=Toolkit.getDefaultToolkit().getScreenSize().height;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds((sysWidth-frameWidth)/2, (sysHeight-frameHeight)/2, frameWidth, frameHeight);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		
		panel.setBounds(0, 0, 994, 121);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		natywOpis = new JLabel("S\u0142owo");
		natywOpis.setFont(new Font("Arial", Font.BOLD, 20));
		natywOpis.setBounds(10, 11, 120, 25);
		panel.add(natywOpis);
		
		obcyOpis = new JLabel("T\u0142umaczenie");
		obcyOpis.setFont(new Font("Arial", Font.BOLD, 20));
		obcyOpis.setBounds(10, 47, 120, 25);
		panel.add(obcyOpis);
		
		natywPole = new JTextField();
		natywPole.setOpaque(false);
		natywPole.setFont(new Font("Arial", Font.PLAIN, 20));
		natywPole.setColumns(10);
		natywPole.setBounds(140, 10, 844, 25);
		panel.add(natywPole);
		
		obcyPole = new JTextField();
		obcyPole.setOpaque(false);
		obcyPole.setFont(new Font("Arial", Font.PLAIN, 20));
		obcyPole.setColumns(10);
		obcyPole.setBounds(140, 46, 844, 25);
		panel.add(obcyPole);
		
		btnPotwierdz = new JButton("Dodaj");
		btnPotwierdz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String natyw = natywPole.getText();
				String obcy = obcyPole.getText();
				czyWalidacjaUdana = true;
				//czy pola s¹ puste?
				if(natyw.equals("") || obcy.equals("")) {
					czyWalidacjaUdana = false;
					JOptionPane.showMessageDialog(null, "Jedno z pól jest puste!", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
				//czy zawiera œrednik? (wa¿ne przy zapisywaniu do pliku!)
				if(natyw.contains(";") || obcy.contains(";")) {
					czyWalidacjaUdana = false;
					JOptionPane.showMessageDialog(null, "S³owo i t³umaczenie nie mog¹ zawieraæ œrednika!", "Info", JOptionPane.INFORMATION_MESSAGE);	
				}
				//czy za d³ugie s³owa?
				else if(natyw.length() > 50 || obcy.length() > 50) {
					czyWalidacjaUdana = false;
					JOptionPane.showMessageDialog(null, "S³owo lub t³umaczenie jest za d³ugie!", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
				//Czy rekord ju¿ istnieje?
				else if(slowka.czyRekordTakiSam(natyw, obcy) && !natywTemp.equals(natyw) && !obcyTemp.equals(obcy)) {
					czyWalidacjaUdana = false;
					JOptionPane.showMessageDialog(null, "Taki sam rekord ju¿ istnieje!", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
				//czy rekord jest podobny do istniej¹cego (w wyj¹tkiem modyfikowanych czêœci)
				else if(slowka.czyRekordPodobny(natyw, obcy) && !natywTemp.equals(natyw) && !obcyTemp.equals(obcy)) {
					Object nazwyOpcji[] = {"Tak", "Nie"};
					int opcja = JOptionPane.showOptionDialog(null, "S³owo lub t³umaczenie siê powtarza.\nCzy na pewno dodaæ?", "Info", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, nazwyOpcji, nazwyOpcji[1]);
					//czy dodaæ tak czy inaczej
					if(opcja == 1) {
						czyWalidacjaUdana = false;
					}
				}
				
				//walidacja udana
				if(czyWalidacjaUdana) {
					czyPotwierdzonie = true;
					dispose();
				}
			}
		});
		btnPotwierdz.setOpaque(false);
		btnPotwierdz.setFont(new Font("Arial", Font.BOLD, 20));
		btnPotwierdz.setBounds(764, 85, 105, 25);
		panel.add(btnPotwierdz);
		
		btnAnuluj = new JButton("Anuluj");
		btnAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				czyPotwierdzonie = false;
				dispose();
			}
		});
		btnAnuluj.setOpaque(false);
		btnAnuluj.setFont(new Font("Arial", Font.BOLD, 20));
		btnAnuluj.setBounds(879, 85, 105, 25);
		panel.add(btnAnuluj);
	}
	
	public String getNatyw() {
		return natywPole.getText();
	}
	
	public String getObcy() {
		return obcyPole.getText();
	}
	
	public boolean getCzyPotwierdzone() {
		return czyPotwierdzonie;
	}
	
	//koniec inicjowania okienka
	public void initFinal() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
	}
	
	public void wygladInterfejsu() {
		natywOpis.setForeground(napis);
		obcyOpis.setForeground(napis);
	}
}
