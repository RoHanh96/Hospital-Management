/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btl_csdl;

import Chung.Connect;
import Chung.SetCursor;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author trieu_000
 */
public class BacSi extends javax.swing.JFrame {

    /**
     * Creates new form BacSi
     */
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet result;
    private ResultSet resultTable;

    private DefaultTableModel tableModel;

    private static String IDBS;
    private String relationName;
    private int tmpI = 0;

    BenhNhan bn;

    public BacSi(String IDBS) {
        this.IDBS = IDBS;
        initComponents();
        this.setLocationRelativeTo(null);//set frame chinh giua man hinh

        con = new Connect().connect();
        new SetCursor().setCusor(this);

        createTmpRelation("BenhNhan", "", "");
        System.out.println("IDBS: " + IDBS);
        setProfile();
        tfIDBN.requestFocusInWindow();

        this.setVisible(true);
    }

    public String convertNameToID(String name, String IDType, String relation) {
        String sql1 = new String("SELECT * FROM \"" + relation + "\" WHERE lower(\"Ten\") = ?");
        try {
            stmt = con.prepareStatement(sql1);
            stmt.setString(1, name.toLowerCase().trim());
            result = stmt.executeQuery();
            if (result.next()) {
                return result.getString(IDType);
            } else {
//                JOptionPane.showMessageDialog(null, "Khong co thuoc tinh \"Ten\" hoac Khong ton tai ten: \"" + name + "\"");
                JOptionPane.showMessageDialog(null, "Khong ton tai ten: \"" + name + "\"");
                return "JAV";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "JAV";//Khong bao gio xay ra TH nay
    }

    public void createTmpRelation(String RelationNeedJoin, String IDIn, String Column) {
        String a = new Integer(tmpI).toString();
        String aPlus = new Integer(tmpI + 1).toString();
        String newSql;
        if (IDIn.equals("")) {
            newSql = new String("CREATE TABLE tmp1 AS (SELECT * FROM \"BenhNhan\")");
        } else {
            newSql = new String("CREATE TABLE tmp" + aPlus + " AS (SELECT DISTINCT \"IDBN\", \"Ten\", \"NgaySinh\", \"GioiTinh\", \"DiaChi\", \"isBHYT\", \"Tuoi\" FROM tmp" + a + " NATURAL JOIN \"" + RelationNeedJoin + "\" WHERE tmp" + a + ".\"IDBN\" = \"" + RelationNeedJoin + "\".\"IDBN\" AND lower(\"" + Column + "\") = lower(\'" + IDIn + "\'))");
        }
        tmpI++;
        System.out.println(newSql);

        try {
            stmt = con.prepareStatement(newSql);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BacSi.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadTable("SELECT * FROM \"tmp" + aPlus + "\"");

    }

    public void deleteOldTmp(int i) {
        String sql2 = new String("DROP TABLE tmp" + i);
        try {
            stmt = con.prepareStatement(sql2);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(BacSi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadTable(String sql) {
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        try {
            stmt = con.prepareStatement(sql);
            result = stmt.executeQuery();

            int numberColumns;
            {//load tên columns từ db
                ResultSetMetaData metaData = result.getMetaData();
                numberColumns = metaData.getColumnCount();
//                ArrayList<String> arrColumns = new ArrayList<String>();
//                for (int i = 1; i <= numberColumns; i++) {
//                    arrColumns.add(metaData.getColumnName(i));
//                }
//                tableModel.setColumnIdentifiers(arrColumns.toArray());
            }
            {//load ten columns thu cong
                tableModel.setColumnIdentifiers(loadColumnNames());
            }

            ArrayList<String> arrRows = new ArrayList<String>();
            while (result.next()) {
                arrRows.clear();
                for (int i = 1; i <= numberColumns; i++) {
                    arrRows.add(result.getString(i));
                }
                tableModel.addRow(arrRows.toArray());
            }

            jTable.setModel(tableModel);

            {//set non edit able//truyen vao gi cung dc
                jTable.isCellEditable(0, 0);
            }
            if (tmpI > 1) {
                deleteOldTmp(tmpI - 1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BacSi.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Object[] loadColumnNames() {
        Object[] ob = {"IDBN", "Tên", "Ngày sinh", "Giới tính; 1: Nam, 0: Nữ", "Địa chỉ", "BHYT; 1: Có, 0: Không", "Tuổi"};
        return ob;

    }

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
            java.util.logging.Logger.getLogger(BacSi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BacSi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BacSi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BacSi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BacSi("BS0001").setVisible(true);
            }
        });
    }

    public void setProfile() {
        String sql = "SELECT * FROM \"BacSi\" WHERE lower(\"IDBS\") = ?";
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, IDBS.toLowerCase().trim());
            result = stmt.executeQuery();
            if (result.next()) {
                welcome.setText("Xin chào bác sĩ: " + result.getString("Ten").trim());
                lbID.setText("ID: " + result.getString("IDBS").trim());
                lbNS.setText("Ngày sinh: " + result.getString("NgaySinh").trim());
                if (result.getString("GioiTinh").trim().equals("0")) {
                    lbGT.setText("Giới tính: Nữ");
                } else {
                    lbGT.setText("Giới tính: Nam");

                }
                lbDC.setText("Địa chỉ: " + result.getString("DiaChi").trim());
                lbHSL.setText("Hệ số lương: " + new Double(result.getString("HSLuong").trim()));
                lbCM.setText("Chuyên môn: " + result.getString("ChuyenMon").trim());
                lbTN.setText("Thâm niên: " + result.getString("ThamNien").trim() + " năm");

                String sql1 = "SELECT * FROM \"Khoa\" WHERE lower(\"IDKhoa\") = ?";
                PreparedStatement stmt1 = con.prepareStatement(sql1);
                stmt1.setString(1, result.getString("IDKhoa").trim().toLowerCase());
                ResultSet result1 = stmt1.executeQuery();
                if (result1.next()) {
                    lbK.setText("Khoa: " + result1.getString("Ten").trim());
                }
            }
        } catch (Exception e) {
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        welcome = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfIDBN = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane4 = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextPane5 = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        lbID = new javax.swing.JLabel();
        lbNS = new javax.swing.JLabel();
        lbDC = new javax.swing.JLabel();
        lbCM = new javax.swing.JLabel();
        lbTN = new javax.swing.JLabel();
        lbK = new javax.swing.JLabel();
        lbGT = new javax.swing.JLabel();
        lbHSL = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        tfIDDieuTri = new javax.swing.JTextField();
        tfIDChanDoan = new javax.swing.JTextField();
        tfIDTheoDoi = new javax.swing.JTextField();
        tfIDThuoc = new javax.swing.JTextField();
        tfIDXetNghiem = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        tfTenBN = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        bSearch = new javax.swing.JButton();
        bClear = new javax.swing.JButton();
        tfTenDieuTri = new javax.swing.JTextField();
        tfTenChanDoan = new javax.swing.JTextField();
        tfTenTheoDoi = new javax.swing.JTextField();
        tfTenThuoc = new javax.swing.JTextField();
        tfTenXetNghiem = new javax.swing.JTextField();
        bDong = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAutoRequestFocus(false);
        setResizable(false);

        welcome.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        welcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcome.setText("Xin chào");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Tìm kiếm bệnh nhân:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("IDBN:");

        tfIDBN.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfIDBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfIDBNActionPerformed(evt);
            }
        });
        tfIDBN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfIDBNKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfIDBNKeyTyped(evt);
            }
        });

        jTextPane1.setEditable(false);
        jTextPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextPane1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextPane1.setText("              Đã từng\n        điều trị ở khoa:");
        jScrollPane1.setViewportView(jTextPane1);

        jTextPane2.setEditable(false);
        jTextPane2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextPane2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextPane2.setText("          Đã từng bị \n  chẩn đoán mắc bệnh:");
        jScrollPane2.setViewportView(jTextPane2);

        jTextPane3.setEditable(false);
        jTextPane3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextPane3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextPane3.setText("          Đang được \n    theo dõi bởi bác sĩ:");
        jScrollPane3.setViewportView(jTextPane3);

        jTextPane4.setEditable(false);
        jTextPane4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextPane4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextPane4.setText("             Đã từng \n       sử dụng thuốc:");
        jScrollPane4.setViewportView(jTextPane4);

        jTextPane5.setEditable(false);
        jTextPane5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextPane5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextPane5.setText("            Đã từng\n  thực hiện xét nghiệm:");
        jScrollPane5.setViewportView(jTextPane5);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lbID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbID.setText("ID: ");

        lbNS.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbNS.setText("Ngày sinh: ");

        lbDC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbDC.setText("Địa chỉ:");

        lbCM.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbCM.setText("Chuyên môn: ");

        lbTN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbTN.setText("Thâm niên:");

        lbK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbK.setText("Khoa: ");

        lbGT.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbGT.setText("Giới tính:");

        lbHSL.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbHSL.setText("Hệ số lương: ");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbDC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbNS, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(lbGT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbHSL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbTN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbCM, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbID)
                    .addComponent(lbHSL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbNS)
                    .addComponent(lbCM))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbGT)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbDC))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbTN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbK)))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lbCM, lbDC, lbGT, lbHSL, lbID, lbK, lbNS, lbTN});

        tfIDDieuTri.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfIDDieuTri.setForeground(new java.awt.Color(204, 204, 204));
        tfIDDieuTri.setText("ID khoa");
        tfIDDieuTri.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfIDDieuTriFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfIDDieuTriFocusLost(evt);
            }
        });
        tfIDDieuTri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfIDDieuTriKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfIDDieuTriKeyTyped(evt);
            }
        });

        tfIDChanDoan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfIDChanDoan.setForeground(new java.awt.Color(204, 204, 204));
        tfIDChanDoan.setText("ID bệnh");
        tfIDChanDoan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfIDChanDoanFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfIDChanDoanFocusLost(evt);
            }
        });
        tfIDChanDoan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfIDChanDoanKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfIDChanDoanKeyTyped(evt);
            }
        });

        tfIDTheoDoi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfIDTheoDoi.setForeground(new java.awt.Color(204, 204, 204));
        tfIDTheoDoi.setText("ID bác sĩ");
        tfIDTheoDoi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfIDTheoDoiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfIDTheoDoiFocusLost(evt);
            }
        });
        tfIDTheoDoi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfIDTheoDoiKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfIDTheoDoiKeyTyped(evt);
            }
        });

        tfIDThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfIDThuoc.setForeground(new java.awt.Color(204, 204, 204));
        tfIDThuoc.setText("ID thuốc");
        tfIDThuoc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfIDThuocFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfIDThuocFocusLost(evt);
            }
        });
        tfIDThuoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfIDThuocKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfIDThuocKeyTyped(evt);
            }
        });

        tfIDXetNghiem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfIDXetNghiem.setForeground(new java.awt.Color(204, 204, 204));
        tfIDXetNghiem.setText("ID xét nghiệm");
        tfIDXetNghiem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfIDXetNghiemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfIDXetNghiemFocusLost(evt);
            }
        });
        tfIDXetNghiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfIDXetNghiemKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfIDXetNghiemKeyTyped(evt);
            }
        });

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTableFocusLost(evt);
            }
        });
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(jTable);

        tfTenBN.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTenBN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTenBNKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfTenBNKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Tên:");

        bSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bSearch.setText("Search");
        bSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                bSearchFocusLost(evt);
            }
        });
        bSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSearchActionPerformed(evt);
            }
        });
        bSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bSearchKeyPressed(evt);
            }
        });

        bClear.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bClear.setText("Clear all");
        bClear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bClearFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                bClearFocusLost(evt);
            }
        });
        bClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bClearActionPerformed(evt);
            }
        });
        bClear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bClearKeyPressed(evt);
            }
        });

        tfTenDieuTri.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfTenDieuTri.setForeground(new java.awt.Color(204, 204, 204));
        tfTenDieuTri.setText("Tên khoa");
        tfTenDieuTri.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfTenDieuTriFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfTenDieuTriFocusLost(evt);
            }
        });
        tfTenDieuTri.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTenDieuTriKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfTenDieuTriKeyTyped(evt);
            }
        });

        tfTenChanDoan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfTenChanDoan.setForeground(new java.awt.Color(204, 204, 204));
        tfTenChanDoan.setText("Tên bệnh");
        tfTenChanDoan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfTenChanDoanFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfTenChanDoanFocusLost(evt);
            }
        });
        tfTenChanDoan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTenChanDoanKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfTenChanDoanKeyTyped(evt);
            }
        });

        tfTenTheoDoi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfTenTheoDoi.setForeground(new java.awt.Color(204, 204, 204));
        tfTenTheoDoi.setText("Tên bác sĩ");
        tfTenTheoDoi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfTenTheoDoiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfTenTheoDoiFocusLost(evt);
            }
        });
        tfTenTheoDoi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTenTheoDoiKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfTenTheoDoiKeyTyped(evt);
            }
        });

        tfTenThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfTenThuoc.setForeground(new java.awt.Color(204, 204, 204));
        tfTenThuoc.setText("Tên thuốc");
        tfTenThuoc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfTenThuocFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfTenThuocFocusLost(evt);
            }
        });
        tfTenThuoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTenThuocKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfTenThuocKeyTyped(evt);
            }
        });

        tfTenXetNghiem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tfTenXetNghiem.setForeground(new java.awt.Color(204, 204, 204));
        tfTenXetNghiem.setText("Tên xét nghiệm");
        tfTenXetNghiem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfTenXetNghiemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfTenXetNghiemFocusLost(evt);
            }
        });
        tfTenXetNghiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTenXetNghiemKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tfTenXetNghiemKeyTyped(evt);
            }
        });

        bDong.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bDong.setText("Đóng phiên làm việc");
        bDong.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bDongFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                bDongFocusLost(evt);
            }
        });
        bDong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bDongActionPerformed(evt);
            }
        });
        bDong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bDongKeyPressed(evt);
            }
        });

        jMenuBar1.add(jMenu1);
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfTenDieuTri, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                    .addComponent(tfIDDieuTri, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfIDChanDoan, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfTenChanDoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfIDTheoDoi, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfTenTheoDoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfIDThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfTenThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfTenXetNghiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfIDXetNghiem, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(welcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bDong))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfTenBN, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                                    .addComponent(tfIDBN))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(bClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(bSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfIDChanDoan, tfIDTheoDoi, tfIDThuoc, tfIDXetNghiem, tfTenChanDoan, tfTenDieuTri, tfTenTheoDoi, tfTenThuoc, tfTenXetNghiem});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel12, jLabel2});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfIDBN, tfTenBN});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane1, jScrollPane2, jScrollPane3, jScrollPane4, jScrollPane5});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bDong, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(welcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfIDBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tfTenBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12))
                            .addComponent(bClear)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfIDXetNghiem)
                    .addComponent(tfIDThuoc)
                    .addComponent(tfIDTheoDoi)
                    .addComponent(tfIDChanDoan)
                    .addComponent(tfIDDieuTri, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTenChanDoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfTenTheoDoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfTenThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfTenXetNghiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfTenDieuTri, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bClear, jLabel12, jLabel2, tfIDBN, tfTenBN});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jScrollPane1, jScrollPane2, jScrollPane3, jScrollPane4, jScrollPane5, tfIDChanDoan, tfIDDieuTri, tfIDTheoDoi, tfIDThuoc, tfIDXetNghiem});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tfTenChanDoan, tfTenDieuTri, tfTenTheoDoi, tfTenThuoc, tfTenXetNghiem});

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void tfIDDieuTriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDDieuTriKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            createTmpRelation("DieuTri", tfIDDieuTri.getText().trim(), "IDKhoa");
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfIDChanDoan.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfIDXetNghiem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bClear.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tfTenDieuTri.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfIDDieuTriKeyPressed

    public void searchIDBN(String id) {
        String sql1 = "SELECT * FROM \"BenhNhan\" WHERE lower(\"IDBN\") = ?";
        try {
            stmt = con.prepareStatement(sql1);
            stmt.setString(1, id.toLowerCase().trim());
            result = stmt.executeQuery();
            if (result.next()) {
                if (result.getString("IDBN").isEmpty() == false) {
                    bn = new BenhNhan(this, result.getString("IDBN").trim());
                }
            } else {
                System.out.println("ID \"" + id + "\" khong ton tai");
                JOptionPane.showMessageDialog(null, "ID \"" + id + "\" khong ton tai");

            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getIDBS() {
        return IDBS;
    }

    private void tfIDBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfIDBNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfIDBNActionPerformed

    private void tfIDBNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDBNKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchIDBN(tfIDBN.getText().trim());
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tfTenBN.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            bSearch.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bDong.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfIDBNKeyPressed

    private void tfIDChanDoanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDChanDoanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            createTmpRelation("ChanDoan", tfIDChanDoan.getText().trim(), "IDBenh");
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfIDTheoDoi.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfIDDieuTri.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tfTenChanDoan.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bClear.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfIDChanDoanKeyPressed

    private void tfIDTheoDoiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDTheoDoiKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            createTmpRelation("TheoDoi", tfIDTheoDoi.getText().trim(), "IDBS");
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfIDThuoc.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfIDChanDoan.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tfTenTheoDoi.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bClear.requestFocusInWindow();

        }    }//GEN-LAST:event_tfIDTheoDoiKeyPressed

    private void tfIDThuocKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDThuocKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            createTmpRelation("KeDon", tfIDThuoc.getText().trim(), "IDThuoc");
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfIDXetNghiem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfIDTheoDoi.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tfTenThuoc.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bClear.requestFocusInWindow();

        }    }//GEN-LAST:event_tfIDThuocKeyPressed

    private void tfIDXetNghiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDXetNghiemKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            createTmpRelation("YeuCauXetNghiem", tfIDXetNghiem.getText().trim(), "IDXN");
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfIDThuoc.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfIDDieuTri.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tfTenXetNghiem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bClear.requestFocusInWindow();

        }    }//GEN-LAST:event_tfIDXetNghiemKeyPressed

    private void bSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSearchActionPerformed
        if (tfTenBN.getText().equals("") == false) {
            String id = new String(convertNameToID(tfTenBN.getText().trim(), "IDBN", "BenhNhan"));
            if (id.equals("JAV") != true) {
                searchIDBN(id);
            }
        } else if (tfIDBN.getText().equals("") == false) {
            searchIDBN(tfIDBN.getText().trim());
        }
    }//GEN-LAST:event_bSearchActionPerformed

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        if (evt.getClickCount() == 2) {
            int row = jTable.rowAtPoint(evt.getPoint());
            String id = jTable.getValueAt(row, 0).toString();
            searchIDBN(id);
        }
    }//GEN-LAST:event_jTableMouseClicked

    private void bDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDongActionPerformed
        deleteOldTmp(tmpI);
        System.exit(0);
    }//GEN-LAST:event_bDongActionPerformed

    private void bClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClearActionPerformed
        tfIDBN.setText("");
        tfIDChanDoan.setText("");
        tfIDDieuTri.setText("");
        tfIDTheoDoi.setText("");
        tfIDThuoc.setText("");
        tfIDXetNghiem.setText("");
        tfTenBN.setText("");
        tfTenChanDoan.setText("");
        tfTenDieuTri.setText("");
        tfTenTheoDoi.setText("");
        tfTenThuoc.setText("");
        tfTenXetNghiem.setText("");
        {//reset jTable
            deleteOldTmp(tmpI);
            tmpI = 0;
            createTmpRelation("BenhNhan", "", "");
        }

    }//GEN-LAST:event_bClearActionPerformed

    private void tfTenBNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenBNKeyTyped
        tfIDBN.setText("");

    }//GEN-LAST:event_tfTenBNKeyTyped

    private void tfIDBNKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDBNKeyTyped
        tfTenBN.setText("");
    }//GEN-LAST:event_tfIDBNKeyTyped

    private void tfIDDieuTriKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDDieuTriKeyTyped
        tfIDDieuTri.setForeground(Color.BLACK);
        tfTenDieuTri.setText("");
        if (tfTenDieuTri.getText().equals("")) {
            tfTenDieuTri.setText("Tên khoa");
            tfTenDieuTri.setForeground(Color.GRAY);
        }

    }//GEN-LAST:event_tfIDDieuTriKeyTyped

    private void tfTenDieuTriKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenDieuTriKeyTyped
        tfTenDieuTri.setForeground(Color.BLACK);
        tfIDDieuTri.setText("");
        if (tfIDDieuTri.getText().equals("")) {
            tfIDDieuTri.setText("ID khoa");
            tfIDDieuTri.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfTenDieuTriKeyTyped

    private void tfIDChanDoanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDChanDoanKeyTyped
        tfIDChanDoan.setForeground(Color.BLACK);
        tfTenChanDoan.setText("");
        if (tfTenChanDoan.getText().equals("")) {
            tfTenChanDoan.setText("Tên bệnh");
            tfTenChanDoan.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfIDChanDoanKeyTyped

    private void tfTenChanDoanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenChanDoanKeyTyped
        tfTenChanDoan.setForeground(Color.BLACK);
        tfIDChanDoan.setText("");
        if (tfIDChanDoan.getText().equals("")) {
            tfIDChanDoan.setText("ID bệnh");
            tfIDChanDoan.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfTenChanDoanKeyTyped

    private void tfIDTheoDoiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDTheoDoiKeyTyped
        tfIDTheoDoi.setForeground(Color.BLACK);
        tfTenTheoDoi.setText("");
        if (tfTenTheoDoi.getText().equals("")) {
            tfTenTheoDoi.setText("Tên bác sĩ");
            tfTenTheoDoi.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfIDTheoDoiKeyTyped

    private void tfTenTheoDoiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenTheoDoiKeyTyped
        tfTenTheoDoi.setForeground(Color.BLACK);
        tfIDTheoDoi.setText("");
        if (tfIDTheoDoi.getText().equals("")) {
            tfIDTheoDoi.setText("ID bác sĩ");
            tfIDTheoDoi.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfTenTheoDoiKeyTyped

    private void tfIDThuocKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDThuocKeyTyped
        tfIDThuoc.setForeground(Color.BLACK);
        tfTenThuoc.setText("");
        if (tfTenThuoc.getText().equals("")) {
            tfTenThuoc.setText("Tên thuốc");
            tfTenThuoc.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfIDThuocKeyTyped

    private void tfTenThuocKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenThuocKeyTyped
        tfTenThuoc.setForeground(Color.BLACK);
        tfIDThuoc.setText("");
        if (tfIDThuoc.getText().equals("")) {
            tfIDThuoc.setText("ID thuốc");
            tfIDThuoc.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfTenThuocKeyTyped

    private void tfIDXetNghiemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfIDXetNghiemKeyTyped
        tfIDXetNghiem.setForeground(Color.BLACK);
        tfTenXetNghiem.setText("");
        if (tfTenXetNghiem.getText().equals("")) {
            tfTenXetNghiem.setText("Tên xét nghiệm");
            tfTenXetNghiem.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfIDXetNghiemKeyTyped

    private void tfTenXetNghiemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenXetNghiemKeyTyped
        tfTenXetNghiem.setForeground(Color.BLACK);
        tfIDXetNghiem.setText("");
        if (tfIDXetNghiem.getText().equals("")) {
            tfIDXetNghiem.setText("ID xét nghiệm");
            tfIDXetNghiem.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfTenXetNghiemKeyTyped

    private void tfTenBNKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenBNKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchIDBN(convertNameToID(tfTenBN.getText().trim(), "IDBN", "BenhNhan"));
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            tfIDBN.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tfIDDieuTri.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            bClear.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfTenBNKeyPressed

    private void tfTenDieuTriKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenDieuTriKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String IDDieuTri = convertNameToID(tfTenDieuTri.getText().trim(), "IDKhoa", "Khoa");
            createTmpRelation("DieuTri", IDDieuTri, "IDKhoa");
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTenChanDoan.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTenXetNghiem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            tfIDDieuTri.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            jTable.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfTenDieuTriKeyPressed

    private void tfTenChanDoanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenChanDoanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String IDChanDoan = convertNameToID(tfTenChanDoan.getText().trim(), "IDBenh", "Benh");
            createTmpRelation("ChanDoan", IDChanDoan, "IDBenh");
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTenTheoDoi.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTenDieuTri.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            tfIDChanDoan.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            jTable.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfTenChanDoanKeyPressed

    private void tfTenTheoDoiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenTheoDoiKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String IDTheoDoi = convertNameToID(tfTenTheoDoi.getText().trim(), "IDBS", "BacSi");
            createTmpRelation("TheoDoi", IDTheoDoi, "IDBS");
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTenThuoc.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTenChanDoan.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            tfIDTheoDoi.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            jTable.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfTenTheoDoiKeyPressed

    private void tfTenThuocKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenThuocKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String IDThuoc = convertNameToID(tfTenThuoc.getText().trim(), "IDThuoc", "Thuoc");
            createTmpRelation("KeDon", IDThuoc, "IDThuoc");
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTenXetNghiem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTenTheoDoi.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            tfIDThuoc.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            jTable.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfTenThuocKeyPressed

    private void tfTenXetNghiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTenXetNghiemKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String IDXetNghiem = convertNameToID(tfTenXetNghiem.getText().trim(), "IDXN", "XetNghiem");
            createTmpRelation("YeuCauXetNghiem", IDXetNghiem, "IDXN");
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTenThuoc.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTenDieuTri.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            tfIDXetNghiem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            jTable.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfTenXetNghiemKeyPressed

    private void tfIDDieuTriFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfIDDieuTriFocusLost
        if (tfIDDieuTri.getText().equals("")) {
            tfIDDieuTri.setText("ID khoa");
            tfIDDieuTri.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfIDDieuTriFocusLost

    private void tfTenDieuTriFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfTenDieuTriFocusLost
        if (tfTenDieuTri.getText().equals("")) {
            tfTenDieuTri.setText("Tên khoa");
            tfTenDieuTri.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfTenDieuTriFocusLost

    private void tfIDChanDoanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfIDChanDoanFocusLost
        if (tfIDChanDoan.getText().equals("")) {
            tfIDChanDoan.setText("ID bệnh");
            tfIDChanDoan.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfIDChanDoanFocusLost

    private void tfTenChanDoanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfTenChanDoanFocusLost
        if (tfTenChanDoan.getText().equals("")) {
            tfTenChanDoan.setText("Tên bệnh");
            tfTenChanDoan.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfTenChanDoanFocusLost

    private void tfIDTheoDoiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfIDTheoDoiFocusLost
        if (tfIDTheoDoi.getText().equals("")) {
            tfIDTheoDoi.setText("ID bác sĩ");
            tfIDTheoDoi.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfIDTheoDoiFocusLost

    private void tfTenTheoDoiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfTenTheoDoiFocusLost
        if (tfTenTheoDoi.getText().equals("")) {
            tfTenTheoDoi.setText("Tên bác sĩ");
            tfTenTheoDoi.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfTenTheoDoiFocusLost

    private void tfIDThuocFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfIDThuocFocusLost
        if (tfIDThuoc.getText().equals("")) {
            tfIDThuoc.setText("ID thuốc");
            tfIDThuoc.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfIDThuocFocusLost

    private void tfTenThuocFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfTenThuocFocusLost
        if (tfTenThuoc.getText().equals("")) {
            tfTenThuoc.setText("Tên thuốc");
            tfTenThuoc.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfTenThuocFocusLost

    private void tfIDXetNghiemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfIDXetNghiemFocusLost
        if (tfIDXetNghiem.getText().equals("")) {
            tfIDXetNghiem.setText("ID xét nghiệm");
            tfIDXetNghiem.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfIDXetNghiemFocusLost

    private void tfTenXetNghiemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfTenXetNghiemFocusLost
        if (tfTenXetNghiem.getText().equals("")) {
            tfTenXetNghiem.setText("Tên xét nghiệm");
            tfTenXetNghiem.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_tfTenXetNghiemFocusLost

    private void tfIDDieuTriFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfIDDieuTriFocusGained
        if (tfIDDieuTri.getText().equals("ID khoa")) {
            tfIDDieuTri.selectAll();
        }
    }//GEN-LAST:event_tfIDDieuTriFocusGained

    private void tfTenDieuTriFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfTenDieuTriFocusGained
        if (tfTenDieuTri.getText().equals("Tên khoa")) {
            tfTenDieuTri.selectAll();
        }
    }//GEN-LAST:event_tfTenDieuTriFocusGained

    private void tfIDChanDoanFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfIDChanDoanFocusGained
        if (tfIDChanDoan.getText().equals("ID bệnh")) {
            tfIDChanDoan.selectAll();
        }
    }//GEN-LAST:event_tfIDChanDoanFocusGained

    private void tfTenChanDoanFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfTenChanDoanFocusGained
        if (tfTenChanDoan.getText().equals("Tên bệnh")) {
            tfTenChanDoan.selectAll();
        }
    }//GEN-LAST:event_tfTenChanDoanFocusGained

    private void tfIDTheoDoiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfIDTheoDoiFocusGained
        if (tfIDTheoDoi.getText().equals("ID bác sĩ")) {
            tfIDTheoDoi.selectAll();
        }
    }//GEN-LAST:event_tfIDTheoDoiFocusGained

    private void tfTenTheoDoiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfTenTheoDoiFocusGained
        if (tfTenTheoDoi.getText().equals("Tên bác sĩ")) {
            tfTenTheoDoi.selectAll();
        }
    }//GEN-LAST:event_tfTenTheoDoiFocusGained

    private void tfIDThuocFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfIDThuocFocusGained
        if (tfIDThuoc.getText().equals("ID thuốc")) {
            tfIDThuoc.selectAll();
        }
    }//GEN-LAST:event_tfIDThuocFocusGained

    private void tfTenThuocFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfTenThuocFocusGained
        if (tfTenThuoc.getText().equals("Tên thuốc")) {
            tfTenThuoc.selectAll();
        }
    }//GEN-LAST:event_tfTenThuocFocusGained

    private void tfIDXetNghiemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfIDXetNghiemFocusGained
        if (tfIDXetNghiem.getText().equals("ID xét nghiệm")) {
            tfIDXetNghiem.selectAll();
        }
    }//GEN-LAST:event_tfIDXetNghiemFocusGained

    private void tfTenXetNghiemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfTenXetNghiemFocusGained
        if (tfTenXetNghiem.getText().equals("Tên xét nghiệm")) {
            tfTenXetNghiem.selectAll();
        }
    }//GEN-LAST:event_tfTenXetNghiemFocusGained

    private void jTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int row = jTable.getSelectedRow();
            String id = jTable.getValueAt(row, 0).toString();
            searchIDBN(id);
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            if (jTable.getRowCount() == 0) {
                tfTenDieuTri.requestFocusInWindow();
            } else if (jTable.isRowSelected(0)) {
                tfTenDieuTri.requestFocusInWindow();
            }
        }
    }//GEN-LAST:event_jTableKeyPressed

    private void jTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableFocusLost
        jTable.clearSelection();
    }//GEN-LAST:event_jTableFocusLost

    private void bSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bSearchKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bSearch.doClick();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfIDBN.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            bClear.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bDong.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            bDong.requestFocusInWindow();
        }
    }//GEN-LAST:event_bSearchKeyPressed

    private void bClearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bClearKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bClear.doClick();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTenBN.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tfIDDieuTri.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bSearch.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            bDong.requestFocusInWindow();
        }
    }//GEN-LAST:event_bClearKeyPressed

    private void bDongKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bDongKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bDong.doClick();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfIDBN.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            tfIDBN.requestFocusInWindow();
        }
    }//GEN-LAST:event_bDongKeyPressed

    private void bSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bSearchFocusGained
        bSearch.setBackground(Color.GRAY);
        bSearch.setForeground(Color.WHITE);
    }//GEN-LAST:event_bSearchFocusGained

    private void bSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bSearchFocusLost
        bSearch.setBackground(Color.LIGHT_GRAY);
        bSearch.setForeground(Color.BLACK);
    }//GEN-LAST:event_bSearchFocusLost

    private void bClearFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bClearFocusGained
        bClear.setBackground(Color.GRAY);
        bClear.setForeground(Color.WHITE);
    }//GEN-LAST:event_bClearFocusGained

    private void bClearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bClearFocusLost
        bClear.setBackground(Color.LIGHT_GRAY);
        bClear.setForeground(Color.BLACK);
    }//GEN-LAST:event_bClearFocusLost

    private void bDongFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bDongFocusGained
        bDong.setBackground(Color.GRAY);
        bDong.setForeground(Color.WHITE);
    }//GEN-LAST:event_bDongFocusGained

    private void bDongFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bDongFocusLost
        bDong.setBackground(Color.LIGHT_GRAY);
        bDong.setForeground(Color.BLACK);
    }//GEN-LAST:event_bDongFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClear;
    private javax.swing.JButton bDong;
    private javax.swing.JButton bSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    private javax.swing.JTextPane jTextPane4;
    private javax.swing.JTextPane jTextPane5;
    private javax.swing.JLabel lbCM;
    private javax.swing.JLabel lbDC;
    private javax.swing.JLabel lbGT;
    private javax.swing.JLabel lbHSL;
    private javax.swing.JLabel lbID;
    private javax.swing.JLabel lbK;
    private javax.swing.JLabel lbNS;
    private javax.swing.JLabel lbTN;
    private javax.swing.JTextField tfIDBN;
    private javax.swing.JTextField tfIDChanDoan;
    private javax.swing.JTextField tfIDDieuTri;
    private javax.swing.JTextField tfIDTheoDoi;
    private javax.swing.JTextField tfIDThuoc;
    private javax.swing.JTextField tfIDXetNghiem;
    private javax.swing.JTextField tfTenBN;
    private javax.swing.JTextField tfTenChanDoan;
    private javax.swing.JTextField tfTenDieuTri;
    private javax.swing.JTextField tfTenTheoDoi;
    private javax.swing.JTextField tfTenThuoc;
    private javax.swing.JTextField tfTenXetNghiem;
    private javax.swing.JLabel welcome;
    // End of variables declaration//GEN-END:variables
}
