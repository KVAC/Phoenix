/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.kvac.phoenix.endpoint.client.gui.searchcs;

import com.github.kvac.phoenix.libs.objects.cs.CS;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jdcs_dev
 */
public class SearchListRenderer extends JLabel implements ListCellRenderer<CS> {

    @Getter
    @Setter
    SearchCS parrent;

    SearchListRenderer(SearchCS ThisThis) {
        setParrent(ThisThis);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends CS> list, CS cs, int index,
            boolean isSelected, boolean cellHasFocus) {

        // String code = country.getCode();
        // ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/" + code + ".png"));
        // setIcon(imageIcon);
        setText(cs.getName());
        if (isSelected) {
            setBackground(Color.GREEN);
            setForeground(Color.GREEN);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
