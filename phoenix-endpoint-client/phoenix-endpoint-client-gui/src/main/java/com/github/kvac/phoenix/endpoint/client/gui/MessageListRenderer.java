package com.github.kvac.phoenix.endpoint.client.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.github.kvac.phoenix.libs.objects.Message;
import lombok.Getter;
import lombok.Setter;

public class MessageListRenderer extends JLabel implements ListCellRenderer<Message> {

    @Getter
    @Setter
    ClientGui parent;
    private static final long serialVersionUID = 1L;

    MessageListRenderer(ClientGui cg) {
        setParent(cg);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Message> list, Message message, int index,
            boolean isSelected, boolean cellHasFocus) {

        // String code = country.getCode();
        // ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/" + code
        // + ".png"));
        // setIcon(imageIcon);
        setText(message.getMessage());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }

}
