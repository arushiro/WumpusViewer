import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {
	private int seed, size, arrow, wumpus, gold, prob;
	private ArrayList<Action> list;

	// コンストラクタ
	public Reader() {
		list = new ArrayList<Action>();
	}

	// ログの読み出し
	public void load(String str) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(str)));
		String input = br.readLine();
		String[] arr = input.split(",");
		seed = Integer.parseInt(arr[0]);
		size = Integer.parseInt(arr[1]);
		arrow = Integer.parseInt(arr[2]);
		wumpus = Integer.parseInt(arr[3]);
		gold = Integer.parseInt(arr[4]);
		prob = Integer.parseInt(arr[5]);
		
		for (int i = 6; i < arr.length; i++)
			list.add(Action.values()[Integer.parseInt(arr[i])]);
		br.close();
	}

	// 行動読み出し
	public Action getAction(int n) {
		return list.get(n);
	}
	
	// 行動の数
	public int getActionLength() {
		return list.size();
	}
	
	public int getSeed() {
		return seed;
	}

	public int getSize() {
		return size;
	}

	public int getArrow() {
		return arrow;
	}

	public int getWumpus() {
		return wumpus;
	}

	public int getGold() {
		return gold;
	}

	public int getProb() {
		return prob;
	}

}
