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
import javax.swing.JOptionPane;

/**
 *
 * @author trieu_000
 */
public class InsertTheoDoi extends javax.swing.JFrame {

    /**
     * Creates new form InsertTheoDoi
     */
    private static String IDBN;
    private static String IDBS;
    private static Connection con;
    private static PreparedStatement stmt;
    private static ResultSet result;
    DateFormat timeType = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();

    public InsertTheoDoi() {

    }

    public InsertTheoDoi(String IDBS, String IDBN) {
        this.IDBN = IDBN;
        this.IDBS = IDBS;
        initComponents();
        con = new Connect().connect();
        new SetCursor().setCusor(this);
        this.setLocationRelativeTo(null);//set frame chinh giua man hinh
        loadTen();
        setTime();
        setVisible(true);
        tfTBDH.requestFocusInWindow();
        tfTBDH.selectAll();
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
                        tfTKTH.setText(new Integer(date.getHours()).toString());
                        tfTKTM.setText(new Integer(date.getMonth() + 1).toString());
                        tfTKTd.setText(new Integer(date.getDate()).toString());
                        tfTKTm.setText(new Integer(date.getMinutes()).toString());
                        tfTKTy.setText(new Integer(date.getYear() + 1900).toString());
                        Thread.sleep(60000);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(BenhNhan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        timeSet.start();
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

    Date getTimeKT() {

        String StrTime = new String(tfTKTy.getText() + "-"
                + tfTKTM.getText() + "-"
                + tfTKTd.getText() + " "
                + tfTKTH.getText() + ":"
                + tfTKTm.getText() + ":00");
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

    boolean compareTime(Date timeBD, Date timeKT) {
        if (timeBD.compareTo(timeKT) >= 0) {
            return true;
        }
        return false;
    }

    public void insert(String relation) {//can IDBN, IDBS, IDBenh
//        if (checkIDValid(IDKhoa, "Khoa")) {

        String sql = new String("INSERT INTO \"" + relation + "\" VALUES (?, ?, ?, ?, ?, ?)");
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, IDBN);
            stmt.setString(2, IDBS);
            stmt.setString(3, tpKetQua.getText());
            try {
                stmt.setInt(4, Integer.parseInt(tfPhi.getText()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Trường \"Phí\" không hợp lệ");
            }
            Date timeBD = getTimeBD();
            Date timeKT = getTimeKT();
            if (compareTime(timeBD, timeKT)) {
                JOptionPane.showMessageDialog(null, "Thời gian bắt đầu phải trước thời gian kết thúc");
                return;
            }
            String StrTimeBD = timeType.format(timeBD);
            String StrTimeKT = timeType.format(timeKT);
            stmt.setString(5, StrTimeBD);
            stmt.setString(6, StrTimeKT);
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfPhi = new javax.swing.JTextField();
        bThem = new javax.swing.JButton();
        tfTBDH = new javax.swing.JTextField();
        tfTBDm = new javax.swing.JTextField();
        tfTBDd = new javax.swing.JTextField();
        tfTBDM = new javax.swing.JTextField();
        tfTBDy = new javax.swing.JTextField();
        tfTKTH = new javax.swing.JTextField();
        tfTKTm = new javax.swing.JTextField();
        tfTKTd = new javax.swing.JTextField();
        tfTKTM = new javax.swing.JTextField();
        tfTKTy = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tpKetQua = new javax.swing.JTextPane();
        jSeparator4 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Thời điểm bắt đầu:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Thời điểm kết thúc:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Kết quả:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Phí:");

        tfPhi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfPhi.setForeground(new java.awt.Color(153, 153, 153));
        tfPhi.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tfPhi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfPhiMouseClicked(evt);
            }
        });
        tfPhi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfPhiKeyPressed(evt);
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
        tfTBDM.setToolTipText("");
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

        tfTKTH.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTKTH.setForeground(new java.awt.Color(153, 153, 153));
        tfTKTH.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTKTH.setText("HH");
        tfTKTH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTKTHMouseClicked(evt);
            }
        });
        tfTKTH.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTKTHKeyPressed(evt);
            }
        });

        tfTKTm.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTKTm.setForeground(new java.awt.Color(153, 153, 153));
        tfTKTm.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTKTm.setText("mm");
        tfTKTm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTKTmMouseClicked(evt);
            }
        });
        tfTKTm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTKTmKeyPressed(evt);
            }
        });

        tfTKTd.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTKTd.setForeground(new java.awt.Color(153, 153, 153));
        tfTKTd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTKTd.setText("dd");
        tfTKTd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTKTdMouseClicked(evt);
            }
        });
        tfTKTd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTKTdKeyPressed(evt);
            }
        });

        tfTKTM.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTKTM.setForeground(new java.awt.Color(153, 153, 153));
        tfTKTM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTKTM.setText("MM");
        tfTKTM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTKTMMouseClicked(evt);
            }
        });
        tfTKTM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTKTMKeyPressed(evt);
            }
        });

        tfTKTy.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tfTKTy.setForeground(new java.awt.Color(153, 153, 153));
        tfTKTy.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfTKTy.setText("yyyy");
        tfTKTy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTKTyMouseClicked(evt);
            }
        });
        tfTKTy.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTKTyKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("H");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("M");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("/");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("/");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("H");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("M");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("/");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("/");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        tpKetQua.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tpKetQua.setForeground(new java.awt.Color(153, 153, 153));
        tpKetQua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tpKetQuaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tpKetQua);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 937, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfTBDH, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfTBDm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfTBDd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfTBDM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfTBDy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tfTKTH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfTKTm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(5, 5, 5)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfTKTd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfTKTM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tfTKTy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bDong, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bThem))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tfTBDH, tfTBDM, tfTBDd, tfTBDm, tfTBDy, tfTKTH, tfTKTM, tfTKTd, tfTKTm, tfTKTy});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bDong, bThem});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bDong))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(tfTBDH, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6)
                                            .addComponent(tfTBDm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(tfTBDy, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfTBDM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfTBDd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel9))
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(27, 27, 27)
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(tfTKTH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfTKTm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfTKTd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfTKTM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tfTKTy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel12)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel11))
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jScrollPane1)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(5, 5, 5)
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tfPhi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bThem)))
                        .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel4, jLabel5});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bThem, jLabel10, jLabel11, jLabel12, jLabel13, jLabel6, jLabel7, jLabel8, jLabel9, jSeparator1, jSeparator2, tfPhi, tfTBDH, tfTBDM, tfTBDd, tfTBDm, tfTKTH, tfTKTM, tfTKTd, tfTKTm, tfTKTy});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bDong, jLabel1});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDongActionPerformed
                this.dispose();
//        System.exit(0);
    }//GEN-LAST:event_bDongActionPerformed

    private void tfTBDHKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTBDHKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTBDm.requestFocusInWindow();
            tfTBDm.selectAll();
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
            tfTKTH.requestFocusInWindow();
            tfTKTH.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTBDM.requestFocusInWindow();
            tfTBDM.selectAll();
        }
    }//GEN-LAST:event_tfTBDyKeyPressed

    private void tfTKTHKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTKTHKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTKTm.requestFocusInWindow();
            tfTKTm.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTBDy.requestFocusInWindow();
            tfTBDy.selectAll();
        }
    }//GEN-LAST:event_tfTKTHKeyPressed

    private void tfTKTmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTKTmKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTKTd.requestFocusInWindow();
            tfTKTd.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTKTH.requestFocusInWindow();
            tfTKTH.selectAll();
        }
    }//GEN-LAST:event_tfTKTmKeyPressed

    private void tfTKTdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTKTdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTKTM.requestFocusInWindow();
            tfTKTM.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTKTm.requestFocusInWindow();
            tfTKTm.selectAll();
        }
    }//GEN-LAST:event_tfTKTdKeyPressed

    private void tfTKTMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTKTMKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfTKTy.requestFocusInWindow();
            tfTKTy.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTKTd.requestFocusInWindow();
            tfTKTd.selectAll();
        }
    }//GEN-LAST:event_tfTKTMKeyPressed

    private void tfTKTyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTKTyKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tpKetQua.requestFocusInWindow();
            tpKetQua.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTKTM.requestFocusInWindow();
            tfTKTM.selectAll();
        }
    }//GEN-LAST:event_tfTKTyKeyPressed

    private void tpKetQuaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tpKetQuaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            tfPhi.requestFocusInWindow();
            tfPhi.selectAll();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfTKTy.requestFocusInWindow();
            tfTKTy.selectAll();
        }
    }//GEN-LAST:event_tpKetQuaKeyPressed

    private void tfPhiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPhiKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            bThem.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tpKetQua.requestFocusInWindow();
            tpKetQua.selectAll();
        }
    }//GEN-LAST:event_tfPhiKeyPressed

    private void bThemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bThemKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bThem.doClick();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            bDong.requestFocusInWindow();
        } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            tfPhi.requestFocusInWindow();
            tfPhi.selectAll();
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

    private void tfTKTHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTKTHMouseClicked
        tfTKTH.selectAll();
    }//GEN-LAST:event_tfTKTHMouseClicked

    private void tfTKTmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTKTmMouseClicked
        tfTKTm.selectAll();
    }//GEN-LAST:event_tfTKTmMouseClicked

    private void tfTKTdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTKTdMouseClicked
        tfTKTd.selectAll();
    }//GEN-LAST:event_tfTKTdMouseClicked

    private void tfTKTMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTKTMMouseClicked
        tfTKTM.selectAll();
    }//GEN-LAST:event_tfTKTMMouseClicked

    private void tfTKTyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTKTyMouseClicked
        tfTKTy.selectAll();
    }//GEN-LAST:event_tfTKTyMouseClicked

    private void tfPhiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfPhiMouseClicked
        tfPhi.selectAll();
    }//GEN-LAST:event_tfPhiMouseClicked

    private void bThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bThemActionPerformed
        insert("TheoDoi");
    }//GEN-LAST:event_bThemActionPerformed

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
            java.util.logging.Logger.getLogger(InsertTheoDoi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InsertTheoDoi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InsertTheoDoi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InsertTheoDoi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InsertTheoDoi("BS0001", "BN0001").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bDong;
    private javax.swing.JButton bThem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField tfPhi;
    private javax.swing.JTextField tfTBDH;
    private javax.swing.JTextField tfTBDM;
    private javax.swing.JTextField tfTBDd;
    private javax.swing.JTextField tfTBDm;
    private javax.swing.JTextField tfTBDy;
    private javax.swing.JTextField tfTKTH;
    private javax.swing.JTextField tfTKTM;
    private javax.swing.JTextField tfTKTd;
    private javax.swing.JTextField tfTKTm;
    private javax.swing.JTextField tfTKTy;
    private javax.swing.JTextPane tpKetQua;
    // End of variables declaration//GEN-END:variables
}
