package mx.digitalcoaster.rzertuche.medicoencasa.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.digitalcoaster.rzertuche.medicoencasa.Activitys.MainActivity;
import mx.digitalcoaster.rzertuche.medicoencasa.DataBase.DataBaseDB;
import mx.digitalcoaster.rzertuche.medicoencasa.R;
import mx.digitalcoaster.rzertuche.medicoencasa.Utils.SharedPreferences;
import mx.digitalcoaster.rzertuche.medicoencasa.models.Item;
import mx.digitalcoaster.rzertuche.medicoencasa.models.ItemAdapter;
import mx.digitalcoaster.rzertuche.medicoencasa.models.User;
import mx.digitalcoaster.rzertuche.medicoencasa.models.VisitasAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PacientesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PacientesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PacientesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static Context appContext;

    public GridView lista;
    private List<Item> items = null;

    public GridView lista2;
    private List<Item> items2 = null;


    public GridView lista3;
    private List<Item> items3 = null;



    private SQLiteDatabase db = null;   // Objeto para usar la base de datos local
    private Cursor c = null;            // Objeto para hacer consultas a la base de datos
    private TextView title;
    SharedPreferences sharedPreferences;

    public static Boolean isSinExp = false;


    public PacientesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PacientesFragment newInstance(String param1, String param2) {
        PacientesFragment fragment = new PacientesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pacientes, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPreferences = SharedPreferences.getInstance();


        GridView gridView = view.findViewById(R.id.gridusers);
        items2 = new ArrayList<>();
        getPacientes();
        gridView.setAdapter(new ItemAdapter(getActivity().getApplicationContext(), items2));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                isSinExp = false;
                Item selectedUser = items2.get(position);
                Log.d("User", "USERUUID:"+selectedUser.getNombre());
                sharedPreferences.setStringData("nameItem",selectedUser.getNombre());
                sharedPreferences.setStringData("curpItem",selectedUser.getCurp());

                sharedPreferences.setStringData("nameHistoric", selectedUser.getNombre());
                sharedPreferences.setStringData("curpHistoric", selectedUser.getCurp());
                sharedPreferences.setStringData("direccionHistoric", selectedUser.getDireccion());

                ((MainActivity)getActivity()).questionExploracion();

            }
        });


        GridView gridView3 = view.findViewById(R.id.gridviewSinExp);
        items3 = new ArrayList<>();
        getSinExpediente();
        gridView3.setAdapter(new ItemAdapter(getActivity().getApplicationContext(), items3));
        gridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                Item selectedUser = items3.get(position);
                Log.d("User", "USERUUID:"+selectedUser.getNombre());
                sharedPreferences.setStringData("nameItem",selectedUser.getNombre());
                sharedPreferences.setStringData("curpItem",selectedUser.getCurp());


                isSinExp = true;
                ((MainActivity)getActivity()).historiaClinicaFragment();

            }
        });

        lista = getActivity().findViewById(R.id.gridSeguimiento);
        items = new ArrayList<>();
        getPacientesSeguimiento();
        lista.setAdapter(new VisitasAdapter(getActivity().getApplicationContext(), items));
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                Item selectedUser = items.get(position);

                String nameUser = selectedUser.getNombre();
                String curp = selectedUser.getCurp();
                String numero_visita= selectedUser.getNumero_visita();
                String expediente= selectedUser.getExpediente();
                String imageStatus = selectedUser.getStatus();

                sharedPreferences.setStringData("nameSeguimiento", nameUser);
                sharedPreferences.setStringData("curpSeguimiento", curp);
                sharedPreferences.setStringData("numero_visita", numero_visita);
                sharedPreferences.setStringData("Expediente", expediente);
                sharedPreferences.setStringData("ImageStatus", imageStatus);

                Log.e("TOUCHME",expediente);
                ((MainActivity)getActivity()).visitasFragment(nameUser);

            }
        });

    }


    private void getPacientesSeguimiento() {

        db = getActivity().openOrCreateDatabase(DataBaseDB.DB_NAME, Context.MODE_PRIVATE, null);
        try {
            c = db.rawQuery("SELECT * FROM " + DataBaseDB.TABLE_NAME_PACIENTES_VISITAS, null);
            if (c.moveToFirst()) {
                do {
                    items.add(new Item(c.getString(1), c.getString(2), c.getString(4), "- - - - - -", c.getString(5),false,false,c.getString(9)));
                }while (c.moveToNext());
            } else {
                System.out.println("No existen PACIENTES");
            }
            c.close();
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        } finally {
            db.close();
        }
    }

    private void getPacientes() {

        db = getActivity().openOrCreateDatabase(DataBaseDB.DB_NAME, android.content.Context.MODE_PRIVATE, null);
        try {
            c = db.rawQuery("SELECT * FROM " + DataBaseDB.TABLE_NAME_PACIENTES, null);
            if (c.moveToFirst()) {
                do {
                    items2.add(new Item(c.getString(1), c.getString(2), c.getString(3)));
                }while (c.moveToNext());
            } else {
                Log.d("PACIENTES...","No existen PACIENTES");
            }
            c.close();
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        } finally {
            db.close();
        }
    }

    private void getSinExpediente() {

        db = getActivity().openOrCreateDatabase(DataBaseDB.DB_NAME, android.content.Context.MODE_PRIVATE, null);
        try {
            c = db.rawQuery("SELECT * FROM " + DataBaseDB.TABLE_NAME_PACIENTES_SIN_EXPEDIENTE + " WHERE " +
                    DataBaseDB.PACIENTES_EXPEDIENTE_CURP + " != ''", null);
            if (c.moveToFirst()) {
                do {
                    items3.add(new Item(c.getString(1), c.getString(2), "SIN EXPEDIENTE"));
                }while (c.moveToNext());
            } else {
                System.out.println("No existen PACIENTES SIN EXPEDIENTE");
            }
            c.close();
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        } finally {
            db.close();
        }
    }

    public void onButtonPressed(Uri uri) {
        callParentMethod();
    }


    public void callParentMethod(){
        getActivity().onBackPressed();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
