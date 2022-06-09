package ru.kpfu.itis.gnt;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

public class MidiMelody implements Serializable {
    private final LinkedList<String[]> melody;
    private int instrument;
    // volume is set to 100 by default
    private static final long serialVersionUID = 10304L;
    private int volume = 100;
    private int channel;
    public MidiMelody() {
        melody = new LinkedList<>();
    }

    public LinkedList<String[]> getMelody() {
        return melody;
    }

    public void addNoteToMelody(String[] note) {
        if (note!=null) {
            melody.add(note);
        }
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (String[] m : melody) {
            res.append(Arrays.toString(m)).append(" Volume: ").append(volume).append(" Channel: ").append(channel).append(" instrument: ").append(instrument);
        }
        return res.toString();
    }

    public void setInstrument(int num) {
       instrument = num;
    }

    public int getInstrument() {
        return instrument;
    }
    public int getVolume(){
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
