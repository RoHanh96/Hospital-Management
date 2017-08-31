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

/**
 *
 * @author trieu_000
 */
public class InsertKeDon extends javax.swing.JFrame {

    /**
     * Creates new form InsertKeDon
     */
    private static String IDBN;
    private static String IDBS;
    private static Connection con;
    private static PreparedStatement stmt;
    private static ResultSet result;
    private DefaultComboBoxModel model;

    DateFormat timeType = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();

    public InsertKeDon() {

    }

    public InsertKeDon(String IDBS, String IDBN) {
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
        String sql = new String("Select \"Ten\" FROM \"Thuoc\" ORDER BY \"Ten\"");
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

    public void loadTen() {
        String sql = new String("Select \"Ten\" FROM \"BenhNhan\" WHERE lower(\"IDBN\") = ?");
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, IDBN.toLowerCase().trim());
            result = stmt.executeQuery();
            if (result.next()) {
                jLabel1.setText("Bệnh nhân: " + result.getString("Ten"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(InsertChanDoan.class.getName()).log(Level.SEVERE, null, ex);
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
                        Thread.sleep(60000);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(BenhNhan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        timeSet.start();
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

    public void insert(String relation, String IDInsert) {//can IDBN, IDBS, IDBenh
        String sql = new String("INSERT INTO \"" + relation + "\" VALUES (?, ?, ?, ?, ?)");
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, IDBS);
            stmt.setString(2, IDBN);
            stmt.setString(3, IDInsert);
            try {
                stmt.setInt(4, Integer.parseInt(tfSoLuong.getText()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Trường \"Số lượng\" không hợp lệ");
            }
            Date timeBD = getTimeBD();
            String StrTimeBD = timeType.format(timeBD);
            stmt.setString(5, StrTimeBD);
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        bDong = new javax.swing.JButton();
        tfSoLuong = new javax.swing.JTextField();
        bThem = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfTBDH = new javax.swing.JTextField();
        tfTBDm = new javax.swing.JTextField();
        tfTBDd = new javax.swing.JTextField();
        tfTBDM = new javax.swing.JTextField();
        tfTBDy = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        cb = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Kê đơn cho bệnh nhân:");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Tên bệnh nhân (đã xong)");

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

        tfSoLuong.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfSoLuong.setForeground(new java.awt.Color(153, 153, 153));
        tfSoLuong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfSoLuongMouseClicked(evt);
            }
        });
        tfSoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfSoLuongKeyPressed(evt);
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Tên thuốc:");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Số lượng:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Thời điểm:");

        tfTBDH.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTBDH.setForeground(new java.awt.Color(153, 153, 153));
        tfTBDH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTBDH.setText("HH");
        tfTBDH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTBDHMouseClicked(evt);
            }
        });
        tfTBDH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTBDHKeyPressed(evt);
            }
        });

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
        jLabel5.setText("H");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("M");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("/");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("/");

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                            .addComponent(cb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                            .addComponent(tfSoLuong))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(97, 97, 97))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(bDong, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tfTBDH, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfTBDm, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)
                                        .addGap(7, 7, 7)
                                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tfTBDd, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfTBDM, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfTBDy, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bThem)))
                                .addContainerGap(20, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 807, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfTBDH, tfTBDM, tfTBDd, tfTBDm, tfTBDy});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bDong))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tfSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel5)
                                            .addComponent(tfTBDm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel8)
                                            .addComponent(tfTBDd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfTBDM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(tfTBDy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bThem))
                                .addComponent(tfTBDH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jSeparator2)
                        .addComponent(jSeparator1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bThem, cb, jLabel5, jLabel6, jLabel7, jLabel8, jSeparator3, tfSoLuong, tfTBDH, tfTBDM, tfTBDd, tfTBDm, tfTBDy});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bDong, jLabel1});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDongActionPerformed
        this.dispose();
//        System.exit(0);
    }//GEN-LAST:event_bDongActionPerformed

    private void bThemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bThemKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bThem.doClick();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bDong.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTBDy.requestFocusInWindow();
            tfTBDy.selectAll();
        }
    }//GEN-LAST:event_bThemKeyPressed

    private void bThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bThemActionPerformed
        String ID;
        if (!model.getSelectedItem().toString().equals("") && !model.getSelectedItem().toString().equals("Tên thuốc")) {//ID rong
            ID = convertNameToID(model.getSelectedItem().toString().trim(), "IDThuoc", "Thuoc");
            insert("KeDon", ID);
        }
    }//GEN-LAST:event_bThemActionPerformed

    private void bDongKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bDongKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bDong.doClick();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            bThem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            bThem.requestFocusInWindow();
        }
    }//GEN-LAST:event_bDongKeyPressed

    private void tfSoLuongKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfSoLuongKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTBDH.requestFocusInWindow();
            tfTBDH.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            cb.requestFocusInWindow();
        }
    }//GEN-LAST:event_tfSoLuongKeyPressed

    private void tfTBDHKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTBDHKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTBDm.requestFocusInWindow();
            tfTBDm.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfSoLuong.requestFocusInWindow();
            tfSoLuong.selectAll();
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
            bThem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTBDM.requestFocusInWindow();
            tfTBDM.selectAll();
        }
    }//GEN-LAST:event_tfTBDyKeyPressed

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

    private void tfSoLuongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfSoLuongMouseClicked
        tfSoLuong.selectAll();
    }//GEN-LAST:event_tfSoLuongMouseClicked

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
            tfSoLuong.requestFocusInWindow();
            tfSoLuong.selectAll();
        } 
    }//GEN-LAST:event_cbKeyPressed

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
            java.util.logging.Logger.getLogger(InsertKeDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InsertKeDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InsertKeDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InsertKeDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InsertKeDon("BS0001", "BN0001").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bDong;
    private javax.swing.JButton bThem;
    private javax.swing.JComboBox<String> cb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField tfSoLuong;
    private javax.swing.JTextField tfTBDH;
    private javax.swing.JTextField tfTBDM;
    private javax.swing.JTextField tfTBDd;
    private javax.swing.JTextField tfTBDm;
    private javax.swing.JTextField tfTBDy;
    // End of variables declaration//GEN-END:variables
}
