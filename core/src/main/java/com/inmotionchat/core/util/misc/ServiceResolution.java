package com.inmotionchat.core.util.misc;

public class ServiceResolution {

    protected static String artifact(Class<?> clazz) {
        // eg com.inmotionchat.<artifact>
        return clazz.getPackage().getName().split("\\.")[2];
    }

    public static boolean isFromService(Class<?> clazz, Class<?> serviceClass) {
        return artifact(clazz).equals(artifact(serviceClass));
    }

}
