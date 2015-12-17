package hatonekoe.x0.com.oneactivityincludestwofragment.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hatonekoe.x0.com.oneactivityincludestwofragment.IndexActivity;
import hatonekoe.x0.com.oneactivityincludestwofragment.R;
import hatonekoe.x0.com.oneactivityincludestwofragment.utils.LogDebug;

public class ButtonsFragment extends Fragment
{
    private static final String TAG = "ButtonsFragment" ;
    private OnFragmentInteractionListener mListener;

    private Context mContext;
    private View mFragment1View;
    private View mFragment2View;

    private Button mButton1;
    private Button mButton2;

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
        mContext = getContext();
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

        // リスナー登録をおこなう
        setupListener();
    }

    /** リスナー登録をおこなう
     *  Fragment の ID を取得し……という形なので、
     *  Fragment.java でなく Activity.java にここらへんの処理をやらせるのが正しいと思う
     *  TODO: 暇があったらここらの機能を IndexActivity.java に持っていく
     */
    private void setupListener()
    {
        mFragment1View = getFragmentManager().findFragmentById( R.id.fragment1 ).getView();
        mFragment2View = getFragmentManager().findFragmentById( R.id.fragment2 ).getView();

        try
        {
            mButton1 = (Button) mFragment1View.findViewById(R.id.button1);
            mButton2 = (Button) mFragment2View.findViewById(R.id.button1);
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            return;
        }

        mButton1.setBackgroundColor(ContextCompat.getColor( mContext, R.color.amber_200 ));
        mButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LogDebug.D(TAG, "はろー！！！！！！！！" + Uri.parse(this.toString()));
                IndexActivity.displayNotification(R.integer.hello_button_notification, "上のはろーボタンが押されました");
                whenButtonPressed( getResources().getInteger( R.integer.button_top ) );
            }
        });

        mButton2.setBackgroundColor(ContextCompat.getColor( mContext, R.color.teal_200 ));
        mButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LogDebug.D(TAG, "hello... : " + Uri.parse(this.toString()));
                IndexActivity.displayNotification(R.integer.hello_button_notification, "下のHELLOボタンが押されました");
                whenButtonPressed( getResources().getInteger( R.integer.button_bottom ) );
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
        void onFragmentInteraction(int id);
    }

    public void whenButtonPressed(int id)
    {
        if(mListener != null)
        {
            mListener.onFragmentInteraction(id);
        }
    }

}
