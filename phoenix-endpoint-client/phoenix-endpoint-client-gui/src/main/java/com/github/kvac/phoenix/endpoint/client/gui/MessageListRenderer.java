package com.github.kvac.phoenix.endpoint.client.gui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.github.kvac.phoenix.libs.objects.Message;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageListRenderer extends JLabel implements ListCellRenderer<Message> {

    protected static final Logger logger = LoggerFactory.getLogger(MessageListRenderer.class);

    @Getter
    @Setter
    ClientGui parentL;
    private static final long serialVersionUID = 1L;

    MessageListRenderer(ClientGui cg) {
        setParentL(cg);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Message> list, Message message, int index,
            boolean isSelected, boolean cellHasFocus) {
        try {
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
        } catch (Exception e) {
            logger.error("", e);
        }
        return this;
    }

}
