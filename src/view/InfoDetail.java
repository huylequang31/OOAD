package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;

import model.Users;
import controller.DetailController;

public class InfoDetail extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private DetailController detailController = new DetailController();
    
    
    private final Color primaryColor = new Color(52, 168, 83);
    private final Color hoverColor = new Color(45, 136, 70);
    private final Color backgroundColor = new Color(241, 248, 244);
    private final Font titleFont = new Font("Segoe UI", Font.PLAIN, 24);
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

    
    public void getDetailTable(int appointment_id) {
        int stt = 1;
        for (Users user : detailController.getParticipants(appointment_id)) {
            tableModel.addRow(new Object[] {
                stt,
                user.getId(),
                user.getName(),
                user.getPhoneNumber()
            });
            stt++;
        }
    }
    
    public List<Integer> getReminderTable(int appId) {
        return detailController.getReminderIds(appId);
    }
    
    public InfoDetail(int appointment_id, String name, String location, Date date, int start, int end) {
        this(appointment_id, name, location, date, start, 0, end, 0);
    }

    public InfoDetail(int appointment_id, String name, String location, Date date, int start, int startMinute, int end, int endMinute) {
        this(appointment_id, name, location, date, start, startMinute, end, endMinute, "");
    }

    public InfoDetail(int appointment_id, String name, String location, Date date, int start, int startMinute, int end, int endMinute, String typeAppointment) {
        setTitle("Thông tin chi tiết");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 860, 700);
        setResizable(false);
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
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 15));
        
        
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout(0, 15));
        
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Thông tin chi tiết sự kiện");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(new Color(60, 64, 67));
        lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout(0, 15));
        
        
        JPanel infoCardPanel = createCardPanel();
        infoCardPanel.setLayout(new BorderLayout(0, 0));
        
        
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 10, 8, 5);
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblNameHeader = createStyledLabel("Tên sự kiện:", labelFont);
        infoPanel.add(lblNameHeader, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblLocationHeader = createStyledLabel("Vị trí:", labelFont);
        infoPanel.add(lblLocationHeader, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblDateHeader = createStyledLabel("Ngày diễn ra:", labelFont);
        infoPanel.add(lblDateHeader, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblStartHeader = createStyledLabel("Thời gian bắt đầu:", labelFont);
        infoPanel.add(lblStartHeader, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblEndHeader = createStyledLabel("Thời gian kết thúc:", labelFont);
        infoPanel.add(lblEndHeader, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel lblTypeHeader = createStyledLabel("Kiểu cuộc hẹn:", labelFont);
        infoPanel.add(lblTypeHeader, gbc);
        
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblNameValue = createStyledLabel(name, valueFont);
        infoPanel.add(lblNameValue, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel lblLocationValue = createStyledLabel(location, valueFont);
        infoPanel.add(lblLocationValue, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
        String strDate = formatter.format(date);
        JLabel lblDateValue = createStyledLabel(strDate, valueFont);
        infoPanel.add(lblDateValue, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        JLabel lblStartValue = createStyledLabel(String.format("%02d:%02d", start, startMinute), valueFont);
        infoPanel.add(lblStartValue, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        JLabel lblEndValue = createStyledLabel(String.format("%02d:%02d", end, endMinute), valueFont);
        infoPanel.add(lblEndValue, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JLabel lblTypeValue = createStyledLabel(typeAppointment == null ? "" : typeAppointment, valueFont);
        infoPanel.add(lblTypeValue, gbc);
        
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.insets = new Insets(8, 30, 8, 5);
        JLabel lblReminderHeader = createStyledLabel("Bộ nhắc:", labelFont);
        infoPanel.add(lblReminderHeader, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 5;
        gbc.fill = GridBagConstraints.BOTH;
        
        
        JPanel reminderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(new Color(232, 245, 233));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(new Color(200, 230, 201));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
            }
        };
        reminderPanel.setOpaque(false);
        reminderPanel.setLayout(new BorderLayout());
        reminderPanel.setPreferredSize(new Dimension(220, 150));
        
        
        JTextArea textAreaReminder = new JTextArea();
        textAreaReminder.setFont(valueFont);
        textAreaReminder.setBackground(new Color(232, 245, 233));
        textAreaReminder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textAreaReminder.setEditable(false);
        
        
        List<Integer> listRmdId = getReminderTable(appointment_id);
        for(int i : listRmdId) {
            String s = "";
            switch(i) {
                case 1: s = "Nhắc trước 15 phút"; break;
                case 2: s = "Nhắc trước 30 phút"; break;
                case 3: s = "Nhắc trước 1 ngày"; break;
            }
            textAreaReminder.append(s + "\n");
        }
        
        reminderPanel.add(textAreaReminder, BorderLayout.CENTER);
        infoPanel.add(reminderPanel, gbc);
        
        infoCardPanel.add(infoPanel, BorderLayout.CENTER);
        centerPanel.add(infoCardPanel, BorderLayout.NORTH);
        
        
        JPanel participantsCardPanel = createCardPanel();
        participantsCardPanel.setLayout(new BorderLayout(0, 10));
        participantsCardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblParticipants = createStyledLabel("Thông tin người tham gia:", labelFont);
        participantsCardPanel.add(lblParticipants, BorderLayout.NORTH);
        
        
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableModel.addColumn("STT"); 
        tableModel.addColumn("ID");
        tableModel.addColumn("Họ và tên"); 
        tableModel.addColumn("SĐT");
        
        table = new JTable(tableModel);
        table.setFont(mainFont);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(new Color(230, 244, 234));
        table.setSelectionForeground(primaryColor);
        
        
        JTableHeader header = table.getTableHeader();
        header.setFont(headerFont);
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(95, 99, 104));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
        
        
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
        
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(247, 252, 248) : Color.WHITE);
                }
                
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setHorizontalAlignment(SwingConstants.CENTER);
                
                return c;
            }
        });
        
        
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  
        columnModel.getColumn(1).setPreferredWidth(80);  
        columnModel.getColumn(2).setPreferredWidth(200); 
        columnModel.getColumn(3).setPreferredWidth(150); 
        
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(), 150)); 
        
        
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
        
        
        getDetailTable(appointment_id);
        
        participantsCardPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(participantsCardPanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        
        JButton btnClose = createStyledButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        
        buttonPanel.add(btnClose);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }
    
    
    private JPanel createCardPanel() {
        JPanel panel = new JPanel() {
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
        panel.setOpaque(false);
        return panel;
    }
    
    
    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(60, 60, 60));
        return label;
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
        btn.setPreferredSize(new Dimension(120, 40));

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
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 18, 18);
                
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
