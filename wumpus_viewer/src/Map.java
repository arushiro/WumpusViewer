import java.util.LinkedList;
import java.util.Random;

public class Map {
	private int startX;
	private int startY;
	private Tile[][] map;
	private int size;

	public Map(int seed, int size, int wumpus, int gold, int prob) {
		Random rnd = new Random(seed);
		map = new Tile[size][size];
		this.size = size;
		
		// �����u����Ă��Ȃ��^�C�������X�g�ɒǉ�
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				map[i][j] = Tile.NONE;
				list.add(j * size + i);
			}
		}

		// �����ʒu�̌���
		int n = rnd.nextInt(list.size());
		map[list.get(n) % size][list.get(n) / size] = Tile.UPSTAIRS;
		startX = list.get(n) % size;
		startY = list.get(n) / size;
		list.remove(n);

		// Wumpus�̔z�u
		for (int i = 0; i < wumpus; i++) {
			n = rnd.nextInt(list.size());
			map[list.get(n) % size][list.get(n) / size] = Tile.WUMPUS;
			list.remove(n);
		}

		// ���݂̔z�u
		for (int i = 0; i < gold; i++) {
			n = rnd.nextInt(list.size());
			map[list.get(n) % size][list.get(n) / size] = Tile.GOLD;
			list.remove(n);
		}

		// �c�����󂫃^�C���Ɍ���z�u
		for (int i : list)
			if (rnd.nextInt(100) < prob)
				map[i % size][i / size] = Tile.HOLE;
	}

	// �����ʒu��X���W��Ԃ�
	public int getStartX() {
		return startX;
	}

	// �����ʒu��Y���W��Ԃ�
	public int getStartY() {
		return startY;
	}

	// �w�肳�ꂽ���W�̃^�C����Ԃ�
	// �͈͊O�ł����Tile.STONE���Ԃ����
	public Tile getTile(int x, int y) {
		if (x >= size || x < 0 || y >= size || y < 0)
			return Tile.STONE;
		return map[x][y];
	}

	// �w�肳�ꂽ���W�ɋ��݂�����΋��݂�������true��Ԃ�
	// �����łȂ����false��Ԃ�
	public boolean pickUp(int x, int y) {
		if (getTile(x, y) == Tile.GOLD) {
			map[x][y] = Tile.NONE;
			return true;
		}
		return false;
	}
	
	public boolean hit(int x, int y) {
		if (getTile(x, y) == Tile.WUMPUS) {
			map[x][y] = Tile.NONE;
			return true;
		}
		return false;
	}
}
