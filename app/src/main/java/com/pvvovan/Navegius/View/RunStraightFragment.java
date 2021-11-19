package com.pvvovan.Navegius.View;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

//import com.agri.pvvovan.agriplanet.Agri_GL.GlView;
//import com.agri.pvvovan.agriplanet.Model.NavigationType;
//import com.agri.pvvovan.agriplanet.R;
import com.pvvovan.Navegius.Agri_GL.GlView;
import com.example.hellojni.R;
import com.pvvovan.Navegius.Agri_GL.Tetragon;
import com.pvvovan.Navegius.Agri_GL.Vector3;
import com.pvvovan.Navegius.Geometry.CartPoint;
import com.pvvovan.Navegius.Geometry.GeoPoint;
import com.pvvovan.Navegius.Geometry.GpsMath;
import com.pvvovan.Navegius.Geometry.LineDefinition;
import com.pvvovan.Navegius.Geometry.Utilities;
import com.pvvovan.Navegius.Model.NavigationType;
import com.pvvovan.Navegius.NMEA.IGeoPointReceived;
import com.pvvovan.Navegius.NMEA.NmeaReader;
import com.pvvovan.Navegius.Serial.ISerial;
import com.pvvovan.Navegius.Serial.SerialPort;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RunStraightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RunStraightFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private NavigationType m_lineType;
    private double m_implementWidth;

    private OnFragmentInteractionListener mListener;

    public RunStraightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param navType Parameter 1.
     * @param implementWidth Parameter 2.
     * @return A new instance of fragment RunStraightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RunStraightFragment newInstance(NavigationType navType, double implementWidth) {
        RunStraightFragment fragment = new RunStraightFragment();
        Bundle args = new Bundle();
        String param1 = "None";
        switch (navType){
            case Curve:
                param1 = "Curve";
                break;
            case Straight:
                param1 = "Straight";
                break;
            default:
                param1 = "None";
                break;
        }
        args.putString(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM2, implementWidth);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            switch (mParam1){
                case "Curve":
                    m_lineType = NavigationType.Curve;
                    break;
                case "Straight":
                    m_lineType = NavigationType.Straight;
                    break;
            }
            m_implementWidth = getArguments().getDouble(ARG_PARAM2);
            halfWidth = m_implementWidth / 2;
        }
    }

    private GlView m_glView;
    private GpsMath gpsMath;
    private IGeoPointReceived PointForAB_Listener;
    private IGeoPointReceived InitButtons_Listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_run_straight, container, false);
        m_glView = (GlView)rootView.findViewById(R.id.openGlView);
        rootView.findViewById(R.id.buttonZoomOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_glView.SetZoom(m_glView.GetZoom() * 1.1f);
            }
        });
        rootView.findViewById(R.id.buttonZoomIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_glView.SetZoom(m_glView.GetZoom() / 1.1f);
            }
        });
//        rootView.findViewById(R.id.buttonSe4t).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                m_glView.Set4();
//                test();
//            }
//        });

        // EMEI
        TelephonyManager tm = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String uid_EMEI = tm.getDeviceId();
        final int done = uid_EMEI.compareTo("863835023207371");

//        btnTimer = (Button)rootView.findViewById(R.id.buttonSe4t);
        buttonA = (ImageButton)rootView.findViewById(R.id.buttonA);
        buttonB = (ImageButton)rootView.findViewById(R.id.buttonB);
        imageSatellite = (ImageView)rootView.findViewById(R.id.imageViewSatellite);
        toggleImplement = (ToggleButton)rootView.findViewById(R.id.toggleImplement);
        buttonB.setVisibility(View.INVISIBLE);
        buttonA.setVisibility(View.INVISIBLE);
        toggleImplement.setVisibility(View.INVISIBLE);
        txtViewSpeed = (TextView)rootView.findViewById(R.id.textViewSpeed);
        txtViewArea = (TextView)rootView.findViewById(R.id.textViewArea);
        txtViewSatNumber = (TextView)rootView.findViewById(R.id.textViewSatelliteNumber);

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonA.setVisibility(View.INVISIBLE);
                //buttonB.setVisibility(View.VISIBLE);
                //GPS.NMEA.NmeaReader.Instance.PointReceived += new Action<GeoPoint>(Instance_PointReceivedForAB);
                if (gpsMath == null)
                    gpsMath = new GpsMath(currentGeoPoint);
                PointA = gpsMath.ToLocalHorizontal(Utilities.GeoToCartesian(currentGeoPoint));
                PointForAB_Listener = new IGeoPointReceived() {
                    @Override
                    public void GeoPointReceived(GeoPoint geoPoint) {
                        CartPoint currP = gpsMath.ToLocalHorizontal(Utilities.GeoToCartesian(geoPoint));
                        if (CartPoint.Subtract(currP, PointA).Norm() > 10)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    buttonB.setVisibility(View.VISIBLE);
                                    NmeaReader.Instance().RemoveGeoPointListener(PointForAB_Listener);
                                }
                            });
                        }
                    }
                };
                NmeaReader.Instance().AddGeoPointListener(PointForAB_Listener);
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonB.setVisibility(View.INVISIBLE);
                //imageSatellite.setImageResource(R.drawable.satellite_red);
                PointB = gpsMath.ToLocalHorizontal(Utilities.GeoToCartesian(currentGeoPoint));
                lineAB = LineDefinition.GetLineDefinition(PointA, PointB);
            }
        });

        toggleImplement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleImplement.isChecked())
                {
                    //imageSatellite.setImageResource(R.drawable.satellite_green);
                    if (gpsMath == null)
                        gpsMath = new GpsMath(currentGeoPoint);
                    addAppliedAreaRequired = true;
                    posNumberApplied = 0;
                }
                else
                {
                    //imageSatellite.setImageResource(R.drawable.satellite_yellow);
                    addAppliedAreaRequired = false;
                }
            }
        });

        NmeaReader.Instance().Start(serial);
        NmeaReader.Instance().AddGeoPointListener(new IGeoPointReceived() {
            @Override
            public void GeoPointReceived(final GeoPoint geoPoint) {
                if (done == 0) {
                    ProcessPoint(geoPoint);
                }
            }
        });
        NmeaReader.Instance().AddGeoPointListener(new IGeoPointReceived() {
            @Override
            public void GeoPointReceived(final GeoPoint geoPoint) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showGpsStatus(currentGeoPoint);
                    }
                });
            }
        });
        InitButtons_Listener = new IGeoPointReceived() {
            @Override
            public void GeoPointReceived(GeoPoint geoPoint) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (toggleImplement.getVisibility() == View.INVISIBLE && currentGeoPoint.NumberOfSatellites > 4) {
                            toggleImplement.setVisibility(View.VISIBLE);
                            if (lineAB == null)
                                buttonA.setVisibility(View.VISIBLE);
                            NmeaReader.Instance().RemoveGeoPointListener(InitButtons_Listener);
                        }
                    }
                });
            }
        };
        NmeaReader.Instance().AddGeoPointListener(InitButtons_Listener);

        return rootView;
    }

    private volatile ArrayList<Vector3> linePoints = new ArrayList<>(6);
    private volatile ArrayList<Vector3> redLine = new ArrayList<>(2);

    private void SetABLines() {
//        int len = 3;//(int)(1000 / m_implementWidth);
//        linePoints = new ArrayList<Vector3>(len * 2);
        linePoints.clear();

        double distanceToAB = LineDefinition.GetDistance(lineAB, currentCartPoint);
        double shift = Math.round(distanceToAB / m_implementWidth) * m_implementWidth;
        LineDefinition guideLine = LineDefinition.GetParallelLine(lineAB, shift);
        double distance = LineDefinition.GetDistance(guideLine, currentCartPoint);
        if (distance > distanceToAB)
            guideLine = LineDefinition.GetParallelLine(lineAB, -shift);

        LineDefinition lineLeft = LineDefinition.GetParallelLine(guideLine, -m_implementWidth);
        LineDefinition lineRight = LineDefinition.GetParallelLine(guideLine, m_implementWidth);
        double dX = m_implementWidth * 5;
        AddLinePoints(linePoints, guideLine, dX);
        AddLinePoints(linePoints, lineLeft, dX);
        AddLinePoints(linePoints, lineRight, dX);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    m_glView.SetLines(linePoints);
                }catch (Exception ex){}
            }
        });
    }

    private void AddLinePoints(ArrayList<Vector3> linePoints, LineDefinition line, double dX) {
        Vector3 A = new Vector3((float)(currentCartPoint.X - dX), 0, 0);
        Vector3 B = new Vector3((float)(currentCartPoint.X + dX), 0, 0);
        A.y = (float) (line.k * A.x + line.b);
        B.y = (float) (line.k * B.x + line.b);
        linePoints.add(A);
        linePoints.add(B);
    }

    private volatile boolean addAppliedAreaRequired = false;
    private int posNumberApplied = 0;

    private volatile CartPoint PointA;
    private volatile CartPoint PointB;
    private volatile CartPoint left;
    private volatile CartPoint right;

    private GeoPoint currentGeoPoint = new GeoPoint();
    private CartPoint previousPoint = new CartPoint();
    private CartPoint currentCartPoint;
    private CartPoint vehicleDirection;
    private LineDefinition lineAB;

    private void ProcessPoint(GeoPoint geoPoint) {
        currentGeoPoint = geoPoint;
        if (gpsMath != null)
        {
            CartPoint cartP = gpsMath.ToLocalHorizontal(Utilities.GeoToCartesian(geoPoint));

            if (CartPoint.Subtract(cartP, previousPoint).Norm() < 1.008 || geoPoint.Azimuth == 0.0 || geoPoint.Speed < 0.5)
                return;

            currentCartPoint = cartP;
            vehicleDirection = CartPoint.Subtract(cartP, previousPoint).Normalize();
            currentGeoPoint.Azimuth = gpsMath.SmoothedAzimuth(vehicleDirection);

            CartPoint orth = new CartPoint(-vehicleDirection.Y, vehicleDirection.X, vehicleDirection.Z);
            left = CartPoint.Subtract(CartPoint.Add(currentCartPoint, CartPoint.Multiply(orth, halfWidth)), CartPoint.Multiply(vehicleDirection, distanceToImplement));
            right = CartPoint.Subtract(CartPoint.Add(currentCartPoint, CartPoint.Multiply(orth, -halfWidth)), CartPoint.Multiply(vehicleDirection, distanceToImplement));

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        redLine.clear();
                        redLine.add(new Vector3((float) left.X, (float) left.Y, 0));
                        redLine.add(new Vector3((float) right.X, (float) right.Y, 0));
                        m_glView.SetRedLine(redLine);
                    }catch (Exception ex){}
                }
            });


            if (addAppliedAreaRequired)
                addAppliedArea(cartP);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    m_glView.SetPosition(new Vector3((float) currentCartPoint.X, (float) currentCartPoint.Y, (float) currentCartPoint.Z), (float) (currentGeoPoint.Azimuth));
                    try {
                        m_glView.SetTriangles(m_processedAreaTetragons);
                    }
                    catch (Exception ex) {
                    }
                }
            });
            if (lineAB != null)
            {
                SetABLines();
            }

            previousPoint = cartP;
        }
    }

    private ArrayList<Tetragon> m_processedAreaTetragons = new ArrayList<>();

    private CartPoint previousAppliedPoint;
    private double appliedArea;
    private double halfWidth;
    private CartPoint previousLeft;
    private CartPoint previousRight;

    private void addAppliedArea(CartPoint cartPoint)
    {
        if (posNumberApplied == 0)
        {
            posNumberApplied++;
            previousAppliedPoint = cartPoint;
        }
        else
        {
            if (CartPoint.Subtract(previousAppliedPoint, cartPoint).Norm() < 8)
                return;
            appliedArea += CartPoint.Subtract(cartPoint, previousAppliedPoint).Norm() * halfWidth * 2;
            CartPoint n = CartPoint.Subtract(cartPoint, previousAppliedPoint).Normalize();
            CartPoint orth = new CartPoint(-n.Y, n.X, n.Z);
            if (posNumberApplied == 1)
            {
                posNumberApplied++;
                previousLeft = CartPoint.Subtract(CartPoint.Add(previousAppliedPoint, CartPoint.Multiply(orth, halfWidth)), CartPoint.Multiply(n, distanceToImplement));
                previousRight = CartPoint.Subtract(CartPoint.Add(previousAppliedPoint, CartPoint.Multiply(orth, -halfWidth)), CartPoint.Multiply(n, distanceToImplement));
            }
            CartPoint left = CartPoint.Subtract(CartPoint.Add(cartPoint, CartPoint.Multiply(orth, halfWidth)), CartPoint.Multiply(n, distanceToImplement));
            CartPoint right = CartPoint.Subtract(CartPoint.Add(cartPoint, CartPoint.Multiply(orth, -halfWidth)), CartPoint.Multiply(n, distanceToImplement));
//            Tetragon q = new Tetragon(new List<PointF>() { previousLeft, (PointF)left, (PointF)right, previousRight });
//            m_quadTree.Insert(q);
            Tetragon tr = new Tetragon();
//            tr.Vertices.add(new Vector3((float)previousLeft.X, (float)previousLeft.Y, (float)previousLeft.Z));
//            tr.Vertices.add(new Vector3((float)left.X, (float)left.Y, (float)left.Z));
//            tr.Vertices.add(new Vector3((float)right.X, (float)right.Y, (float)right.Z));
//            tr.Vertices.add(new Vector3((float)previousRight.X, (float)previousRight.Y, (float)previousRight.Z));
            tr.Vertices.add(new Vector3((float)previousLeft.X, (float)previousLeft.Y, 0));
            tr.Vertices.add(new Vector3((float)left.X, (float)left.Y, 0));
            tr.Vertices.add(new Vector3((float)right.X, (float)right.Y, 0));
            tr.Vertices.add(new Vector3((float)previousRight.X, (float)previousRight.Y, 0));
            m_processedAreaTetragons.add(tr);

            previousLeft = left;
            previousRight = right;
            previousAppliedPoint = cartPoint;
        }
    }

    private double distanceToImplement = 1.5;

    private void showGpsStatus(GeoPoint geoPoint)
    {
        if (geoPoint.NumberOfSatellites == 0)
        {
            imageSatellite.setImageResource(R.drawable.satellite_red);
        }
        else if (geoPoint.NumberOfSatellites < 5)
        {
            imageSatellite.setImageResource(R.drawable.satellite_yellow);
        }
        else
        {
            imageSatellite.setImageResource(R.drawable.satellite_green);
        }
        txtViewSpeed.setText(String.format( "%.1f км/ч", currentGeoPoint.Speed * 3.6 ));
        txtViewArea.setText(String.format( "%.1f га", appliedArea / 10000 ));
        txtViewSatNumber.setText(String.valueOf(currentGeoPoint.NumberOfSatellites));
    }

    //Button btnTimer;
    private ImageButton buttonA;
    private ImageButton buttonB;
    private ImageView imageSatellite;
    private ToggleButton toggleImplement;
    private ISerial serial = new SerialPort();
    private TextView txtViewSpeed;
    private TextView txtViewArea;
    private TextView txtViewSatNumber;

//    void test()
//    {
//        Double.toString(m_implementWidth);
//    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(String uri) {
//        if (mListener != null) {
//            mListener.onFinishActivity(uri);
//        }
//    }

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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
