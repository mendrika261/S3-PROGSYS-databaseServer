package file;

import java.io.*;

public class FileManager {
    public static String FILEPATH = "src/file/db/";

    public static void save(String filename, Object object) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(FILEPATH+filename);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static Object load(String filename) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(FILEPATH+filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object result = objectInputStream.readObject();
        objectInputStream.close();
        fileInputStream.close();
        return result;
    }

    public static void clear(String filename) {
        File file = new File(FILEPATH+filename);
        if(file.exists()) file.delete();
    }
}
