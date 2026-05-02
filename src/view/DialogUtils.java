package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public final class DialogUtils {
    private static final Color PRIMARY = new Color(52, 168, 83);
    private static final Color PRIMARY_HOVER = new Color(45, 136, 70);
    private static final Color GREEN_SOFT = new Color(129, 201, 149);
    private static final Color GREEN_SOFT_HOVER = new Color(102, 187, 122);
    private static final Font MESSAGE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font CHOICE_MESSAGE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);

    private DialogUtils() {
    }

    public static void showInfo(Component parent, String title, String message) {
        showMessage(parent, title, message);
    }

    public static void showWarning(Component parent, String title, String message) {
        showMessage(parent, title, message);
    }

    public static void showError(Component parent, String title, String message) {
        showMessage(parent, title, message);
    }

    private static void showMessage(Component parent, String title, String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(12, 16, 10, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(new Color(60, 64, 67));
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 14, 0);
        JLabel messageLabel = new JLabel("<html><div style='text-align:center;width:280px;'>" + message + "</div></html>", SwingConstants.CENTER);
        messageLabel.setFont(MESSAGE_FONT);
        messageLabel.setForeground(new Color(95, 99, 104));
        panel.add(messageLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton okButton = createButton("OK", false);
        okButton.setPreferredSize(new Dimension(116, 40));
        panel.add(okButton, gbc);

        JDialog dialog = buildDialog(parent, title, panel);
        okButton.addActionListener(e -> dialog.dispose());
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setVisible(true);
    }

    public static int showYesNo(Component parent, String title, String message) {
        final int[] result = { JOptionPane.NO_OPTION };
        JDialog dialog = buildChoiceDialog(parent, title, message, false, result);
        dialog.setVisible(true);
        return result[0];
    }

    public static int showYesNoCancel(Component parent, String title, String message) {
        final int[] result = { JOptionPane.CANCEL_OPTION };
        JDialog dialog = buildChoiceDialog(parent, title, message, true, result);
        dialog.setVisible(true);
        return result[0];
    }

    private static JDialog buildChoiceDialog(Component parent, String title, String message, boolean includeCancel, int[] resultRef) {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(12, 16, 10, 16));

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(new Color(60, 64, 67));
        content.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel messageLabel = new JLabel("<html><div style='text-align:center;width:320px;'>" + message + "</div></html>", SwingConstants.CENTER);
        messageLabel.setFont(CHOICE_MESSAGE_FONT);
        messageLabel.setForeground(new Color(95, 99, 104));
        content.add(messageLabel, gbc);

        panel.add(content, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setOpaque(false);

        JButton yesButton = createButton("Có", false);
        JButton noButton = createButton("Không", true);
        yesButton.setPreferredSize(new Dimension(106, 38));
        noButton.setPreferredSize(new Dimension(106, 38));
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        JButton cancelButton = null;
        if (includeCancel) {
            cancelButton = createButton("Hủy", true);
            cancelButton.setPreferredSize(new Dimension(106, 38));
            buttonPanel.add(cancelButton);
        }
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JDialog dialog = buildDialog(parent, title, panel);
        JButton finalCancelButton = cancelButton;
        yesButton.addActionListener(e -> {
            resultRef[0] = JOptionPane.YES_OPTION;
            dialog.dispose();
        });
        noButton.addActionListener(e -> {
            resultRef[0] = JOptionPane.NO_OPTION;
            dialog.dispose();
        });
        if (includeCancel && finalCancelButton != null) {
            finalCancelButton.addActionListener(e -> {
                resultRef[0] = JOptionPane.CANCEL_OPTION;
                dialog.dispose();
            });
        }
        dialog.getRootPane().setDefaultButton(yesButton);
        return dialog;
    }

    private static JDialog buildDialog(Component parent, String title, JPanel panel) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = owner instanceof JDialog ? new JDialog((JDialog) owner, title, true) : new JDialog((java.awt.Frame) null, title, true);
        dialog.setContentPane(panel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 220, 224)),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent);
        return dialog;
    }

    private static JButton createButton(String text, boolean secondary) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setBackground(secondary ? GREEN_SOFT : PRIMARY);
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                JButton b = (JButton) c;
                Color bg = secondary
                        ? (b.getModel().isPressed() ? GREEN_SOFT.darker()
                                : b.getModel().isRollover() ? GREEN_SOFT_HOVER : GREEN_SOFT)
                        : (b.getModel().isPressed() ? PRIMARY.darker()
                                : b.getModel().isRollover() ? PRIMARY_HOVER : PRIMARY);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 16, 16);
                g2.dispose();
                super.paint(g, c);
            }
        });
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setCursor(java.awt.Cursor.getDefaultCursor());
            }
        });
        return button;
    }
}
