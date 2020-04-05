package com.github.kvac.phoenix.endpoint.client.gui;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.kvac.phoenix.event.EventHEADER.EventHEADER;
import com.github.kvac.phoenix.libs.objects.Message;
import com.github.kvac.phoenix.libs.objects.cs.CS;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;
import lombok.Setter;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

public class ClientGui extends JFrame {
	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar;

	// root
	private JSplitPane splitPane;

	// contacts
	private JScrollPane scrollPane_contact;
	@Getter
	private DefaultListModel<CS> csListModel;
	@Getter
	private JList<CS> list_CS;
	@Getter
	@Setter
	private DefaultListModel<Message> messageListModel;
	@Getter
	private JPanel panel_message;
	private JScrollPane scrollPane;
	private JPanel panel_messages_btns;
	@Getter
	private JList<Message> list_messages;
	private JButton btn_message_send;
	@Getter
	private JTextField textField_message;

	public ClientGui() {
		EventHEADER.getBus_cs_clear().register(this);
		setBounds(new Rectangle(500, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);

		// Contacts
		scrollPane_contact = new JScrollPane();
		csListModel = new DefaultListModel<CS>();
		list_CS = new JList<CS>(csListModel);
		list_CS.setCellRenderer(new CSListRenderer(this));
		list_CS.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list_CS.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					final List<CS> selectedValuesList = list_CS.getSelectedValuesList();
					System.out.println(selectedValuesList);
				}
			}
		});

		scrollPane_contact.setViewportView(list_CS);
		splitPane.setLeftComponent(scrollPane_contact);

		// MESSAGES
		panel_message = new JPanel();
		splitPane.setRightComponent(panel_message);
		panel_message.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		panel_message.add(scrollPane);

		messageListModel = new DefaultListModel<Message>();
		list_messages = new JList<Message>(getMessageListModel());
		scrollPane.setViewportView(list_messages);

		panel_messages_btns = new JPanel();
		panel_message.add(panel_messages_btns, BorderLayout.SOUTH);
		panel_messages_btns.setLayout(new BorderLayout(0, 0));
		
		btn_message_send = new JButton("New button");
		panel_messages_btns.add(btn_message_send, BorderLayout.EAST);
		
		textField_message = new JTextField();
		panel_messages_btns.add(textField_message, BorderLayout.CENTER);
		textField_message.setColumns(10);
		// MESSAGES
	}

	@Subscribe
	private void showCS(CS cs) {
		if (!csListModel.contains(cs)) {
			csListModel.addElement(cs);
		} else {
			CS csFromList = csListModel.getElementAt(csListModel.indexOf(cs));
			if (csFromList.getID().equals(cs.getID())) {
				if (cs.getNameTime() > csFromList.getNameTime()) {
					csFromList.setName(cs.getName());
					csFromList.setNameTime(cs.getNameTime());
				}
			}
			list_CS.repaint();
		}
	}

}
