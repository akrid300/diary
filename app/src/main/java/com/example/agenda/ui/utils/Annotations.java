package com.example.agenda.ui.utils;

import androidx.annotation.IntDef;

public class Annotations {

    @IntDef({CardType.HEADER, CardType.EVENT})
    public @interface CardType {
        int HEADER = 0;
        int EVENT = 1;
        int HOLIDAY = 2;
    }
}
