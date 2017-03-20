
public class Const {
	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 480;
	
	// デバッグ用出力を出すかどうか
	// 集計の際にfalseにするのでtrueのままで構いません
	public static final boolean DEBUG = true;
		
	// ダンジョンの大きさ (SIZE * SIZE) : 5
	public static final int SIZE = 5;
	
	// ターンの最大値 : 1000
	public static final int MAX_TURN = 1000;
	
	// 1ターン経過 : -1
	public static final int POINT_TURN = -1;
	
	// 矢を放つ : 0
	public static final int POINT_ARROW = -50;
	
	// 金貨を拾う : 100
	public static final int POINT_PICKUP = 100;
	
	// Wumpusを倒す : 200
	public static final int POINT_HIT = 200;
	
	// 脱出したときに金貨を持っている : 1000
	public static final int POINT_ESCAPE_GOLD = 1000;
	
	// 脱出する : 50
	public static final int POINT_ESCAPE = 50;
	
	// 脱出したときにWumpusを倒している : 1000
	public static final int POINT_ESCAPE_WUMPUS = 1000;
	
	// 穴に落ちる : -200
	public static final int POINT_HOLE = -200;
	
	// Wumpusに捕まる : -200
	public static final int POINT_WUMPUS = -200;
	
	// 自殺する : 0
	public static final int POINT_SUICIDE = 0;
}
