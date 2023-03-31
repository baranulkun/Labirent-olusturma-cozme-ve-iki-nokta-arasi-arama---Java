import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class robotIslemleri {
	
	//harita �zerindeki noktay� olu�turan s�n�f
	private static class Cell {
		int x;
		int y;
		Cell root; //ana h�cre
		
		Cell(int x, int y, Cell prev) { //noktan�n tan�mlanmas�
			this.x = x;
			this.y = y;
			this.root = prev; //cocu�un atanmas�
		}
	}
	
	//s�radaki noktan�n yol olup olmad���n�n kontrolu
	public boolean boyaKontrolu(int mtrs[][], Cell temp) {
		return (mtrs[temp.x][temp.y] == 9);
	}
	
	//matri degerinin d�zenlenmesi
	public void renkNumarasiAtama(int[][] mtrs, Cell temp, int x) {
		mtrs[temp.x][temp.y] = x;
	}
	
	//daha �nce ge�ilmi� yol uzerinden en k�sa yolun BFS ile hesaplanmas�
	public int[][] kisaYolBul(int[][] mtrs, int[] giris, int[] cikis) {
		Cell baslangic = new Cell(giris[0], giris[1], null);
		Queue<Cell> que = new LinkedList<>();
		que.add(baslangic);

		Cell temp = null;
		Cell tasiyici = null;
		while (!que.isEmpty() || (temp.x == cikis[0] && temp.y == cikis[1])) {
			temp = que.poll();
			boolean kontrol=boyaKontrolu(mtrs, temp);
			
			//noktan�n kuzeyinin kontrol�
			if (kontrol && mtrs[temp.x - 1][temp.y] == 9) {
				renkNumarasiAtama(mtrs, temp, 7);
				Cell next = new Cell(temp.x - 1, temp.y, temp);
				que.add(next);
				tasiyici=next;
				System.out.println("kuzey stack'e eklendi");
				System.out.println("temp: "+temp.x+" "+temp.y);
				if((temp.x-1 == cikis[0] && temp.y == cikis[1])) {
					System.out.println("Sonuc bulundu"+(temp.x-1)+" "+temp.y);
					break;
				}
			}
			
			//noktan�n do�usunun kontrol�
			if (boyaKontrolu(mtrs, temp) && mtrs[temp.x][temp.y + 1] == 9) {
				renkNumarasiAtama(mtrs, temp, 7);
				Cell next = new Cell(temp.x, temp.y + 1, temp);
				que.add(next);
				tasiyici=next;
				System.out.println("dogu stack'e eklendi");
				System.out.println("temp: "+temp.x+" "+temp.y);
				if((temp.x == cikis[0] && temp.y+1 == cikis[1])) {
					System.out.println("Sonuc bulundu"+temp.x+" "+(temp.y+1));
					break;
				}
			}
			
			//noktan�n bat�s�n�n kontrol�
			if (kontrol && mtrs[temp.x][temp.y - 1] == 9) {
				renkNumarasiAtama(mtrs, temp, 7);
				Cell next = new Cell(temp.x, temp.y - 1, temp);
				que.add(next);
				tasiyici=next;
				System.out.println("bat� stack'e eklendi");
				System.out.println("temp: "+temp.x+" "+temp.y);
				if((temp.x == cikis[0] && temp.y-1 == cikis[1])) {
					System.out.println("Sonuc bulundu : "+temp.x+" "+(temp.y-1));
					break;
				}
			}
			
			//noktan�n g�neyinin kontrol�
			if (kontrol && mtrs[temp.x + 1][temp.y] == 9) {
				renkNumarasiAtama(mtrs, temp, 7);
				Cell next = new Cell(temp.x + 1, temp.y, temp);
				que.add(next);
				tasiyici=next;
				System.out.println("guney stack'e eklendi");
				System.out.println("temp: "+temp.x+" "+temp.y);
				if((temp.x+1 == cikis[0] && temp.y == cikis[1])) {
					System.out.println("Sonuc bulundu"+(temp.x+1)+" "+temp.y);
					break;
				}
			}
		}
		
		//matris �zerindeki numaralar�n yol numaras�na (9) �evrilmesi
		for(int i=0;i<mtrs.length;i++) {
			for(int j=0;j<mtrs.length;j++) {
				if(mtrs[i][j]==7 || mtrs[i][j]==9)
					mtrs[i][j]=9;
			}
		}
		//��k�� noktas�ndan k�ke do�ru ilerlenerek k�sa yol numaras�n�n (5) atanmas�
		while(tasiyici.root!=null) {
			mtrs[tasiyici.x][tasiyici.y]=5;
			System.out.println("sonuca giden yol: "+tasiyici.x+" "+tasiyici.y);
			tasiyici=tasiyici.root;
		}
		mtrs[giris[0]][giris[1]]=6;
		mtrs[cikis[0]][cikis[1]]=8;
		return mtrs;
	}

	//Verilen Url adresindeki txt dosyas�ndan matrisin �ekilmesi ve d�zenlenmesi
	public int[][] txtRead(String path) throws MalformedURLException {
		URL url = new URL(path);
		Scanner scan = null;
		try {
			scan = new Scanner(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		String flag = "";
		while (scan.hasNextLine()) {
			flag = flag.concat(scan.nextLine() + "\n");
		}
		
		int satir = 0;
		for (int i = 0; i < flag.length(); i++) {
			if (flag.charAt(i) == '\n') {
				satir++;
			}
		}

		int bagimsiz = 0;
		int[][] matris = new int[satir + 2][flag.substring(0, flag.indexOf("\n")).length() + 2];
		for (int i = 1; i < matris.length - 1; i++) {	//�ekilen string de�erin int t�r�ne �evrilerek matrise atanmas�
			for (int j = 1; j < matris[i].length - 1; j++) {
				if (flag.charAt(bagimsiz) == '\n' || flag.charAt(bagimsiz) == ' ') {
					bagimsiz++;
					j--;
					continue;
				}
				matris[i][j] = Character.getNumericValue(flag.charAt(bagimsiz));
				bagimsiz++;
			}
		}
		for (int i = 0; i < matris.length; i++) { //artan bo�lu�a atama yap�lmas�
			for (int j = 0; j < matris[i].length; j++) {
				if (i == 0 || j == 0 || i == satir + 1 || j == flag.substring(0, flag.indexOf("\n")).length() + 1)
					matris[i][j] = 1;
			}
		}
		for (int i = 0; i < matris.length - 2; i++) { //engellerin rastgele olarak s�k�lmesi
			for (int j = 0; j < matris[i].length - 2; j++) {
				engelDuzenle(matris, i, j);
			}
		}
		return girC�kAtama(matris);
	}

	//Daha �nce matris �zerinde giri� ve ��k�� noktalar� belirlenmi� ise bu noktalar�n kald�r�lmas�
	public int[][] girC�kVarM�(int[][] mtrs) {
		for (int i = 0; i < mtrs.length; i++) {
			for (int j = 0; j < mtrs[i].length; j++) {
				if (mtrs[i][j] == 6 || mtrs[i][j] == 8)
					mtrs[i][j] = 0;
			}
		}
		return mtrs;
	}

	//Matris �zerine giri� ve ��k�� noktalar�n�n atanmas�
	public int[][] girC�kAtama(int[][] mtrs) {
		mtrs = girC�kVarM�(mtrs);
		Random randomCrt = new Random();
		while (true) {
			int grs1 = randomCrt.nextInt(mtrs.length);
			int grs2 = randomCrt.nextInt(mtrs[0].length);

			int cks1 = randomCrt.nextInt(mtrs.length);
			int cks2 = randomCrt.nextInt(mtrs[0].length);
			if (grs1 != cks1 && grs2 != cks2 && mtrs[grs1][grs2] == 0 && mtrs[cks1][cks2] == 0) {
				mtrs[grs1][grs2] = 6;
				mtrs[cks1][cks2] = 8;
				break;
			}
		}
		return mtrs;
	}

	//engellerin boyut s�n�rlar�n� a�mama kaidesi ile s�k�lmesi
	public int[][] engelDuzenle(int[][] mtrs, int i, int j) {
		System.out.println("i: " + i + " j:  " + j);
		if (mtrs[i][j] == 2 && mtrs[i + 1][j] == 2 && mtrs[i][j + 1] == 2 && mtrs[i + 1][j + 1] == 2)
			ikilikEngel(mtrs, i, j);
		else if (mtrs[i][j] == 3 && mtrs[i + 1][j] == 3 && mtrs[i + 2][j] == 3 && mtrs[i][j + 1] == 3
				&& mtrs[i + 1][j + 2] == 3 && mtrs[i + 1][j + 1] == 3 && mtrs[i + 1][j + 2] == 3
				&& mtrs[i + 2][j + 1] == 3 && mtrs[i + 2][j + 2] == 3)
			uclukEngel(mtrs, i, j);
		return mtrs;

	}

	//ucluk engel tipindeki nesnelerin �cl�k engel olmalar�n� engellemeden s�kme i�lemi
	public int[][] uclukEngel(int[][] mtrs, int i, int j) {
		Random r = new Random();
		int dongu = r.nextInt(6);
		for (int c = 0; c < dongu; c++) {
			int ilkDeger = r.nextInt(3);
			int ikinciDeger = r.nextInt(3);
			mtrs[i + ilkDeger][j + ikinciDeger] = 0;
		}
		return mtrs;

	}

	//ikilik engel tipindeki nesnelerin ikilik engel olmalar�n� engellemeden s�kme i�lemi
	public int[][] ikilikEngel(int[][] mtrs, int i, int j) {
		System.out.println("cagrildi");
		Random r = new Random();
		int dongu = r.nextInt(3);
		for (int c = 0; c < dongu; c++) {
			int ilkDeger = r.nextInt(2);
			int ikinciDeger = r.nextInt(2);
			mtrs[i + ilkDeger][j + ikinciDeger] = 0;
		}
		return mtrs;
	}

	//mevcut noktan�n sonu� noktas� olup olmad���n� kontrol etme
	public boolean sonucBulma(MazePos pos, int END_I, int END_J) {
		return (pos.i() == END_I - 1 && pos.j() == END_J - 1) ? true : false;
	}
	
	//matris �zerinde de�i�iklik yap�lmas� ve ge�ilen yolun belirlenmesi
	public void renkNumarasiAtama(int[][] labirentMtrs, MazePos pos, int hedef) {
		labirentMtrs[pos.i()][pos.j] = hedef;
	}

	//s�radaki noktan�n ge�ilebilir olup olmad���n�n kontrol�
	public boolean boyaKontrolu(int[][] labirentMtrs, MazePos pos) {
		return (labirentMtrs[pos.i()][pos.j] == 9);

	}
}
