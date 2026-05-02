package view;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicButtonUI;

import model.Reminder;
import model.Session;
import model.Users;
import controller.AppointmentController;
import dao.UserDAO;
import dto.AppointmentRequest;
import dto.AppointmentResult;
import dto.AppointmentResultStatus;

public class AssignmentDetail extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtNameEvent;
    private JTextField txtLocation;
    private JRadioButton rdBtnDon, rdBtnNhom;
    private JComboBox<String> cbbStart, cbbStartMinute, cbbEnd, cbbEndMinute;
    private JList<Users> lstMembers;
    private JPanel pnlMembers;
    private JScrollPane scrMembers;
    private JButton btnConfirmDetail;
    private AppointmentController appointmentController = new AppointmentController();
    private UserDAO userDAO = new UserDAO();
    private static final Dimension TEXT_FIELD_SIZE = new Dimension(420, 38);
    
    private java.util.Date selectedDate;

    private final Color primaryColor = new Color(52, 168, 83);
    private final Color hoverColor = new Color(45, 136, 70);
    private final Color backgroundColor = new Color(241, 248, 244);
    private final Font titleFont = new Font("Segoe UI", Font.PLAIN, 24);
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

    public AssignmentDetail(java.util.Date date) {
        this.selectedDate = date;
        
        setTitle("Thêm cuộc hẹn");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 860, 620);
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

        JPanel headerPanel = createHeaderPanel();
        contentPane.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        contentPane.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Thêm cuộc hẹn");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(60, 64, 67));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(titleLabel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout()) {
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
        containerPanel.setOpaque(false);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        
        JPanel formPanel = new JPanel() {
            @Override
            public boolean isOpaque() {
                return false;
            }
        };
        formPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblNameEvent = new JLabel("Tên sự kiện:");
        lblNameEvent.setFont(labelFont);
        formPanel.add(lblNameEvent, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0; 
        gbc.gridwidth = GridBagConstraints.REMAINDER; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNameEvent = createStyledTextField();
        formPanel.add(txtNameEvent, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridwidth = 1; 
        gbc.weightx = 0.0; 
        JLabel lblLocation = new JLabel("Vị trí:");
        lblLocation.setFont(labelFont);
        formPanel.add(lblLocation, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0; 
        gbc.gridwidth = GridBagConstraints.REMAINDER; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtLocation = createStyledTextField();
        formPanel.add(txtLocation, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.0; 
        gbc.gridwidth = 1; 
        JLabel lblStartTime = new JLabel("Thời gian bắt đầu:");
        lblStartTime.setFont(labelFont);
        formPanel.add(lblStartTime, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        cbbStart = createStyledComboBox(new String[] {
            "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"
        });
        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        startPanel.setOpaque(false);
        cbbStartMinute = createStyledComboBox(createMinuteItems());
        resizeTimeComboBox(cbbStart);
        resizeTimeComboBox(cbbStartMinute);
        startPanel.add(cbbStart);
        startPanel.add(new JLabel("giờ"));
        startPanel.add(cbbStartMinute);
        startPanel.add(new JLabel("phút"));
        formPanel.add(startPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblEndTime = new JLabel("Thời gian kết thúc:");
        lblEndTime.setFont(labelFont);
        formPanel.add(lblEndTime, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        cbbEnd = createStyledComboBox(new String[] {
            "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"
        });
        JPanel endPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        endPanel.setOpaque(false);
        cbbEndMinute = createStyledComboBox(createMinuteItems());
        resizeTimeComboBox(cbbEnd);
        resizeTimeComboBox(cbbEndMinute);
        endPanel.add(cbbEnd);
        endPanel.add(new JLabel("giờ"));
        endPanel.add(cbbEndMinute);
        endPanel.add(new JLabel("phút"));
        formPanel.add(endPanel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblMeetingType = new JLabel("Kiểu cuộc họp:");
        lblMeetingType.setFont(labelFont);
        formPanel.add(lblMeetingType, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        radioPanel.setOpaque(false);
        
        rdBtnDon = createStyledRadioButton("Đơn");
        rdBtnNhom = createStyledRadioButton("Nhóm");
        
        ButtonGroup group = new ButtonGroup();
        group.add(rdBtnDon);
        group.add(rdBtnNhom);
        rdBtnDon.setSelected(true);
        rdBtnDon.addActionListener(e -> toggleMemberSelection(false));
        rdBtnNhom.addActionListener(e -> toggleMemberSelection(true));
        
        radioPanel.add(rdBtnDon);
        radioPanel.add(rdBtnNhom);
     
        formPanel.add(radioPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        JLabel lblMembers = new JLabel("Thành viên nhóm:");
        lblMembers.setFont(labelFont);
        formPanel.add(lblMembers, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        pnlMembers = new JPanel(new BorderLayout());
        pnlMembers.setOpaque(false);
        lstMembers = createMemberList();
        scrMembers = new JScrollPane(lstMembers);
        scrMembers.setPreferredSize(new Dimension(420, 130));
        pnlMembers.add(scrMembers, BorderLayout.CENTER);
        formPanel.add(pnlMembers, gbc);

        loadMembers();
        toggleMemberSelection(false);
        
        containerPanel.add(formPanel, BorderLayout.CENTER);
        return containerPanel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setOpaque(false);
        
        btnConfirmDetail = createStyledButton("Xác nhận");
        btnConfirmDetail.setPreferredSize(new Dimension(150, 40));
        btnConfirmDetail.addActionListener(e -> submitAppointment());

        JButton btnCancel = createStyledButton("Hủy bỏ");
        btnCancel.setPreferredSize(new Dimension(150, 40));
        btnCancel.setBackground(new Color(230, 230, 230));
        btnCancel.setForeground(new Color(80, 80, 80));
        btnCancel.addActionListener(e -> dispose());
        
        panel.add(btnConfirmDetail);
        panel.add(btnCancel);
        return panel;
    }
    
    private void submitAppointment() {
        AppointmentRequest request = createAppointmentRequest();
        AppointmentResult result = appointmentController.addAppointment(request);
        handleAppointmentResult(request, result);
    }

    private AppointmentRequest createAppointmentRequest() {
        String type = rdBtnDon.isSelected() ? "Đơn" : "Nhóm";
        AppointmentRequest request = new AppointmentRequest(
            Session.getUserId(),
            txtNameEvent.getText(),
            txtLocation.getText(),
            selectedDate,
            Integer.parseInt((String)cbbStart.getSelectedItem()),
            Integer.parseInt((String)cbbStartMinute.getSelectedItem()),
            Integer.parseInt((String)cbbEnd.getSelectedItem()),
            Integer.parseInt((String)cbbEndMinute.getSelectedItem()),
            type
        );
        request.setParticipantUserIds(getSelectedMemberIds());
        return request;
    }

    private JList<Users> createMemberList() {
        JList<Users> list = new JList<>();
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setFont(mainFont);
        list.setVisibleRowCount(6);
        for (MouseListener listener : list.getMouseListeners()) {
            list.removeMouseListener(listener);
        }
        for (MouseMotionListener listener : list.getMouseMotionListeners()) {
            list.removeMouseMotionListener(listener);
        }
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) {
                    e.consume();
                    return;
                }
                int index = list.locationToIndex(e.getPoint());
                if (index < 0) {
                    return;
                }
                Rectangle cellBounds = list.getCellBounds(index, index);
                if (cellBounds == null || !cellBounds.contains(e.getPoint())) {
                    return;
                }
                if (list.isSelectedIndex(index)) {
                    list.removeSelectionInterval(index, index);
                } else {
                    list.addSelectionInterval(index, index);
                }
                e.consume();
            }
        });
        list.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Users) {
                    Users user = (Users) value;
                    label.setText(user.getName() + " - " + user.getPhoneNumber());
                }
                return label;
            }
        });
        return list;
    }

    private void loadMembers() {
        DefaultListModel<Users> model = new DefaultListModel<>();
        for (Users user : userDAO.findAllExceptUserId(Session.getUserId())) {
            model.addElement(user);
        }
        lstMembers.setModel(model);
    }

    private List<Integer> getSelectedMemberIds() {
        List<Integer> ids = new ArrayList<>();
        if (!rdBtnNhom.isSelected()) {
            return ids;
        }
        for (Users user : lstMembers.getSelectedValuesList()) {
            ids.add(user.getId());
        }
        return ids;
    }

    private void toggleMemberSelection(boolean visible) {
        if (!visible) {
            lstMembers.clearSelection();
        }
        pnlMembers.setVisible(visible);
        pnlMembers.revalidate();
        pnlMembers.repaint();
    }

    private void handleAppointmentResult(AppointmentRequest request, AppointmentResult result) {
        if (result.getStatus() == AppointmentResultStatus.INVALID) {
            DialogUtils.showInfo(this, "Thông báo", result.getMessage());
            return;
        }

        if (result.getStatus() == AppointmentResultStatus.JOIN_GROUP) {
            int confirm = DialogUtils.showYesNo(this, "Xác nhận", result.getMessage());
            if (confirm == JOptionPane.YES_OPTION) {
                request.setJoinGroupIfExists(true);
                handleAppointmentResult(request, appointmentController.addAppointment(request));
            }
            return;
        }

        if (result.getStatus() == AppointmentResultStatus.CONFLICT) {
            int confirm = DialogUtils.showYesNo(this, "Xác nhận", result.getMessage());
            if (confirm == JOptionPane.YES_OPTION) {
                request.setReplaceConflictIfExists(true);
                handleAppointmentResult(request, appointmentController.addAppointment(request));
            }
            return;
        }

        int confirmReminder = DialogUtils.showYesNo(this, "Xác nhận", "Bạn có muốn thêm bộ nhắc?");
        if (confirmReminder == JOptionPane.YES_OPTION) {
            showFormReminderAndDispose(result.getAppointmentId());
        } else if (confirmReminder == JOptionPane.NO_OPTION) {
            DialogUtils.showInfo(this, "Thông báo", result.getMessage());
            showForm3AndDispose();
        }
    }
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(mainFont);
        textField.setColumns(24);
        textField.setPreferredSize(TEXT_FIELD_SIZE);
        textField.setMinimumSize(TEXT_FIELD_SIZE);
        textField.setBackground(Color.WHITE);
        textField.setForeground(new Color(50, 50, 50));
        textField.setCaretColor(primaryColor);
        textField.setSelectedTextColor(Color.WHITE);
        textField.setSelectionColor(primaryColor);
        textField.setOpaque(true);
        textField.setFocusable(true);
        textField.setEnabled(true);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 220, 224), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(primaryColor, 2, true),
                    BorderFactory.createEmptyBorder(4, 9, 4, 9)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(218, 220, 224), 1, true),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });
        
        return textField;
    }
    
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(mainFont);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(50, 50, 50));
        comboBox.setPreferredSize(new Dimension(100, 30));
        comboBox.setFocusable(true);
        comboBox.setEnabled(true);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 220, 224), 1, true),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        return comboBox;
    }

    private String[] createMinuteItems() {
        String[] minutes = new String[60];
        for (int i = 0; i < minutes.length; i++) {
            minutes[i] = String.format("%02d", i);
        }
        return minutes;
    }

    private void resizeTimeComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(64, 30));
    }
    
    private JRadioButton createStyledRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(mainFont);
        radioButton.setForeground(new Color(50, 50, 50));
        radioButton.setOpaque(false);
        radioButton.setFocusPainted(false);
        radioButton.setIcon(new RadioButtonIcon(false));
        radioButton.setSelectedIcon(new RadioButtonIcon(true));
        return radioButton;
    }
    
    private class RadioButtonIcon implements Icon {
        private final boolean selected;
        
        public RadioButtonIcon(boolean selected) {
            this.selected = selected;
        }
        
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            
            g2.setColor(selected ? primaryColor : new Color(180, 180, 180));
            g2.fillOval(x, y, 16, 16);

            if (selected) {
                g2.setColor(Color.WHITE);
                g2.fillOval(x + 4, y + 4, 8, 8);
            }
            
            g2.dispose();
        }
        
        @Override
        public int getIconWidth() {
            return 16;
        }
        
        @Override
        public int getIconHeight() {
            return 16;
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
                if (b.getBackground().equals(new Color(230, 230, 230))) {
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
    
    private void showForm3AndDispose() {
        MyCalendar form3 = new MyCalendar();
        form3.setVisible(true);
        this.dispose();
    }
    
    private void showFormReminderAndDispose(int appId) {
        ReminderUI form = new ReminderUI(appId);
        form.setVisible(true);
        dispose();
    }
}
