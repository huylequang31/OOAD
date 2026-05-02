package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicButtonUI;

import model.Reminder;
import controller.ReminderController;

public class ReminderUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private ReminderController reminderController = new ReminderController();
    private JTextArea textArea;               
    private JComboBox<String> cbReminder;
    
    
    private final Color primaryColor    = new Color(52, 168, 83);
    private final Color hoverColor      = new Color(45, 136, 70);
    private final Color backgroundColor = new Color(241, 248, 244);
    private final Font  titleFont       = new Font("Segoe UI", Font.PLAIN, 24);
    private final Font  mainFont        = new Font("Segoe UI", Font.PLAIN, 14);

    public static List<String> getLinesFromTextArea(JTextArea textArea) {
        List<String> lines = new ArrayList<>();
        try {
            int lineCount = textArea.getLineCount();
            for (int i = 0; i < lineCount; i++) {
                int start = textArea.getLineStartOffset(i);
                int end   = textArea.getLineEndOffset(i);
                String line = textArea.getText(start, end - start);
                lines.add(line.trim());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lines;
    }
    
    private void showForm3AndDispose() {
        MyCalendar form3 = new MyCalendar();
        form3.setVisible(true);
        this.dispose();
    }
    
    private int appointmentId;
    
    public ReminderUI(int appointmentId) {
        this.appointmentId = appointmentId;

        setTitle("Bộ nhắc");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100,100,500,450);
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
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.dispose();
            }
        };

        contentPane.setBorder(new EmptyBorder(20,20,20,20));
        contentPane.setLayout(new BorderLayout(0,15));
        setContentPane(contentPane);

        contentPane.add(createHeaderPanel(), BorderLayout.NORTH);
        contentPane.add(createFormPanel(),   BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel titleLabel = new JLabel("Bộ nhắc", SwingConstants.LEFT);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(60,64,67));
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createFormPanel() {
        
        JPanel container = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(
                  RenderingHints.KEY_ANTIALIASING,
                  RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(
                  0,0,getWidth()-1,getHeight()-1,16,16
                ));
                g2.setColor(new Color(218,220,224));
                g2.draw(new RoundRectangle2D.Double(
                  0,0,getWidth()-1,getHeight()-1,16,16
                ));
                g2.dispose();
            }
        };
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        
        
        JPanel inner = new JPanel(new BorderLayout(0,15)) {
            @Override public boolean isOpaque() { return false; }
        };
        
        
        JPanel top = new JPanel(new BorderLayout(10,0));
        top.setOpaque(false);
        cbReminder = createStyledComboBox();
        for (Reminder r : reminderController.getAllReminder()) {
            cbReminder.addItem(r.getTitle());
        }
        JButton btnAdd = createStyledButton("Thêm");
        btnAdd.addActionListener(e -> {
            List<String> rmd = getLinesFromTextArea(textArea);
            String sel = (String)cbReminder.getSelectedItem();
            if (!rmd.contains(sel)) {
                textArea.append(sel + "\n");
            } else {
                DialogUtils.showWarning(this, "Thông báo", "Lời nhắc này đã được thêm rồi!");
            }
        });
        top.add(cbReminder, BorderLayout.CENTER);
        top.add(btnAdd,     BorderLayout.EAST);
        
        
        textArea = createStyledTextArea();               
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(218,220,224),1,true),
          BorderFactory.createEmptyBorder(5,5,5,5)
        ));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        
        inner.add(top,    BorderLayout.NORTH);
        inner.add(scroll, BorderLayout.CENTER);
        container.add(inner, BorderLayout.CENTER);
        return container;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        panel.setOpaque(false);
        JButton btnConfirm = createStyledButton("Xác nhận");
        btnConfirm.setPreferredSize(new Dimension(150,40));
        btnConfirm.addActionListener(e -> {

            if (appointmentId <= 0) {
                DialogUtils.showError(this, "Lỗi", "Không tìm thấy appointmentId.");
                return;
            }

            List<String> rmd = getLinesFromTextArea(textArea);

            for (String r : rmd) {
                if (!r.trim().isEmpty()) {
                    reminderController.InsertTakeReminder(appointmentId, r.trim());
                }
            }

            DialogUtils.showInfo(this, "Thành công", "Thêm reminder thành công!");
            showForm3AndDispose();
        });
        
        JButton btnCancel = createStyledButton("Hủy bỏ");
        btnCancel.setPreferredSize(new Dimension(150,40));
        btnCancel.setBackground(new Color(230,230,230));
        btnCancel.setForeground(new Color(80,80,80));
        btnCancel.addActionListener(e -> dispose());
        
        panel.add(btnConfirm);
        panel.add(btnCancel);
        return panel;
    }
    
    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setFont(mainFont);
        combo.setBackground(Color.WHITE);
        combo.setForeground(new Color(50,50,50));
        combo.setPreferredSize(new Dimension(250,30));
        combo.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(218,220,224),1,true),
          BorderFactory.createEmptyBorder(2,5,2,5)
        ));
        return combo;
    }
    
    private JTextArea createStyledTextArea() {
        JTextArea ta = new JTextArea();
        ta.setFont(mainFont);
        ta.setBackground(Color.WHITE);
        ta.setForeground(new Color(50,50,50));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return ta;
    }
    
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI",Font.BOLD,14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(primaryColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setUI(new BasicButtonUI() {
            @Override public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(
                  RenderingHints.KEY_ANTIALIASING,
                  RenderingHints.VALUE_ANTIALIAS_ON
                );
                AbstractButton b = (AbstractButton)c;
                ButtonModel m = b.getModel();
                Color bg = m.isPressed()   ? primaryColor.darker()
                         : m.isRollover() ? hoverColor
                         : primaryColor;
                if (b.getBackground().equals(new Color(230,230,230))) {
                    bg = m.isRollover() ? new Color(241,248,244) : Color.WHITE;
                    g2.setColor(bg);
                    g2.fillRoundRect(0,0,c.getWidth(),c.getHeight(),18,18);
                    g2.setColor(new Color(218,220,224));
                    g2.drawRoundRect(0,0,c.getWidth()-1,c.getHeight()-1,18,18);
                } else {
                    g2.setColor(bg);
                    g2.fillRoundRect(0,0,c.getWidth(),c.getHeight(),18,18);
                }
                g2.dispose();
                super.paint(g,c);
            }
        });
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setCursor(
                  Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                );
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setCursor(Cursor.getDefaultCursor());
            }
        });
        return btn;
    }
}
