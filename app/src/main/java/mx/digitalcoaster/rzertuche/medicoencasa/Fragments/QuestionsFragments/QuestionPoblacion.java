package mx.digitalcoaster.rzertuche.medicoencasa.Fragments.QuestionsFragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import mx.digitalcoaster.rzertuche.medicoencasa.Activitys.MainActivity;
import mx.digitalcoaster.rzertuche.medicoencasa.R;
import mx.digitalcoaster.rzertuche.medicoencasa.Utils.SharedPreferences;
import mx.digitalcoaster.rzertuche.medicoencasa.models.Contexto;
import mx.digitalcoaster.rzertuche.medicoencasa.models.HistoriaClinica;
import mx.digitalcoaster.rzertuche.medicoencasa.models.Question;
import mx.digitalcoaster.rzertuche.medicoencasa.models.User;

import static mx.digitalcoaster.rzertuche.medicoencasa.Activitys.MainActivity.inicio;
import static mx.digitalcoaster.rzertuche.medicoencasa.Activitys.MainActivity.registros;
import static mx.digitalcoaster.rzertuche.medicoencasa.Activitys.MainActivity.seguimiento;
import static mx.digitalcoaster.rzertuche.medicoencasa.Activitys.MainActivity.sincronizacion;


public class QuestionPoblacion extends Fragment {


    Question currentQuestion;

    TextView question,question2,question3;
    TextView title;
    TextView category;
    EditText answer,answer2,answer3;

    LinearLayout multiple;
    LinearLayout open,open2, open3,open4;
    LinearLayout finish;
    LinearLayout review;

    ImageButton next;
    ImageView imageLogo;
    ImageView imageIcon2;

    String checking = "personales";

    User user;
    Contexto contexto;
    HistoriaClinica historiaClinica;
    String random_uuid = "";



    Activity activity;
    View container;
    int count = 0;




    private QuestionsFragment.OnFragmentInteractionListener mListener;
    SharedPreferences sharedPreferences;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.dialog_question_poblacion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.activity = getActivity();
        this.container = view;


        open = (LinearLayout) view.findViewById(R.id.open);
        open2 = (LinearLayout) view.findViewById(R.id.open2);
        open3 = (LinearLayout) view.findViewById(R.id.open3);




        title = (TextView) view.findViewById(R.id.title);

        category = (TextView) view.findViewById(R.id.category);




        imageLogo = (ImageView) view.findViewById(R.id.imageView8);
        imageIcon2 = (ImageView) view.findViewById(R.id.icon2);
        final ImageView icon3 = (ImageView) view.findViewById(R.id.icon3);


        final ImageView imageView8 = (ImageView) view.findViewById(R.id.imageView8);


        final RadioGroup radioVisita = getActivity().findViewById(R.id.radioCandidato);
        final RadioGroup radioDerecho = getActivity().findViewById(R.id.radioDerechohabiencia);
        final RadioGroup radioPoblacion = getActivity().findViewById(R.id.radioPoblacion);



        sharedPreferences = SharedPreferences.getInstance();


        final ImageButton back = (ImageButton) view.findViewById(R.id.back);

        ImageButton next = (ImageButton) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count ++;

                if(count == 1){
                    back.setVisibility(View.VISIBLE);

                    open.setVisibility(View.GONE);
                    open2.setVisibility(View.VISIBLE);

                    icon3.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.number_three_pink));
                    imageLogo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.icon_antecedentes));
                    category.setText("DERECHOHABIENCIA");



                }else if(count == 2){

                    String tipoVisita = sharedPreferences.getStringData("DerechoHabi");

                    if(tipoVisita.equals("")){
                        count --;
                        Toast.makeText(getActivity(), "Selecciona uno antes de continuar", Toast.LENGTH_SHORT).show();
                    }else{

                        if(tipoVisita.equals("IMSS") || tipoVisita.equals("ISSSTE")){

                            ((MainActivity)getActivity()).stopCronometro();
                            ((MainActivity)getActivity()).domRegistrado();

                        }else{

                            imageLogo.setVisibility(View.GONE);
                            category.setVisibility(View.GONE);


                            open2.setVisibility(View.GONE);
                            open3.setVisibility(View.VISIBLE);

                        }

                    }



                }else if(count ==3){

                    String tipoVisita = sharedPreferences.getStringData("Candidato");
                    if(tipoVisita.equals("")){
                        count --;
                        Toast.makeText(getActivity(), "Selecciona uno antes de continuar", Toast.LENGTH_SHORT).show();
                    }else{

                        if(tipoVisita.equals("No")){

                            ((MainActivity)getActivity()).stopCronometro();
                            ((MainActivity)getActivity()).domRegistrado();

                        }else if (tipoVisita.equals("Si")){

                            ((MainActivity)getActivity()).activityRegistros();

                        }

                    }

                }

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count --;

                if(open2.getVisibility() == View.VISIBLE){

                    open.setVisibility(View.VISIBLE);
                    open2.setVisibility(View.GONE);

                    back.setVisibility(View.GONE);

                }

                if(open3.getVisibility() == View.VISIBLE){

                    open2.setVisibility(View.VISIBLE);
                    open3.setVisibility(View.GONE);

                }

                if(open3.getVisibility() == View.VISIBLE) {

                    //open.setVisibility(View.VISIBLE);
                    //open3.setVisibility(View.GONE);

                }

            }
        });

        radioVisita.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.si){
                    sharedPreferences.setStringData("Candidato","Si");
                }else if (checkedId == R.id.no){
                    sharedPreferences.setStringData("Candidato","No");
                }

            }

        });


        radioDerecho.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.imss){
                    sharedPreferences.setStringData("DerechoHabi","IMSS");
                }else if (checkedId == R.id.issste){
                    sharedPreferences.setStringData("DerechoHabi","ISSSTE");
                }else if (checkedId == R.id.otros){
                    sharedPreferences.setStringData("DerechoHabi","Otros");
                }else if (checkedId == R.id.seguro_popular){
                    sharedPreferences.setStringData("DerechoHabi","Seguro Popular");
                }else if (checkedId == R.id.gratuidad){
                    sharedPreferences.setStringData("DerechoHabi","Gratuidad");
                }

            }

        });

        radioPoblacion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.discapacidad){
                    sharedPreferences.setStringData("Poblacion","Discapacidad");
                }else if (checkedId == R.id.postrada){
                    sharedPreferences.setStringData("Poblacion","Postrada");
                }else if (checkedId == R.id.adulto){
                    sharedPreferences.setStringData("Poblacion","Adulto mayor sin posibilidad de trasladarse");
                }else if (checkedId == R.id.situacion){
                    sharedPreferences.setStringData("Poblacion","Situación Abandono");
                }else if (checkedId == R.id.enf_terminal){
                    sharedPreferences.setStringData("Poblacion","Enf. Terminal");
                }else if (checkedId == R.id.embarazada){
                    sharedPreferences.setStringData("Poblacion","Embarazada");
                }else if (checkedId == R.id.embarazada_sin){
                    sharedPreferences.setStringData("Poblacion","Embarazada sin control prenatal");
                }

            }

        });








    }


    public Boolean checkName(String name){
        if(name.isEmpty() || name == null){
            return false;
        }else{
            return true;
        }
    }

    public Boolean checkAP(String name){
        if(name.isEmpty() || name == null){
            return false;
        }else{
            return true;
        }
    }

    public Boolean checkAM(String name){
        if(name.isEmpty() || name == null){
            return false;
        }else{
            return true;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuestionsFragment.OnFragmentInteractionListener) {
            mListener = (QuestionsFragment.OnFragmentInteractionListener) context;
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

    public void blockListeners(){
        inicio.setEnabled(false);
        registros.setEnabled(false);
        seguimiento.setEnabled(false);
        sincronizacion.setEnabled(false);
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
