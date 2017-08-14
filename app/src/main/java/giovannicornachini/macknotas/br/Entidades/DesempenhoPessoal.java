package giovannicornachini.macknotas.br.Entidades;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by GiovanniCornachini on 05/07/15.
 */
public class DesempenhoPessoal {

    ArrayList<Entry> entries;
    ArrayList<String> labels;
    float mediaGeral;

    public DesempenhoPessoal() {
        entries = new ArrayList<>();
        labels = new ArrayList<>();
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public float getMediaGeral() {
        return mediaGeral;
    }

    public void setMediaGeral(float mediaGeral) {
        this.mediaGeral = mediaGeral;
    }
}
