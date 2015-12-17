package hatonekoe.x0.com.oneactivityincludestwofragment.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hatonekoe.x0.com.oneactivityincludestwofragment.IndexActivity;
import hatonekoe.x0.com.oneactivityincludestwofragment.R;
import hatonekoe.x0.com.oneactivityincludestwofragment.utils.LogDebug;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ButtonsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ButtonsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ButtonsFragment extends Fragment
{
    private static final String TAG = "ButtonsFragment" ;
    private Button mButton;

    private OnFragmentInteractionListener mListener;

    public ButtonsFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ButtonsFragment newInstance()
    {
        ButtonsFragment fragment = new ButtonsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buttons, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mButton = (Button) getActivity().findViewById(R.id.button1) ;
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LogDebug.D(TAG, "STARTボタンが押されました" + this.getClass() + Uri.parse(this.toString()));
                IndexActivity.displayNotification( R.integer.hello_button_notification, "zzzzz");
            }
        });
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }else
        {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
