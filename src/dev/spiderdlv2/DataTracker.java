package dev.spiderdlv2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class DataTracker {

    private String nomeFile;

    private Map<String, int[]> map;

    public DataTracker(String nomeFile) 
    {
        this.nomeFile = nomeFile;
        map = new TreeMap<>();
        leggiDati();
    }

    public void reset(String diff) {
        int[] a = getData(diff);
        Arrays.fill(a, 0);
    }

    public void scriviSulFile() {
        try {
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(nomeFile)));

            for (String difficulty : new String[] { "Facile", "Medio", "Difficile" }) {
                int[] a = getData(difficulty);

                for (int i = 0; i < 4; i++) {
                    dos.writeInt(a[i]);
                }
            }

            dos.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public int[] getData(String diff) {
        return map.get(diff);
    }

    
    private void leggiDati() {
        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(nomeFile)));

            for (String difficulty : new String[] { "Facile", "Medio", "Difficile" }) {
            // readInt -> 	@return  the next four bytes of this input stream, interpreted as a <code>int</code>.
                map.put(difficulty, new int[] { dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt() });
                
                // Nel file stats.db vengono salvati i punteggi dei giocatori attraverso una mappa così composta -> Difficoltà-Punteggio.
            }

            dis.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

}
