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
    private ClientGui parentL;

    public CSListRenderer(ClientGui clientGui) {
        setParentL(clientGui);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends CS> list, CS cs, int index, boolean isSelected,
            boolean cellHasFocus) {
        try {
            setText(cs.getName());
            if (isSelected) {
                if (getParentL() != null) {
                    if (getParentL().getSelectedLAst() != null && getParentL().messageListModel != null) {
                        if (getParentL().getSelectedLAst().equals(cs.getId())) {
                            getParentL().messageListModel.removeAllElements();
                        }
                    }
                }
                setBackground(Color.GREEN);
                setForeground(Color.GREEN);
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            if (getParentL().getJlist_Contact().getSelectedValuesList().size() != 1) {
                getParentL().getTextField_message().setEditable(false);
                getParentL().getJButton_send().setEnabled(false);
                getParentL().getJlist_Messages().setEnabled(false);
            } else {
                getParentL().getTextField_message().setEditable(true);
                getParentL().getJButton_send().setEnabled(true);
                getParentL().getJlist_Messages().setEnabled(true);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return this;
    }
}
