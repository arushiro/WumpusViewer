import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JSlider;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JTextField;

public class Viewer {
	private JFrame window;
	private JPanel panel;
	private JSlider slider;
	private JButton left, right, start, end, open;
	private JTextArea textArea;
	private Reader reader;
	private Game game;
	private int turn;
	private HashMap<Tile, Image> imageMap;
	private JTextField textField;

	@SuppressWarnings("serial")
	public Viewer() {
		window = new JFrame();
		window.setTitle("Wumpus Viewer");
		window.setSize(Const.WINDOW_WIDTH, Const.WINDOW_HEIGHT);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		window.getContentPane().setLayout(springLayout);

		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				draw(g);
			}
		};
		springLayout.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.WEST, window.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel, -42, SpringLayout.SOUTH, window.getContentPane());
		panel.setPreferredSize(new Dimension(400, 400));
		window.getContentPane().add(panel);

		slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				turn = slider.getValue();
				refresh();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, slider, 6, SpringLayout.SOUTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, slider, 10, SpringLayout.WEST, window.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, slider, -10, SpringLayout.EAST, window.getContentPane());
		slider.setValue(0);
		window.getContentPane().add(slider);

		right = new JButton("->");
		springLayout.putConstraint(SpringLayout.SOUTH, right, -6, SpringLayout.NORTH, slider);
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (turn + 1 <= slider.getMaximum()) {
					slider.setValue(++turn);
					refresh();
				}
			}
		});
		window.getContentPane().add(right);

		end = new JButton(">>");
		springLayout.putConstraint(SpringLayout.NORTH, end, 0, SpringLayout.NORTH, right);
		springLayout.putConstraint(SpringLayout.WEST, end, 6, SpringLayout.EAST, right);
		end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				turn = slider.getMaximum();
				slider.setValue(turn);
				refresh();
			}
		});
		window.getContentPane().add(end);

		left = new JButton("<-");
		springLayout.putConstraint(SpringLayout.WEST, right, 6, SpringLayout.EAST, left);
		springLayout.putConstraint(SpringLayout.SOUTH, left, 0, SpringLayout.SOUTH, panel);
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (turn - 1 >= slider.getMinimum()) {
					slider.setValue(--turn);
					refresh();
				}
			}
		});
		window.getContentPane().add(left);

		start = new JButton("<<");
		springLayout.putConstraint(SpringLayout.WEST, left, 6, SpringLayout.EAST, start);
		springLayout.putConstraint(SpringLayout.WEST, start, 6, SpringLayout.EAST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, start, 0, SpringLayout.SOUTH, panel);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				turn = slider.getMinimum();
				slider.setValue(turn);
				refresh();
			}
		});
		window.getContentPane().add(start);

		open = new JButton("Open...");
		springLayout.putConstraint(SpringLayout.WEST, open, 6, SpringLayout.EAST, panel);
		springLayout.putConstraint(SpringLayout.EAST, open, -139, SpringLayout.EAST, window.getContentPane());
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				open();
			}
		});
		window.getContentPane().add(open);

		textArea = new JTextArea();
		springLayout.putConstraint(SpringLayout.NORTH, open, 6, SpringLayout.SOUTH, textArea);
		springLayout.putConstraint(SpringLayout.NORTH, textArea, 10, SpringLayout.NORTH, window.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, textArea, -104, SpringLayout.SOUTH, window.getContentPane());
		textArea.setEditable(false);
		springLayout.putConstraint(SpringLayout.WEST, textArea, 6, SpringLayout.EAST, panel);
		springLayout.putConstraint(SpringLayout.EAST, textArea, -10, SpringLayout.EAST, window.getContentPane());
		window.getContentPane().add(textArea);
		
		textField = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, textField, 7, SpringLayout.SOUTH, textArea);
		springLayout.putConstraint(SpringLayout.WEST, textField, 6, SpringLayout.EAST, open);
		springLayout.putConstraint(SpringLayout.SOUTH, textField, -6, SpringLayout.NORTH, right);
		springLayout.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, slider);
		textField.setEditable(false);
		window.getContentPane().add(textField);
		textField.setColumns(10);

		imageMap = new HashMap<Tile, Image>();
		try {
			for (Tile t : Tile.values()) {
				imageMap.put(t, ImageIO.read(new File("./image/" + t.toString().toLowerCase() + ".png")));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void run() {
		window.setVisible(true);

	}

	private void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(Color.WHITE);
		g2.clearRect(0, 0, panel.getWidth(), panel.getHeight());
		if (game != null) {
			Tile[][] map = game.getMapArr(turn);
			int size = reader.getSize();
			for (int j = 0; j < size; j++) {
				for (int i = 0; i < size; i++) {
					g2.drawImage(imageMap.get(map[i][j]), i * (400 / size), j * (400 / size), 400 / size, 400 / size, null);
				}
			}
		}		
	}

	private void refresh() {
		if (game != null)
			textArea.setText(game.getInfo(turn));
		panel.repaint();
	}

	private void open() {
		JFileChooser filechooser = new JFileChooser();
		int selected = filechooser.showOpenDialog(window);
		if (selected == JFileChooser.APPROVE_OPTION) {
			try {
				textField.setText(filechooser.getSelectedFile().getName());
				reader = new Reader();
				reader.load(filechooser.getSelectedFile().getAbsolutePath());
				game = new Game(reader.getSeed(), reader.getSize(), reader.getWumpus(), reader.getGold(),
						reader.getProb(), reader.getArrow());
				game.run(reader);
				slider.setMinimum(0);
				slider.setMaximum(reader.getActionLength() + 1);
				turn = 0;
				slider.setValue(0);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
}
