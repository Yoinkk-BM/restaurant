package Restaurant.View.Form.Staff_Form.Admin;

import Restaurant.Controller.ServiceAdmin;
import Restaurant.Controller.ServiceStaff;
import Restaurant.Model.ModelNguoiDung;
import Restaurant.Model.ModelNhanVien;
import Restaurant.View.Form.MainForm;
import Restaurant.View.Swing.CustomScrollBar.ScrollBarCustom;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaffManagement_Form extends javax.swing.JPanel {

    private ServiceAdmin serviceA;
    private ServiceStaff serviceS;
    private ArrayList<ModelNhanVien> list;
    private ModelNguoiDung user;
    private ModelNhanVien admin;
    private final MainForm main;

    public StaffManagement_Form(ModelNguoiDung user, MainForm main) {
        this.user=user;
        this.main = main;
        serviceA = new ServiceAdmin();
        serviceS = new ServiceStaff();
        initComponents();
        init();
    }

    /**
     * Khởi tạo giao diện Quản lý.
     * Định dạng UI, tải danh sách từ CSDL lên bảng và gắn sự kiện (Event Listener)
     * cho các nút Thêm và Sửa để chuyển sang Form chi tiết (InsertAndUpdate_Form).
     */
    public void init() {
        txtSearch.setHint("Tìm kiếm Nhân Viên . . .");
        jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        getInfoNQL();
        //Thêm data cho Menu
        initTable();
        getNumberofS();
        
        /*
         * Xử lý sự kiện click nút Thêm.
         * Mở form InsertAndUpdate với tham số truyền vào là NULL (đại diện cho trạng thái tạo mới).
         */
        cmdAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.showForm(new InsertAndUpdate_Staff_Form(user, admin, null, main));
            }
        });

        /**
         * Xử lý sự kiện click nút Sửa.
         * Lấy ID của dòng đang được chọn trên JTable, tìm kiếm đối tượng Model tương ứng trong list.
         * Truyền đối tượng đó sang form InsertAndUpdate để hiển thị dữ liệu cũ lên form.
         */
        cmdUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ModelNhanVien staff=serviceA.getNV(tableNV.getFirstCol_RowSelected(tableNV.getSelectedRow()));
                    main.showForm(new InsertAndUpdate_Staff_Form(user, admin, staff, main));
                } catch (SQLException ex) {
                    Logger.getLogger(StaffManagement_Form.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    /**
     * Lấy thông tin chi tiết của người Quản lý (User đang đăng nhập).
     * Dùng để gán ID Quản Lý (NQL) cho những nhân viên mới được thêm vào.
     */
    public void getInfoNQL() {
        try {
            admin = serviceS.getStaff(user.getUserID());
        } catch (SQLException ex) {
            Logger.getLogger(StaffManagement_Form.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lấy tổng số lượng nhân sự hiện có trong List và hiển thị lên giao diện.
     */
    public void getNumberofS() {
        txtTong.setText(list.size() + " HR");
    }

    public void initTable() {
        try {
            list = serviceA.getListNV();
            for (ModelNhanVien data : list) {
                tableNV.addRow(new Object[]{data.getId_NV(), data.getTenNV(), data.getNgayVL(), data.getSdt(), data.getChucvu(),data.getTinhTrang()});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void searchTable(String txt) {
        tableNV.removeAllRow();
        for (ModelNhanVien data : list) {
            if ((data.getTenNV()).toLowerCase().contains(txt.toLowerCase())) {
                tableNV.addRow(new Object[]{data.getId_NV(), data.getTenNV(), data.getNgayVL(), data.getSdt(), data.getChucvu(),data.getTinhTrang()});
            }
        }
        tableNV.repaint();
        tableNV.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbTitle = new javax.swing.JLabel();
        txtSearch = new Restaurant.View.Swing.MyTextField();
        lbTong = new javax.swing.JLabel();
        txtTong = new Restaurant.View.Swing.MyTextField();
        lbNV = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableNV = new Restaurant.View.Swing.Table();
        cmdAdd = new Restaurant.View.Swing.Button();
        cmdUpdate = new Restaurant.View.Swing.Button();

        setBackground(new java.awt.Color(247, 247, 247));

        lbTitle.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/bell-boy.png"))); // NOI18N
        lbTitle.setText("Quản lý Nhân Sự");
        lbTitle.setIconTextGap(10);

        txtSearch.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/loupe (1).png"))); // NOI18N
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        lbTong.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbTong.setForeground(new java.awt.Color(89, 89, 89));
        lbTong.setText("Tổng số Nhân Viên");

        txtTong.setEditable(false);
        txtTong.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTong.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N

        lbNV.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbNV.setForeground(new java.awt.Color(89, 89, 89));
        lbNV.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbNV.setText("Danh sách Nhân Viên");

        jSeparator2.setBackground(new java.awt.Color(76, 76, 76));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tableNV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã NV", "Tên nhân viên", "Ngày vào làm", "Số điện thoại", "Chức vụ", "Tình Trạng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableNV);
        if (tableNV.getColumnModel().getColumnCount() > 0) {
            tableNV.getColumnModel().getColumn(0).setMinWidth(100);
            tableNV.getColumnModel().getColumn(0).setMaxWidth(100);
            tableNV.getColumnModel().getColumn(1).setMinWidth(200);
            tableNV.getColumnModel().getColumn(1).setPreferredWidth(30);
            tableNV.getColumnModel().getColumn(1).setMaxWidth(200);
            tableNV.getColumnModel().getColumn(2).setPreferredWidth(100);
        }

        cmdAdd.setBackground(new java.awt.Color(108, 91, 123));
        cmdAdd.setForeground(new java.awt.Color(255, 255, 255));
        cmdAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/plus.png"))); // NOI18N
        cmdAdd.setText("THÊM NV");
        cmdAdd.setFocusable(false);
        cmdAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cmdAdd.setIconTextGap(15);

        cmdUpdate.setBackground(new java.awt.Color(108, 91, 123));
        cmdUpdate.setForeground(new java.awt.Color(255, 255, 255));
        cmdUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/change.png"))); // NOI18N
        cmdUpdate.setText("SỬA THÔNG TIN");
        cmdUpdate.setFocusable(false);
        cmdUpdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 857, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbTong)
                                .addGap(50, 50, 50)
                                .addComponent(txtTong, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbTitle)
                            .addComponent(lbNV))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(cmdUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTong, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(lbNV)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed

        searchTable(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Restaurant.View.Swing.Button cmdAdd;
    private Restaurant.View.Swing.Button cmdUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbNV;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbTong;
    private Restaurant.View.Swing.Table tableNV;
    private Restaurant.View.Swing.MyTextField txtSearch;
    private Restaurant.View.Swing.MyTextField txtTong;
    // End of variables declaration//GEN-END:variables
}
