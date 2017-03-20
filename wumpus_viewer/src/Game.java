import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Game {
	// 現在位置のx,y、マップの大きさ、得点、矢の数、金貨を拾った数、Wumpusを倒した数
	private int x, y, size, point, arrow, pickup, hit, wumpus, gold;
	// ゲーム終了フラグ
	private boolean isEnd;
	// マップ管理
	private Map map;
	// センサー
	private HashMap<Status, Boolean> sensor;

	// マップリスト
	private LinkedList<Tile[][]> mapList;
	private LinkedList<String> infoList;

	public Game(int seed, int size, int wumpus, int gold, int prob, int arrow) {
		this.size = size;
		map = new Map(seed, size, wumpus, gold, prob);
		point = 0;
		this.arrow = arrow;
		this.wumpus = wumpus;
		this.gold = gold;
		x = map.getStartX();
		y = map.getStartY();
		isEnd = false;
		pickup = 0;
		hit = 0;
		mapList = new LinkedList<Tile[][]>();
		infoList = new LinkedList<String>();

		// センサーの初期化
		sensor = new HashMap<Status, Boolean>();
		sensor.put(Status.WALL, false);
		sensor.put(Status.HOLE, false);
		sensor.put(Status.GOLD, false);
		sensor.put(Status.HIT, false);
		sensor.put(Status.WUMPUS, false);

		// 穴やWumpusが周囲にあったらセンサーを更新する
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (Math.abs(i + j) == 1) {
					sensorUpdate(i, j);
				}
			}
		}

		Tile[][] mapArr = new Tile[size][size];
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				mapArr[i][j] = map.getTile(i, j);
			}
		}
		mapList.add(mapArr);

		String info = "";
		info += "Turn :\tSTART\n";
		info += "Point :\t" + point + "\n";
		info += "Arrow Left :\t" + arrow + "\n";
		info += "Action :\t\n";
		info += "\nNext Turn Sensors :\n";
		for (Status s : sensor.keySet()) {
			info += s.toString() + " :\t" + sensor.get(s).toString() + "\n";
		}
		infoList.add(info);
	}

	public void run(Reader reader) throws IOException {
		int turn = 0;

		// ターン処理(メインループ)
		for (; turn < reader.getActionLength() && !isEnd; turn++) {
			// ターン経過による加点/減点
			point += Const.POINT_TURN;

			// アクションの取得
			Action action = reader.getAction(turn);

			// センサーの初期化
			sensor = new HashMap<Status, Boolean>();
			sensor.put(Status.WALL, false);
			sensor.put(Status.HOLE, false);
			sensor.put(Status.GOLD, false);
			sensor.put(Status.HIT, false);
			sensor.put(Status.WUMPUS, false);

			// アクションを実行
			switch (action) {

			// 移動
			case UP:
				move(0, -1);
				break;
			case DOWN:
				move(0, 1);
				break;
			case LEFT:
				move(-1, 0);
				break;
			case RIGHT:
				move(1, 0);
				break;

			// 矢を放つ
			case SHOOT_UP:
				if (arrow > 0) {
					shoot(x, y, 0, -1);
					point += Const.POINT_ARROW;
					arrow--;
				}
				break;
			case SHOOT_DOWN:
				if (arrow > 0) {
					shoot(x, y, 0, 1);
					point += Const.POINT_ARROW;
					arrow--;
				}
				break;
			case SHOOT_LEFT:
				if (arrow > 0) {
					shoot(x, y, -1, 0);
					point += Const.POINT_ARROW;
					arrow--;
				}
				break;
			case SHOOT_RIGHT:
				if (arrow > 0) {
					shoot(x, y, 1, 0);
					point += Const.POINT_ARROW;
					arrow--;
				}
				break;

			// 金貨を拾う
			case PICKUP:
				if (map.pickUp(x, y)) {
					pickup++;
					point += Const.POINT_PICKUP;
				}
				break;

			// ゲーム終了
			case END:
				if (map.getTile(x, y) == Tile.UPSTAIRS) {
					if (pickup == gold)
						point += Const.POINT_ESCAPE_GOLD;
					if (hit == wumpus)
						point += Const.POINT_ESCAPE_WUMPUS;

					point += Const.POINT_ESCAPE;
				} else {
					point += Const.POINT_SUICIDE;
				}
				isEnd = true;
				break;

			// 何もしない
			case NONE:
				point += Const.POINT_SUICIDE;
				break;

			default:
				isEnd = true;
				break;
			}

			// 穴やWumpusが周囲にあったらセンサーを更新する
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (Math.abs(i + j) == 1) {
						sensorUpdate(i, j);
					}
				}
			}

			// 足元に金貨があったらセンサーを更新する
			if (map.getTile(x, y) == Tile.GOLD)
				sensor.put(Status.GOLD, true);

			// デバッグ用の情報を出力
			Tile[][] mapArr = new Tile[size][size];
			for (int j = 0; j < size; j++) {
				for (int i = 0; i < size; i++) {
					if (i == x && j == y)
						mapArr[i][j] = Tile.AGENT;
					else
						mapArr[i][j] = map.getTile(i, j);
				}
			}
			mapList.add(mapArr);

			String info = "";
			info += "Turn :\t" + turn + "\n";
			info += "Point :\t" + point + "\n";
			info += "Arrow Left :\t" + arrow + "\n";
			info += "Action :\t" + action.toString() + "\n";
			info += "\nNext Turn Sensors :\n";
			for (Status s : sensor.keySet()) {
				info += s.toString() + " :\t" + sensor.get(s).toString() + "\n";
			}
			infoList.add(info);
		}

		// 最大ターンが過ぎたら自殺
		if (turn == reader.getActionLength() && !isEnd) {
			point += Const.POINT_SUICIDE;
		}

		// デバッグ用の情報を出力
		Tile[][] mapArr = new Tile[size][size];
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				mapArr[i][j] = map.getTile(i, j);
			}
		}
		mapList.add(mapArr);

		String info = "";
		info += "Turn :\tEND\n";
		info += "Point :\t" + point + "\n";
		info += "Arrow Left :\t" + arrow + "\n";
		info += "Action :\t\n";
		info += "\nNext Turn Sensors :\n";
		for (Status s : sensor.keySet()) {
			info += s.toString() + " :\t" + sensor.get(s).toString() + "\n";
		}
		infoList.add(info);
	}

	// 移動処理
	private void move(int dx, int dy) {
		point += Const.POINT_TURN;
		switch (map.getTile(x + dx, y + dy)) {
		case NONE:
		case GOLD:
		case UPSTAIRS:
			break;
		case HOLE:
			point += Const.POINT_HOLE;
			isEnd = true;
			break;
		case WUMPUS:
			point += Const.POINT_WUMPUS;
			isEnd = true;
			break;
		case STONE:
			x -= dx;
			y -= dy;
			sensor.put(Status.WALL, true);
			break;
		default:
			break;
		}
		x += dx;
		y += dy;
	}

	// 矢を撃つ処理
	private void shoot(int x, int y, int dx, int dy) {
		if (arrow > 0) {
			switch (map.getTile(x + dx, y + dy)) {
			case NONE:
			case GOLD:
			case UPSTAIRS:
			case HOLE:
				shoot(x + dx, y + dy, dx, dy);
				break;
			case WUMPUS:
				map.hit(x + dx, y + dy);
				sensor.put(Status.HIT, true);
				hit++;
				point += Const.POINT_HIT;
				break;
			case STONE:
				break;
			default:
				break;
			}
		}
	}

	// 周囲のセンサーの更新処理(WumpusとHole用)
	private void sensorUpdate(int dx, int dy) {
		switch (map.getTile(x + dx, y + dy)) {
		case NONE:
		case GOLD:
		case UPSTAIRS:
			break;
		case HOLE:
			sensor.put(Status.HOLE, true);
			break;
		case WUMPUS:
			sensor.put(Status.WUMPUS, true);
			break;
		case STONE:
			break;
		default:
			break;
		}
	}
	
	public Tile[][] getMapArr(int n) {
		return mapList.get(n);
	}
	
	public String getInfo(int n) {
		return infoList.get(n);
	}

}
