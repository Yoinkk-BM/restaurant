package Restaurant.View.Form.Staff_Form.Kitchen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Restaurant.Controller.ServiceKitchen;
import Restaurant.Model.ModelKitchenOrder;

/**
 * Giao diện chính cho đầu bếp.
 * - Hiển thị danh sách món đang chờ / đang làm dưới dạng JTable.
 * - Tự động refresh mỗi 3 giây bằng javax.swing.Timer.
 * - Đầu bếp bấm "Bắt đầu làm" hoặc "Hoàn thành" để cập nhật trạng thái.
 */
public class Kitchen_Form extends JPanel {

    // ─── Service & dữ liệu ──────────────────────────────────────────────────
    private final ServiceKitchen service = new ServiceKitchen();
    private List<ModelKitchenOrder> currentOrders; // Cache dòng hiện tại

    // ─── Timer auto-refresh ──────────────────────────────────────────────────
    private javax.swing.Timer refreshTimer;
    private static final int REFRESH_INTERVAL_MS = 3000; // 3 giây

    // ─── Tiêu đề cột bảng ────────────────────────────────────────────────────
    private static final String[] COLUMNS = {
        "#", "Hóa Đơn", "Tên Món", "SL", "Bàn", "Trạng Thái", "Thời Gian Đặt"
    };
    // Index cột ẩn (ID_KO) — lưu trong model nhưng ẩn khỏi UI
    // Ta dùng currentOrders.get(row) để lấy ID thay vì cột ẩn

    // ─── UI Components ────────────────────────────────────────────────────────
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnStart;
    private JButton btnDone;
    private JLabel lblStatus;
    private JLabel lblCount;

    public Kitchen_Form() {
        initUI();
        startAutoRefresh();
        loadData(); // Load lần đầu ngay lập tức
    }

    // =========================================================================
    // KHỞI TẠO GIAO DIỆN
    // =========================================================================
    private void initUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(245, 245, 245));

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        // ── Bảng món ──────────────────────────────────────────────────────────
        JScrollPane scrollPane = createTable();
        add(scrollPane, BorderLayout.CENTER);

        // ── Footer (nút bấm + trạng thái) ────────────────────────────────────
        JPanel footer = createFooter();
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel(" MÀN HÌNH BẾP  —  DANH SÁCH MÓN CẦN CHẾ BIẾN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        lblCount = new JLabel("Đang tải...");
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCount.setForeground(new Color(180, 220, 255));

        panel.add(title, BorderLayout.WEST);
        panel.add(lblCount, BorderLayout.EAST);
        return panel;
    }

    private JScrollPane createTable() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Không cho sửa trực tiếp
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(36);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ==========================================
        // 1. KẺ VẠCH CHO NỘI DUNG BẢNG
        // ==========================================
        table.setGridColor(new Color(180, 180, 180)); 
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new java.awt.Dimension(1, 1));

        // ==========================================
        // 2. ÉP MÀU VÀ KẺ VẠCH CHO TIÊU ĐỀ
        // ==========================================
        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(50, 50, 50)); 
        headerRenderer.setForeground(Color.WHITE);           
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        headerRenderer.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(100, 100, 100)));
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        // Căn giữa các cột số
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // #
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // HĐ
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // SL

        // Renderer tô màu dòng theo trạng thái
        table.setDefaultRenderer(Object.class, new StatusColorRenderer());

        // Độ rộng cột
        int[] widths = {40, 80, 260, 50, 100, 130, 160};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // ==========================================
        // 3. ẨN CỘT ID VÀ HÓA ĐƠN
        // ==========================================
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);

        table.getColumnModel().getColumn(1).setMinWidth(0);
        table.getColumnModel().getColumn(1).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(0);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 14, 0, 14));
        return sp;
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 210)));

        // Nút Bắt đầu làm
        btnStart = new JButton(" Bắt đầu làm");
        styleButton(btnStart, new Color(52, 152, 219), Color.WHITE);
        btnStart.addActionListener(e -> onStartCooking());

        // Nút Hoàn thành
        btnDone = new JButton(" Hoàn thành");
        styleButton(btnDone, new Color(39, 174, 96), Color.WHITE);
        btnDone.addActionListener(e -> onMarkDone());

        // Nhãn trạng thái hệ thống
        lblStatus = new JLabel("● Đang kết nối...");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(Color.GRAY);

        panel.add(btnStart);
        panel.add(btnDone);
        panel.add(Box.createHorizontalStrut(30));
        panel.add(lblStatus);
        return panel;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(170, 40));
    }

    // =========================================================================
    // LOAD DỮ LIỆU TỪ DB
    // =========================================================================
    public void loadData() {
        try {
            currentOrders = service.getPendingOrders();
            populateTable(currentOrders);

            int pending = (int) currentOrders.stream()
                    .filter(o -> "Dang cho".equals(o.getTrangThai())).count();
            int cooking = (int) currentOrders.stream()
                    .filter(o -> "Dang lam".equals(o.getTrangThai())).count();

            lblCount.setText(String.format("Chờ: %d  |  Đang làm: %d", pending, cooking));
            lblStatus.setText("● Cập nhật lúc " +
                    java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            lblStatus.setForeground(new Color(39, 174, 96));

        } catch (SQLException e) {
            lblStatus.setText("● Lỗi kết nối DB!");
            lblStatus.setForeground(Color.RED);
            e.printStackTrace();
        }
    }

    private void populateTable(List<ModelKitchenOrder> orders) {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm  dd/MM");
        int idx = 1;
        for (ModelKitchenOrder o : orders) {
            String thoiGian = (o.getThoiGianDat() != null)
                    ? o.getThoiGianDat().format(fmt) : "--";

            // Hiển thị trạng thái tiếng Việt
            String trangThaiHien = switch (o.getTrangThai()) {
                case "Dang cho"     -> "⏳ Đang chờ";
                case "Dang lam"     -> "🔥 Đang làm";
                case "Hoan thanh"   -> "✅ Hoàn thành";
                default             -> o.getTrangThai();
            };

            tableModel.addRow(new Object[]{
                idx++,
                "HD #" + o.getIdHoaDon(),
                o.getTenMon(),
                o.getSoLuong(),
                o.getTenBan(),
                trangThaiHien,
                thoiGian
            });
        }
    }

    // =========================================================================
    // XỬ LÝ NÚT BẤM
    // =========================================================================
    private void onStartCooking() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một món để bắt đầu làm.",
                "Chưa chọn món", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ModelKitchenOrder order = currentOrders.get(selectedRow);

        // Chỉ chuyển từ 'Dang cho' → 'Dang lam'
        if (!"Dang cho".equals(order.getTrangThai())) {
            JOptionPane.showMessageDialog(this,
                "Món này đang ở trạng thái: " + order.getTrangThai() +
                "\nChỉ có thể bắt đầu làm món đang chờ.",
                "Không thể thực hiện", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            boolean ok = service.updateStatus(order.getId(), "Dang lam");
            if (ok) {
                loadData(); // Refresh ngay lập tức
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi cập nhật trạng thái: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onMarkDone() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn một món để đánh dấu hoàn thành.",
                "Chưa chọn món", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ModelKitchenOrder order = currentOrders.get(selectedRow);

        // Cho phép chuyển từ 'Dang cho' hoặc 'Dang lam' → 'Hoan thanh'
        String trangThaiHienTai = order.getTrangThai();
        if (!"Dang lam".equals(trangThaiHienTai) && !"Dang cho".equals(trangThaiHienTai)) {
            JOptionPane.showMessageDialog(this,
                "Chỉ có thể hoàn thành món đang chờ hoặc đang được làm.",
                "Không thể thực hiện", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            // Gọi hàm update trong Service
            boolean ok = service.updateStatus(order.getId(), "Hoan thanh");
            if (ok) {
                loadData(); // Refresh ngay lập tức màn hình bếp
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy món để cập nhật trong Database!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi cập nhật trạng thái: " + e.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    // =========================================================================
    // AUTO-REFRESH TIMER
    // =========================================================================
    private void startAutoRefresh() {
        refreshTimer = new javax.swing.Timer(REFRESH_INTERVAL_MS, e -> loadData());
        refreshTimer.setRepeats(true);
        refreshTimer.start();
    }

    /** Gọi khi form bị đóng để dừng Timer, tránh leak memory. */
    public void stopRefresh() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }

    // =========================================================================
    // RENDERER TÔ MÀU DÒNG THEO TRẠNG THÁI
    // =========================================================================
    private static class StatusColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, col);

            if (!isSelected) {
                String statusVal = String.valueOf(table.getValueAt(row, 5)); // cột Trạng Thái
                if (statusVal.contains("Đang chờ")) {
                    c.setBackground(new Color(255, 249, 219)); // vàng nhạt
                } else if (statusVal.contains("Đang làm")) {
                    c.setBackground(new Color(220, 240, 255)); // xanh nhạt
                } else {
                    c.setBackground(Color.WHITE);
                }
                c.setForeground(Color.BLACK);
            }
            return c;
        }
    }
}