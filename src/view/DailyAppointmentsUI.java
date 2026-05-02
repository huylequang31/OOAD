package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import model.Appointment;
import model.Session;
import controller.AppointmentController;

public class DailyAppointmentsUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private final Date date;
	private final AppointmentController appointmentController = new AppointmentController();
	private final Color primaryColor = new Color(52, 168, 83);
	private final Color hoverColor = new Color(45, 136, 70);
	private final Color backgroundColor = new Color(241, 248, 244);
	private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
	private final Font titleFont = new Font("Segoe UI", Font.PLAIN, 24);

	private DefaultTableModel tableModel;
	private JTable table;

	public DailyAppointmentsUI(Date date) {
		this.date = date;
		SimpleDateFormat titleFormat = new SimpleDateFormat("dd/MM/yyyy");

		setTitle("Lịch hẹn ngày " + titleFormat.format(date));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(120, 120, 760, 460);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel(new BorderLayout(0, 16)) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(backgroundColor);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.dispose();
			}
		};
		contentPane.setBorder(new EmptyBorder(18, 20, 18, 20));
		setContentPane(contentPane);

		JLabel title = new JLabel("Lịch hẹn ngày " + titleFormat.format(date));
		title.setFont(titleFont);
		title.setForeground(new Color(60, 64, 67));
		contentPane.add(title, BorderLayout.NORTH);
		contentPane.add(createTablePanel(), BorderLayout.CENTER);
		contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
		loadAppointments();
	}

	private JPanel createTablePanel() {
		JPanel panel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.WHITE);
				g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
				g2.setColor(new Color(218, 220, 224));
				g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
				g2.dispose();
			}
		};
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(12, 12, 12, 12));

		tableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableModel.addColumn("STT");
		tableModel.addColumn("Tên sự kiện");
		tableModel.addColumn("Vị trí");
		tableModel.addColumn("Thời gian");
		tableModel.addColumn("Kiểu");

		table = new JTable(tableModel);
		table.setFont(mainFont);
		table.setRowHeight(40);
		table.setGridColor(new Color(232, 234, 237));
		table.setSelectionBackground(new Color(230, 244, 234));
		table.setSelectionForeground(primaryColor);

		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Segoe UI", Font.BOLD, 14));
		header.setBackground(Color.WHITE);
		header.setForeground(new Color(95, 99, 104));
		header.setPreferredSize(new Dimension(header.getWidth(), 40));
		header.setDefaultRenderer(new DefaultTableCellRenderer() {
			@Override
			public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				label.setBackground(Color.WHITE);
				label.setForeground(new Color(95, 99, 104));
				label.setHorizontalAlignment(SwingConstants.CENTER);
				return label;
			}
		});

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (!isSelected) {
					c.setBackground(Color.WHITE);
				}
				setHorizontalAlignment(SwingConstants.CENTER);
				return c;
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(null);
		panel.add(scrollPane, BorderLayout.CENTER);
		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 4));
		panel.setOpaque(false);

		JButton add = createStyledButton("Thêm lịch hẹn");
		add.addActionListener(e -> {
			new AssignmentDetail(date).setVisible(true);
			dispose();
		});

		JButton refresh = createStyledButton("Làm mới");
		refresh.setBackground(new Color(52, 168, 83));
		refresh.addActionListener(e -> loadAppointments());

		JButton close = createStyledButton("Đóng");
		close.setBackground(Color.WHITE);
		close.setForeground(new Color(60, 64, 67));
		close.addActionListener(e -> dispose());

		panel.add(add);
		panel.add(refresh);
		panel.add(close);
		return panel;
	}

	private void loadAppointments() {
		tableModel.setRowCount(0);
		List<Appointment> appointments = appointmentController.getAppointmentsByDate(Session.getUserId(), date);
		int stt = 1;
		for (Appointment appointment : appointments) {
			tableModel.addRow(new Object[] {
				stt++,
				appointment.getName(),
				appointment.getLocation(),
				String.format("%02d:%02d - %02d:%02d",
					appointment.getStartHour(), appointment.getStartMinute(),
					appointment.getEndHour(), appointment.getEndMinute()),
				appointment.getTypeAppointment()
			});
		}
		if (appointments.isEmpty()) {
			DialogUtils.showInfo(this, "Thông báo", "Ngày này chưa có lịch hẹn.");
		}
	}

	private JButton createStyledButton(String text) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setForeground(Color.WHITE);
		btn.setBackground(primaryColor);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setContentAreaFilled(false);
		btn.setOpaque(false);
		btn.setPreferredSize(new Dimension(140, 38));
		btn.setUI(new BasicButtonUI() {
			@Override
			public void paint(Graphics g, javax.swing.JComponent c) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				AbstractButton b = (AbstractButton) c;
				Color bg = b.getModel().isPressed() ? primaryColor.darker()
						: b.getModel().isRollover() ? hoverColor
						: b.getBackground();
				if (b.getBackground().equals(Color.WHITE)) {
					bg = b.getModel().isRollover() ? new Color(241, 248, 244) : Color.WHITE;
					g2.setColor(bg);
					g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 18, 18);
					g2.setColor(new Color(218, 220, 224));
					g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 18, 18);
				} else {
					g2.setColor(bg);
					g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 18, 18);
				}
				g2.dispose();
				super.paint(g, c);
			}
		});
		btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btn.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
			}
		});
		return btn;
	}
}
