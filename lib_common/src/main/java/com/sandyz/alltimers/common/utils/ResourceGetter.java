package com.sandyz.alltimers.common.utils;

import com.sandyz.alltimers.common.R;

import java.lang.reflect.Field;
import java.util.Objects;

//根据资源名称取资源id

public class ResourceGetter {
    public static int getDrawableId(String fileName) {
        try {
            Field field = R.drawable.class.getField(fileName);
            return Integer.parseInt(Objects.requireNonNull(field.get(null)).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
