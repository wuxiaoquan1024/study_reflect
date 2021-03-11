package com.study.reflect;

import java.util.ArrayList;
import java.util.List;

public class Generic {

    public static void main(String[] args) {
        List<? extends String> list = new ArrayList();
    }
}
