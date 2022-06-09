package ru.kpfu.itis.gnt;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashMap;

// entity for all the constants
public class MidiNotesEntity {
    //it can be six by default
    private int octave = 6;
    public final NoteSwitcherAdapter[] allNotes = new NoteSwitcherAdapter[]{
            this::DO,
            this::DO_SHARP,
            this::RE,
            this::RE_SHARP,
            this::MI,
            this::FA,
            this::FA_SHARP,
            this::SOL,
            this::SOL_SHARP,
            this::LA,
            this::LA_SHARP,
            this::SI
    };

    public void setOctave(int octave) {
        this.octave = octave;
    }

    public String[] DO(int duration) {
        return new String[]{"Do", octaves.get(octave)[0][0], String.valueOf(duration)};
    }

    public String[] DO_SHARP(int duration) {
        return new String[]{"Do#", octaves.get(octave)[0][1], String.valueOf(duration)};
    }

    public String[] RE(int duration) {
        return new String[]{"Re", octaves.get(octave)[0][2], String.valueOf(duration)};
    }

    public String[] RE_SHARP(int duration) {
        return new String[]{"Re#", octaves.get(octave)[0][3], String.valueOf(duration)};
    }

    public String[] MI(int duration) {
        return new String[]{"Mi", octaves.get(octave)[0][4], String.valueOf(duration)};
    }

    public String[] FA(int duration) {
        return new String[]{"Fa", octaves.get(octave)[0][5], String.valueOf(duration)};
    }

    public String[] FA_SHARP(int duration) {
        return new String[]{"Fa#", octaves.get(octave)[0][6], String.valueOf(duration)};
    }

    public String[] SOL(int duration) {
        return new String[]{"Sol", octaves.get(octave)[0][7], String.valueOf(duration)};
    }

    public String[] SOL_SHARP(int duration) {
        return new String[]{"Sol#", octaves.get(octave)[0][8], String.valueOf(duration)};
    }

    public String[] LA(int duration) {
        return new String[]{"La", octaves.get(octave)[0][9], String.valueOf(duration)};
    }

    public String[] LA_SHARP(int duration) {
        return new String[]{"La#", octaves.get(octave)[0][10], String.valueOf(duration)};
    }

    public String[] SI(int duration) {
        return new String[]{"Si", octaves.get(octave)[0][11], String.valueOf(duration)};
    }

    public static final HashMap<Integer, String> instruments = new HashMap<Integer, String>() {{
        put(0, "Акустический рояль");
        put(1, "Яркий рояль");
        put(2, "Большой рояль");
        put(3, "Разбитной рояль");
        put(4, "Электропианино 1 (Родес)");
        put(5, "Электропианино 2 (с эффектом хорус)");
        put(6, "Клавесин");
        put(7, "Клавинет");
        put(8, "Челеста");
        put(9, "Колокольчики");
        put(10, "Музыкальная шкатулка");
        put(11, "Вибрафон");
        put(12, "Маримба");
        put(13, "Ксилофон");
        put(14, "Колокола");
        put(15, "Цимбалы");
        put(16, "Орган Хаммонда");
        put(17, "Перкуссионный электроорган");
        put(18, "Рок-орган");
        put(19, "Кафедральный орган");
        put(20, "Язычковый орган");
        put(21, "Аккордеон");
        put(22, "Гармоника");
        put(23, "Аккордеон танго");
        put(24, "Акустическая гитара (с нейлоновыми струнами)");
        put(25, "Акустическая гитара (с металлическими струнами)");
        put(26, "Джазовая электрогитара");
    }};
    public static final int[] channels = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private ArrayList<String[][]> octaves = new ArrayList<>();

    public ArrayList<String[][]> getOctaves() {
        return octaves;
    }
    private final String[] octaveNames = new String[]{
            "- (-1)",
            "Субконтроктава",
            "Контрактава",
            "Большая",
            "Малая",
            "Первая",
            "Вторая",
            "Третья",
            "Четвертая",
            "Пятая",
            "- (9)"
    };

    public void initOctaves() {
        int amountOfOctaves = 11;
        int n = 0;
        for (int i = 0; i < amountOfOctaves; i++) {
            String[][] noteOctaves = new String[2][12];
            for (int j = 0; j < 12; j++) {
                noteOctaves[0][j] = String.valueOf(n);
                noteOctaves[1][j] = octaveNames[i];
                n++;
            }
            octaves.add(noteOctaves);
        }
    }
    public static final KeyCode[][] numButtons = new KeyCode[][]{
            {KeyCode.NUMPAD1, KeyCode.END},
            {KeyCode.NUMPAD2, KeyCode.DOWN},
            {KeyCode.NUMPAD3, KeyCode.PAGE_DOWN},
            {KeyCode.NUMPAD4, KeyCode.LEFT},
            {KeyCode.NUMPAD5, KeyCode.CLEAR},
            {KeyCode.NUMPAD6, KeyCode.RIGHT},
            {KeyCode.NUMPAD7, KeyCode.HOME},
            {KeyCode.NUMPAD8, KeyCode.UP},
            {KeyCode.NUMPAD9, KeyCode.PAGE_UP},
            {KeyCode.NUM_LOCK, KeyCode.NUM_LOCK},
            {KeyCode.DIVIDE, KeyCode.DIVIDE},
            {KeyCode.MULTIPLY, KeyCode.MULTIPLY,}

    };
}
