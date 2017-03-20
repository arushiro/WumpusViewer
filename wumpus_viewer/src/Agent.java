import java.util.HashMap;
import java.util.Random;

public class Agent {
	// Wumpus World
	//
	// 1. �T�v
	// �����͌������Wumpus�ȂǗl�X�Ȋ댯�������ς��̃_���W�����I
	// ���Ȃ��͂��̃_���W�����ɋM�d�ȋ��݂����߁A�|����g������Ă����I
	// �������A�_���W�����͈Â��ڂ̑O�ɉ������邩���킩��Ȃ��c�c
	// ���ɐ������ޕ���AWumpus�̏L���Ȃǂ̏�����g���āA
	// Wumpus��|���_���W�����ɗ����Ă�����݂𖳎��Ɏ����A�낤�I
	//
	// 2. �ړI
	// N*N�̃}�X�ō\�����ꂽ�_���W�����ɗ����Ă�����݂��E���AWumpus��|�������Ƀ_���W������E�o���Ă��������B
	//
	// 3. ����
	// ���̃Q�[���̓^�[���i�s������Q�[���ł��B
	// �e�^�[�����Ȃ��̓_���W�������ł̍s�������߂�K�v������܂��B
	// �s���̐����͂��̃t�@�C��(Agent.java)��Do���\�b�h�ɂ���čs���܂��B
	// Agent.java�����������邱�Ƃɂ���Ă��ǂ��s���𐶐����܂��傤�B
	// �s���̌��ʁA�_���W�������Ŏ��S����A�������͖����ɒE�o�����ꍇ�ɃQ�[���͏I�����܂��B
	// �e�^�[���̍s�������߂邽�߂̃q���g�Ƃ��āA�����אڂ��Ă��邩�ǂ����Ȃǂ̏�񂪓n����܂��B
	//
	// 4. �s��
	// �^�[���ɏo����s���͈ȉ���11���ڂł��B
	//
	// �������Ȃ�
	//   Action.NONE : �������Ȃ��B
	//
	// �ړ�
	//   �w�肵��������1�}�X�i�݂܂��B�i�񂾐悪��,Wumpus�̏ꍇ���Ȃ��͎��ɂ܂��B�ǂ̏ꍇ���̏�ɂƂǂ܂�܂��B
	//   Action.UP : ���1�}�X�i�݂܂��B
	//   Action.DOWN : ����1�}�X�i�݂܂��B
	//   Action.LEFT : ����1�}�X�i�݂܂��B
	//   Action.Right : �E��1�}�X�i�݂܂��B
	//
	// ������
	//   �w�肵�������ɖ������܂��B��̎c����0�̏ꍇ�����N����܂���B�����ꂽ��͕ǂɓ�����܂Ői�݂܂��B�r����Wumpus�ɓ��������ꍇWumpus�͏��ł��܂��B
	//   Action.SHOOT_UP : ��ɖ������܂��B
	//   Action.SHOOT_DOWN : ���ɖ������܂��B
	//   Action.SHOOT_LEFT : ���ɖ������܂��B
	//   Action.SHOOT_Right : �E�ɖ������܂��B
	//
	// �E��
	//   Action.PICKUP : ���ݒn�ɋ��݂�����ΏE���܂��B�Ȃ���Ή����N����܂���B
	//
	// �E�o����/���E����
	//   Action.END : ���ݒn��������ł���ΒE�o���܂��B�K�i�łȂ���ΒT������߂Ď��E���܂��B
	//
	// 5. ���
	// �e�^�[���ɓn�������ł��B
	// ���͕����n����邱�Ƃ�����܂��B(�����אڂ��Ă��� ���� Wumpus���אڂ��Ă��� �Ȃ�)
	// ���͈ȉ���5���ڂł��B
	//   Status.WALL : �O�̃^�[���Ɉړ��������ʁA�ǂɏՓ˂��Ĉړ��ł��Ȃ������B
	//   Status.HOLE : ���݈ʒu��4���̂����ꂩ�Ɍ���1�ȏ㑶�݂��Ă���B
	//   Status.GOLD : ���݈ʒu�ɋ��݂����݂��Ă���B
	//   Status.WUMPUS : ���݈ʒu��4���̂����ꂩ��Wumpus�����݂��Ă���B
	//   Status.HIT : �O�̃^�[���ɕ������Wumpus�ɓ��������B
	//
	// 6. �^�[���̐i�ߕ�
	// ���^�[�����̃N���X��Do���\�b�h���Ă΂�܂��B
	// Do���\�b�h�ɂ�5.�ŏЉ���󋵂��i�[���Ă���HashMap<Status, Boolean>�ł���sensor���n����܂��B
	// sensor.get(Status.GOLD)�Ƃ���ƁA���݈ʒu�ɋ��݂����݂��Ă���Ȃ�true�A�Ȃ����false���Ԃ��Ă��܂��B
	// ���̌��ʂ����ɂ��ĉ�������̏������s�����̂��AEnum�ł���Action�̂���1��Ԃ����Ƃōs�����N�����܂��B
	// return Action.UP; �Ƃ���Ə��1�}�X�i�ނƂ����s�����N�����܂��B
	//
	// 7. �Q�[���̓_��
	// ����͈ȉ��̍s���œ_��������܂��B
	//
	// �Q�[�����ɓ��链�_
	//   1�^�[���o�� : -1
	//   ������ : 0
	//   ���݂��E�� : 100
	//   Wumpus��|�� : 200
	//
	// �Q�[���I�����ɓ��链�_
	//   �E�o
	//     �E�o����(�������Action.End���s��) : 100
	//     ���݂�S�ďE������ԂŒE�o���� : 1000
	//     Wunpus��S�ē|������ԂŒE�o���� : 1000
	
	//   ���S
	//     ���ɗ����� : -200
	//     Wumpus�ɕ߂܂� : -200
	//     ���E����(������ȊO��Action.End���s��/1000�^�[���𒴉߂���) : 0
	//
	// 8. ���ʂ̉{��
	//
	//
	// 9. ��o
	// Agent.java�݂̂��o���Ă��������B
	//
	// 10. �]��
	// �}�b�v�����Ɏg�������̃V�[�h�l��5��߁A����5�̃V�[�h�l��Agent.java�����s�����_���̍��v��]���Ɏg���܂��B
	// ���̃Q�[���͋��݂���邱�Ƃ��o���Ȃ���ʂ����݂��邽�߁A�]���͒�o���ꂽ�v���O�����Ԃł̑��ΓI�Ȃ��̂ƂȂ�܂��B
	//
	// �Ȃ��AAgent.java�ŗ������g�����ꍇ�A��������s���čŗǂ̂��̂����Ȃǂ̌��ʂ̕ۏ؂͒v���܂���B
	//
	// 11. �֎~�s��
	// * Agent.java�ȊO�̃t�@�C���ɃR�[�h���������ƁB
	// ��o��Agent.java�݂̂ɂȂ�܂��B

	// ��̖{��
	private int arrow;
	// �����̑傫�� (size * size�̐����`)
	private int size;

	private Random rnd;

	// ���Έʒu
	private int x, y;

	private class Flag {
		public boolean breeze;
		public boolean stench;
		public boolean qHole;
		public boolean qWumpus;
		public boolean isHole;
		public boolean isWumpus;
		public boolean isNone;
		public boolean isWall;
	}

	private Flag[][] map;

	private Action last;

	// �R���X�g���N�^
	public Agent(int arrow, int size) {
		this.arrow = arrow;
		this.size = size;
		last = Action.NONE;
		rnd = new Random(1000);

		x = size; y = size;

		map = new Flag[size * 2 - 1][size * 2 - 1];
		for (int i = 0; i < size * 2 - 1; i++) {
			for (int j = 0; j < size * 2 - 1; j++) {
				map[i][j] = new Flag();
			}
		}
	}

	// ���^�[���Ă΂��֐�
	// Enum�ł���Action��Ԃ����Ƃɂ��s�����N�����܂��B
	// ���݂̏�(�O�̃^�[���ɋN��������)��sensor�Ɋi�[����Ă��܂��B
	// �󋵂̈ꗗ�F
	public Action Do(HashMap<Status, Boolean> sensor) {
		Action action = Action.NONE;

		update(sensor);

		if (sensor.get(Status.GOLD))
			action = Action.PICKUP;
		
		if (sensor.get(Status.HOLE))
			action = Action.SHOOT_DOWN;

		last = action;
		return action;
	}

	public void update(HashMap<Status, Boolean> sensor) {
		if (sensor.get(Status.WALL)) {
			switch (last) {
			case UP:
				for (int i = 0; i < size * 2 - 1; i++) {
					for (int j = y - 1; j >= 0; j--) {
						map[i][j].isWall = true;
					}
				}
				break;
			case DOWN:
				for (int i = 0; i < size * 2 - 1; i++) {
					for (int j = y + 1; j < size * 2 - 1; j++) {
						map[i][j].isWall = true;
					}
				}
				break;
			case LEFT:
				for (int j = 0; j < size * 2 - 1; j++) {
					for (int i = x - 1; i >= 0; i--) {
						map[i][j].isWall = true;
					}
				}
				break;
			case RIGHT:
				for (int j = 0; j < size * 2 - 1; j++) {
					for (int i = x + 1; i < size * 2 - 1; i++) {
						map[i][j].isWall = true;
					}
				}
				break;
			default:
				break;
			}
		}
		if (sensor.get(Status.WUMPUS)) {
			int n = 0;
			int lx = 0;
			int ly = 0;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (Math.abs(i + j) == 1) {
						if (x + i >= 0 && x + i < size * 2 - 1 && y + j >= 0 && y + j < size * 2 - 1) {
							if (!map[x + i][y + j].isNone) {
								n++;
								lx = x + i;
								ly = y + i;
								map[x + i][y + j].qWumpus = true;
							}
						}
					}
				}
			}
			if (n == 1)
				map[lx][ly].isWumpus = true;
		}
	}
}
