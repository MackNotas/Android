package giovannicornachini.macknotas.br;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class FragmentTest extends Fragment {

    private static final String ARG_TEXT = "ARG_TEXT";

    public FragmentTest() {
    }

    public static FragmentTest newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);

        FragmentTest sampleFragment = new FragmentTest();
        sampleFragment.setArguments(args);

        return sampleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(getArguments().getString(ARG_TEXT));

        return textView;
    }

}
