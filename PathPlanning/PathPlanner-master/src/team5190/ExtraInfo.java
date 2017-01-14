package team5190;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class ExtraInfo extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;

	public CalculatePath path;

	/**
	 * Create the dialog.
	 */
	public ExtraInfo(CalculatePath path) {
		super(path);
		this.path = path;
		setTitle("Extra Path Information");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("fill, inset 1", "[grow]", "[grow]"));
		{
			table = new JTable();
			contentPanel.add(table, "cell 0 0,grow");
			table.setModel(new ExtraInfoTableModel(this));
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton closeBtn = new JButton("Close");
				closeBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ExtraInfo.this.dispose();
					}
				});
				closeBtn.setActionCommand("Cancel");
				buttonPane.add(closeBtn);
			}
		}
	}

	public void updateTable() {
		ExtraInfoTableModel model = (ExtraInfoTableModel) table.getModel();
		model.fireTableDataChanged();
		contentPanel.updateUI();
	}

}
