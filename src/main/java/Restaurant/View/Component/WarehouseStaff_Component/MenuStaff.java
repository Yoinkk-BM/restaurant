package Restaurant.View.Component.WarehouseStaff_Component;

import Restaurant.Controller.Event.EventMenu;
import Restaurant.Controller.Event.EventMenuSelected;
import Restaurant.Model.ModelMenu;
import Restaurant.View.Swing.CustomScrollBar.ScrollBarCustom;
import Restaurant.View.Swing.MenuAnimation;
import Restaurant.View.Swing.MenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import net.miginfocom.swing.MigLayout;

// Panel điều hướng dành cho nhân viên kho.
// Chứa các mục quản lý nguyên liệu, thông tin kho, nhập/xuất kho, tài khoản và đăng xuất.
public class MenuStaff extends javax.swing.JPanel {

    // Gắn bộ xử lý sự kiện khi một mục menu được chọn hoặc tương tác với giao diện.
    // Đây là cách cho phép lớp bên ngoài nhận thông tin từ menu và điều khiển luồng hành động tiếp theo.
    public void addEvent(EventMenuSelected event) {
        this.event = event;
    }

    // Cho phép bật hoặc tắt khả năng mở/đóng submenu.
    // Khi bị vô hiệu hóa, menu sẽ không phản hồi thao tác người dùng để tránh chuyển hướng sai.
    public void setEnableMenu(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }

    private final MigLayout layout;
    private EventMenuSelected event;
    private boolean enableMenu = true;

    // Constructor khởi tạo giao diện menu cho nhân viên kho.
    // Ban đầu sẽ tạo các thành phần giao diện, thiết lập thanh cuộn tuỳ chỉnh và sắp xếp các mục menu theo kiểu dọc.
    public MenuStaff() {
        initComponents();
        setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setVerticalScrollBar(new ScrollBarCustom());
        layout = new MigLayout("wrap, fillx, insets 0", "[fill]", "[]0[]");
        panel.setLayout(layout);

    }

    // Khởi tạo danh sách các mục menu cho nhân viên kho.
    // Mỗi mục được gắn một icon và một nhãn hiển thị để người dùng dễ nhận biết chức năng tương ứng.
    // Các mục này thường đại diện cho các tác vụ quản lý nguyên liệu, kho và tài khoản cá nhân.
    public void initMenuItem() {
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/Icons/MenuBar/nglieu.png")), "Quản lý Nguyên Liệu"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/Icons/MenuBar/kho.png")), "Thông tin Kho"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/Icons/MenuBar/import.png")), "Quản lý Nhập Kho"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/Icons/MenuBar/export.png")), "Quản lý Xuất Kho"));
        addMenu(new ModelMenu(null, ""));
        addMenu(new ModelMenu(null, "Thông tin cá nhân"));
        addMenu(new ModelMenu(null, ""));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/Icons/MenuBar/user.png")), "Tài Khoản"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/Icons/MenuBar/logout.png")), "Đăng Xuất"));
    }

    // Thêm một mục menu mới vào panel chính bằng cách tạo một đối tượng MenuItem.
    // MenuItem sẽ nhận dữ liệu nội dung, sự kiện mở/đóng và vị trí index để hiển thị đúng trong danh sách.
    private void addMenu(ModelMenu menu) {
        panel.add(new MenuItem(menu, getEventMenu(), event, panel.getComponentCount()), "h 40!");
    }

    // Tạo bộ xử lý sự kiện cho thao tác mở hoặc đóng menu con.
    // Khi người dùng nhấn vào mục, hệ thống sẽ gọi animation để hiển thị hoặc ẩn nội dung liên quan.
    private EventMenu getEventMenu() {
        return new EventMenu() {
            @Override
            public boolean menuPress(Component com, boolean open) {
                if (enableMenu) {
                    if (open) {
                        new MenuAnimation(layout, com).openMenu();
                    } else {
                        new MenuAnimation(layout, com).closeMenu();
                    }
                    return true;
                }
                return false;
            }
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));
        setFocusCycleRoot(true);

        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setFocusable(false);
        sp.setOpaque(false);

        panel.setFocusable(false);
        panel.setOpaque(false);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );

        sp.setViewportView(panel);

        logo.setBackground(new java.awt.Color(255, 255, 255));
        logo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        logo.setForeground(new java.awt.Color(255, 255, 255));
        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/warehouse.png"))); // NOI18N
        logo.setText("<html>Warehouse Management<br> Restaurant ★★★★★</html>");
        logo.setIconTextGap(15);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Vẽ nền cho panel bằng gradient màu để tạo hiệu ứng giao diện hiện đại hơn.
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp= new GradientPaint(0, 0, Color.decode("#4B79A1"), 0, getHeight(), Color.decode("#283E51"));
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel logo;
    private javax.swing.JPanel panel;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
