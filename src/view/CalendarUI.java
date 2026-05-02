package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import com.toedter.calendar.JCalendar;
import controller.AppointmentController;

public class CalendarUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Color primaryColor    = new Color(52, 168, 83);
    private final Color hoverColor      = new Color(45, 136, 70);
    private final Color backgroundColor = new Color(241, 248, 244);
    private final Font  mainFont        = new Font("Segoe UI", Font.PLAIN, 14);
    private final AppointmentController appointmentController = new AppointmentController();

    private JPanel contentPane;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
              UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) { }

        model.Session.setUserId(1);

        EventQueue.invokeLater(() -> {
            CalendarUI f = new CalendarUI();
            f.setVisible(true);
        });
    }

    public CalendarUI() {
        setTitle("Calendar Appointment");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 700);
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
        contentPane.setBorder(new EmptyBorder(18, 20, 18, 20));
        contentPane.setLayout(new BorderLayout(0, 16));
        setContentPane(contentPane);

        contentPane.add(createHeaderPanel(),   BorderLayout.NORTH);
        contentPane.add(createCalendarPanel(), BorderLayout.CENTER);
        contentPane.add(createButtonPanel(),   BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lbl = new JLabel("Lịch hẹn");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lbl.setForeground(new Color(60, 64, 67));
        header.add(lbl, BorderLayout.WEST);

        
        URL u = getClass().getClassLoader().getResource("icons/calendar.png");
        ImageIcon icon = (u != null) ? new ImageIcon(u) : null;

        JButton btnMy = createStyledButton("Lịch hẹn của tôi", icon);
        btnMy.addActionListener(e -> {
            MyCalendar mc = new MyCalendar();
            mc.setVisible(true);
        });
        header.add(btnMy, BorderLayout.EAST);

        return header;
    }

    private JPanel createCalendarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        
        JPanel cont = new JPanel() {
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
        cont.setOpaque(false);
        cont.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        cont.setLayout(new BorderLayout());

        
        JCalendar calendar = new JCalendar();
        calendar.setDate(new java.util.Date());
        calendar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        calendar.setWeekOfYearVisible(false);
        calendar.setDecorationBackgroundVisible(false);
        calendar.setDecorationBordersVisible(false);
        calendar.setSundayForeground(new Color(217, 48, 37));
        calendar.setWeekdayForeground(new Color(60, 64, 67));

        
        styleMonthYearChooser(calendar);

        
        styleDayButtons(calendar);

        cont.add(calendar, BorderLayout.CENTER);
        panel.add(cont, BorderLayout.CENTER);
        return panel;
    }

    private void styleMonthYearChooser(JCalendar calendar) {

        Component monthComp = calendar.getMonthChooser().getComboBox();
        if (monthComp instanceof JComboBox) {
            @SuppressWarnings("unchecked")
            JComboBox<String> combo = (JComboBox<String>) monthComp;

            combo.setFont(new Font("Segoe UI", Font.BOLD, 15));
            combo.setBackground(Color.WHITE);
            combo.setForeground(new Color(60, 64, 67));

            combo.setPreferredSize(new Dimension(170, 35));
            combo.setMinimumSize(new Dimension(170, 35));
            combo.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

            combo.setUI(new BasicComboBoxUI() {

                @Override
                protected ComboPopup createPopup() {
                    return new BasicComboPopup(comboBox) {
                        @Override
                        protected void configureList() {
                            super.configureList();
                            list.setBackground(Color.WHITE);
                            list.setSelectionBackground(new Color(230, 244, 234));
                            list.setSelectionForeground(new Color(52,168,83));
                            list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        }
                    };
                }

                @Override
                protected JButton createArrowButton() {
                    JButton btn = new JButton() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(new Color(120,120,120));
                            int s = 8;
                            int x = getWidth()/2 - s/2;
                            int y = getHeight()/2 - s/2;
                            int[] xs = {x, x+s, x+s/2};
                            int[] ys = {y, y, y+s};
                            g2.fillPolygon(xs, ys, 3);
                            g2.dispose();
                        }
                    };
                    btn.setBorderPainted(false);
                    btn.setContentAreaFilled(false);
                    btn.setFocusPainted(false);
                    return btn;
                }

                @Override
                public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 12, 12);

                    g2.setColor(new Color(218,220,224));
                    g2.drawRoundRect(bounds.x, bounds.y, bounds.width-1, bounds.height-1, 12, 12);

                    g2.dispose();
                }
            });
        }

        Component yearComp = calendar.getYearChooser().getSpinner();
        if (yearComp instanceof JSpinner) {
            JSpinner spinner = (JSpinner) yearComp;

            spinner.setPreferredSize(new Dimension(110, 38));
            spinner.setMinimumSize(new Dimension(110, 38));

            JComponent editor = spinner.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                JSpinner.DefaultEditor ed = (JSpinner.DefaultEditor) editor;

                ed.getTextField().setFont(new Font("Segoe UI", Font.BOLD, 15));
                ed.getTextField().setHorizontalAlignment(JTextField.CENTER);
                ed.getTextField().setBorder(null);

                ed.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            }

            spinner.setBorder(new LineBorder(new Color(218,220,224), 1, true));
        }

        calendar.getMonthChooser().setPreferredSize(new Dimension(200, 50));
        calendar.getYearChooser().setPreferredSize(new Dimension(120, 50));

        calendar.revalidate();
        calendar.repaint();
    }
    private void styleDayButtons(JCalendar calendar) {
        JPanel dayPanel = calendar.getDayChooser().getDayPanel();
        for (Component c : dayPanel.getComponents()) {
            if (!(c instanceof JButton)) continue;
            JButton b = (JButton) c;
            b.setPreferredSize(new Dimension(60, 60));

            b.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            
            b.setUI(new BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    AbstractButton b = (AbstractButton) c;
                    ButtonModel model = b.getModel();

                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                    );

                    int w = c.getWidth();
                    int h = c.getHeight();

                    
                    if (model.isSelected()) {
                        g2.setColor(new Color(52,168,83));
                        g2.fillOval(8, 8, w-16, h-16);
                    }

                    String text = b.getText();
                    g2.setColor(Color.BLACK);
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));

                    FontMetrics fm = g2.getFontMetrics();
                    int tx = (w - fm.stringWidth(text)) / 2;
                    int ty = h/2;

                    g2.drawString(text, tx, ty);

                    try {
                        int day = Integer.parseInt(text);
                        Calendar cal = (Calendar) calendar.getCalendar().clone();
                        cal.set(Calendar.DAY_OF_MONTH, day);

                        int count = countAppointments(cal.getTime());

                        if (count > 0) {
                            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                            g2.setColor(new Color(52,168,83));

                            String s = count + " cuộc hẹn";
                            int sx = (w - g2.getFontMetrics().stringWidth(s)) / 2;
                            int sy = ty + 14;

                            g2.drawString(s, sx, sy);
                        }

                    } catch (Exception e) {}

                    g2.dispose();
                }
            });
            
            for (MouseListener ml : b.getMouseListeners()) {
                if (ml instanceof DayButtonMouseListener) {
                    b.removeMouseListener(ml);
                }
            }
            
            b.addMouseListener(new DayButtonMouseListener(calendar));
        }
        dayPanel.revalidate();
        dayPanel.repaint();
    }

    private int countAppointments(java.util.Date date) {
        return appointmentController.getAppointmentsByDate(model.Session.getUserId(), date).size();
    }
    
    private class DayButtonMouseListener extends MouseAdapter {
        private final JCalendar calendar;

        DayButtonMouseListener(JCalendar cal) {
            this.calendar = cal;
        }
        
        @Override 
        public void mouseEntered(MouseEvent e) {
            JButton b = (JButton)e.getComponent();
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.repaint();
        }
        
        @Override 
        public void mouseExited(MouseEvent e) {
            JButton b = (JButton)e.getComponent();
            b.setCursor(Cursor.getDefaultCursor());
            b.repaint();
        }
        
        @Override 
        public void mousePressed(MouseEvent e) {
            JButton b = (JButton)e.getComponent();
            b.getModel().setPressed(true);
            b.repaint();
        }
        
        @Override 
        public void mouseReleased(MouseEvent e) {
            JButton b = (JButton)e.getComponent();
            b.getModel().setPressed(false);
            b.repaint();
            
            
            try {
                int d = Integer.parseInt(b.getText());
                Calendar cal = calendar.getCalendar();
                cal.set(Calendar.DAY_OF_MONTH, d);
                calendar.setCalendar(cal);
            } catch (NumberFormatException ex) { }
        }
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(
          new FlowLayout(FlowLayout.RIGHT, 12, 4)
        );
        p.setOpaque(false);

        JButton view = createStyledButton("Xem cuộc hẹn", null);
        view.setPreferredSize(new Dimension(150, 38));
        view.addActionListener(e -> {
            JCalendar cal = findCalendar(contentPane);
            if (cal != null) {
                new DailyAppointmentsUI(cal.getDate()).setVisible(true);
            }
        });

        JButton ok = createStyledButton("Thêm cuộc hẹn", null);
        ok.setPreferredSize(new Dimension(160, 38));
        ok.addActionListener(e -> {
            JCalendar cal = findCalendar(contentPane);
            if (cal != null) {
                new AssignmentDetail(cal.getDate()).setVisible(true);
            }
        });

        JButton cancel = createStyledButton("Đóng", null);
        cancel.setPreferredSize(new Dimension(110, 38));
        cancel.setBackground(Color.WHITE);
        cancel.setForeground(new Color(60, 64, 67));
        cancel.addActionListener(e -> dispose());

        p.add(view);
        p.add(ok);
        p.add(cancel);
        return p;
    }

    private JCalendar findCalendar(Container root) {
        for (Component c : root.getComponents()) {
            if (c instanceof JCalendar) return (JCalendar)c;
            if (c instanceof Container) {
                JCalendar f = findCalendar((Container)c);
                if (f != null) return f;
            }
        }
        return null;
    }

    private JButton createStyledButton(String txt, ImageIcon ic) {
        JButton btn = new JButton(txt, ic);
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
