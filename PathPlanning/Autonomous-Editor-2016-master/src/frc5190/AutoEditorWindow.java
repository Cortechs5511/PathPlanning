package frc5190;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import frc5190.auto.AutoCommand;
import frc5190.auto.AutoPathPlanner;
import frc5190.auto.AutoPoint;
import frc5190.auto.actions.AutoAction;
import frc5190.auto.actions.AutoGoto;
import frc5190.auto.actions.AutoLowerArm;
import frc5190.auto.actions.AutoRaiseArm;
import frc5190.auto.actions.AutoShoot;
import frc5190.auto.optionpanel.GotoOptions;
import frc5190.auto.optionpanel.LowerArmOptions;
import frc5190.auto.optionpanel.RaiseArmOptions;
import frc5190.auto.optionpanel.TurnOptions;
import frc5190.canvas.FieldCanvas;
import frc5190.table.AutonomousAutoActionModel;
import frc5190.table.AutonomousCommandsModel;
import net.miginfocom.swing.MigLayout;

public class AutoEditorWindow extends JFrame {

	private AutoPathPlanner planner;
	private JPanel contentPane;
	private FieldCanvas canvas;
	private JButton btnNew;
	private JButton btnLoad;
	private JButton btnSave;
	private JPanel panel_1;
	private JLabel lblNewLabel;
	private JTable commandTable;
	private JButton btnMoveRobot;
	private JLabel lblCommands;
	private JButton btnAddPoint_1;
	private JButton btnRemovePoint;
	private JPanel panel_3;
	private JLabel lblPointOptions;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_7;
	private JLabel lblPlayOptions;
	private JLabel lblName;
	private JTextField nameTextField;
	private JPanel panel_8;
	private JPanel panel_6;
	private JTable pointTable;
	private JLabel lblPoints;
	private JPanel panel_9;
	private JScrollPane scrollPane;
	private JPanel panel_10;
	private JScrollPane scrollPane_1;
	private JPanel leftPanel;
	private JPanel canvasPanel;
	private JPanel pointButtonPanel;
	private JPanel pointsPanel;
	private JPanel autoActionButtons;
	private JButton btnRaiseArm;
	private JButton btnLowerArm;
	private JButton btnDeleteAction;
	private JPanel panel_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UIManager.put("ButtonUI", "frc5190.FRCButtonUI");
					AutoEditorWindow frame = new AutoEditorWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AutoEditorWindow() {
		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent arg0) {
				recalculateLeftPanel();
			}
		});
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(AutoEditorWindow.class.getResource("/frc5190/canvas/images/falcon.png")));
		setTitle("FRC Stronghold 2016 - Autonomous Editor - Team 5190");
		planner = new AutoPathPlanner(this);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1274, 825);
		contentPane = new JPanel();
		contentPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (canvas.selectedPoint != null) {
					planner.removePoint(canvas.selectedPoint);
					canvas.selectedPoint = null;
					canvas.repaint();
				}
			}
		});
		contentPane.setBackground(Color.WHITE);
		contentPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				canvas.repaint();
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow,fill][]", "[grow,fill]"));

		leftPanel = new JPanel();
		pointsPanel = new JPanel();
		pointsPanel.setBackground(Color.WHITE);
		leftPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				recalculateLeftPanel();
			}
		});
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(leftPanel, "cell 0 0,grow");

		canvasPanel = new JPanel();
		canvasPanel.setBorder(null);
		canvasPanel.setBounds(8, 8, 598, 516);
		leftPanel.setLayout(null);
		leftPanel.add(canvasPanel);
		canvasPanel.setLayout(null);

		canvas = new FieldCanvas(planner);
		canvas.setBorder(null);
		canvas.setBounds(0, 0, 598, 638);
		canvasPanel.add(canvas);
		canvas.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (canvas.movingRobot) {
					canvas.robotRotation += e.getWheelRotation() * 45;
					canvas.repaint();
				}
			}
		});
		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (canvas.movingRobot) {
					ArrayList<AutoCommand> commands = planner.getCommands();
					planner.setRobotStartPos(new AutoGoto(canvas.robot.clone()));
					planner.setRobotStartRotation(canvas.robotRotation);
					planner.setCommands(commands);
					canvas.movingRobot = false;
				} else if (canvas.addingPoint) {
					AutoPoint point = planner.addPoint();
					point.x = canvas.point.x;
					point.y = canvas.point.y;
					canvas.addingPoint = false;
				} else {
					AutoPoint point = canvas.getPoint(e.getX(), e.getY());
					canvas.selectedLine = false;
					if (point == null) {
						point = canvas.getLine(e.getX(), e.getY());
						canvas.selectedLine = true;
						if (point == null) {
							setOptions(null);
						} else {
							setOptions(new GotoOptions(AutoEditorWindow.this, planner.getAction(point)));
						}
					} else {
						setOptions(new TurnOptions(AutoEditorWindow.this, planner.getAction(point)));
					}
					canvas.selectedPoint = point;
				}
				fireUpdate();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				AutoPoint point = canvas.getPoint(e.getX(), e.getY());
				canvas.selectedLine = false;
				if (point != null) {
					// setOptions(new TurnOptions(AutoEditorWindow.this,
					// point));
				}
				canvas.selectedPoint = point;
			}
		});
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (canvas.selectedPoint != null) {
					canvas.selectedPoint.x = canvas.point.x;
					canvas.selectedPoint.y = canvas.point.y;
					canvas.mouseX = e.getX();
					canvas.mouseY = e.getY();
					fireUpdate();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				canvas.mouseX = e.getX();
				canvas.mouseY = e.getY();
				canvas.repaint();
			}
		});

		pointsPanel.setBounds(8, 535, 598, 171);
		leftPanel.add(pointsPanel);
		pointsPanel.setLayout(new MigLayout("insets 5 0 0 0", "[grow 50,fill][grow 50,fill]", "[fill][grow,fill]"));

		pointButtonPanel = new JPanel();
		pointButtonPanel.setBackground(Color.WHITE);
		pointsPanel.add(pointButtonPanel, "flowx,cell 0 0");
		pointButtonPanel.setLayout(new MigLayout("insets 0", "[][grow,fill]", "[]"));

		btnMoveRobot = new JButton("Move Robot");
		pointButtonPanel.add(btnMoveRobot, "flowx,cell 0 0");

		btnAddPoint_1 = new JButton("Add Point");
		pointButtonPanel.add(btnAddPoint_1, "cell 0 0");

		btnRemovePoint = new JButton("Remove Point");
		pointButtonPanel.add(btnRemovePoint, "cell 0 0");
		btnRemovePoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canvas.selectedPoint != null) {
					planner.removePoint(canvas.selectedPoint);
					canvas.selectedPoint = null;
					fireUpdate();
				}
			}
		});
		btnAddPoint_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (canvas.selectedPoint != null && canvas.selectedLine) {
					AutoPoint end = canvas.selectedPoint;
					AutoPoint start = planner.getLineStart(end);
					AutoPoint average = start.average(end);
					planner.addPoint(start, average);
					fireUpdate();
				} else {
					canvas.addingPoint = true;
					canvas.movingRobot = false;
				}
			}
		});
		btnMoveRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				canvas.movingRobot = true;
				canvas.addingPoint = false;
			}
		});

		panel_6 = new JPanel();
		pointsPanel.add(panel_6, "cell 0 1");
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_6.setBackground(Color.WHITE);
		panel_6.setLayout(new MigLayout("", "[grow][]", "[][grow][]"));

		lblPoints = new JLabel("Autonomous Actions");
		lblPoints.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		panel_6.add(lblPoints, "cell 0 0");

		panel_2 = new JPanel();
		panel_6.add(panel_2, "cell 1 0");
		panel_2.setBackground(Color.WHITE);
		panel_2.setLayout(new MigLayout("insets 0", "[grow,right]", "[fill]"));

		btnDeleteAction = new JButton("Delete Action");
		btnDeleteAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				planner.getAutoActions().remove(pointTable.getSelectedRow());
				setOptions(null);
				fireUpdate();
			}
		});
		panel_2.add(btnDeleteAction, "cell 0 0,alignx right,aligny top");

		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setBorder(new LineBorder(Color.BLACK));
		panel_6.add(scrollPane, "cell 0 1 2 1,grow");

		panel_9 = new JPanel();
		scrollPane.setViewportView(panel_9);
		panel_9.setLayout(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));

		pointTable = new JTable();
		pointTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent paramListSelectionEvent) {
				AutoAction action = planner.getAutoActions().get(pointTable.getSelectedRow());
				if (action instanceof AutoGoto) {
					setOptions(new GotoOptions(AutoEditorWindow.this, (AutoGoto) action));
				} else if (action instanceof AutoRaiseArm) {
					setOptions(new RaiseArmOptions(AutoEditorWindow.this, (AutoRaiseArm) action));
				} else if (action instanceof AutoLowerArm) {
					setOptions(new LowerArmOptions(AutoEditorWindow.this, (AutoLowerArm) action));
				}
			}
		});
		pointTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pointTable.setDragEnabled(true);
		pointTable.setDropMode(DropMode.ON);
		pointTable.setTransferHandler(new TransferHandler() {

			public int getSourceActions(JComponent comp) {
				return MOVE;
			}

			private int index = 0;
			private TransferableAutoAction action;

			public Transferable createTransferable(JComponent comp) {
				index = pointTable.getSelectedRow();
				action = new TransferableAutoAction(planner.getAutoActions().get(index));
				return action;
			}

			public void exportDone(JComponent comp, Transferable trans, int action) {
			}

			public boolean canImport(TransferSupport support) {
				return true;
			}

			public boolean importData(TransferSupport support) {
				// fetch the drop location
				JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();

				int row = dl.getRow();
				planner.getAutoActions().remove(index);
				planner.getAutoActions().add(row, action.getAutoAction());
				System.out.println(index + " : " + row);
				fireUpdate();
				return true;
			}
		});
		panel_9.add(pointTable, "cell 0 0,alignx left,aligny top");
		pointTable.setCellSelectionEnabled(true);
		pointTable.setBorder(null);
		pointTable.setBackground(Color.WHITE);
		pointTable.setModel(new AutonomousAutoActionModel(planner));
		autoActionButtons = new JPanel();
		autoActionButtons.setBackground(Color.WHITE);
		panel_6.add(autoActionButtons, "cell 0 2 2 1,grow");

		JButton btnActionShoot = new JButton("Shoot");
		btnActionShoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				addAction(new AutoShoot());
			}
		});
		autoActionButtons.setLayout(new MigLayout("insets 0", "[][][]", "[]"));
		autoActionButtons.add(btnActionShoot, "cell 0 0,alignx left,aligny top");

		btnRaiseArm = new JButton("Raise Arm");
		btnRaiseArm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				addAction(new AutoRaiseArm());
			}
		});
		autoActionButtons.add(btnRaiseArm, "cell 1 0");

		btnLowerArm = new JButton("Lower Arm");
		btnLowerArm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				addAction(new AutoLowerArm());
			}
		});
		autoActionButtons.add(btnLowerArm, "cell 2 0");

		panel_3 = new JPanel();
		pointsPanel.add(panel_3, "cell 1 0 1 2");
		panel_3.setBackground(Color.WHITE);
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setLayout(new MigLayout("", "[grow,fill]", "[][]"));

		panel_5 = new JPanel();
		panel_5.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) panel_5.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_3.add(panel_5, "cell 0 0,grow");

		lblPointOptions = new JLabel("Action Options");
		panel_5.add(lblPointOptions);
		lblPointOptions.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		contentPane.add(panel, "cell 1 0,grow");
		panel.setLayout(new MigLayout("", "[grow]", "[][grow][][grow]"));

		btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				planner.setAutoActions(new ArrayList<AutoAction>());
				planner.setRobotStartPos(new AutoGoto());
				planner.setRobotStartRotation(0);
				canvas.addingPoint = false;
				canvas.movingRobot = false;
				canvas.selectedPoint = null;
				fireUpdate();
			}
		});
		panel.add(btnNew, "flowx,cell 0 0,alignx right,aligny top");

		lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(
				new ImageIcon(AutoEditorWindow.class.getResource("/frc5190/canvas/images/first-stronghold.jpg")));
		panel.add(lblNewLabel, "flowy,cell 0 1,aligny top");

		panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_7.setBackground(Color.WHITE);
		panel.add(panel_7, "cell 0 2,grow");
		panel_7.setLayout(new MigLayout("", "[grow]", "[][grow,fill]"));

		lblPlayOptions = new JLabel("Play Options");
		lblPlayOptions.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		panel_7.add(lblPlayOptions, "flowy,cell 0 0");

		panel_8 = new JPanel();
		panel_8.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_8.setBackground(Color.WHITE);
		panel_7.add(panel_8, "cell 0 1,grow");
		panel_8.setLayout(new MigLayout("", "[][grow,fill]", "[]"));

		lblName = new JLabel("Name:");
		panel_8.add(lblName, "cell 0 0,alignx left,aligny center");

		nameTextField = new JTextField();
		panel_8.add(nameTextField, "cell 1 0");
		nameTextField.setColumns(10);

		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1, "cell 0 3,grow");
		panel_1.setLayout(new MigLayout("", "[grow]", "[][grow,fill]"));

		lblCommands = new JLabel("Commands");
		lblCommands.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		panel_1.add(lblCommands, "cell 0 0");

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_1.add(scrollPane_1, "cell 0 1,grow");

		panel_10 = new JPanel();
		scrollPane_1.setViewportView(panel_10);
		panel_10.setLayout(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));

		commandTable = new JTable();
		panel_10.add(commandTable, "cell 0 0,grow");
		commandTable.setCellSelectionEnabled(true);
		commandTable.setBackground(Color.WHITE);
		commandTable.setBorder(null);
		commandTable.setModel(new AutonomousCommandsModel(planner));

		btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(AutoEditorWindow.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					planner.open(file);
					fireUpdate();
				}
			}
		});
		panel.add(btnLoad, "cell 0 0,alignx right,aligny top");

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(AutoEditorWindow.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					planner.save(file);
				}
			}
		});
		panel.add(btnSave, "cell 0 0,alignx right");
	}

	public void recalculateLeftPanel() {
		int padding = 10;
		int width = leftPanel.getWidth() - padding * 2;
		int height = leftPanel.getHeight() - padding * 2;
		canvas.recalc(width, leftPanel.getHeight() - (int) pointsPanel.getMinimumSize().getHeight());
		canvas.setSize(width, canvas.PIXEL_HEIGHT);
		canvasPanel.setSize(canvas.getWidth(), canvas.getHeight());
		canvasPanel.setLocation(padding, padding);
		pointsPanel.setSize(width, height - canvas.getHeight());
		pointsPanel.setLocation(padding, height - pointsPanel.getHeight() + padding);
		fireUpdate();
	}

	public void setOptions(JPanel panel) {
		if (panel_4 != null) {
			panel_3.remove(panel_4);
		}
		if (panel != null) {
			panel_4 = panel;
			panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
			panel_4.setBackground(Color.WHITE);
			panel_3.add(panel_4, "flowy,cell 0 1,grow");
		}
		fireUpdate();
	}

	public void addAction(AutoAction action) {
		planner.addAction(action);
		fireUpdate();
	}

	public void fireUpdate() {
		((AbstractTableModel) commandTable.getModel()).fireTableDataChanged();
		((AbstractTableModel) pointTable.getModel()).fireTableDataChanged();
		commandTable.repaint();
		pointTable.repaint();
		canvas.repaint();
		scrollPane.repaint();
		scrollPane_1.repaint();
		contentPane.updateUI();
	}

	public String getName() {
		return nameTextField.getText();
	}

	public void setName(String name) {
		nameTextField.setText(name);
	}
}
