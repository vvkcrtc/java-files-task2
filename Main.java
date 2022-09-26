import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    static int nextSave = 1;
    static int countGP = 3;

    static void saveGame(String pathName, GameProgress gp) {
        try (FileOutputStream fos = new FileOutputStream(pathName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gp);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void zipFiles(String zipFile, List<String> files) {

        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i = 0; i < files.size(); i++) {
                File srcFile = new File(files.get(i));
                FileInputStream fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int length = fis.available();
                byte[] buffer = new byte[length];
                fis.read(buffer);
                zos.write(buffer, 0, length);
                zos.closeEntry();
                fis.close();
            }

            zos.close();
            System.out.println("Создан файл " + zipFile);

        } catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
        }

    }

    static void deleteFiles(List<String> files) {
        for (String path : files) {
            File file = new File(path);
            if (file.delete()) {
                System.out.println("Файл " + file + " удален");
            }

        }
    }

    static String getFileName() {
        String result = "save" + Integer.toString(nextSave) + ".dat";
        nextSave++;
        return result;
    }

    public static void main(String[] args) {
        String saveDir = "/home/vvk/Games/savegames";
        GameProgress[] gp = new GameProgress[countGP];
        for (int i = 0; i < countGP; i++) {
            gp[i] = new GameProgress(50 + (i * 10), 3 + i, i + 1, 100 * (i + 1));
        }
        List<String> savedFilesPath = new ArrayList<>();

        for (GameProgress g : gp) {
            String fileName = getFileName();
            String strPath = saveDir + '/' + fileName;
            saveGame(strPath, g);
            savedFilesPath.add(strPath);
        }

        zipFiles(saveDir + '/' + "saves.zip", savedFilesPath);
        deleteFiles(savedFilesPath);

    }
}
