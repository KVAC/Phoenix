package com.github.kvac.phoenix.endpoint.client.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.github.kvac.phoenix.libs.objects.cs.CS;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSListRenderer extends JLabel implements ListCellRenderer<CS> {

    protected static final Logger logger = LoggerFactory.getLogger(CSListRenderer.class);

    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private ClientGui parent;

    public CSListRenderer(ClientGui clientGui) {
        setParent(clientGui);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends CS> list, CS cs, int index, boolean isSelected,
            boolean cellHasFocus) {
        try {
            setText(cs.getName());
            if (isSelected) {
                if (getParent() != null) {
                    if (getParent().getSelected_LAst() != null && getParent().messageListModel != null) {
                        if (getParent().getSelected_LAst().equals(cs.getID())) {
                            getParent().messageListModel.removeAllElements();
                        }
                    }
                }
                setBackground(Color.GREEN);
                setForeground(Color.GREEN);
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            if (getParent().getJlist_Contact().getSelectedValuesList().size() != 1) {
                getParent().getTextField_message().setEditable(false);
                getParent().getJButton_send().setEnabled(false);
                getParent().getJlist_Messages().setEnabled(false);
            } else {
                getParent().getTextField_message().setEditable(true);
                getParent().getJButton_send().setEnabled(true);
                getParent().getJlist_Messages().setEnabled(true);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return this;
    }
}
