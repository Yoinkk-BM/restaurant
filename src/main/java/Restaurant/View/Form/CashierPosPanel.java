package Restaurant.View.Form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Restaurant.Controller.PosController;

class ComboItem {
    private int id;
    private String name;
    public ComboItem(int id, String name) { this.id = id; this.name = name; }
    public int getId() { return id; }
    @Override public String toString() { return name; }
}

public class CashierPosPanel extends JPanel {
    private PosController posController = new PosController();
    private DefaultTableModel billTableModel;
    private JTable billTable;
    private JLabel lblTotalAmount;
    private JComboBox<ComboItem> cbTable;
    private double totalAmount = 0;
    private DecimalFormat df = new DecimalFormat("###,###đ");
    private JPanel menuPanel;

    public CashierPosPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); // Nền xám xanh cực nhạt

        // ==========================================
        // 1. HEADER: NÂNG CẤP GIAO DIỆN ĐỎ SANG TRỌNG
        // ==========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(198, 40, 40)); // Màu đỏ đậm nhà hàng (Material Red 800)
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("HỆ THỐNG THU NGÂN (POS)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE); // Chữ trắng nổi bật
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JLabel lblStaff = new JLabel("Ca làm việc: Sáng - Quản lý");
        lblStaff.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblStaff.setForeground(new Color(255, 205, 210)); // Màu hồng nhạt
        headerPanel.add(lblStaff, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // ==========================================
        // 2. BÊN TRÁI: FIX LỖI BỊ CHE MÓN BẰNG GRIDLAYOUT
        // ==========================================
        // Dùng GridLayout(0, 3) nghĩa là: Không giới hạn số Hàng, nhưng luôn ép thành 3 Cột
        menuPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        menuPanel.setBackground(new Color(245, 247, 250));
        menuPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        loadFoodFromDatabase();

        // Mẹo fix lỗi bị kéo giãn: Nhét menuPanel vào phía Bắc của một JPanel trung gian
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(new Color(245, 247, 250));
        wrapperPanel.add(menuPanel, BorderLayout.NORTH);

        JScrollPane scrollMenu = new JScrollPane(wrapperPanel);
        scrollMenu.setBorder(BorderFactory.createEmptyBorder());
        scrollMenu.getVerticalScrollBar().setUnitIncrement(20); // Lăn chuột mượt hơn
        add(scrollMenu, BorderLayout.CENTER);

        // ==========================================
        // 3. BÊN PHẢI: KHU VỰC HÓA ĐƠN
        // ==========================================
        JPanel billPanel = new JPanel(new BorderLayout());
        billPanel.setPreferredSize(new Dimension(480, 0));
        billPanel.setBackground(Color.WHITE);
        // Thêm viền đổ bóng giả bên trái
        billPanel.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(220, 220, 220)));

        // -- Thanh chọn bàn --
        JPanel tableSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        tableSelectionPanel.setBackground(Color.WHITE);
        tableSelectionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        
        JLabel lblTable = new JLabel("BÀN / ĐƠN HÀNG:");
        lblTable.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTable.setForeground(new Color(100, 100, 100));
        
        cbTable = new JComboBox<>();
        cbTable.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cbTable.setPreferredSize(new Dimension(200, 40));
        cbTable.setBackground(new Color(250, 250, 250));
        loadTablesFromDatabase(); 
        
        tableSelectionPanel.add(lblTable); 
        tableSelectionPanel.add(cbTable);
        billPanel.add(tableSelectionPanel, BorderLayout.NORTH);

        // -- Bảng Hóa đơn --
        String[] columns = {"ID", "TÊN MÓN", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"};
        billTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; } 
        };
        billTable = new JTable(billTableModel);
        billTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        billTable.setRowHeight(45);
        billTable.setSelectionBackground(new Color(227, 242, 253)); // Xanh nhạt khi chọn
        billTable.setSelectionForeground(Color.BLACK);
        
        // Header bảng
        billTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        billTable.getTableHeader().setBackground(new Color(240, 240, 240));
        billTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Ẩn cột ID_MonAn
        billTable.getColumnModel().getColumn(0).setMinWidth(0);
        billTable.getColumnModel().getColumn(0).setMaxWidth(0);
        billTable.getColumnModel().getColumn(0).setWidth(0);
        
        // Chỉnh độ rộng
        billTable.getColumnModel().getColumn(1).setPreferredWidth(170);
        billTable.getColumnModel().getColumn(2).setPreferredWidth(40);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        billTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        billTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        billTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        JScrollPane scrollBill = new JScrollPane(billTable);
        scrollBill.setBorder(BorderFactory.createEmptyBorder());
        scrollBill.getViewport().setBackground(Color.WHITE);
        billPanel.add(scrollBill, BorderLayout.CENTER);

        // -- Khu vực thanh toán --
        JPanel checkoutPanel = new JPanel();
        checkoutPanel.setLayout(new BoxLayout(checkoutPanel, BoxLayout.Y_AXIS));
        checkoutPanel.setBackground(new Color(250, 250, 250));
        checkoutPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setBackground(new Color(250, 250, 250));
        JLabel lblTotalText = new JLabel("TỔNG CỘNG:");
        lblTotalText.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalText.setForeground(new Color(80, 80, 80));
        
        lblTotalAmount = new JLabel("0đ");
        lblTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTotalAmount.setForeground(new Color(198, 40, 40)); // Màu đỏ đậm
        
        totalRow.add(lblTotalText, BorderLayout.WEST); 
        totalRow.add(lblTotalAmount, BorderLayout.EAST);

        // NÚT XÓA & THANH TOÁN
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        actionPanel.setBackground(new Color(250, 250, 250));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JButton btnClear = createFlatButton("XÓA MÓN", new Color(224, 224, 224), new Color(97, 97, 97));
        btnClear.addActionListener(e -> {
            int selectedRow = billTable.getSelectedRow();
            if (selectedRow != -1) {
                String strTotal = billTableModel.getValueAt(selectedRow, 4).toString().replaceAll("[^0-9]", "");
                totalAmount -= Double.parseDouble(strTotal);
                lblTotalAmount.setText(df.format(totalAmount));
                billTableModel.removeRow(selectedRow);
            } else { JOptionPane.showMessageDialog(this, "Vui lòng chọn một món trong hóa đơn để xóa!"); }
        });

        JButton btnPay = createFlatButton("THANH TOÁN", new Color(46, 125, 50), Color.WHITE); // Xanh lá cây xịn
        btnPay.addActionListener(e -> {
            if (billTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Hóa đơn đang trống!"); return;
            }
            if(cbTable.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Chưa có bàn nào trống để chọn!"); return;
            }

            ComboItem selectedTable = (ComboItem) cbTable.getSelectedItem();
            int idBan = selectedTable.getId();

            boolean isSuccess = posController.luuHoaDon(idBan, billTableModel);

            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "THANH TOÁN THÀNH CÔNG!\nBàn: " + selectedTable.toString() + "\nTổng tiền thu: " + df.format(totalAmount));
                billTableModel.setRowCount(0); totalAmount = 0; lblTotalAmount.setText("0đ");
                loadTablesFromDatabase(); 
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        actionPanel.add(btnClear); 
        actionPanel.add(btnPay);
        checkoutPanel.add(totalRow); 
        checkoutPanel.add(actionPanel);
        
        billPanel.add(checkoutPanel, BorderLayout.SOUTH);
        add(billPanel, BorderLayout.EAST);
    }

    private void loadFoodFromDatabase() {
        menuPanel.removeAll();
        List<Object[]> foods = posController.layDanhSachMonAn();
        for (Object[] f : foods) {
            int idMon = (int) f[0];
            String ten = (String) f[1];
            double gia = (double) f[2];
            menuPanel.add(createPosItemBtn(idMon, ten, gia));
        }
        menuPanel.revalidate(); menuPanel.repaint();
    }

    private void loadTablesFromDatabase() {
        cbTable.removeAllItems();
        List<Object[]> tables = posController.layDanhSachBanTrong();
        for (Object[] t : tables) {
            cbTable.addItem(new ComboItem((int)t[0], (String)t[1]));
        }
    }

    // ==========================================
    // LÀM ĐẸP THẺ MÓN ĂN (CARD UI)
    // ==========================================
    private JPanel createPosItemBtn(int idMon, String name, double price) {
        JPanel item = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Vẽ nền trắng
                g2.setColor(Color.WHITE); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                // Vẽ viền xám nhạt
                g2.setColor(new Color(220, 220, 220)); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        item.setPreferredSize(new Dimension(180, 120));
        item.setLayout(new GridBagLayout()); // Dùng GridBagLayout để chữ luôn ra giữa
        item.setOpaque(false); 
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(5, 5, 5, 5);

        // Khống chế độ dài tên món, nếu dài quá thì thêm "..."
        String displayName = name;
        if(name.length() > 20) displayName = name.substring(0, 17) + "...";
        
        JLabel lblName = new JLabel("<html><center>" + displayName + "</center></html>"); 
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 15)); 
        lblName.setForeground(new Color(50, 50, 50));
        item.add(lblName, gbc);

        gbc.gridy = 1;
        JLabel lblPrice = new JLabel(df.format(price)); 
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        lblPrice.setForeground(new Color(46, 125, 50)); // Xanh lá cây
        item.add(lblPrice, gbc);

        // Hiệu ứng Hover đổi viền
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { item.setBorder(BorderFactory.createLineBorder(new Color(198, 40, 40), 2)); }
            public void mouseExited(MouseEvent e) { item.setBorder(BorderFactory.createEmptyBorder()); }
            
            public void mouseClicked(MouseEvent e) {
                boolean found = false;
                for (int i = 0; i < billTableModel.getRowCount(); i++) {
                    if ((int)billTableModel.getValueAt(i, 0) == idMon) {
                        int qty = (int) billTableModel.getValueAt(i, 2);
                        billTableModel.setValueAt(qty + 1, i, 2);
                        billTableModel.setValueAt(df.format((qty + 1) * price), i, 4);
                        found = true; break;
                    }
                }
                if (!found) billTableModel.addRow(new Object[]{idMon, name, 1, df.format(price), df.format(price)});
                totalAmount += price; lblTotalAmount.setText(df.format(totalAmount));
            }
        });
        return item;
    }

    // ==========================================
    // LÀM ĐẸP NÚT BẤM (FLAT BUTTON)
    // ==========================================
    private JButton createFlatButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(bgColor.darker());
                else if (getModel().isRollover()) g2.setColor(bgColor.brighter());
                else g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        btn.setForeground(fgColor); 
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(0, 60));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        JFrame frame = new JFrame("Test Giao diện POS Thu Ngân - Đã nối CSDL & Nâng cấp UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 800); // Mở to ra thêm chút
        frame.setLocationRelativeTo(null); 
        frame.add(new CashierPosPanel()); 
        frame.setVisible(true);
    }
}