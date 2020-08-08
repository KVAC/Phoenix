/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.endpoint.client.gui.searchcs;

import com.github.kvac.phoenix.endpoint.client.gui.GuiHEADER;
import com.github.kvac.phoenix.endpoint.client.network.NetWorkHeader;
import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.github.kvac.phoenix.libs.objects.events.ra.request.RSearchCS;
import com.google.common.eventbus.Subscribe;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phoenixendpointclient.phoenix.endpoint.client.events.ClientEventHEADER;

/**
 *
 * @author jdcs_dev
 */
public class SearchCS extends javax.swing.JFrame implements WindowListener {

    private static final long serialVersionUID = 240513231950251807L;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    SearchCS thisThis = this;

    /**
     * Creates new form SearchCS
     */
    public SearchCS() {
        this.addWindowListener(thisThis);
        initComponents();
    }
    private final CopyOnWriteArrayList<CS> selected = new CopyOnWriteArrayList<CS>();

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JTextField_whatSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton_add_finded = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 400, 400));

        JTextField_whatSearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        JTextField_whatSearch.setText("Введите для поиска");
        JTextField_whatSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTextField_whatSearchActionPerformed(evt);
            }
        });
        JTextField_whatSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textEditedEvent(evt);
            }
        });

        jList1.setModel(GuiHEADER.getListModel());
        jList1.setCellRenderer(new SearchListRenderer(thisThis));
        jScrollPane1.setViewportView(jList1);

        jButton_add_finded.setText("Добавить веделенные");
        jButton_add_finded.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewCS(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(JTextField_whatSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_add_finded)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JTextField_whatSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_add_finded))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textEditedEvent(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textEditedEvent
        GuiHEADER.getListModel().clear();
        if (evt.getSource() instanceof javax.swing.JTextField) {
            javax.swing.JTextField field = (javax.swing.JTextField) evt.getSource();
            String text = field.getText();
            if (text.isEmpty()) {
                return;
            }
            MyEvent event = new MyEvent();

            RSearchCS rscs = new RSearchCS();
            rscs.setRequestData(text);
            rscs.setWho(NetWorkHeader.getMycs());

            event.setObject(rscs);
            event.setType(MyEvent.TYPE.CS_SEARCHR);

            EventHEADER.getSERVERS_REQUEST_BUS().post(event);
        } else {
            logger.warn("Unexpected source");
        }
    }//GEN-LAST:event_textEditedEvent

    private void JTextField_whatSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JTextField_whatSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JTextField_whatSearchActionPerformed

    private void addNewCS(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewCS
        List<CS> selectedd = jList1.getSelectedValuesList();
        selectedd.forEach((cs1) -> {
            cs1.save();
        });
    }//GEN-LAST:event_addNewCS

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField JTextField_whatSearch;
    private javax.swing.JButton jButton_add_finded;
    private static javax.swing.JList<CS> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void windowOpened(WindowEvent e) {
        //открыто
        register();
    }

    @Override
    public void windowActivated(WindowEvent e) {
        //фокус
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //скрыто
    }

    @Override
    public void windowClosing(WindowEvent e) {
        //закрытие
    }

    @Override
    public void windowClosed(WindowEvent e) {
        //закрыто
        unregister();
    }

    @Override
    public void windowIconified(WindowEvent e) {
        System.out.println(4);
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        System.out.println(5);
    }

    private void unregister() {
        ClientEventHEADER.getFORSEARCHANSWER_EVENT_BUS().unregister(this);
    }

    private void register() {
        ClientEventHEADER.getFORSEARCHANSWER_EVENT_BUS().register(this);
    }

    @Subscribe
    public void csListener(ArrayList<CS> list) {
        list.stream().filter((cs) -> (!GuiHEADER.getListModel().contains(cs))).filter((cs) -> (cs.getName().toLowerCase().contains(JTextField_whatSearch.getText().toLowerCase()))).forEachOrdered((cs) -> {
            GuiHEADER.getListModel().addElement(cs);
        });
    }
}
