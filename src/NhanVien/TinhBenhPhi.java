/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NhanVien;

import Chung.Connect;
import Chung.SetCursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Ronaldo Hanh
 */
public class TinhBenhPhi extends javax.swing.JFrame {

    /**
     * Creates new form TinhBenhPhi
     */
    private PreparedStatement stmt;
    private PreparedStatement stmt1;
    private Connection con;
    private ResultSet rs;
    private ResultSet rs1;
    private int tien = 0;
    private int phiTheoDoi;
    private int phiXetNghiem;
    private int tienThuoc = 0;
    boolean check = false;

    QuanLy PreObject;

    public TinhBenhPhi() {
    }

    public TinhBenhPhi(Object PreObject) {
        if (!(PreObject instanceof QuanLy)) {
            JOptionPane.showMessageDialog(null, "WARNING: TinhBenhPhi. PreObject != QuanLy");
        }
        this.PreObject = (QuanLy) PreObject;
        con = new Connect().connect();
        initComponents();
        new SetCursor().setCusor(this);
        this.setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void tinhTien() {
        tien = 0;
        phiXetNghiem = 0;
        phiTheoDoi = 0;
        String sql4 = "Select \"Ten\" from \"BenhNhan\" where lower(\"IDBN\")=?";
        try {
            stmt = con.prepareStatement(sql4);
            stmt.setString(1, tfMaSo.getText().toLowerCase());
            rs = stmt.executeQuery();
            while (rs.next()) {
                 check = true;
                tfTenBenhNhan.setText(rs.getString(1));
               
            }
        } catch (Exception e) {
        }
        if (check) {
            
            String sql = "Select \"Gia\" from \"YeuCauXetNghiem\" natural join \"XetNghiem\" where lower(\"IDBN\")= ?";
            try {
                
                stmt = con.prepareStatement(sql);
                stmt.setString(1, tfMaSo.getText().toLowerCase().trim());
                rs = stmt.executeQuery();
                while (rs.next()) {
                    System.out.println("1");
                    tien += rs.getInt(1);
                    phiXetNghiem += rs.getInt(1);
                }
                tfPhiXetNghiem.setText(new Integer(phiXetNghiem).toString() + " VNĐ");
                //System.out.println(tien);
            } catch (Exception e) {
                //System.out.println("Loi");
            }
            String sql1 = "Select \"PhiTheoDoi\" from \"TheoDoi\" where lower(\"IDBN\")=?";
            try {
                stmt = con.prepareStatement(sql1);
                stmt.setString(1, tfMaSo.getText().toLowerCase().trim());
                rs = stmt.executeQuery();
                while (rs.next()) {
                    tien += rs.getInt(1);
                    phiTheoDoi += rs.getInt(1);
                }
                tfPhiTheoDoi.setText(new Integer(phiTheoDoi).toString() + " VNĐ");
                //System.out.println(tien);
            } catch (Exception e) {
            }
            String sql2 = "Select \"IDThuoc\",\"SoLuong\" from \"KeDon\" where lower(\"IDBN\")=?";
            try {
                stmt = con.prepareStatement(sql2);
                stmt.setString(1, tfMaSo.getText().toLowerCase().trim());
                rs = stmt.executeQuery();

                while (rs.next()) {
                    //System.out.println(rs.getInt(2));
                    String sql3 = "Select \"Gia\" from \"Thuoc\" where lower(\"IDThuoc\")=?";
                    stmt1 = con.prepareStatement(sql3);
                    stmt1.setString(1, rs.getString(1).toLowerCase().trim());
                    rs1 = stmt1.executeQuery();
                    //
                    while (rs1.next()) {
                        //System.out.println(rs1.getInt(1));
                        tien += rs.getInt(2) * rs1.getInt(1);
                        tienThuoc += rs.getInt(2) * rs1.getInt(1);
                    }
                    tfTienThuoc.setText(new Integer(tienThuoc).toString() + " VNĐ");
                }
            } catch (Exception e) {
            }
            tfBenhPhi.setText(new Integer(tien).toString() + " VNĐ");
            check=false;
        }
        else if (!check){
            JOptionPane.showMessageDialog(null, "Không tồn tại bệnh nhân");
            tfBenhPhi.setText("");
            tfPhiTheoDoi.setText("");
            tfPhiXetNghiem.setText("");
            tfTenBenhNhan.setText("");
            tfTienThuoc.setText("");
            
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfMaSo = new javax.swing.JTextField();
        btTinhTien = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfTenBenhNhan = new javax.swing.JTextField();
        tfBenhPhi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfPhiXetNghiem = new javax.swing.JTextField();
        lb = new javax.swing.JLabel();
        tfPhiTheoDoi = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tfTienThuoc = new javax.swing.JTextField();
        btThoat = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tính bệnh phí");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tính viện phí");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Nhập mã số bệnh nhân");

        tfMaSo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btTinhTien.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btTinhTien.setText("Tính tiền");
        btTinhTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTinhTienActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Tên bệnh nhân");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Viện phí");

        tfTenBenhNhan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        tfBenhPhi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfBenhPhi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfBenhPhiActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Phí xét nghiệm");

        tfPhiXetNghiem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfPhiXetNghiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPhiXetNghiemActionPerformed(evt);
            }
        });

        lb.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lb.setText("Phí theo dõi");

        tfPhiTheoDoi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfPhiTheoDoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPhiTheoDoiActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Tiền thuốc");

        tfTienThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btThoat.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThoat.setText("Thoát");
        btThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThoatActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("Tìm kiếm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(tfPhiXetNghiem, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(tfPhiTheoDoi, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tfTenBenhNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfMaSo, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 25, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tfBenhPhi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfTienThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(82, 82, 82)))
                        .addComponent(btTinhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))))
            .addGroup(layout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel3});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfBenhPhi, tfTenBenhNhan, tfTienThuoc});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfPhiTheoDoi, tfPhiXetNghiem});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel4, jLabel5, jLabel6});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btTinhTien, jButton1});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tfMaSo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btTinhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tfTenBenhNhan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tfPhiXetNghiem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfPhiTheoDoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tfTienThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tfBenhPhi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, tfBenhPhi, tfMaSo, tfTenBenhNhan, tfTienThuoc});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tfPhiTheoDoi, tfPhiXetNghiem});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btTinhTien, jButton1});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btTinhTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTinhTienActionPerformed
        tinhTien();
    }//GEN-LAST:event_btTinhTienActionPerformed

    private void tfBenhPhiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfBenhPhiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfBenhPhiActionPerformed

    private void tfPhiXetNghiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPhiXetNghiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfPhiXetNghiemActionPerformed

    private void tfPhiTheoDoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPhiTheoDoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfPhiTheoDoiActionPerformed

    private void btThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThoatActionPerformed
        PreObject.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btThoatActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
        TimThongTinBenhNhan tk = new TimThongTinBenhNhan(this);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TinhBenhPhi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TinhBenhPhi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TinhBenhPhi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TinhBenhPhi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TinhBenhPhi("NV0001").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btThoat;
    private javax.swing.JButton btTinhTien;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lb;
    private javax.swing.JTextField tfBenhPhi;
    private javax.swing.JTextField tfMaSo;
    private javax.swing.JTextField tfPhiTheoDoi;
    private javax.swing.JTextField tfPhiXetNghiem;
    private javax.swing.JTextField tfTenBenhNhan;
    private javax.swing.JTextField tfTienThuoc;
    // End of variables declaration//GEN-END:variables
}