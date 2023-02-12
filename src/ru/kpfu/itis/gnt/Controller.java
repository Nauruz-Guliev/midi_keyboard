package ru.kpfu.itis.gnt;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;


public class Controller implements Initializable {
    public javafx.scene.control.TextField inputFileName;
    @FXML
    private Pane pane;
    @FXML
    private Label notesLabel, statusLabel;
    @FXML
    private TextField filenameInput;
    @FXML
    private Button buttonPrevious, buttonPlay, buttonNext, buttonSaveRecording, buttonRecord, buttonStop;
    @FXML
    private ComboBox<String> instrumentsMenu, channelBox, octaveMenu;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private MidiPlayer player;
    @FXML
    private Slider volumeSlider;
    private File directory;
    private File[] files;
    private ArrayList<File> melodies;
    private long pressStartTime;
    private long timeDifference;
    private String[] notePlayed;
    private Synthesizer synth;
    private MidiMelody melody;
    private MidiChannel[] channels;
    private Controller controller;
    private boolean isPlaying;
    private int volume;
    private int instrument;
    private int channel;
    private boolean isSaving;
    private StringBuilder notesPlaying;
    private static final int[] ints = MidiNotesEntity.channels;
    private static final int DEFAULT_DURATION = 100;
    private MidiNotesEntity notesEntity;
    private int currentSong;
    private ArrayList<String[][]> octaves;
    private final HashMap<NoteSwitcherAdapter, KeyCode[]> notesKeysEquivalent = new HashMap<>();
    private static final String MIDI_DIRECTORY = "src/ru/kpfu/itis/gnt/melodies";
    private static final HashMap<Integer, String> instruments = MidiNotesEntity.instruments;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            synth = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
        try {
            synth.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
        notesEntity = new MidiNotesEntity();
        notesPlaying = new StringBuilder();
        melody = new MidiMelody();
        volume = 100;
        channels = synth.getChannels();
        pressStartTime = System.currentTimeMillis();
        melodies = new ArrayList<File>();
        directory = new File(MIDI_DIRECTORY);
        isPlaying = false;
        notesEntity.initOctaves();
        octaves = notesEntity.getOctaves();
        //adding all available midies from directory "/melodies/"
        updateFileList();
        //linking keys to notes and saving them in hashmap
        for(int i =0; i< notesEntity.allNotes.length; i++) {
            notesKeysEquivalent.put(notesEntity.allNotes[i], MidiNotesEntity.numButtons[i]);
        }
        //adding instruments to dropdown menu
        for (int i = 0; i < instruments.size(); i++) {
            instrumentsMenu.getItems().add(instruments.get(i));
        }
        instrumentsMenu.setOnAction(this::changeInstrument);
        //adding channels to dropdown menu
        for (int i = 0; i < ints.length; i++) {
            channelBox.getItems().add(String.valueOf(ints[i]));
        }
        channelBox.setOnAction(this::changeChannel);
        //adding octaves to dropdown menu
        for (int i = 0; i < octaves.size(); i++) {
            octaveMenu.getItems().add(octaves.get(i)[1][1]);
        }
        octaveMenu.setOnAction(this::changeOctave);
    }


    public void playPrevious() {
        updateFileList();
        if (currentSong > 1 && !isPlaying) {
            currentSong--;
            playCurrent();
        }
    }

    public void displayUserMessage(String message) {
        if (inputFileName == null || Objects.equals(inputFileName.getText(), "")) {
            notesLabel.setText(message);
        }
    }

    public void playCurrent() {
        updateFileList();
        //checking if there is a thread playing music
        if (!isPlaying) {
            try {
                isPlaying = true;
                // playing music on another thread
                File midiToPlay = melodies.get(currentSong);
                displayUserMessage(midiToPlay.getName() + " is playing");
                MidiFileHandler midiFileHandler = new MidiFileHandler();
                Thread threadFileHandler = new Thread(midiFileHandler);
                threadFileHandler.start();
                player = new MidiPlayer(midiFileHandler.readFromFile(midiToPlay));
                this.notifyAll();
                //player = new MidiPlayer(MidiFileHandler.readFromFile(midiToPlay));
                Thread t1 = new Thread(player);
                t1.start();
                //creating anonymous thread for handling progress bar state
                new Thread(() -> {
                    //progress bar should change dynamically
                    //depending on how many notes has been played
                    while (t1.isAlive()) {
                        progressBar.setProgress(player.getProgress() / 100);
                    }
                    player.close();
                    isPlaying = false;
                }).start();
            } catch (FileNotFoundException | IndexOutOfBoundsException ex) {
                displayUserMessage("There is nothing to play");
            }
        }
    }
    //updates "playlist" from "/melodies" directory
    private void updateFileList() {
        try {
            files = directory.listFiles();
            if (files !=null) {
                melodies.addAll(Arrays.asList(files));
            }
        } catch (Exception ex) {
            displayUserMessage("There is no files");
        }
    }

    public void playNext() {
        updateFileList();
        if(currentSong < files.length-1 && !isPlaying) {
            currentSong ++;
        } else if (!isPlaying) {
            currentSong = 0;
        }
        playCurrent();
    }

    public void saveRecording() {
        if (!(melody.getMelody().size() == 0)) {
            if(filenameInput.getText() == null || Objects.equals(filenameInput.getText(), "")){
                displayUserMessage("Name of file missing");
            } else {
                MidiFileHandler midiFileHandler = new MidiFileHandler();
                Thread threadWriter = new Thread(midiFileHandler);
                threadWriter.start();
                midiFileHandler.writeToFile(melody, filenameInput.getText());
                this.notifyAll();
                resetMelody();
                displayUserMessage(filenameInput.getText() + " is saved");
                filenameInput.setText("");
            }
        } else {
            displayUserMessage("Nothing to save");
        }
    }

    private void resetMelody() {
        melody = new MidiMelody();
        notePlayed = null;
        //updating properties for the new song
        melody.setInstrument(instrument);
        melody.setVolume((int) volumeSlider.getValue());
        try {
            melody.setChannel(Integer.parseInt(channelBox.getValue()));
        } catch (NumberFormatException ex) {
            melody.setChannel(0);
        }
    }

    public void startRecording() {
        if (!isSaving) {
            resetMelody();
            pressStartTime = System.currentTimeMillis();
            isSaving = true;
            startRecordingMelody(notePlayed);
            changeStatusMessage("Recording");
        } else {
            changeStatusMessage("Already recording");
        }
    }

    private void changeStatusMessage(String message) {
        statusLabel.setText(message);
    }

    public void stopRecording() {
        if (isSaving) {
            changeStatusMessage("Stopped recording");
            isSaving = false;
        } else {
            changeStatusMessage("Recording is stopped already");
        }
    }

    public void onKeyPressed(KeyEvent event) {
        timeDifference = (System.currentTimeMillis() - pressStartTime);
        for (Map.Entry<NoteSwitcherAdapter, KeyCode[]> entry : notesKeysEquivalent.entrySet()) {
            NoteSwitcherAdapter key = entry.getKey();
            KeyCode[] value = entry.getValue();
            if (event.getCode().equals(value[0]) || event.getCode().equals(value[1])) {
                notePlayed = key.play(DEFAULT_DURATION);
            }
        }
        if (notePlayed != null) {
            channels[channel].noteOn(Integer.parseInt(notePlayed[1]), volume);
        }
    }

    public void onKeyReleased(KeyEvent event) {
        pressStartTime = System.currentTimeMillis();
        if (notePlayed != null) {
            channels[channel].noteOff(Integer.parseInt(notePlayed[1]));
            //saving the duration of the pause between two button clicks
            notePlayed[2] = String.valueOf(timeDifference);

            // appending the notes to the notesLabel so that user can see what note was clicked
            if (notesPlaying.length() < 28) {
                notesPlaying.append(notePlayed[0]);
            } else {
                notesPlaying = new StringBuilder();
                notesPlaying.append(notePlayed[0]);
            }
            displayUserMessage(notesPlaying + " ");
        }
        if (isSaving) {
            startRecordingMelody(notePlayed);
        }
    }

    private void startRecordingMelody(String[] noteToSave) {
        melody.addNoteToMelody(noteToSave);
    }

    public void changeInstrument(ActionEvent actionEvent) {
        for (Map.Entry<Integer, String> e : instruments.entrySet()) {
            if (instrumentsMenu.getValue().equals(e.getValue())) {
                instrument = e.getKey();
            }
        }
        channels[channel].programChange(instrument);
        melody.setInstrument(instrument);
    }

    public void changeChannel(ActionEvent actionEvent) {
        channel = Integer.parseInt(channelBox.getValue());
        melody.setChannel(Integer.parseInt(channelBox.getValue()));
    }

    public void changeVolume(MouseEvent mouseEvent) {
        volume = (int) volumeSlider.getValue();
        melody.setVolume(volume);
    }

    public void changeOctave(ActionEvent actionEvent) {
        for (int i = 0; i < octaves.size(); i++) {
            if (octaves.get(i)[1][0].equals(octaveMenu.getValue())) {
                notesEntity.setOctave(i);
            }
        }
    }

    public void nullifyNotePlayed(MouseEvent mouseEvent) {
        notePlayed = null;
    }
}


