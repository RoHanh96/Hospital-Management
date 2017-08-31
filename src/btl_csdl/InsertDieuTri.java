/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btl_csdl;

import Chung.Connect;
import Chung.SetCursor;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

//import btl_csdl.GetTime;
/**
 *
 * @author trieu_000
 */
public class InsertDieuTri extends javax.swing.JFrame {

    /**
     * Creates new form InsertDieuTri
     */
    private static String IDBN;
    private static String IDBS;
    private static Connection con;
    private static PreparedStatement stmt;
    private static ResultSet result;
    private DefaultComboBoxModel model;
//    private static Thread showTime;
    DateFormat timeType = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();

    public InsertDieuTri(String IDBS, String IDBN) {
        this.IDBN = IDBN;
        this.IDBS = IDBS;
        
        initComponents();
        con = new Connect().connect();
        this.setLocationRelativeTo(null);//set frame chinh giua man hinh
        loadTen();
        setTime();
        loadComboBox();
        new SetCursor().setCusor(this);
        setVisible(true);
        cb.requestFocusInWindow();
    }
    
    public void loadComboBox() {
        String sql = new String("Select \"Ten\" FROM \"Khoa\" ORDER BY \"Ten\"");
        try {
            stmt = con.prepareStatement(sql);
            result = stmt.executeQuery();
            model = new DefaultComboBoxModel();
            while (result.next()) {
                model.addElement(result.getString("Ten"));
            }
            cb.setModel(model);
        } catch (SQLException ex) {
        }
    }

    public void setTime() {
        Thread timeSet = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        
                        tfTBDH.setText(new Integer(date.getHours()).toString());
                        tfTBDM.setText(new Integer(date.getMonth() + 1).toString());
                        tfTBDd.setText(new Integer(date.getDate()).toString());
                        tfTBDm.setText(new Integer(date.getMinutes()).toString());
                        tfTBDy.setText(new Integer(date.getYear() + 1900).toString());
//                        tfTKTH.setText(new Integer(date.getHours()).toString());
//                        tfTKTM.setText(new Integer(date.getMonth() + 1).toString());
//                        tfTKTd.setText(new Integer(date.getDate()).toString());
//                        tfTKTm.setText(new Integer(date.getMinutes()).toString());
//                        tfTKTy.setText(new Integer(date.getYear() + 1900).toString());
//                        System.out.println(new Integer(date.getYear() + 1900).toString());
                        Thread.sleep(60000);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(BenhNhan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        timeSet.start();
    }

    public void loadTen() {
        String sql = new String("Select \"Ten\" FROM \"BenhNhan\" WHERE lower(\"IDBN\") = ?");
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, IDBN.toLowerCase().trim());
            result = stmt.executeQuery();
            if (result.next()) {
                jLabel.setText("Bệnh nhân: " + result.getString("Ten"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(InsertChanDoan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String convertNameToID(String name, String IDType, String relation) {
        String sql1 = new String("SELECT * FROM \"" + relation + "\" WHERE lower(\"Ten\") = ?");
        try {
            stmt = con.prepareStatement(sql1);
            stmt.setString(1, name.toLowerCase().trim());
            result = stmt.executeQuery();
            if (result.next()) {
                return result.getString(IDType).trim();
            } else {
                JOptionPane.showMessageDialog(null, "Khong co thuoc tinh \"Ten\" hoac Khong ton tai ten: \"" + name + "\"");
                return "JAV";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "JAV";//Khong bao gio xay ra TH nay
    }

    public boolean checkIDValid(String IDKhoa, String relation) {
        String sql1 = new String("SELECT \"IDKhoa\" FROM \"" + relation + "\" WHERE lower(\"IDKhoa\") = ?");
        try {
            stmt = con.prepareStatement(sql1);
            stmt.setString(1, IDKhoa.toLowerCase().trim());
            result = stmt.executeQuery();
            if (result.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    boolean compareTime(Date timeBD, Date timeKT) {
        if (timeBD.compareTo(timeKT) >= 0) {
            return true;
        }
        return false;
    }

    Date getTimeBD() {
        String StrTime = new String(tfTBDy.getText() + "-"
                + tfTBDM.getText() + "-"
                + tfTBDd.getText() + " "
                + tfTBDH.getText() + ":"
                + tfTBDm.getText() + ":00");
        Date time = null;
        try {
            time = timeType.parse(StrTime);
            return time;
        } catch (Exception ex) {
            Logger.getLogger(InsertDieuTri.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "ERROR at Date getTimeBD()");
        }
        return time;
    }

//    Date getTimeKT() {
//
//        String StrTime = new String(tfTKTy.getText() + "-"
//                + tfTKTM.getText() + "-"
//                + tfTKTd.getText() + " "
//                + tfTKTH.getText() + ":"
//                + tfTKTm.getText() + ":00");
//        Date time = null;
//        try {
//            time = timeType.parse(StrTime);
//            return time;
//        } catch (Exception ex) {
//            Logger.getLogger(InsertDieuTri.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, "ERROR at Date getTimeBD()");
//        }
//        return time;
//    }

    public void insert(String relation, String IDInsert) {//can IDBN, IDBS, IDBenh
//        if (checkIDValid(IDKhoa, "Khoa")) {

//            String sql = new String("INSERT INTO \"" + relation + "\" VALUES (?, ?, ?, ?, ?)");
            String sql = new String("INSERT INTO \"" + relation + "\" VALUES (?, ?, ?, ?)");
            try {
                stmt = con.prepareStatement(sql);
                stmt.setString(1, IDBN);
                stmt.setString(2, IDInsert);
                stmt.setString(3, tpLyDo.getText());
                Date timeBD = getTimeBD();
//                Date timeKT = getTimeKT();
//                if (compareTime(timeBD, timeKT)) {
//                    JOptionPane.showMessageDialog(null, "Thời gian bắt đầu phải trước thời gian kết thúc");
//                    return;
//                }
                String StrTimeBD = timeType.format(timeBD);
//                String StrTimeKT = timeType.format(timeKT);
                stmt.setString(4, StrTimeBD);
//                stmt.setString(5, StrTimeKT);
                System.out.println(stmt);
                try {
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Đã thêm");
                } catch (Exception e) {
                    System.out.println(e.toString());
                    JOptionPane.showMessageDialog(null, "Lỗi!");
                }

            } catch (SQLException ex) {
                Logger.getLogger(InsertChanDoan.class.getName()).log(Level.SEVERE, null, ex);
            }
//        } else {
//            JOptionPane.showMessageDialog(null, "INSERT FAIL");
//        }
    }
    
    public Object [] getTextFields(){
        Object [] components = new Object[10];
        components[0] = tfTBDH;
        components[1] = tfTBDm;
        components[2] = tfTBDd;
        components[3] = tfTBDM;
        components[4] = tfTBDy;
//        components[5] = tfTKTH;
//        components[6] = tfTKTm;
//        components[7] = tfTKTd;
//        components[8] = tfTKTM;
//        components[9] = tfTKTy;
        return components;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel = new javax.swing.JLabel();
        bDong = new javax.swing.JButton();
        bThem = new javax.swing.JButton();
        tfTBDH = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tpLyDo = new javax.swing.JTextPane();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfTBDm = new javax.swing.JTextField();
        tfTBDd = new javax.swing.JTextField();
        tfTBDM = new javax.swing.JTextField();
        tfTBDy = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        cb = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Điều trị cho bênh nhân:");
        setResizable(false);

        jLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel.setText("Tên bệnh nhân (đã xong)");

        bDong.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        bDong.setText("Đóng");
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

        bThem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        bThem.setText("Thêm");
        bThem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bThemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                bThemFocusLost(evt);
            }
        });
        bThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bThemActionPerformed(evt);
            }
        });
        bThem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bThemKeyPressed(evt);
            }
        });

        tfTBDH.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTBDH.setForeground(new java.awt.Color(153, 153, 153));
        tfTBDH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTBDH.setText("HH");
        tfTBDH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTBDHMouseClicked(evt);
            }
        });
        tfTBDH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfTBDHActionPerformed(evt);
            }
        });
        tfTBDH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTBDHKeyPressed(evt);
            }
        });

        tpLyDo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tpLyDo.setForeground(new java.awt.Color(153, 153, 153));
        tpLyDo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tpLyDoKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tpLyDo);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Tên khoa:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Thời điểm bắt đầu:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Lý do:");

        tfTBDm.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTBDm.setForeground(new java.awt.Color(153, 153, 153));
        tfTBDm.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTBDm.setText("mm");
        tfTBDm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTBDmMouseClicked(evt);
            }
        });
        tfTBDm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTBDmKeyPressed(evt);
            }
        });

        tfTBDd.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTBDd.setForeground(new java.awt.Color(153, 153, 153));
        tfTBDd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTBDd.setText("dd");
        tfTBDd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTBDdMouseClicked(evt);
            }
        });
        tfTBDd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTBDdKeyPressed(evt);
            }
        });

        tfTBDM.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTBDM.setForeground(new java.awt.Color(153, 153, 153));
        tfTBDM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTBDM.setText("MM");
        tfTBDM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTBDMMouseClicked(evt);
            }
        });
        tfTBDM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTBDMKeyPressed(evt);
            }
        });

        tfTBDy.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTBDy.setForeground(new java.awt.Color(153, 153, 153));
        tfTBDy.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTBDy.setText("yyyy");
        tfTBDy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTBDyMouseClicked(evt);
            }
        });
        tfTBDy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTBDyKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("H");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("M");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("/");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("/");

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        cb.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfTBDH, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfTBDm, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfTBDd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfTBDM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfTBDy, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bThem)
                    .addComponent(bDong, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfTBDH, tfTBDM, tfTBDd, tfTBDm, tfTBDy});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(bDong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tfTBDM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfTBDy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tfTBDd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(tfTBDH, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfTBDm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jSeparator3))
                            .addComponent(bThem, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cb, jLabel5, tfTBDH, tfTBDM, tfTBDd, tfTBDm, tfTBDy});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jLabel3});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDongActionPerformed
                this.dispose();
//        System.exit(0);
    }//GEN-LAST:event_bDongActionPerformed

    private void tfTBDHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfTBDHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfTBDHActionPerformed

    private void bThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bThemActionPerformed
        String ID;
        if (!model.getSelectedItem().toString().equals("") && !model.getSelectedItem().toString().equals("Tên khoa")) {//ID rong
            ID = convertNameToID(model.getSelectedItem().toString().trim(), "IDKhoa", "Khoa");
            if(!ID.equals("JAV")){
                insert("DieuTri", ID);
            }
        }
    }//GEN-LAST:event_bThemActionPerformed

    private void tfTBDHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTBDHMouseClicked
        tfTBDH.selectAll();
    }//GEN-LAST:event_tfTBDHMouseClicked

    private void tfTBDmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTBDmMouseClicked
        tfTBDm.selectAll();
    }//GEN-LAST:event_tfTBDmMouseClicked

    private void tfTBDdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTBDdMouseClicked
        tfTBDd.selectAll();
    }//GEN-LAST:event_tfTBDdMouseClicked

    private void tfTBDMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTBDMMouseClicked
        tfTBDM.selectAll();
    }//GEN-LAST:event_tfTBDMMouseClicked

    private void tfTBDyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTBDyMouseClicked
        tfTBDy.selectAll();
    }//GEN-LAST:event_tfTBDyMouseClicked

    private void tfTBDHKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTBDHKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTBDm.requestFocusInWindow();
            tfTBDm.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            cb.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfTBDHKeyPressed

    private void tfTBDmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTBDmKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTBDd.requestFocusInWindow();
            tfTBDd.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTBDH.requestFocusInWindow();
            tfTBDH.selectAll();
        }
    }//GEN-LAST:event_tfTBDmKeyPressed

    private void tfTBDdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTBDdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTBDM.requestFocusInWindow();
            tfTBDM.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTBDm.requestFocusInWindow();
            tfTBDm.selectAll();
        }
    }//GEN-LAST:event_tfTBDdKeyPressed

    private void tfTBDMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTBDMKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTBDy.requestFocusInWindow();
            tfTBDy.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTBDd.requestFocusInWindow();
            tfTBDd.selectAll();
        }
    }//GEN-LAST:event_tfTBDMKeyPressed

    private void tfTBDyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTBDyKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tpLyDo.requestFocusInWindow();
            tpLyDo.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTBDM.requestFocusInWindow();
            tfTBDM.selectAll();
        }
    }//GEN-LAST:event_tfTBDyKeyPressed

    private void tpLyDoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tpLyDoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            bThem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTBDy.requestFocusInWindow();
            tfTBDy.selectAll();
        }
    }//GEN-LAST:event_tpLyDoKeyPressed

    private void bThemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bThemKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bThem.doClick();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bDong.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tpLyDo.requestFocusInWindow();
            tpLyDo.selectAll();
        }
    }//GEN-LAST:event_bThemKeyPressed

    private void bDongKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bDongKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bDong.doClick();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            bThem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            bThem.requestFocusInWindow();
        }
    }//GEN-LAST:event_bDongKeyPressed

    private void bDongFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bDongFocusGained
        bDong.setBackground(Color.GRAY);
        bDong.setForeground(Color.WHITE);
    }//GEN-LAST:event_bDongFocusGained

    private void bDongFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bDongFocusLost
        bDong.setBackground(Color.LIGHT_GRAY);
        bDong.setForeground(Color.BLACK);
    }//GEN-LAST:event_bDongFocusLost

    private void bThemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bThemFocusGained
        bThem.setBackground(Color.GRAY);
        bThem.setForeground(Color.WHITE);
    }//GEN-LAST:event_bThemFocusGained

    private void bThemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bThemFocusLost
        bThem.setBackground(Color.LIGHT_GRAY);
        bThem.setForeground(Color.BLACK);
    }//GEN-LAST:event_bThemFocusLost

    private void cbKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTBDH.requestFocusInWindow();
            tfTBDH.selectAll();
        } 
    }//GEN-LAST:event_cbKeyPressed

    /**
     * @param args the command line arguments
     */
    public static InsertDieuTri insertDT;

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
            java.util.logging.Logger.getLogger(InsertDieuTri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InsertDieuTri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InsertDieuTri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InsertDieuTri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InsertDieuTri("BS0001", "BN0001");
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bDong;
    private javax.swing.JButton bThem;
    private javax.swing.JComboBox<String> cb;
    private javax.swing.JLabel jLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField tfTBDH;
    private javax.swing.JTextField tfTBDM;
    private javax.swing.JTextField tfTBDd;
    private javax.swing.JTextField tfTBDm;
    private javax.swing.JTextField tfTBDy;
    private javax.swing.JTextPane tpLyDo;
    // End of variables declaration//GEN-END:variables
}
