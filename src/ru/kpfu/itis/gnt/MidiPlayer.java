package ru.kpfu.itis.gnt;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.io.FileNotFoundException;

public class MidiPlayer implements Runnable {
    private MidiChannel[] channels = null;
    private Synthesizer synth = null;
    private final MidiMelody melody;
    private int progress;
    private static final int TIME_COEFFICIENT = 100;

    public MidiPlayer(MidiMelody melody) throws FileNotFoundException {
        this.melody = melody;
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();
        } catch (MidiUnavailableException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void close() {
        synth.close();
    }

    public double getProgress() {
        return progress;
    }

    public void playSound() {
        channels[melody.getChannel()].programChange(melody.getInstrument());
        double i = 1;
        for (String[] note : melody.getMelody()) {

            channels[melody.getChannel()].noteOn(Integer.parseInt(note[1]), melody.getVolume());
            progress = (int) (i / (double) melody.getMelody().size() * 100);
            try {
                Thread.sleep(TIME_COEFFICIENT);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
            channels[melody.getChannel()].noteOff(Integer.parseInt(note[1]));
            try {
                Thread.sleep(Integer.parseInt(note[2]));
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
            i++;
        }
        try {
            //putting thread to sleep so that music stops with a pause at the end
            //this way user can hear an echo instead of the sound being cut off
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        playSound();
    }
}
