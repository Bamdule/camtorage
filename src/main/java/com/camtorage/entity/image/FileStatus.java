package com.camtorage.entity.image;

public enum FileStatus {
    CREATE, UPDATE, DELETE;

    static public FileStatus get(Object id, Object file) {

        if (id == null && file != null) {
            return CREATE;
        } else if (id != null && file != null) {
            return UPDATE;
        } else {
            return DELETE;
        }

    }
}
