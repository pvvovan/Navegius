package com.pvvovan.Navegius.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.agri.pvvovan.agriplanet.AppAgri;
import com.example.hellojni.R;
import com.pvvovan.Navegius.Model.NavigationType;
//import com.agri.pvvovan.agriplanet.NMEA.INmeaData;
//import com.agri.pvvovan.agriplanet.NMEA.INmeaListen;
//import com.agri.pvvovan.agriplanet.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DimensionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DimensionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private NavigationType m_lineType;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DimensionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param lineType Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DimensionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DimensionsFragment newInstance(NavigationType lineType, String param2) {
        DimensionsFragment fragment = new DimensionsFragment();
        Bundle args = new Bundle();
        if (lineType == NavigationType.Straight)
            args.putString(ARG_PARAM1, "Straight");
        else if (lineType == NavigationType.Curve)
            args.putString(ARG_PARAM1, "Curve");
        else
            args.putString(ARG_PARAM1, "None");
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String lType = getArguments().getString(ARG_PARAM1);
            if (lType.compareTo("Straight") == 0)
                m_lineType = NavigationType.Straight;
            else if (lType.compareTo("Curve") == 0)
                m_lineType = NavigationType.Curve;
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    double m_implementWidth = 12.0;
    TextView txtViewWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dimensions, container, false);
        //final TextView txtV = (TextView)rootView.findViewById(R.id.textViewNmea);

//        INmeaData intsa = AppAgri.NmeaProvider;
//        intsa.SetListener(new INmeaListen() {
//            @Override
//            public void NmeaReceived(String data) {
//                if (c < 10)
//                {
//                    c++;
//                    txtV.append(data);
//                }
//                else {
//                    c = 0;
//                    txtV.setText(data);
//                }
//            }
//        });
//
//        rootView.findViewById(R.id.buttonFragClose).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onFinishActivity("Close!");
//            }
//        });
//        rootView.findViewById(R.id.buttonRun).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onShowFragment(RunStraightFragment.newInstance(lineType, ""));
//            }
//        });
        txtViewWidth = (TextView)rootView.findViewById(R.id.textViewWidth);
        txtViewWidth.setText(String.format("%.1f", m_implementWidth));
        rootView.findViewById(R.id.buttonWidthPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_implementWidth += 0.1;
                txtViewWidth.setText(String.format("%.1f", m_implementWidth));
            }
        });
        rootView.findViewById(R.id.buttonWidthMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_implementWidth > 1.0)
                    m_implementWidth -= 0.1;
                txtViewWidth.setText(String.format("%.1f", m_implementWidth));
            }
        });
        rootView.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShowFragment(MainFragment.newInstance("", ""));
            }
        });
        rootView.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_lineType == NavigationType.Straight) {
                    mListener.onShowFragment(RunStraightFragment.newInstance(m_lineType, m_implementWidth));
                }
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
