package com.github.kvac.phoenix.endpoint.client.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.github.kvac.phoenix.libs.objects.cs.CS;

import lombok.Getter;
import lombok.Setter;

public class CSListRenderer extends JLabel implements ListCellRenderer<CS> {

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

        // String code = country.getCode();
        // ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/" + code
        // + ".png"));
        // setIcon(imageIcon);
        setText(cs.getName());

        if (isSelected) {
            setBackground(Color.GREEN);
            setForeground(Color.GREEN);

        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        if (getParent().getJlist_Contact().getSelectedValuesList().size() != 1) {
            // getParent().getList_messages().setEnabled(false);
            getParent().getTextField_message().setEditable(false);
        } else {

            getParent().getTextField_message().setEditable(true);
            // getParent().getList_messages().setEnabled(true);
        }
        return this;
    }

}
