package com.better.demclassloader.loader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by better on 2020/6/2.
 */
public class DiskClassLoader extends ClassLoader {
    String path;

    public DiskClassLoader(String path) {
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classPath = path + name + ".class";
        System.out.println(classPath);
        byte[] classBytes = null;
        Path path;
        try {
            path = Paths.get(new URI(classPath));
            classBytes = Files.readAllBytes(path);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        // create class
        // Exception in thread "main" java.lang.NoClassDefFoundError: Secret (wrong name: lg/test/Secret)
        return defineClass(name, classBytes, 0, classBytes.length);
    }
}
