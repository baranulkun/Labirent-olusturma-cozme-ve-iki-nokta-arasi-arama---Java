
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class ekran extends JFrame {

	robotIslemleri islemler = new robotIslemleri();
	static int boyut1;
	static int boyut2;
	final static int engel1 = 1;
	final static int C = 0;
	final static int girisRng = 6;
	final static int cikisRng = 8;
	final static int yol = 9;
	final static int engel2 = 2;
	final static int engel3 = 3;
	final static int kYol = 5;
	final static int geriYol = 4;
	static int aKontrol = 0;
	static int topYolSay;
	static int kisaYolSay;
	static long endTime;
	static long startTime;
	static boolean hizliBtr = true;
	static int hzlbtr = 0;
	static float speed;
	private Graphics2D g;
	boolean kont = false;

	static int start1 = 0, start2 = 0, end1 = 0, end2 = 0;
	static int[] baslangic = new int[2];
	static int[] bitis = new int[2];

	static boolean isLab = true;
	static String path;
	static String path1 = "http://bilgisayar.kocaeli.edu.tr/prolab2/url1.txt";
	static String path2 = "http://bilgisayar.kocaeli.edu.tr/prolab2/url2.txt";

	static int START_I = 1, START_J = 1;
	static int END_I, END_J;
	static int[][] labirentMtrs = new int[boyut1][boyut2];

	//labirent sisteminde giriþ ve çýkýþ noktalarýnýn atanmasý ve static deðiþken olan labirentMtrs
	//deðiþkenine atanma iþleminin yapýlmasý
	public void restorasyon(int[][] yeniLab) {
		labirentMtrs = yeniLab;
		labirentMtrs[1][1] = girisRng;
		labirentMtrs[boyut1 - 2][boyut2 - 2] = cikisRng;
	}

	//labirentin çizilmesi
	public int[][] labirentGuncelleme() {
		labirentMtrs = new int[boyut1][boyut2];
		int i, j;
		int boslukSayýsý = 0;
		int duvarSayisi = 0;

		int flag = (boyut1 * boyut2) / 2;
		int[] duvarSutunu = new int[flag];
		int[] duvarSirasi = new int[flag];
		for (i = 0; i < boyut1; i++) // her yerin duvar yapýlmasý
			for (j = 0; j < boyut2; j++)
				labirentMtrs[i][j] = 1;
		for (i = 1; i < boyut1 - 1; i += 2)
			for (j = 1; j < boyut2 - 1; j += 2, boslukSayýsý++) {
				labirentMtrs[i][j] = -boslukSayýsý; // iki kutucuktan birinin yol (1) yapýlmasý
				if (i < boyut1 - 2) {//duvarlarýn kordinatlarýnýn tutulmasý
					duvarSirasi[duvarSayisi] = i + 1;
					duvarSutunu[duvarSayisi] = j;
					duvarSayisi++;
					
				}
				if (j < boyut2 - 2) {//duvarlarýn kordinatlarýnýn tutulmasý
					duvarSirasi[duvarSayisi] = i;
					duvarSutunu[duvarSayisi] = j + 1;
					duvarSayisi++;
				}
			}
	
		
		int rastGele;
		for (i = duvarSayisi - 1; i > 0; i--) {
			rastGele = (int) (Math.random() * i);
			System.out.println("duvarSirasi["+rastGele+"] : "+duvarSirasi[rastGele]);
			System.out.println("duvarSutunu["+rastGele+"] : "+duvarSutunu[rastGele]);
			sokme(duvarSirasi[rastGele], duvarSutunu[rastGele]);
			duvarSirasi[rastGele] = duvarSirasi[i]; //boþalan yere en üstten deðer atama iþlemi bu sayede kullanýlan
			duvarSutunu[rastGele] = duvarSutunu[i];	//nokta silinirken o noktanýn yerine kullanýlmamýþ en üst deðer atanacak
		}
		
		//yol olan noktalara 0 deðerinin atanmasý
		for (i = 1; i < boyut1 - 1; i++)
			for (j = 1; j < boyut2 - 1; j++)
				if (labirentMtrs[i][j] < 0)
					labirentMtrs[i][j] = 0;
		return labirentMtrs;
	}

	//aralarýndaki duvar kalkan noktalarýn ayný deðere sahip olmasý için atama iþlemi
	void doldurma(int satir, int sutun, int replace, int replaceWith) {
		if (labirentMtrs[satir][sutun] == replace) {
			System.out.println("Güncellendi");
			labirentMtrs[satir][sutun] = replaceWith;
			doldurma(satir + 1, sutun, replace, replaceWith);
			doldurma(satir - 1, sutun, replace, replaceWith);
			doldurma(satir, sutun + 1, replace, replaceWith);
			doldurma(satir, sutun - 1, replace, replaceWith);
		}
	}
	
	//noktanýn adresine göre dikey veya yatay kontolle duvarýn kaldýrýlma iþlemi
	public void sokme(int satir, int sutun) {
		if (satir % 2 == 1 && labirentMtrs[satir][sutun - 1] != labirentMtrs[satir][sutun + 1]) {
			System.out.println("girilen nokta : "+satir+" "+sutun);
			doldurma(satir, sutun - 1, labirentMtrs[satir][sutun - 1], labirentMtrs[satir][sutun + 1]);
			labirentMtrs[satir][sutun] = labirentMtrs[satir][sutun + 1];

		} else if (satir % 2 == 0 && labirentMtrs[satir - 1][sutun] != labirentMtrs[satir + 1][sutun]) {
			System.out.println("girilen nokta : "+satir+" "+sutun);
			doldurma(satir - 1, sutun, labirentMtrs[satir - 1][sutun], labirentMtrs[satir + 1][sutun]);
			labirentMtrs[satir][sutun] = labirentMtrs[satir + 1][sutun];
		}
	}
	
	//matris üzerindeki noktanýn deðiþtirilmesi ve çiziminin yapýlmasý
	public void renkNumarasiAtama(MazePos pos, int hedef) {
		labirentMtrs[pos.i()][pos.j] = hedef;
		topYolSay++;
		if (hizliBtr && labirentMtrs[pos.i()][pos.j] != girisRng) {
			Graphics ggg = this.getGraphics();
			paint(ggg, pos.i, pos.j);
		}
	}
	
	//mevcut noktanýn sonuç olup olmadýðýnýn kontrolü
	public boolean sonucBulma(MazePos pos) {
		return (pos.i() == END_I - 1 && pos.j() == END_J - 1) ? true : false;
	}

	//sýradaki noktanýn gidilebilir bir yol olup olmadýðýnýn kontolü
	public boolean boyaKontrolu(MazePos pos) {
		return (labirentMtrs[pos.i()][pos.j] != engel1 && labirentMtrs[pos.i()][pos.j] != engel2
				&& labirentMtrs[pos.i()][pos.j] != engel3 && labirentMtrs[pos.i()][pos.j] != yol
				&& labirentMtrs[pos.i()][pos.j] != geriYol && labirentMtrs[pos.i()][pos.j] != 6);
	}

	//görselleþme olabilsin diye yavaþlatma iþlemi
	public void pause() {

		long last = System.currentTimeMillis();
		long now = 0;

		do {
			now = System.currentTimeMillis();
		} while (now - last < speed);
	}

	static Stack<MazePos> stack;
	static MazePos firstNode;
	static MazePos next;
	static Stack<MazePos> kisYol;

	//DFS aramasý için atama iþlemi
	public void degerAta() {
		stack = new Stack<MazePos>();
		kisYol = new Stack<MazePos>();
		stack.push(new MazePos(START_I, START_J));
		kisYol.push(new MazePos(START_I, START_J));
	}
	
	//DFS ile arama iþlemi
	public void run() {
		
		while (!stack.empty()) {
			firstNode = stack.pop();
			Boolean kontrBoolean = true;
			renkNumarasiAtama(kisYol.peek(), yol);
			next = firstNode.kuzey();
			
			//kuzey noktasýnýn kontrolü
			if (boyaKontrolu(next)) {
				kontrBoolean = false;
				kisYol.push(next);
				stack.push(next);
				System.out.println("kuzey stack'e eklendi : " + next.i + " " + next.j);
				if (hizliBtr == true)
					pause();
				if (sonucBulma(kisYol.peek())) {
					System.out.println("end: " + END_I + "   " + END_J);
					labirentMtrs[END_I - 1][END_J - 1] = cikisRng;
					break;
				}
			}
			next = firstNode.dogu();
			//doðu noktasýnýn kontrolü
			if (boyaKontrolu(next)) {
				kontrBoolean = false;
				kisYol.push(next);
				stack.push(next);
				System.out.println("doðu stack'e eklendi : " + next.i + " " + next.j);
				if (hizliBtr == true)
					pause();
				if (sonucBulma(kisYol.peek())) {
					System.out.println("end: " + END_I + "   " + END_J);
					labirentMtrs[END_I - 1][END_J - 1] = cikisRng;
					break;
				}
			}
			next = firstNode.bati();
			//batý noktasýnýn kontrolü
			if (boyaKontrolu(next)) {
				kontrBoolean = false;
				kisYol.push(next);
				stack.push(next);
				System.out.println("batý stack'e eklendi : " + next.i + " " + next.j);
				if (hizliBtr == true)
					pause();
				if (sonucBulma(kisYol.peek())) {
					System.out.println("end: " + END_I + "   " + END_J);
					labirentMtrs[END_I - 1][END_J - 1] = cikisRng;
					break;
				}
			}
			next = firstNode.guney();
			//guney noktasýnýn kontrolü
			if (boyaKontrolu(next)) {
				kontrBoolean = false;
				kisYol.push(next);
				stack.push(next);
				System.out.println("güney stack'e eklendi : " + next.i + " " + next.j);
				if (hizliBtr == true)
					pause();
				if (sonucBulma(kisYol.peek())) {
					System.out.println("end: " + END_I + "   " + END_J);
					labirentMtrs[END_I - 1][END_J - 1] = cikisRng;
					break;
				}
			}
			//iki stack arasýnda fark oluþma durumunda (haritada çýkmaz noktaya girilmesi)
			//çizim yapan stack elemanýnýn çözüm yapan stack elemanýna eþitlenmesi
			
			if (kontrBoolean == true) {
				while (!(kisYol.lastElement().i == stack.lastElement().i
						&& kisYol.lastElement().j == stack.lastElement().j)) {
					System.out.println(kisYol.lastElement().i + " " + kisYol.lastElement().j + " çýkarýldý");
					if (hizliBtr == true)
						pause();
					renkNumarasiAtama(kisYol.peek(), geriYol);
					kisYol.pop();
				}
			}
		}
		labirentMtrs[kisYol.firstElement().i][kisYol.firstElement().j]=6;
		kisYol.remove(kisYol.indexOf(kisYol.firstElement()));
		//kýsa yol çiziminde stack'e eklenip de kullanýlmamýþ elemanlarýn silinmesi
		for (int i = 0; i < stack.search(stack.firstElement()); i++) {
			for (int z = 0; z < kisYol.search(kisYol.firstElement()); z++) {
				if (stack.get(i).i == kisYol.get(z).i && stack.get(i).j == kisYol.get(z).j) {
					System.out
							.println("Fazlalýk eleman stack'den çýkarýldý : " + stack.get(i).i + " " + stack.get(i).j);
					kisYol.remove(z);
				}
			}
		}
	}

	//menü ve menü iþlemleri
	public ekran() {
		JPanel contentPane;
		Image labirentImage = new ImageIcon(this.getClass().getResource("labirent.jpg")).getImage();
		Image robotImage = new ImageIcon(this.getClass().getResource("gezginRobot.png")).getImage();
		JTextField textField;

		URL urlIcon = getClass().getResource("flat-theme-action-maze-icon.png");
		ImageIcon image = new ImageIcon(urlIcon);
		setIconImage(image.getImage());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 761, 511);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(new Color(255, 255, 255));
		contentPane.add(menuPanel, "name_364384852246800");

		JLabel secim = new JLabel("Oyun Seçimi");
		secim.setHorizontalAlignment(SwingConstants.CENTER);
		secim.setFont(new Font("Wide Latin", Font.PLAIN, 25));

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		GroupLayout gl_menuPanel = new GroupLayout(menuPanel);
		gl_menuPanel.setHorizontalGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_menuPanel.createSequentialGroup()
						.addGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_menuPanel.createSequentialGroup().addGap(195)
										.addComponent(secim, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE).addGap(52))
								.addGroup(gl_menuPanel.createSequentialGroup().addGap(159).addComponent(panel,
										GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)))
						.addGap(151)));
		gl_menuPanel.setVerticalGroup(gl_menuPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_menuPanel.createSequentialGroup().addContainerGap()
						.addComponent(secim, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE).addGap(36)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE).addGap(138)));
		JButton hizliBitir = new JButton("Hýzlý Bitir");
		JLabel zaman = new JLabel("");
		JLabel toplamYolMesafesi = new JLabel("");
		
		//çözüm sýrasýnda bekleme süresinin olup olmamasýna karar verilmesi
		hizliBitir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (hzlbtr % 2 == 0) {
					hizliBitir.setText("Yavaþ Bitir");
					hizliBtr = false;
				} else {
					hizliBitir.setText("Hizli Bitir");
					hizliBtr = true;
				}
				hzlbtr++;
			}
		});
		hizliBitir.setEnabled(false);
		JPanel boyutSorusu = new JPanel();
		boyutSorusu.setVisible(false);
		JLabel labirentIcon = new JLabel("");
		labirentIcon.setHorizontalAlignment(SwingConstants.CENTER);
		labirentIcon.setIcon(new ImageIcon(labirentImage));
		JLabel robotIcon = new JLabel("");
		robotIcon.setIcon(new ImageIcon(robotImage));
		robotIcon.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel labirentButon = new JLabel("Labirent");
		labirentButon.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		
		//labirent yazýsýný üzerindeki iþlemler
		labirentButon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				labirentButon.setFont(new Font("Times New Roman", Font.PLAIN, 18));
				labirentButon.setForeground(Color.orange);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				labirentButon.setFont(new Font("Times New Roman", Font.PLAIN, 14));
				labirentButon.setForeground(Color.black);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				menuPanel.setVisible(false);
				boyutSorusu.setVisible(true);
			}
		});
		labirentButon.setHorizontalAlignment(SwingConstants.CENTER);
		JPanel urlSoru = new JPanel();
		JLabel robotButon = new JLabel("Gezgin Robot");
		robotButon.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		
		//gezgin robot yazýsý üzerindeki iþlemler
		robotButon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				robotButon.setFont(new Font("Times New Roman", Font.PLAIN, 18));
				robotButon.setForeground(Color.orange);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				robotButon.setFont(new Font("Times New Roman", Font.PLAIN, 14));
				robotButon.setForeground(Color.black);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				menuPanel.setVisible(false);
				urlSoru.setVisible(true);
			}
		});
		robotButon.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(29)
						.addComponent(labirentIcon, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE).addGap(70)
						.addComponent(robotIcon, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE).addGap(37))
				.addGroup(gl_panel.createSequentialGroup().addGap(48)
						.addComponent(labirentButon, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE).addGap(115)
						.addComponent(robotButon, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE).addGap(51)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel
						.createParallelGroup(Alignment.BASELINE).addComponent(robotIcon).addComponent(labirentIcon))
				.addGap(18)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(robotButon, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(labirentButon, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(23, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		menuPanel.setLayout(gl_menuPanel);

		boyutSorusu.setBackground(new Color(255, 255, 255));
		contentPane.add(boyutSorusu, "name_365804907248600");
		JLabel bytSoru = new JLabel("Labirent Boyutunu giriniz :  ");
		bytSoru.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		JPanel labirentPanel = new JPanel();
		textField = new JTextField();
		
		//labirent boyutlarýnýn alýnmasý ve kontolü
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean byt1Kontrol = true;
				boolean byt2Kontrol = true;
				boolean girisSekli = true;
				if (textField.getText().length() != 5 || textField.getText().charAt(2) != '-'
						|| textField.getText().charAt(0) > 57 || textField.getText().charAt(0) < 48
						|| textField.getText().charAt(1) > 57 || textField.getText().charAt(1) < 48
						|| textField.getText().charAt(3) > 57 || textField.getText().charAt(3) < 48
						|| textField.getText().charAt(4) > 57 || textField.getText().charAt(4) < 48) {
					girisSekli = false;
					JOptionPane.showMessageDialog(null, "Örnek giriþ þekli: 64-64");

				} else {
					girisSekli = true;
				}
				if (girisSekli == true) {
					if (Integer.parseInt(textField.getText().substring(textField.getText().indexOf("-") + 1))
							% 2 == 0) {
						byt2Kontrol = false;
					}
					if (Integer.parseInt(textField.getText().substring(0, textField.getText().indexOf("-"))) % 2 == 0) {
						byt1Kontrol = false;
					}
					boyut1 = Integer.parseInt(textField.getText().substring(0, textField.getText().indexOf("-")));
					boyut2 = Integer.parseInt(textField.getText().substring(textField.getText().indexOf("-") + 1));
					if (byt1Kontrol == false)
						boyut1++;
					if (byt2Kontrol == false)
						boyut2++;
					if (byt1Kontrol)
						speed = 350 / boyut1;
					END_I = boyut1 - 1;
					END_J = boyut2 - 1;
					int[][] copy = new int[boyut1][boyut2];
					labirentMtrs = copy;
					labirentPanel.setVisible(true);
					boyutSorusu.setVisible(false);
				}
			}
		});
		textField.setColumns(10);
		GroupLayout gl_boyutSorusu = new GroupLayout(boyutSorusu);
		gl_boyutSorusu.setHorizontalGroup(gl_boyutSorusu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_boyutSorusu.createSequentialGroup().addGap(115)
						.addComponent(bytSoru, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE).addGap(65)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE).addGap(228)));
		gl_boyutSorusu.setVerticalGroup(gl_boyutSorusu.createParallelGroup(Alignment.LEADING).addGroup(gl_boyutSorusu
				.createSequentialGroup().addGap(225)
				.addGroup(gl_boyutSorusu.createParallelGroup(Alignment.BASELINE)
						.addComponent(bytSoru, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
						.addGroup(gl_boyutSorusu.createSequentialGroup().addGap(2).addComponent(textField,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addGap(218)));
		boyutSorusu.setLayout(gl_boyutSorusu);

		labirentPanel.setBackground(new Color(255, 255, 255));
		contentPane.add(labirentPanel, "name_367237835366700");

		JPanel labirent = new JPanel();
		labirent.setBackground(new Color(255, 255, 255));

		JPanel labirentMenu = new JPanel();
		labirentMenu.setBackground(new Color(255, 255, 255));

		GroupLayout gl_labirentPanel = new GroupLayout(labirentPanel);
		gl_labirentPanel
				.setHorizontalGroup(
						gl_labirentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_labirentPanel.createSequentialGroup().addContainerGap()
										.addComponent(labirent, GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(labirentMenu,
												GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));
		gl_labirentPanel.setVerticalGroup(gl_labirentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_labirentPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_labirentPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(labirentMenu, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 433,
										Short.MAX_VALUE)
								.addComponent(labirent, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 433,
										Short.MAX_VALUE))
						.addGap(21)));

		JButton labirentGuncelle = new JButton("Labirent Güncelle");
		JLabel kisaYolMesafesi = new JLabel("New label");

		zaman.setForeground(new Color(128, 128, 128));
		labirentGuncelle.setBackground(new Color(255, 255, 255));
		labirentGuncelle.setForeground(new Color(0, 0, 0));
		labirentGuncelle.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		
		//labirentin güncellenmesi
		labirentGuncelle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aKontrol=0;
				if (hizliBtr == true)
					speed = 350 / boyut1;
				repaint();
				zaman.setText("");
				kisaYolMesafesi.setText("");
				toplamYolMesafesi.setText("");
				int x[][] = labirentGuncelleme();
				restorasyon(x);
			}
		});

		JButton anaMenuDon = new JButton("Oyundan Çýk");
		anaMenuDon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				labirentPanel.setVisible(false);
				menuPanel.setVisible(true);
			}
		});

		anaMenuDon.setForeground(Color.BLACK);
		anaMenuDon.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		anaMenuDon.setBackground(Color.WHITE);
		zaman.setFont(new Font("Times New Roman", Font.PLAIN, 8));
		JButton labirentCoz = new JButton("Labirenti Çöz");
		
		//labirentin çözümünün yapýlmasý ve geçen süre, adým sayýsý gibi deðiþkenlerin ayarlanmasý
		//çözülen labirentin matris deerlerinin txt dosyasýna kaydedilmesi
		labirentCoz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isLab = true;
				degerAta();
				topYolSay = 0;
				startTime = System.nanoTime();
				restorasyon(labirentMtrs);
				run();
				if (hizliBtr == false)
					repaint();
				endTime = System.nanoTime();
				long estimatedTime = endTime - startTime;
				double seconds = (double) estimatedTime / 1000000000;
				zaman.setText("Çözüm süresi: " + Double.toString(seconds));
				System.out.println(topYolSay);
				toplamYolMesafesi.setText("Toplam yol: " + Integer.toString(topYolSay));
				File file = new File("labirentOut.txt");
				FileWriter fW = null;
				try {
					fW = new FileWriter(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				PrintWriter pw = new PrintWriter(fW);

				for (int i = 0; i < labirentMtrs.length; i++) {
					for (int j = 0; j < labirentMtrs[i].length; j++)
						pw.print(labirentMtrs[i][j]);
					pw.println();
				}
				pw.close();

			}
		});
		JButton kisaYolGoster = new JButton("Kýsa Yolu Göster");
		labirentCoz.setForeground(Color.BLACK);
		labirentCoz.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		labirentCoz.setBackground(Color.WHITE);

		kisaYolGoster.setEnabled(false);
		labirentCoz.setEnabled(false);
		labirentGuncelle.setEnabled(false);

		JButton labirentOlustur = new JButton("Labirent Oluþtur");
		//labirentin oluþturulmasý
		labirentOlustur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hizliBitir.setEnabled(true);
				labirentCoz.setEnabled(true);
				labirentGuncelle.setEnabled(true);
				kisaYolGoster.setEnabled(true);
				if (kont == false) {
					int x[][] = labirentGuncelleme();
					restorasyon(x);
					repaint();
					kont = true;
				}
			}
		});

		kisaYolMesafesi.setForeground(new Color(128, 128, 128));
		kisaYolMesafesi.setText("");
		labirentOlustur.setForeground(Color.BLACK);
		labirentOlustur.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		labirentOlustur.setBackground(Color.WHITE);
		//labirentin kýsa yolunun çizilmesi
		kisaYolGoster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kisaYolSay = 0;
				while (!kisYol.empty()) {
					kisaYolSay++;
					System.out.println(kisYol.lastElement().i + " " + kisYol.lastElement().j);
					renkNumarasiAtama(kisYol.pop(), kYol);
				}
				if (hizliBtr == false)
					repaint();
				System.out.println(kisaYolSay);
				kisaYolMesafesi.setText("Adým Sayýsý k.y: " + Integer.toString(kisaYolSay));
			}
		});
		kisaYolGoster.setForeground(Color.BLACK);
		kisaYolGoster.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		kisaYolGoster.setEnabled(false);
		kisaYolGoster.setBackground(Color.WHITE);

		zaman.setHorizontalAlignment(SwingConstants.LEFT);

		kisaYolMesafesi.setHorizontalAlignment(SwingConstants.LEFT);
		kisaYolMesafesi.setFont(new Font("Times New Roman", Font.PLAIN, 8));

		toplamYolMesafesi.setHorizontalAlignment(SwingConstants.LEFT);
		toplamYolMesafesi.setForeground(Color.GRAY);
		toplamYolMesafesi.setFont(new Font("Times New Roman", Font.PLAIN, 8));

		hizliBitir.setForeground(Color.BLACK);
		hizliBitir.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		hizliBitir.setBackground(Color.WHITE);
		GroupLayout gl_labirentMenu = new GroupLayout(labirentMenu);
		gl_labirentMenu.setHorizontalGroup(gl_labirentMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_labirentMenu.createSequentialGroup().addGap(93)
						.addComponent(kisaYolMesafesi, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
						.addContainerGap())
				.addGroup(
						gl_labirentMenu.createSequentialGroup().addContainerGap(93, Short.MAX_VALUE)
								.addComponent(
										toplamYolMesafesi, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
				.addGroup(gl_labirentMenu.createSequentialGroup().addGap(43).addGroup(gl_labirentMenu
						.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_labirentMenu.createSequentialGroup().addGap(50)
								.addComponent(zaman, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE).addContainerGap())
						.addGroup(Alignment.TRAILING, gl_labirentMenu.createSequentialGroup()
								.addGroup(gl_labirentMenu.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(anaMenuDon, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(hizliBitir, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(labirentCoz, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(labirentOlustur, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
										.addComponent(labirentGuncelle, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												147, Short.MAX_VALUE)
										.addComponent(kisaYolGoster, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 147,
												Short.MAX_VALUE))
								.addGap(38)))));
		gl_labirentMenu.setVerticalGroup(gl_labirentMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_labirentMenu.createSequentialGroup().addGap(31)
						.addComponent(labirentOlustur, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(labirentCoz, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(labirentGuncelle)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(kisaYolGoster, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(hizliBitir, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(anaMenuDon, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 168, Short.MAX_VALUE).addComponent(zaman)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(kisaYolMesafesi)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(toplamYolMesafesi).addGap(46)));
		labirentMenu.setLayout(gl_labirentMenu);
		labirentPanel.setLayout(gl_labirentPanel);

		JPanel robotPanel = new JPanel();
		robotPanel.setBackground(new Color(255, 255, 255));
		urlSoru.setBackground(new Color(255, 255, 255));
		contentPane.add(urlSoru, "name_287265087785100");

		JLabel urlSoruTxt1 = new JLabel("Url-1");
		//url1 yazýsýnýn üzerindeki iþlemler ve dosya yolunun atanmasý
		urlSoruTxt1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				urlSoruTxt1.setFont(new Font("Times New Roman", Font.PLAIN, 18));
				urlSoruTxt1.setForeground(Color.orange);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				urlSoruTxt1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
				urlSoruTxt1.setForeground(Color.black);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				isLab = false;
				path = path1;
				urlSoru.setVisible(false);
				robotPanel.setVisible(true);
				try {
					labirentMtrs = islemler.txtRead(path);
					boyut1 = labirentMtrs.length;
					boyut2 = labirentMtrs[0].length;
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
			}
		});
		urlSoruTxt1.setHorizontalAlignment(SwingConstants.CENTER);
		urlSoruTxt1.setFont(new Font("Times New Roman", Font.PLAIN, 14));

		JLabel urlSoruTxt2 = new JLabel("Url-2");
		//url2 yazýsýnýn üzerindeki iþlemler ve dosya yolunun atanmasý
		urlSoruTxt2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				urlSoruTxt2.setFont(new Font("Times New Roman", Font.PLAIN, 18));
				urlSoruTxt2.setForeground(Color.orange);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				urlSoruTxt2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
				urlSoruTxt2.setForeground(Color.black);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				isLab = false;
				path = path2;
				urlSoru.setVisible(false);
				robotPanel.setVisible(true);
				try {
					labirentMtrs = islemler.txtRead(path);
					boyut1 = labirentMtrs.length;
					boyut2 = labirentMtrs[0].length;
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
			}
		});
		urlSoruTxt2.setHorizontalAlignment(SwingConstants.CENTER);
		urlSoruTxt2.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		GroupLayout gl_urlSoru = new GroupLayout(urlSoru);
		gl_urlSoru.setHorizontalGroup(gl_urlSoru.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_urlSoru.createSequentialGroup().addGap(154)
						.addComponent(urlSoruTxt1, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE).addGap(93)
						.addComponent(urlSoruTxt2, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE).addGap(178)));
		gl_urlSoru.setVerticalGroup(gl_urlSoru.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_urlSoru.createSequentialGroup().addGap(233)
						.addGroup(gl_urlSoru.createParallelGroup(Alignment.BASELINE)
								.addComponent(urlSoruTxt2, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
								.addComponent(urlSoruTxt1, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
						.addGap(192)));
		urlSoru.setLayout(gl_urlSoru);

		contentPane.add(robotPanel, "name_287684479927100");

		JPanel robot = new JPanel();
		robot.setBackground(new Color(255, 255, 255));

		JPanel robotMenu = new JPanel();
		robotMenu.setBackground(new Color(255, 255, 255));
		GroupLayout gl_robotPanel = new GroupLayout(robotPanel);
		gl_robotPanel
				.setHorizontalGroup(
						gl_robotPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_robotPanel.createSequentialGroup().addContainerGap()
										.addComponent(robot, GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(robotMenu,
												GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));
		gl_robotPanel.setVerticalGroup(gl_robotPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_robotPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_robotPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(robotMenu, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 444,
										Short.MAX_VALUE)
								.addComponent(robot, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE))
						.addContainerGap()));
		JButton robotCoz = new JButton("Çöz");
		JButton kisaYolRobot = new JButton("Kýsa yolu Göster");
		JButton konumYenile = new JButton("Labirenti Güncelle");
		kisaYolRobot.setEnabled(false);
		
		//haritanýn baþtan oluþturulmasý
		konumYenile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kisaYolRobot.setEnabled(false);
				if (hizliBtr == true)
					speed = 350 / boyut1;
				aKontrol = 0;
				robotCoz.setEnabled(true);
				try {
					labirentMtrs = islemler.txtRead(path);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				islemler.girCýkAtama(labirentMtrs);
				for (int i = 0; i < boyut1; i++) {
					for (int j = 0; j < boyut2; j++) {
						if (labirentMtrs[i][j] == cikisRng) {
							END_I = i + 1;
							END_J = j + 1;
						}
						if (labirentMtrs[i][j] == girisRng) {
							START_I = i;
							START_J = j;
						}
					}
				}
				for (int i = 0; i < labirentMtrs.length; i++) {
					for (int j = 0; j < labirentMtrs[i].length; j++) {
						if (labirentMtrs[i][j] == 6) {
							start1 = i;
							start2 = j;
						}
						if (labirentMtrs[i][j] == 8) {
							end1 = i;
							end2 = j;
						}

					}
				}
				System.out.println("start: " + start1 + " " + start2 + "\nend: " + end1 + " " + end2);
				baslangic[0] = start1;
				baslangic[1] = start2;
				bitis[0] = end1;
				bitis[1] = end2;
				repaint();
			}
		});
		konumYenile.setBackground(new Color(255, 255, 255));
		konumYenile.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		JLabel gecenZamanRbt = new JLabel("");
		JLabel toplamYolRbt = new JLabel("");
		robotCoz.setEnabled(false);
		
		//çýkýþ noktasýnýn aranmasý
		robotCoz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kisaYolRobot.setEnabled(true);
				draw();
				speed = 45;
				isLab = false;
				degerAta();
				topYolSay = 0;
				startTime = System.nanoTime();
				run();
				if (hizliBtr == false)
					repaint();
				endTime = System.nanoTime();
				long estimatedTime = endTime - startTime;
				double seconds = (double) estimatedTime / 1000000000;
				gecenZamanRbt.setText("Çözüm süresi: " + Double.toString(seconds));
				System.out.println(topYolSay);
				toplamYolRbt.setText("Toplam yol: " + Integer.toString(topYolSay));

				File file = new File("gezginRobotOut.txt");
				FileWriter fW = null;
				try {
					fW = new FileWriter(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				PrintWriter pw = new PrintWriter(fW);

				for (int i = 0; i < labirentMtrs.length; i++) {
					for (int j = 0; j < labirentMtrs[i].length; j++)
						pw.print(labirentMtrs[i][j]);
					pw.println();
				}
				pw.close();
			}
		});
		robotCoz.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		robotCoz.setBackground(Color.WHITE);

		gecenZamanRbt.setForeground(new Color(128, 128, 128));
		gecenZamanRbt.setFont(new Font("Times New Roman", Font.PLAIN, 8));

		toplamYolRbt.setForeground(Color.GRAY);
		toplamYolRbt.setFont(new Font("Times New Roman", Font.PLAIN, 8));
		
		//gezgin robot projesinde kýsa yolun hesaplanmasý ve txt üzerine matris þeklinde kaydedilmesi
		kisaYolRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				labirentMtrs[start1][start2] = 9;
				labirentMtrs[end1][end2] = 9;
				for (int i = 0; i < labirentMtrs.length; i++) {
					for (int j = 0; j < labirentMtrs[i].length; j++) {
						if (labirentMtrs[i][j] == 8)
							labirentMtrs[i][j] = 9;
						if (labirentMtrs[i][j] == 4)
							labirentMtrs[i][j] = 9;
						if (labirentMtrs[i][j] == 6)
							labirentMtrs[i][j] = 9;
					}
				}
				islemler.kisaYolBul(labirentMtrs, baslangic, bitis);
				repaint();
				File file = new File("gezginRobotOut.txt");
				FileWriter fW = null;
				try {
					fW = new FileWriter(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				PrintWriter pw = new PrintWriter(fW);

				for (int i = 0; i < labirentMtrs.length; i++) {
					for (int j = 0; j < labirentMtrs[i].length; j++)
						pw.print(labirentMtrs[i][j]);
					pw.println();
				}
				pw.close();
			}
		});
		kisaYolRobot.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		kisaYolRobot.setBackground(Color.WHITE);

		JButton urlDegis = new JButton("Url Deðiþtir");
		//haritanýn hangi url den çekileceginin deðiþtirilmesi
		urlDegis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (path.equals(path1)) {
					path = path2;
					try {
						labirentMtrs = islemler.txtRead(path);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
					boyut1 = labirentMtrs.length;
					boyut2 = labirentMtrs[0].length;
					urlDegis.setText("Url Deðiþ - Url2");
				}

				else {
					path = path1;
					try {
						labirentMtrs = islemler.txtRead(path);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}
					boyut1 = labirentMtrs.length;
					boyut2 = labirentMtrs[0].length;
					urlDegis.setText("Url Deðiþ - Url1");
				}
			}
		});

		urlDegis.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		urlDegis.setBackground(Color.WHITE);

		JButton oyundanCýk = new JButton("Oyundan \u00C7\u0131k");
		oyundanCýk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotPanel.setVisible(false);
				menuPanel.setVisible(true);
			}
		});
		oyundanCýk.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		oyundanCýk.setBackground(Color.WHITE);

		JButton hizliBitirRobot = new JButton("Hýzlý Bitir");
		//çizim yapýlýrken bekleme süresinin kaldýrýlmasý
		hizliBitirRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				speed = 0;
				if (hzlbtr % 2 == 0) {
					hizliBitirRobot.setText("Yavaþ Bitir");
					hizliBtr = false;
				} else {
					hizliBitirRobot.setText("Hizli Bitir");
					hizliBtr = true;
				}
				hzlbtr++;
			}
		});
		hizliBitirRobot.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		hizliBitirRobot.setBackground(Color.WHITE);
		GroupLayout gl_robotMenu = new GroupLayout(robotMenu);
		gl_robotMenu
				.setHorizontalGroup(
						gl_robotMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_robotMenu.createSequentialGroup().addContainerGap(99, Short.MAX_VALUE)
										.addComponent(gecenZamanRbt, GroupLayout.PREFERRED_SIZE, 109,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap())
								.addGroup(gl_robotMenu
										.createSequentialGroup().addContainerGap(99, Short.MAX_VALUE)
										.addComponent(toplamYolRbt, GroupLayout.PREFERRED_SIZE, 109,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap())
								.addGroup(gl_robotMenu.createSequentialGroup().addGap(32).addGroup(gl_robotMenu
										.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_robotMenu
												.createSequentialGroup()
												.addComponent(hizliBitirRobot, GroupLayout.PREFERRED_SIZE, 161,
														GroupLayout.PREFERRED_SIZE)
												.addContainerGap())
										.addGroup(gl_robotMenu.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_robotMenu
														.createSequentialGroup()
														.addComponent(oyundanCýk, GroupLayout.PREFERRED_SIZE, 161,
																GroupLayout.PREFERRED_SIZE)
														.addContainerGap())
												.addGroup(gl_robotMenu.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_robotMenu
																.createSequentialGroup()
																.addComponent(urlDegis, GroupLayout.PREFERRED_SIZE, 161,
																		GroupLayout.PREFERRED_SIZE)
																.addContainerGap())
														.addGroup(gl_robotMenu.createParallelGroup(Alignment.LEADING)
																.addGroup(gl_robotMenu
																		.createSequentialGroup()
																		.addComponent(kisaYolRobot,
																				GroupLayout.PREFERRED_SIZE, 161,
																				GroupLayout.PREFERRED_SIZE)
																		.addContainerGap())
																.addGroup(gl_robotMenu
																		.createParallelGroup(Alignment.LEADING)
																		.addGroup(gl_robotMenu.createSequentialGroup()
																				.addComponent(robotCoz,
																						GroupLayout.PREFERRED_SIZE, 161,
																						GroupLayout.PREFERRED_SIZE)
																				.addContainerGap())
																		.addGroup(gl_robotMenu.createSequentialGroup()
																				.addComponent(konumYenile,
																						GroupLayout.DEFAULT_SIZE, 161,
																						Short.MAX_VALUE)
																				.addGap(25)))))))));
		gl_robotMenu.setVerticalGroup(gl_robotMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_robotMenu.createSequentialGroup().addGap(23).addComponent(konumYenile)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(robotCoz, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(kisaYolRobot, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(urlDegis, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(hizliBitirRobot, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(oyundanCýk, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addGap(175).addComponent(gecenZamanRbt).addGap(23)
						.addComponent(toplamYolRbt, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(44, Short.MAX_VALUE)));
		robotMenu.setLayout(gl_robotMenu);
		robotPanel.setLayout(gl_robotPanel);
	}

	@Override
	//labirentin çizimi
	public void paint(Graphics g2) {
		super.paintComponents(g2);
		g = (Graphics2D) g2;
		labCiz();
	}
	//gezgin robot prjesinde oluþan bulutun çizimi
	public void draw() {
		Graphics g5 = this.getGraphics();
		paint(g5, "");
	}

	public void paint(Graphics g3, String a) {
		g3.translate(70, 70);
		for (int sira = 1; sira < labirentMtrs.length - 1; sira++) {
			for (int sutun = 1; sutun < labirentMtrs[0].length - 1; sutun++) {
				g3.setColor(Color.white);
				g3.fillRect(700 / boyut1 * sutun, 700 / boyut1 * sira, 700 / boyut1, 700 / boyut1);
			}
		}
	}

	public void paint(Graphics g2, int sira, int sutun) {
		int flag1 = sira;
		int flag = sutun;
		if (aKontrol > 1) {
			g2.translate(70, 70);
			Color color;
			for (int i = -2; i < 1; i++) {
				sira += i;
				sira++;
				for (int j = -1; j < 2; j++) {
					sutun = sutun + j;
					switch (labirentMtrs[sira][sutun]) {
					case 8:
						color = Color.RED;
						break;
					case 1:
						color = Color.gray;
						break;
					case 2:
						color = Color.DARK_GRAY;
						break;
					case 3:
						color = Color.black;
						break;
					case 6:
						color = Color.green;
						break;
					case 9:
						color = Color.blue;
						break;
					case 4:
						color = Color.cyan;
						break;
					case 5:
						color = Color.orange;
						break;
					default:
						color = Color.WHITE;
					}
					g2.setColor(color);
					g2.fillRect(700 / boyut1 * sutun, 700 / boyut1 * sira, 700 / boyut1, 700 / boyut1);
					g2.setColor(Color.black);
					if(!isLab)
					g2.drawRect(700 / boyut1 * sutun, 700 / boyut1 * sira, 700 / boyut1, 700 / boyut1);
					sutun = flag;
				}
				sira = flag1;
			}
		}
		aKontrol++;
	}

	public void labCiz() {
		g.translate(70, 70);
		Color color;

		for (int sira = 0; sira < labirentMtrs.length; sira++) {
			for (int sutun = 0; sutun < labirentMtrs[0].length; sutun++) {

				switch (labirentMtrs[sira][sutun]) {
				case 8:
					color = Color.RED;
					break;
				case 1:
					color = Color.gray;
					break;
				case 2:
					color = Color.DARK_GRAY;
					break;
				case 3:
					color = Color.black;
					break;
				case 6:
					color = Color.green;
					break;
				case 9:
					color = Color.blue;
					break;
				case 5:
					color = Color.orange;
					break;
				case 4:
					color = Color.cyan;
					break;
				default:
					color = Color.WHITE;
				}
				g.setColor(color);
				g.fillRect(700 / boyut1 * sutun, 700 / boyut1 * sira, 700 / boyut1, 700 / boyut1);
				g.setColor(Color.black);
				if(!isLab)
				g.drawRect(700 / boyut1 * sutun, 700 / boyut1 * sira, 700 / boyut1, 700 / boyut1);
			}
			if (isLab) {
				g.setColor(Color.green);
				g.fillRect(700 / boyut1 * 1, 700 / boyut1 * 1, 700 / boyut1, 700 / boyut1);
				g.setColor(Color.black);
				g.drawRect(700 / boyut1 * 1, 700 / boyut1 * 1, 700 / boyut1, 700 / boyut1);
			} else {
				g.setColor(Color.green);
				g.fillRect(700 / boyut1 * START_J, 700 / boyut1 * START_I, 700 / boyut1, 700 / boyut1);
				g.setColor(Color.black);
				g.drawRect(700 / boyut1 * START_J, 700 / boyut1 * START_I, 700 / boyut1, 700 / boyut1);
			}
		}
		System.out.println("ekran cizildi");
	}
}
