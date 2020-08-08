/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.endpoint.client.gui;

import com.github.kvac.phoenix.endpoint.client.gui.searchcs.SearchCS;
import com.github.kvac.phoenix.endpoint.client.network.NetWorkHeader;
import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.event.PhoenixEvent;
import com.github.kvac.phoenix.event.msg.MessageEvent;
import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.MySettings;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.github.kvac.phoenix.libs.objects.events.MyEvent;
import com.github.kvac.phoenix.libs.objects.events.MyEvent.TYPE;
import phoenixendpointclient.phoenix.endpoint.client.events.ClientEventHEADER;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.DefaultListModel;
import java.util.UUID;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jdcs_dev
 */
@Getter
@Setter
public class ClientGui extends javax.swing.JFrame implements WindowListener {

    protected static final Logger logger = LoggerFactory.getLogger(ClientGui.class);

    //my var
    ClientGui thisClient = this;
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

        register();
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
        jMenuItem1 = new javax.swing.JMenuItem();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jlist_Messages = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane_contact = new javax.swing.JScrollPane();
        jlist_Contact = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        TextField_message = new javax.swing.JTextField();
        jButton_send = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

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

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jlist_Messages.setModel(messageListModel);
        jlist_Messages.setCellRenderer(mlr);
        jScrollPane2.setViewportView(jlist_Messages);

        jSplitPane2.setRightComponent(jScrollPane2);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jlist_Contact.setModel(csListModel);
        jlist_Contact.setCellRenderer(cslr);
        jScrollPane_contact.setViewportView(jlist_Contact);

        jPanel3.add(jScrollPane_contact, java.awt.BorderLayout.CENTER);

        jButton1.setText("обновить");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_contact(evt);
            }
        });
        jPanel3.add(jButton1, java.awt.BorderLayout.PAGE_START);

        jSplitPane2.setTopComponent(jPanel3);

        getContentPane().add(jSplitPane2, java.awt.BorderLayout.CENTER);

        jPanel1.setAlignmentX(0.8F);
        jPanel1.setLayout(new java.awt.BorderLayout());

        TextField_message.setText("message");
        jPanel1.add(TextField_message, java.awt.BorderLayout.CENTER);

        jButton_send.setText("SEND");
        jButton_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                message_send(evt);
            }
        });
        jPanel1.add(jButton_send, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("контакты");

        jMenuItem2.setText("поиск");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        SearchCS searchCS = new SearchCS();
        searchCS.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void message_send(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_message_send
        Message message = new Message();
        message.setFrom(NetWorkHeader.getMycs());
        message.setTo(getJlist_Contact().getSelectedValue());
        message.setMessage(TextField_message.getText());
        message.setMessageID(UUID.randomUUID().toString());

        MessageEvent event = new MessageEvent();
        event.setObject(message);
        event.setType(PhoenixEvent.TYPE.MESSAGE_SAVE);
        EventHEADER.getMESSAGES_EVENT_BUS().post(event);
    }//GEN-LAST:event_message_send

    private void refresh_contact(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_contact
        this.csListModel.removeAllElements();
        this.jlist_Contact.repaint();
        this.jlist_Contact.updateUI();
    }//GEN-LAST:event_refresh_contact

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField TextField_message;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton_send;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane_contact;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JList<CS> jlist_Contact;
    public javax.swing.JList<Message> jlist_Messages;
    // End of variables declaration//GEN-END:variables

    @Subscribe
    private void update(MyEvent event) {
        if (MyEvent.isSettingsUpdate(event)) {
            MySettings settings = (MySettings) event.getObject();
            if (settings.getName() != null) {
                this.setTitle(settings.getName());
            }
        }
    }

    private void register() {
        EventHEADER.getBus_mysettings().register(thisClient);
        EventHEADER.getBus_cs_show().register(thisClient);
        ClientEventHEADER.getFORMESSAG_EVENT_BUS().register(thisClient);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("com.github.kvac.phoenix.endpoint.client.gui.ClientGui.windowClosing()");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosed(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Subscribe
    public void showCS(MyEvent event) {
        if (event.getType().equals(TYPE.CS_SHOW) && event.getObject() instanceof CS) {
            CS cs = (CS) event.getObject();

            if (!checkContainsCSInListModel(csListModel, cs)) {
                csListModel.addElement(cs);
            } else {
                for (int i = 0; i < csListModel.size(); i++) {
                    if (cs.getId().equals(csListModel.get(i).getId())) {
                        if (cs.getNameTime() > csListModel.get(i).getNameTime()) {
                            csListModel.set(i, cs);
                        }
                    }
                }
            }

        }
    }

    private boolean checkContainsCSInListModel(DefaultListModel<CS> listModel2, CS cs) {
        String id = cs.getId();
        for (int i = 0; i < listModel2.size(); i++) {
            if (listModel2.get(i).getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    //##############################_MESSAGES_##################################
    @Subscribe
    public void MESSAGE_SHOW(MessageEvent messageEvent) {
        PhoenixEvent.TYPE typem = messageEvent.getType();
        if (typem.equals(PhoenixEvent.TYPE.MESSAGE_CLEAR)) {
            if (messageEvent.getObject() instanceof Message) {

                Message message = (Message) messageEvent.getObject();
                String myid = NetWorkHeader.getMycs().getId().toLowerCase();
                String fromid = message.getFrom().getId().toLowerCase();
                String toid = message.getTo().getId().toLowerCase();

                if (myid.equals(fromid) || myid.equals(toid)) {
                    addMessageMY(message);
                } else {
                    System.out.println(System.currentTimeMillis() / 1000 + ":FOR OTHER:" + typem + " message:" + message);
                }
            }
        }
    }
    // Message
    @Getter
    @Setter
    private String selectedLAst = null;

    @Getter
    @Setter
    private String selectedCur = null;

    public void addMessageMY(Message message) {
        if (getJlist_Contact().getSelectedValuesList().size() == 1) {
            CS Selected = getJlist_Contact().getSelectedValue();
            // Сообщения для выделенного контакта
            if (message.getTo().getId().equals(Selected.getId()) || message.getFrom().getId().equals(Selected.getId())) {
                selectedCur = Selected.getId();
                // первое выделение
                if (selectedLAst == null) {
                    selectedLAst = selectedCur;
                    MessageShow(Selected, message);
                } // первое выделение
                // Смена контакта
                else if (!selectedLAst.equals(selectedCur)) {
                    messageListModel.removeAllElements();
                    MessageShow(Selected, message);
                    selectedLAst = selectedCur;
                } // Смена контакта
                // выбор не изменен
                else if (selectedLAst.equals(selectedCur)) {
                    MessageShow(Selected, message);
                    selectedLAst = selectedCur;
                }
                // выбор не изменен
            }
        }
    }

    public void MessageShow(CS selected, Message message) {
        if (message.getTo().getId().equals(selected.getId()) || message.getFrom().getId().equals(selected.getId())) {
            if (message.getTo().getId().equals(NetWorkHeader.getMycs().getId()) || message.getFrom().getId().equals(NetWorkHeader.getMycs().getId())) {
                if (!checkContainsMessageInListModel(messageListModel, message)) {
                    messageListModel.addElement(message);
                }
            }
        }
    }

    private boolean checkContainsMessageInListModel(DefaultListModel<Message> listMessageModel_For_Check,
            Message message) {
        for (int i = 0; i < listMessageModel_For_Check.size(); i++) {
            try {
                if (listMessageModel_For_Check.get(i).getMessageID().equals(message.getMessageID())) {
                    return true;
                }
            } catch (Exception e) {
                logger.error("checkContainsMessageInListModel:", e);
            }
        }
        return false;
    }
}
