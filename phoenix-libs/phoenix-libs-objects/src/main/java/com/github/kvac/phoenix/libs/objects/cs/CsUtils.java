package com.github.kvac.phoenix.libs.objects.cs;

public class CsUtils {

    public static void updateName(CS from, CS to) {
        if (from.getNameTime() > to.getNameTime()) {
            to.setName(from.getName());
            to.setNameTime(from.getNameTime());
        }
    }
}
