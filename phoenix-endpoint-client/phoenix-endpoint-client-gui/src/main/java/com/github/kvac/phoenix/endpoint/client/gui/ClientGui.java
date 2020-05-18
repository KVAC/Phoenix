/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.endpoint.client.gui;

import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import javax.swing.DefaultListModel;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jdcs_dev
 */
@Getter
@Setter
public class ClientGui extends javax.swing.JFrame {

    //my var
    DefaultListModel<CS> csListModel = new DefaultListModel<>();
    DefaultListModel<Message> messageListModel = new DefaultListModel<>();
    CSListRenderer cslr;
    MessageListRenderer mlr;

    //my var
    /**
     * Creates new form ClientGui
     */
    public ClientGui() {
        this.mlr = new MessageListRenderer(this);
        this.cslr = new CSListRenderer(this);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlist_Contact = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jlist_Messages = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        TextField_message = new javax.swing.JTextField();
        jButton_send = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jlist_Contact.setModel(csListModel);
        jlist_Contact.setCellRenderer(cslr);
        jScrollPane1.setViewportView(jlist_Contact);

        jSplitPane2.setLeftComponent(jScrollPane1);

        jlist_Messages.setModel(messageListModel);
        jlist_Messages.setCellRenderer(mlr);
        jScrollPane2.setViewportView(jlist_Messages);

        jSplitPane2.setRightComponent(jScrollPane2);

        getContentPane().add(jSplitPane2, java.awt.BorderLayout.CENTER);

        jPanel1.setAlignmentX(0.8F);
        jPanel1.setLayout(new java.awt.BorderLayout());

        TextField_message.setText("message");
        jPanel1.add(TextField_message, java.awt.BorderLayout.CENTER);

        jButton_send.setText("SEND");
        jPanel1.add(jButton_send, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TextField_message;
    private javax.swing.JButton jButton_send;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JList<CS> jlist_Contact;
    public javax.swing.JList<Message> jlist_Messages;
    // End of variables declaration//GEN-END:variables
}
