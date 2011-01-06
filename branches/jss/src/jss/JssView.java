/*
 * JssView.java
 */
package jss;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import com.mrj.dm.dao.*;
import com.mrj.operate.policy.DayAvgOperatePolicy;
import com.mrj.operate.policy.DayAvgOperatePolicy;
import com.mrj.operate.policy.LastDayFinalPricePolicy;
import com.mrj.person.CapitalSituation;
import com.mrj.person.Person;
import com.mrj.person.ShareHolding;
import com.mrj.policy.DayAavAnalysePolicy;
import com.mrj.policy.GeneralFormularyPolicy;
import com.mrj.server.LetSinglePersonToInvestTask;
import com.mrj.server.TaskDispatcher;
import com.mrj.util.StringUtil;
import com.mrj.util.chart.ChartUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import jss.simple.IsCanShowChart;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;

/**
 * The application's main frame.
 */
public class JssView extends FrameView {

    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JssView.class);

    public JssView(SingleFrameApplication app) {
        super(app);
        initComponents0();
        initComponents();

        initComponents2();
        ;


        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = JssApp.getApplication().getMainFrame();
            aboutBox = new JssAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        JssApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        mainChartPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel_hangqing = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel_myasset = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        robotTable = new javax.swing.JTable();
        dateFromJTextField = new javax.swing.JTextField();
        dateToJTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        robotInvestTestJlabel = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jTextField_initMoney = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jss.JssApp.class).getContext().getResourceMap(JssView.class);
        mainChartPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("mainChartPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), resourceMap.getColor("mainChartPanel.border.titleColor"))); // NOI18N
        mainChartPanel.setName("mainChartPanel"); // NOI18N

        javax.swing.GroupLayout mainChartPanelLayout = new javax.swing.GroupLayout(mainChartPanel);
        mainChartPanel.setLayout(mainChartPanelLayout);
        mainChartPanelLayout.setHorizontalGroup(
            mainChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 810, Short.MAX_VALUE)
        );
        mainChartPanelLayout.setVerticalGroup(
            mainChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );

        jTabbedPane1.setBackground(resourceMap.getColor("jTabbedPane_tabMenu.background")); // NOI18N
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jTabbedPane_tabMenu.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("宋体", 0, 12), resourceMap.getColor("jTabbedPane_tabMenu.border.titleColor"))); // NOI18N
        jTabbedPane1.setForeground(resourceMap.getColor("jTabbedPane_tabMenu.foreground")); // NOI18N
        jTabbedPane1.setName("jTabbedPane_tabMenu"); // NOI18N
        jTabbedPane1.setOpaque(true);

        jPanel_hangqing.setName("jPanel_hangqing"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "股票代码", "股票名称", "收盘价", "成交量"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable1.columnModel.title0")); // NOI18N
        jTable1.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable1.columnModel.title1")); // NOI18N
        jTable1.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable1.columnModel.title2")); // NOI18N
        jTable1.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTable1.columnModel.title3")); // NOI18N

        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N

        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel_hangqingLayout = new javax.swing.GroupLayout(jPanel_hangqing);
        jPanel_hangqing.setLayout(jPanel_hangqingLayout);
        jPanel_hangqingLayout.setHorizontalGroup(
            jPanel_hangqingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_hangqingLayout.createSequentialGroup()
                .addGroup(jPanel_hangqingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_hangqingLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_hangqingLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)))
                .addContainerGap(201, Short.MAX_VALUE))
        );
        jPanel_hangqingLayout.setVerticalGroup(
            jPanel_hangqingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_hangqingLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel_hangqingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(246, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel_hangqing.TabConstraints.tabTitle"), jPanel_hangqing); // NOI18N

        jPanel_myasset.setName("jPanel_myasset"); // NOI18N
        jPanel_myasset.setRequestFocusEnabled(false);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "股票代码", "股票名称", "持股数量", "成本价", "浮动盈亏", "盈亏比率", "最新市值", "当前价"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable2.setName("jTable2"); // NOI18N
        jScrollPane2.setViewportView(jTable2);
        jTable2.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable2.columnModel.title0")); // NOI18N
        jTable2.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable2.columnModel.title1")); // NOI18N
        jTable2.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable2.columnModel.title2")); // NOI18N
        jTable2.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTable2.columnModel.title3")); // NOI18N
        jTable2.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jTable2.columnModel.title4")); // NOI18N
        jTable2.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("jTable2.columnModel.title5")); // NOI18N
        jTable2.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("jTable2.columnModel.title6")); // NOI18N
        jTable2.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("jTable2.columnModel.title7")); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jButton1.setIcon(resourceMap.getIcon("jButton1.icon")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton2.setIcon(resourceMap.getIcon("jButton2.icon")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout jPanel_myassetLayout = new javax.swing.GroupLayout(jPanel_myasset);
        jPanel_myasset.setLayout(jPanel_myassetLayout);
        jPanel_myassetLayout.setHorizontalGroup(
            jPanel_myassetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_myassetLayout.createSequentialGroup()
                .addGroup(jPanel_myassetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_myassetLayout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(jLabel2))
                    .addGroup(jPanel_myassetLayout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addGroup(jPanel_myassetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_myassetLayout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(106, 106, 106)
                                .addComponent(jButton1))
                            .addGroup(jPanel_myassetLayout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(jLabel3))))
                    .addGroup(jPanel_myassetLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel_myassetLayout.setVerticalGroup(
            jPanel_myassetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_myassetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(jPanel_myassetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_myassetLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(43, 43, 43)
                        .addComponent(jButton2)))
                .addGap(119, 119, 119))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel_myasset.TabConstraints.tabTitle"), jPanel_myasset); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        robotTable.setAutoCreateRowSorter(true);
        robotTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"a", null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "机器人名称", "算法描述", "排名", "盈利情况"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        robotTable.setName("robotTable"); // NOI18N
        robotTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                robotTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(robotTable);
        robotTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("robotTable.columnModel.title0")); // NOI18N
        robotTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("robotTable.columnModel.title1")); // NOI18N
        robotTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("robotTable.columnModel.title2")); // NOI18N
        robotTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("robotTable.columnModel.title3")); // NOI18N

        dateFromJTextField.setText(resourceMap.getString("dateFromJTextField.text")); // NOI18N
        dateFromJTextField.setName("dateFromJTextField"); // NOI18N

        dateToJTextField.setText(resourceMap.getString("dateToJTextField.text")); // NOI18N
        dateToJTextField.setName("dateToJTextField"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextField4.setText(resourceMap.getString("jTextField4.text")); // NOI18N
        jTextField4.setName("jTextField4"); // NOI18N

        jButton4.setIcon(resourceMap.getIcon("jButton4.icon")); // NOI18N
        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setName("jButton4"); // NOI18N
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton5.setIcon(resourceMap.getIcon("jButton5.icon")); // NOI18N
        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setName("jButton5"); // NOI18N
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        robotInvestTestJlabel.setText(resourceMap.getString("robotInvestTestJlabel.text")); // NOI18N
        robotInvestTestJlabel.setName("robotInvestTestJlabel"); // NOI18N
        robotInvestTestJlabel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                robotInvestTestJlabelPropertyChange(evt);
            }
        });

        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jTextField_initMoney.setText(resourceMap.getString("jTextField_initMoney.text")); // NOI18N
        jTextField_initMoney.setName("jTextField_initMoney"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setName("jComboBox1"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(jButton4)
                .addGap(106, 106, 106)
                .addComponent(jButton5)
                .addContainerGap(154, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(robotInvestTestJlabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(181, 181, 181))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton6))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(dateFromJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(23, 23, 23)
                                    .addComponent(jLabel5)
                                    .addGap(18, 18, 18)
                                    .addComponent(dateToJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addGap(33, 33, 33)
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField_initMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel8)))
                            .addGap(28, 28, 28)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(56, 56, 56)
                            .addComponent(jButton3))))
                .addContainerGap(48, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel6)
                .addContainerGap(358, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateToJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(dateFromJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jButton3)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6)
                    .addComponent(jLabel7)
                    .addComponent(jTextField_initMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(robotInvestTestJlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(73, 73, 73))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jTable3.setAutoCreateRowSorter(true);
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable3.setName("jTable3"); // NOI18N
        jScrollPane5.setViewportView(jTable3);
        jTable3.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable3.columnModel.title0")); // NOI18N
        jTable3.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable3.columnModel.title1")); // NOI18N
        jTable3.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable3.columnModel.title2")); // NOI18N
        jTable3.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTable3.columnModel.title3")); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE))
                    .addComponent(mainChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mainChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName(resourceMap.getString("jTabbedPane_tabMenu.AccessibleContext.accessibleName")); // NOI18N

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1392, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1222, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void robotTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_robotTableMouseClicked
        if (this.isCanShowChart.getIsCanShowChart() == 1) {//show chart
            int[] rowId = this.robotTable.getSelectedRows();
            String[] userUuidArray = new String[rowId.length];
            PersonDao pdao = new PersonDao();
            for (int i = 0; i < rowId.length; i++) {
                userUuidArray[i] = pdao.getPersonByUserId((String) this.robotTable.getModel().getValueAt(rowId[i], 0)).getUserUuid();//根据userid取person
            }
            JFreeChart jchart = ChartUtil.getAssetChart_autoAXIS(userUuidArray);
            if (chartPanel == null) {
                initChartPanel(jchart);
            } else {
                chartPanel.setChart(jchart);
            }
        } else {
            //System.out.println("table row:" + this.robotTable.getSelectedRow());
        }




    }//GEN-LAST:event_robotTableMouseClicked
    private List<Person> plist = new ArrayList<Person>();
    private Map<String, Person> pMap = new HashMap<String, Person>();

    private void investCircleTest(final String fromMMDDYYYY, final String toMMDDYYYY, final String mode) {
        Thread t = new Thread() {

            @Override
            public void run() {
                Object callbackNotifyObject = new Object();
                List<String[]> dateList = new ArrayList<String[]>();
                dateList = StringUtil.parseDateRangeToSmallRange(new String[]{fromMMDDYYYY, toMMDDYYYY}, mode);
                Float[][] resultArray = new Float[plist.size()][dateList.size()];

                jTable3.setName("测试结果");
                String[] pcolumn = new String[dateList.size() + 2];
                pcolumn[0] = "姓名";
                for (int i = 1; i < pcolumn.length - 1; i++) {
                    pcolumn[i] = "第" + i + "次投资";
                }
                pcolumn[pcolumn.length - 1] = "平均结果";

                jTable3.setModel(new DefaultTableModel(pcolumn, plist.size()));
                //jTable3.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                for (int i = 0; i < plist.size(); i++) {
                    jTable3.getModel().setValueAt(plist.get(i).getUserId(), i, 0);
                }

                for (int i = 0; i < dateList.size(); i++) {
                    for (Person p : plist) {
                        p.clearCurrentInvestResult();
                        Float beginAsset = Float.parseFloat(jTextField_initMoney.getText());
                        p.setCs(new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset)));
                    }
                    logger.info("开始测试：" + dateList.get(i)[0] + "-" + dateList.get(i)[1]);
                    letPersonListInvest_multyThread(plist, dateList.get(i)[0], dateList.get(i)[1], dateList.get(i)[0] + "-" + dateList.get(i)[1], callbackNotifyObject);
                    try {
                        synchronized (callbackNotifyObject) {
                            callbackNotifyObject.wait();
                        }
                        jButton3.setEnabled(true);
                        for (int j = 0; j < plist.size(); j++) {
                            jTable3.getModel().setValueAt(StringUtil.formatFloat(plist.get(j).getCurrentInvestRate()), j, i+1);
                            resultArray[j][i] = plist.get(j).getCurrentInvestRate();
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JssView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                for (int j = 0; j < plist.size(); j++) {
                    float temp = 0;
                    for (int k = 0; k < dateList.size(); k++) {
                        temp += resultArray[j][k];
                    }
                    temp = temp / (float) dateList.size();
                    jTable3.getModel().setValueAt(StringUtil.fourBlanks() + StringUtil.formatFloat(temp), j,pcolumn.length-1);
                }
            }
        };

        t.start();

    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //initComponents0();
        //this.initComponents2();
        String otype = (String) this.jComboBox1.getSelectedItem();
        String fromMMDDYYYY = this.dateFromJTextField.getText();
        String toMMDDYYYY = this.dateToJTextField.getText();
         this.jButton3.setEnabled(false);
            this.robotInvestTestJlabel.setText("正在开始测试!");
        if (otype.equals("整段时间比赛")) {           
            letPersonListInvest_multyThread(plist, fromMMDDYYYY, toMMDDYYYY, fromMMDDYYYY + "-" + toMMDDYYYY, null);
        } else if (otype.equals("按月比赛")) {
            investCircleTest(fromMMDDYYYY, toMMDDYYYY, "月");
        } else if (otype.equals("按周比赛")) {
            investCircleTest(fromMMDDYYYY, toMMDDYYYY, "周");
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String searchCondition = this.jTextField4.getText();
        initRobotTable(searchCondition);
        this.isCanShowChart.setIsCanShowChart(0);

    }//GEN-LAST:event_jButton6ActionPerformed

    private void robotInvestTestJlabelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_robotInvestTestJlabelPropertyChange
        if (this.robotInvestTestJlabel.getText().equals("全部测试完成!")) {
            this.jButton3.setEnabled(true);
            List<Person> plist = this.isCanShowChart.getPersonList();
            if (plist != null) {
                for (Person p : plist) {
                    setPersonRateIntoTable(p);
                }
            }
        } else {
            //System.out.println("事件已经被监听!");
        }
    }//GEN-LAST:event_robotInvestTestJlabelPropertyChange

    private void setPersonRateIntoTable(Person p) {
        TableModel tm = this.robotTable.getModel();
        int rowNum = tm.getRowCount();
        for (int i = 0; i < rowNum; i++) {
            if (tm.getValueAt(i, 0).equals(p.getUserId())) {
                tm.setValueAt(p.getCurrentInvestRate(), i, 3);//设置赢利值
                return;
            }
        }
    }

    public void letPersonListInvest_multyThread(List<Person> plist, String fromMMDDYYYY, String toMMDDYYYY, String batchTimeSeries, Object callbackNotifyObject) {
        TaskDispatcher tp = TaskDispatcher.getInstance();
        List<Person> personList = new ArrayList<Person>();
        FinishCallback sccb = new FinishCallback(plist.size(), personList, this.robotInvestTestJlabel, isCanShowChart);
        sccb.setCallbackNotifyObject(callbackNotifyObject);
        new Thread(TaskDispatcher.getInstance()).start();
        for (Person p : plist) {
            p.setCurrentInvestResultUuid(batchTimeSeries + "");
            tp.addTask(new LetSinglePersonToInvestTask(fromMMDDYYYY, toMMDDYYYY, p, sccb));
        }
    }
    IsCanShowChart isCanShowChart = new IsCanShowChart(0);
    int selectedIndexlast = 0;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField dateFromJTextField;
    private javax.swing.JTextField dateToJTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel_hangqing;
    private javax.swing.JPanel jPanel_myasset;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField_initMoney;
    private javax.swing.JPanel mainChartPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel robotInvestTestJlabel;
    private javax.swing.JTable robotTable;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;

    private void initComponents2() {
        initPersonIntoDb();
        initRobotTable();
        //initChartPanel();
        initOperationType();
        initJTable3();
    }

    private void initOperationType() {
        this.jComboBox1.removeAllItems();
        this.jComboBox1.addItem("按月比赛");
        this.jComboBox1.addItem("按周比赛");
        this.jComboBox1.addItem("整段时间比赛");
    }

    //初始化机器人表格；从数据库里读书现有机器人，放置在表格中
    private void initRobotTable() {
        ArrayList<Person> pl = new PersonDao().getPersonList();
        int row = 0;
        int column = 0;
        int size = pl.size();

        DefaultTableModel dtm = new javax.swing.table.DefaultTableModel(
                new Object[size][4],
                new String[]{
                    "机器人名称", "算法描述", "排名", "盈利情况"
                });
        this.robotTable.setModel(dtm);
        for (Person p : pl) {
            this.robotTable.getModel().setValueAt(p.getUserId(), row, column++);
            this.robotTable.getModel().setValueAt(p.getChoosePolicyName() + ":" + p.getChoosePolicyArgs() + "," + p.getOperatePolicyName() + ":" + p.getOperatePolicyArgs(), row, column++);
            row++;
            column = 0;
        }

    }

    //初始化机器人表格；从数据库里读书现有机器人，放置在表格中
    private void initRobotTable(String searchStr) {
        ArrayList<Person> pl = new ArrayList<Person>();
        pl.add(new PersonDao().getPersonByUserId(searchStr));
        int row = 0;
        int column = 0;
        int size = pl.size();

        DefaultTableModel dtm = new javax.swing.table.DefaultTableModel(
                new Object[size][4],
                new String[]{
                    "机器人名称", "算法描述", "排名", "盈利情况"
                });
        this.robotTable.setModel(dtm);
        for (Person p : pl) {
            this.robotTable.getModel().setValueAt(p.getUserId(), row, column++);
            this.robotTable.getModel().setValueAt(p.getChoosePolicyName() + ":" + p.getChoosePolicyArgs() + "," + p.getOperatePolicyName() + ":" + p.getOperatePolicyArgs(), row, column++);
            row++;
            column = 0;
        }

    }
    private boolean isInitPersonIntoDb=false;
    private void initPersonIntoDb() {
        if(isInitPersonIntoDb)return;
        isInitPersonIntoDb=true;
        ArrayList<Person> plist = new ArrayList<Person>();
        Float beginAsset = Float.parseFloat(this.jTextField_initMoney.getText());
        PersonDao pdao = new PersonDao();

        int i = 1;
        /*plist.add(new Person("p" + i++, new DayAavAnalysePolicy(180, 180, true, 2, 1), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
        plist.add(new Person("p" + i++, new DayAavAnalysePolicy(60, 60, true, 2, 1), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
        plist.add(new Person("p" + i++, new DayAavAnalysePolicy(30, 30, true, 2, 1), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
        */i=4;
        plist.add(new Person("p" + i++, new GeneralFormularyPolicy("chargeDescription_size:1;avg:5,60,up;vol:5,10,5,放量;sell:up,20,down,20"), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
        plist.add(new Person("p" + i++, new GeneralFormularyPolicy("chargeDescription_size:3;avg:5,60,up;vol:5,10,5,放量;sell:up,20,down,20"), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
        plist.add(new Person("p" + i++, new GeneralFormularyPolicy("chargeDescription_size:5;avg:5,60,up;vol:5,10,5,放量;sell:up,20,down,20"), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));

        plist.add(new Person("p" + i++, new GeneralFormularyPolicy("chargeDescription_size:1;avg:5,60,up;vol:5,10,5,放量;sell:up,25,down,10"), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
                plist.add(new Person("p" + i++, new GeneralFormularyPolicy("chargeDescription_size:3;avg:5,60,up;vol:5,10,5,放量;sell:up,15,down,10"), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
                plist.add(new Person("p" + i++, new GeneralFormularyPolicy("chargeDescription_size:5;avg:5,60,up;vol:5,10,5,放量;sell:up,10,down,10"), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));


       /* plist.add(new Person("p" + i++, new GeneralFormularyPolicy("chargeDescription_size:5;avg:5,60,up,longTerm,60;vol:5,10,5,放量;sell:up,20,down,10"), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
        plist.add(new Person("p" + i++, new GeneralFormularyPolicy("chargeDescription_size:5;avg:5,60,up,longTerm,40;vol:5,10,5,放量;sell:up,15,down,5"), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
        plist.add(new Person("p" + i++, new GeneralFormularyPolicy("chargeDescription_size:5;avg:5,60,up,longTerm,30;vol:5,10,5,放量;sell:up,15,down,20"), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
*/
        for (Person p : plist) {
            pdao.addIfNotExist(p);
        }


        //   PersonDao pdao = new PersonDao();
        int size = this.robotTable.getModel().getRowCount();
        for (int j = 0; j < size; j++) {
            String name = (String) this.robotTable.getModel().getValueAt(j, 0);
//            Person p = pdao.getPersonByUserId(name);
//            p = Person.getInstance(p.getUserUuid(), p.getUserId(), p.getChoosePolicyName(), p.getChoosePolicyArgs(), p.getOperatePolicyName(), p.getOperatePolicyArgs(),
//                    new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(Float.parseFloat(this.jTextField_initMoney.getText()))));
//            plist.add(p);
        }
        for (Person p : plist) {
            pMap.put(p.getUserId(), p);
        }
        this.plist = plist;

    }
    ChartPanel chartPanel = null;

    private void initChartPanel(JFreeChart chart) {
        logger.info("test");
        chartPanel = new ChartPanel(chart);
        chartPanel.setName("chartPanel"); // NOI18N
        javax.swing.GroupLayout jPanelTestLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(jPanelTestLayout);
        jPanelTestLayout.setHorizontalGroup(
                jPanelTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 822, Short.MAX_VALUE));
        jPanelTestLayout.setVerticalGroup(
                jPanelTestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 346, Short.MAX_VALUE));

        javax.swing.GroupLayout mainChartPanelLayout = new javax.swing.GroupLayout(mainChartPanel);
        mainChartPanel.setLayout(mainChartPanelLayout);
        mainChartPanelLayout.setHorizontalGroup(
                mainChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(chartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        mainChartPanelLayout.setVerticalGroup(
                mainChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(chartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    }

    private void initComponents0() {
        Dao dao = new Dao();
        try {
            dao.clearData();
        } catch (SQLException ex) {
            Logger.getLogger(JssView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void initJTable3() {
        jTable3.setModel(new DefaultTableModel());
    }
}
