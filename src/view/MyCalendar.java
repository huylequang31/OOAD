package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

import model.Appointment;
import model.Session;
import controller.AppointmentController;

public class MyCalendar extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblNewLabel;
    private JButton btnEdit;
    private JButton btnSave;
    private AppointmentController appointmentController = new AppointmentController();
    
    
    private final Color primaryColor = new Color(52, 168, 83);
    private final Color hoverColor = new Color(45, 136, 70);
    private final Color backgroundColor = new Color(241, 248, 244);
    private final Font titleFont = new Font("Segoe UI", Font.PLAIN, 24);
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
    
    
    private boolean dataChanged = false;

    public void getAllAppointment() {
        tableModel.setRowCount(0);
        int stt = 1;
        for (Appointment appointment : appointmentController.getAppointmentsByUser(Session.getUserId())) {
            tableModel.addRow(new Object[] {
                stt,
                appointment.getId(),
                appointment.getName(),
                appointment.getLocation(),
                appointment.getMeetingDate(),
                formatTime(appointment.getStartHour(), appointment.getStartMinute()),
                formatTime(appointment.getEndHour(), appointment.getEndMinute()),
                appointment.getTypeAppointment()
            });
            stt++;
        }
    }
    
    public int updateInfo(int appId, String name, String location, Date date, int start, int startMinute, int end, int endMinute, String type) {
        return appointmentController.updateInfo(Session.getUserId(), appId, name, location, date, start, startMinute, end, endMinute, type);
    }
    
    
    public boolean deleteAppointment(int appointmentId) {
        return appointmentController.deleteAppointment(appointmentId);
    }
    
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String formatTime(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }

    private int[] parseTime(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Thời gian không được để trống");
        }
        String[] parts = value.trim().split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Thời gian phải có dạng HH:mm");
        }
        return new int[] {
            Integer.parseInt(parts[0].trim()),
            Integer.parseInt(parts[1].trim())
        };
    }
    
    public MyCalendar() {
        setTitle("Lịch hẹn của tôi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1120, 650);
        setLocationRelativeTo(null);
        
        
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                
                g2.setColor(backgroundColor);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        contentPane.setBorder(new EmptyBorder(18, 20, 18, 20));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 20));
        
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        lblNewLabel = new JLabel("Danh sách các buổi hẹn");
        lblNewLabel.setFont(titleFont);
        lblNewLabel.setForeground(new Color(60, 64, 67));
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titlePanel.add(lblNewLabel, BorderLayout.CENTER);
        
        contentPane.add(titlePanel, BorderLayout.NORTH);
        
        
        JPanel tablePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(
                    0, 0, getWidth() - 1, getHeight() - 1, 16, 16
                ));
                g2.setColor(new Color(218, 220, 224));
                g2.draw(new RoundRectangle2D.Double(
                    0, 0, getWidth() - 1, getHeight() - 1, 16, 16
                ));
                g2.dispose();
            }
        };
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        
        table = new JTable();
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    int column = table.getSelectedColumn();
                    
                    if (row >= 0 && column >= 2 && column <= 7) {
                        table.editCellAt(row, column);
                        if (table.getEditorComponent() != null) {
                            table.getEditorComponent().requestFocus();
                        }
                    }
                }
            }
        });
        
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                
                return column >= 2 && column <= 7; 
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) { 
                    return Date.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        
        tableModel.addColumn("STT");
        tableModel.addColumn("Mã sự kiện");
        tableModel.addColumn("Tên sự kiện");
        tableModel.addColumn("Vị trí");
        tableModel.addColumn("Ngày diễn ra");
        tableModel.addColumn("Giờ bắt đầu");
        tableModel.addColumn("Giờ kết thúc");
        tableModel.addColumn("Kiểu nhóm");
        
        table.setModel(tableModel);
        table.setFont(mainFont);
        table.setRowHeight(42);
        table.setShowGrid(true);
        table.setGridColor(new Color(232, 234, 237));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(230, 244, 234));
        table.setSelectionForeground(primaryColor);
        
        
        JTableHeader header = table.getTableHeader();
        header.setFont(headerFont);
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(95, 99, 104));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(218, 220, 224)));
        
        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setBackground(Color.WHITE);
                label.setForeground(new Color(95, 99, 104));
                label.setFont(headerFont);
                label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });
        
        
        DefaultCellEditor dateCellEditor = new DefaultCellEditor(new JTextField()) {
            private JTextField textField;
            private Date originalValue;
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                        boolean isSelected, int row, int column) {
                textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                
                if (value instanceof Date) {
                    originalValue = (Date) value;
                    textField.setText(dateFormat.format(value));
                } else {
                    originalValue = null;
                    textField.setText("");
                }
                return textField;
            }
            
            @Override
            public Object getCellEditorValue() {
                try {
                    return dateFormat.parse(textField.getText());
                } catch (ParseException e) {
                    DialogUtils.showError(MyCalendar.this,
                        "Lỗi",
                        "Định dạng ngày không hợp lệ. Sử dụng định dạng yyyy-MM-dd.");
                    return originalValue;
                }
            }
        };
        
        
        DefaultCellEditor hourCellEditor = new DefaultCellEditor(new JTextField()) {
            private JTextField textField;
            private Integer originalValue;
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                       boolean isSelected, int row, int column) {
                textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                
                if (value instanceof Integer) {
                    originalValue = (Integer) value;
                    textField.setText(value.toString());
                } else {
                    originalValue = null;
                    textField.setText("");
                }
                return textField;
            }
            
            @Override
            public Object getCellEditorValue() {
                try {
                    int value = Integer.parseInt(textField.getText());
                    if (value < 0 || value > 23) {
                        DialogUtils.showError(MyCalendar.this,
                            "Lỗi",
                            "Giờ phải từ 0 đến 23.");
                        return originalValue;
                    }
                    return value;
                } catch (NumberFormatException e) {
                    DialogUtils.showError(MyCalendar.this,
                        "Lỗi",
                        "Vui lòng nhập một số nguyên.");
                    return originalValue;
                }
            }
        };
        
        
        table.getColumnModel().getColumn(4).setCellEditor(dateCellEditor);
        
        
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                                                          boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value instanceof Date) {
                    setText(dateFormat.format((Date) value));
                }
                
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }
                
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setHorizontalAlignment(SwingConstants.CENTER);
                
                return c;
            }
        });
        
        
        CellEditorListener cellEditorListener = new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                dataChanged = true;
                if (btnSave != null) {
                    btnSave.setEnabled(true);
                }
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                
            }
        };

        
        table.getModel().addTableModelListener(e -> {
            dataChanged = true;
            if (btnSave != null) {
                btnSave.setEnabled(true);
            }
        });
        
	    
	    
	    if (table.getColumnModel().getColumn(4).getCellEditor() != null) {
	        table.getColumnModel().getColumn(4).getCellEditor().addCellEditorListener(cellEditorListener);
	    }
	    if (table.getColumnModel().getColumn(5).getCellEditor() != null) {
	        table.getColumnModel().getColumn(5).getCellEditor().addCellEditorListener(cellEditorListener);
	    }
	    if (table.getColumnModel().getColumn(6).getCellEditor() != null) {
	        table.getColumnModel().getColumn(6).getCellEditor().addCellEditorListener(cellEditorListener);
	    }
        
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }
                
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setHorizontalAlignment(SwingConstants.CENTER);
                
                return c;
            }
        });
        
        
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(100); 
        columnModel.getColumn(2).setPreferredWidth(180);
        columnModel.getColumn(3).setPreferredWidth(150);
        columnModel.getColumn(4).setPreferredWidth(120);
        columnModel.getColumn(5).setPreferredWidth(110); 
        columnModel.getColumn(6).setPreferredWidth(110); 
        columnModel.getColumn(7).setPreferredWidth(100);
        
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        
        
        scrollPane.setColumnHeaderView(header);
        
        
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(129, 201, 149);
                this.trackColor = Color.WHITE;
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
            
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }
                
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                                thumbBounds.width - 4, thumbBounds.height - 4, 
                                10, 10);
                g2.dispose();
            }
        });
        
        
        getAllAppointment();
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(tablePanel, BorderLayout.CENTER);
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 4)); 
        
        btnSave = createStyledButton("Lưu thay đổi");
        btnSave.setEnabled(false); 
        btnSave.addActionListener(e -> saveAllChanges());
        
















        
        JButton btnDetail = createStyledButton("Chi tiết");
        btnDetail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    DialogUtils.showInfo(contentPane,
                        "Thông báo",
                        "Vui lòng chọn một buổi hẹn từ danh sách");
                    return;
                }
                
                int appId = (Integer)table.getValueAt(selectedRow, 1);
                String name = (String)table.getValueAt(selectedRow, 2);
                String location = (String)table.getValueAt(selectedRow, 3);
                Date date = null;
                try {
                    Object dateValue = table.getValueAt(selectedRow, 4);
                    if (dateValue instanceof String) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        date = format.parse((String) dateValue);
                    } else if (dateValue instanceof Date) {
                        date = (Date) dateValue;
                    }
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                
                int[] start = parseTime((String)table.getValueAt(selectedRow, 5));
                int[] end = parseTime((String)table.getValueAt(selectedRow, 6));
                String type = (String)table.getValueAt(selectedRow, 7);
                
                InfoDetail form4 = new InfoDetail(appId, name, location, date, start[0], start[1], end[0], end[1], type);
                form4.setVisible(true);
            }
        });
        
        
        JButton btnDelete = createStyledButton("Xóa");
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                DialogUtils.showInfo(this,
                    "Thông báo",
                    "Vui lòng chọn một buổi hẹn để xóa");
                return;
            }

            int appId = (Integer) table.getValueAt(row, 1);
            String appName = (String) table.getValueAt(row, 2);
            int confirm = DialogUtils.showYesNo(this,
                "Xác nhận xóa",
                "Bạn có chắc chắn muốn xóa buổi hẹn '" + appName + "' không?");

            if (confirm == JOptionPane.YES_OPTION) {
                if (deleteAppointment(appId)) {
                    DialogUtils.showInfo(this,
                        "Thành công",
                        "Xóa buổi hẹn thành công!");
                    tableModel.setRowCount(0);
                    getAllAppointment();
                } else {
                    DialogUtils.showError(this,
                        "Lỗi",
                        "Không thể xóa buổi hẹn. Vui lòng thử lại sau.");
                }
            }
        });
        
        JButton btnClose = createStyledButton("Đóng");
        btnClose.setBackground(Color.WHITE);
        btnClose.setForeground(new Color(60, 64, 67));
        btnClose.addActionListener(e -> {
            if (dataChanged) {
                int option = DialogUtils.showYesNoCancel(
                    this,
                    "Thay đổi chưa lưu",
                    "Bạn có thay đổi chưa được lưu. Bạn có muốn lưu trước khi đóng không?"
                );
                
                if (option == JOptionPane.YES_OPTION) {
                    if (saveAllChanges()) {
                        dispose();
                    }
                } else if (option == JOptionPane.NO_OPTION) {
                    dispose();
                }
                
            } else {
                dispose();
            }
        });
        
        JButton btnRefresh = createStyledButton("Làm mới");
        btnRefresh.setBackground(new Color(52, 168, 83));
        btnRefresh.addActionListener(e -> {
            if (dataChanged) {
                int option = DialogUtils.showYesNo(
                    this,
                    "Thay đổi chưa lưu",
                    "Bạn có thay đổi chưa được lưu. Bạn có muốn làm mới và hủy các thay đổi không?"
                );
                
                if (option == JOptionPane.YES_OPTION) {
                    getAllAppointment();
                    dataChanged = false;
                    btnSave.setEnabled(false);
                }
            } else {
                getAllAppointment();
            }
        });
        
        buttonPanel.add(btnSave);

        buttonPanel.add(btnDetail);
        buttonPanel.add(btnDelete); 
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClose);
        
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    
    private boolean saveAllChanges() {
        boolean allSaved = true;
        
        
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        
        
        for (int row = 0; row < table.getRowCount(); row++) {
            try {
                int appId = (Integer) table.getValueAt(row, 1);
                String name = (String) table.getValueAt(row, 2);
                String location = (String) table.getValueAt(row, 3);
                Date date = (Date) table.getValueAt(row, 4);
                int[] start = parseTime((String) table.getValueAt(row, 5));
                int[] end = parseTime((String) table.getValueAt(row, 6));
                int startHour = start[0];
                int startMinute = start[1];
                int endHour = end[0];
                int endMinute = end[1];
                String type = (String) table.getValueAt(row, 7);
                
                
                if (name == null || name.trim().isEmpty()) {
                    DialogUtils.showError(this, "Lỗi", "Tên sự kiện không được để trống ở hàng " + (row + 1));
                    allSaved = false;
                    continue;
                }
                
                if (location == null || location.trim().isEmpty()) {
                    DialogUtils.showError(this, "Lỗi", "Vị trí không được để trống ở hàng " + (row + 1));
                    allSaved = false;
                    continue;
                }
                
                if (date == null) {
                    DialogUtils.showError(this, "Lỗi", "Ngày không hợp lệ ở hàng " + (row + 1));
                    allSaved = false;
                    continue;
                }
                
                if (startHour < 0 || startHour > 23 || endHour < 0 || endHour > 23 || startMinute < 0 || startMinute > 59 || endMinute < 0 || endMinute > 59) {
                    DialogUtils.showError(this, "Lỗi", "Thời gian phải có dạng HH:mm hợp lệ ở hàng " + (row + 1));
                    allSaved = false;
                    continue;
                }
                
                if (startHour * 60 + startMinute >= endHour * 60 + endMinute) {
                    DialogUtils.showError(this, "Lỗi", "Giờ bắt đầu phải nhỏ hơn giờ kết thúc ở hàng " + (row + 1));
                    allSaved = false;
                    continue;
                }
                
                
                int result = updateInfo(appId, name, location, date, startHour, startMinute, endHour, endMinute, type);
                if (result == -2) {
                    DialogUtils.showError(this, "Lỗi", "Lịch hẹn ở hàng " + (row + 1) + " bị trùng thời gian với lịch hẹn khác của bạn.");
                    allSaved = false;
                } else if (result <= 0) {
                    DialogUtils.showError(this, "Lỗi", "Không thể cập nhật thông tin cho hàng " + (row + 1));
                    allSaved = false;
                }
            } catch (Exception e) {
                DialogUtils.showError(this, "Lỗi", "Lỗi xử lý dữ liệu ở hàng " + (row + 1) + ": " + e.getMessage());
                e.printStackTrace();
                allSaved = false;
            }
        }
        
        if (allSaved) {
            DialogUtils.showInfo(this, "Thành công", "Đã lưu tất cả thay đổi thành công!");
            dataChanged = false;
            btnSave.setEnabled(false);
            return true;
        } else {
            DialogUtils.showWarning(this, "Cảnh báo", "Có lỗi xảy ra khi lưu. Vui lòng kiểm tra và thử lại.");
            return false;
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
        btn.setPreferredSize(new Dimension(180, 40));

        btn.setUI(new BasicButtonUI() {
            @Override 
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                  RenderingHints.KEY_ANTIALIASING,
                  RenderingHints.VALUE_ANTIALIAS_ON
                );
                
                AbstractButton b = (AbstractButton)c;
                ButtonModel m = b.getModel();
                
                Color bgColor = m.isPressed() ? primaryColor.darker() 
                              : m.isRollover() ? hoverColor 
                              : primaryColor;
                if (b.getBackground().equals(Color.WHITE)) {
                    bgColor = m.isRollover() ? new Color(241, 248, 244) : Color.WHITE;
                    g2.setColor(bgColor);
                    g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 18, 18);
                    g2.setColor(new Color(218, 220, 224));
                    g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 18, 18);
                } else {
                    g2.setColor(bgColor);
                    g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 18, 18);
                }
                
                g2.dispose();
                super.paint(g, c);
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            @Override 
            public void mouseEntered(MouseEvent e) {
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override 
            public void mouseExited(MouseEvent e) {
                btn.setCursor(Cursor.getDefaultCursor());
            }
        });

        return btn;
    }
    
}
