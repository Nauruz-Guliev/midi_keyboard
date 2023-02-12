package ru.kpfu.itis.gnt;

import java.io.*;

public class MidiFileHandler implements Runnable {
    private static final String FILE_PATH = "src/ru/kpfu/itis/gnt/melodies/";
    private static final String EXTENSION = ".ngm";
    private boolean isWorkingWithFile;
    public MidiFileHandler() {
        isWorkingWithFile = true;
    }

    public void writeToFile(MidiMelody melody, String melodyName) {
        String path = FILE_PATH + melodyName + EXTENSION;
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(melody);
            oos.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        isWorkingWithFile = false;
    }
    public MidiMelody readFromFile(File fileName) throws FileNotFoundException{
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (MidiMelody) ois.readObject();

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        isWorkingWithFile = false;
        return null;
    }

    @Override
    public void run() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            System.out.println("FileHandling was interrupted." + e.getMessage());
        }
    }

}
