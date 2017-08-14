package giovannicornachini.macknotas.br.HorarioTest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Locale;

import giovannicornachini.macknotas.br.Entidades.Horario;

import static org.apache.http.util.EncodingUtils.getString;

/**
 * Created by GiovanniCornachini on 06/06/15.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    Horario horario;
    Fragment fragment;

    public SectionsPagerAdapter(FragmentManager fm, Horario horario) {
        super(fm);
        this.horario = horario;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DummySectionFragment (defined as a static inner class
        // below) with the page number as its lone argument.
        DummySectionFragment dummySectionFragmentnew = new DummySectionFragment();
        dummySectionFragmentnew.setHorario(horario);
        Fragment fragment = dummySectionFragmentnew;
        Bundle args = new Bundle();
        args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 6;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return ("Segunda").toUpperCase(l);
            case 1:
                return ("Terça").toUpperCase(l);
            case 2:
                return ("Quarta").toUpperCase(l);
            case 3:
                return ("Quinta").toUpperCase(l);
            case 4:
                return ("Sexta").toUpperCase(l);
            case 5:
                return ("Sábado").toUpperCase(l);
        }
        return null;
    }
}