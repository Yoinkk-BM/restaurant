package Restaurant.View.Form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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

public class CashierPosPanel extends JPanel {
    private PosController posController = new PosController();
    
    // Bảng bên trái (Danh sách Hóa đơn)
    private DefaultTableModel invoiceTableModel;
    private JTable invoiceTable;
    
    // Bảng bên phải (Chi tiết Hóa đơn)
    private DefaultTableModel detailTableModel;
    private JTable detailTable;
    
    private JLabel lblTotalAmount;
    private JLabel lblSelectedTable;
    
    private int selectedInvoiceId = -1; // Lưu lại ID hóa đơn đang chọn để thanh toán
    private double totalAmount = 0;
    private DecimalFormat df = new DecimalFormat("###,###đ");

    public CashierPosPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); // Nền xám xanh cực nhạt

        // ==========================================
        // 1. HEADER: NÂNG CẤP GIAO DIỆN ĐỎ SANG TRỌNG
        // ==========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(198, 40, 40)); 
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("HỆ THỐNG THU NGÂN (POS)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JLabel lblStaff = new JLabel("Ca làm việc: Sáng - Quản lý");
        lblStaff.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblStaff.setForeground(new Color(255, 205, 210)); 
        headerPanel.add(lblStaff, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // ==========================================
        // 2. BÊN TRÁI: DANH SÁCH HÓA ĐƠN ĐANG CHỜ
        // ==========================================
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(245, 247, 250));
        leftPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblLeftTitle = new JLabel("DANH SÁCH BÀN CHƯA THANH TOÁN");
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLeftTitle.setForeground(new Color(80, 80, 80));
        lblLeftTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        leftPanel.add(lblLeftTitle, BorderLayout.NORTH);

        String[] invCols = {"Mã HĐ", "Tầng", "Tên Bàn", "Tạm Tính"};
        invoiceTableModel = new DefaultTableModel(invCols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; } 
        };
        invoiceTable = new JTable(invoiceTableModel);
        setupTableUI(invoiceTable);
        
        // Căn phải cột tiền
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        invoiceTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        JScrollPane scrollInvoice = new JScrollPane(invoiceTable);
        scrollInvoice.getViewport().setBackground(Color.WHITE);
        leftPanel.add(scrollInvoice, BorderLayout.CENTER);
        
        add(leftPanel, BorderLayout.CENTER);

        // ==========================================
        // 3. BÊN PHẢI: CHI TIẾT HÓA ĐƠN & THANH TOÁN
        // ==========================================
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(550, 0)); // Rộng hơn một chút để chứa bảng chi tiết
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(220, 220, 220)));

        // -- Thanh tiêu đề bàn đang chọn --
        JPanel tableSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        tableSelectionPanel.setBackground(Color.WHITE);
        tableSelectionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        
        lblSelectedTable = new JLabel("CHI TIẾT: Vui lòng chọn bàn bên trái...");
        lblSelectedTable.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSelectedTable.setForeground(new Color(198, 40, 40));
        tableSelectionPanel.add(lblSelectedTable);
        rightPanel.add(tableSelectionPanel, BorderLayout.NORTH);

        // -- Bảng Chi tiết món ăn --
        String[] detailCols = {"TÊN MÓN", "SL", "ĐƠN GIÁ", "THÀNH TIỀN"};
        detailTableModel = new DefaultTableModel(detailCols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; } 
        };
        detailTable = new JTable(detailTableModel);
        setupTableUI(detailTable);
        
        detailTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        detailTable.getColumnModel().getColumn(1).setPreferredWidth(40);
        detailTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        detailTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        JScrollPane scrollDetail = new JScrollPane(detailTable);
        scrollDetail.setBorder(BorderFactory.createEmptyBorder());
        scrollDetail.getViewport().setBackground(Color.WHITE);
        rightPanel.add(scrollDetail, BorderLayout.CENTER);

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
        lblTotalAmount.setForeground(new Color(198, 40, 40)); 
        
        totalRow.add(lblTotalText, BorderLayout.WEST); 
        totalRow.add(lblTotalAmount, BorderLayout.EAST);

        // NÚT LÀM MỚI & THANH TOÁN
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        actionPanel.setBackground(new Color(250, 250, 250));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JButton btnRefresh = createFlatButton("LÀM MỚI TẢI LẠI", new Color(224, 224, 224), new Color(97, 97, 97));
        btnRefresh.addActionListener(e -> {
            loadDanhSachHoaDon();
            clearDetailPanel();
        });

        JButton btnPay = createFlatButton("THANH TOÁN", new Color(46, 125, 50), Color.WHITE); 
        btnPay.addActionListener(e -> {
            if (selectedInvoiceId == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một bàn đang ăn bên trái để thanh toán!"); return;
            }

            // Gọi hàm thanh toán trong controller
            boolean isSuccess = posController.thanhToanHoaDon(selectedInvoiceId);

            if (isSuccess) {
                JOptionPane.showMessageDialog(this, "THANH TOÁN THÀNH CÔNG!\n" + lblSelectedTable.getText() + "\nTổng tiền thu: " + df.format(totalAmount));
                loadDanhSachHoaDon(); // Tải lại danh sách bàn (Bàn vừa thanh toán sẽ biến mất)
                clearDetailPanel();   // Xóa trắng bảng bên phải
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        actionPanel.add(btnRefresh); 
        actionPanel.add(btnPay);
        checkoutPanel.add(totalRow); 
        checkoutPanel.add(actionPanel);
        
        rightPanel.add(checkoutPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);

        // ==========================================
        // 4. BẮT SỰ KIỆN CLICK VÀO BẢNG DANH SÁCH BÀN
        // ==========================================
        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = invoiceTable.getSelectedRow();
                if (row >= 0) {
                    // Lấy ID hóa đơn và tên bàn từ bảng
                    selectedInvoiceId = (int) invoiceTableModel.getValueAt(row, 0);
                    String tang = invoiceTableModel.getValueAt(row, 1).toString();
                    String tenBan = invoiceTableModel.getValueAt(row, 2).toString();
                    
                    lblSelectedTable.setText("CHI TIẾT: " + tenBan + " (" + tang + ")");
                    loadChiTietHoaDon(selectedInvoiceId); // Đổ dữ liệu chi tiết
                }
            }
        });

        // Nạp dữ liệu danh sách hóa đơn lần đầu
        loadDanhSachHoaDon();
    }

    // ==========================================
    // CÁC HÀM XỬ LÝ DỮ LIỆU & GIAO DIỆN
    // ==========================================
    
    // Hàm nạp danh sách Hóa đơn chưa thanh toán (Bên trái)
    private void loadDanhSachHoaDon() {
        invoiceTableModel.setRowCount(0);
        List<Object[]> invoices = posController.layDanhSachHoaDonChuaThanhToan();
        for (Object[] inv : invoices) {
            int id = (int) inv[0];
            String tang = (String) inv[1];
            String ban = (String) inv[2];
            String tien = df.format((double) inv[3]);
            invoiceTableModel.addRow(new Object[]{id, tang, ban, tien});
        }
    }

    // Hàm nạp chi tiết các món của 1 hóa đơn (Bên phải)
    private void loadChiTietHoaDon(int idHoaDon) {
        detailTableModel.setRowCount(0);
        totalAmount = 0;
        List<Object[]> details = posController.layChiTietHoaDon(idHoaDon);
        for (Object[] d : details) {
            String tenMon = (String) d[0];
            int sl = (int) d[1];
            double donGia = (double) d[2];
            double thanhTien = (double) d[3];
            
            detailTableModel.addRow(new Object[]{tenMon, sl, df.format(donGia), df.format(thanhTien)});
            totalAmount += thanhTien;
        }
        lblTotalAmount.setText(df.format(totalAmount));
    }

    // Xóa trắng bảng chi tiết sau khi thanh toán xong
    private void clearDetailPanel() {
        selectedInvoiceId = -1;
        lblSelectedTable.setText("CHI TIẾT: Vui lòng chọn bàn bên trái...");
        detailTableModel.setRowCount(0);
        totalAmount = 0;
        lblTotalAmount.setText("0đ");
    }

   // Hàm thiết lập giao diện chuẩn cho các bảng JTable
    private void setupTableUI(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(40);
        table.setSelectionBackground(new Color(227, 242, 253));
        table.setSelectionForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Thêm 2 dòng này để ĐÓNG ĐINH các cột lại:
        table.getTableHeader().setReorderingAllowed(false); // Cấm kéo thả đổi chỗ các cột
        table.getTableHeader().setResizingAllowed(false);   // Cấm kéo giãn thay đổi độ rộng cột
    }

    // Nút bấm phẳng mượt mà
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
        JFrame frame = new JFrame("Hệ Thống Thu Ngân POS - Cập nhật Luồng dữ liệu mới");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 800); 
        frame.setLocationRelativeTo(null); 
        frame.add(new CashierPosPanel()); 
        frame.setVisible(true);
    }
}