//matris üzerindeki noktalarýn deðerlerini tutabilen nesne sýnýfý
public class MazePos {

	int i, j;
	
	public MazePos(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public int i() {
		return i;
	}

	public int j() {
		return j;
	}

	public MazePos kuzey() {
		return new MazePos(i - 1, j);
	}
	public MazePos guney() {
		return new MazePos(i + 1, j);
	}

	public MazePos dogu() {
		return new MazePos(i, j + 1);
	}

	public MazePos bati() {
		return new MazePos(i, j - 1);
	}
}
