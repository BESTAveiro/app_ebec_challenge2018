package com.example.jmfs1.ebec.schedulefragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jmfs1.ebec.R;
import com.example.jmfs1.ebec.schedulefragment.classes.DaySchedule;

import java.sql.Time;

import static com.example.jmfs1.ebec.MainApplication.getContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Log.d("present", "I'm here");
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        final ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewpager);

        ScheduleFragmentPagerAdapter adapter = new ScheduleFragmentPagerAdapter(getChildFragmentManager(), getContext());

        viewpager.setAdapter(adapter);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewpager);
            }
        });

        return view;
    }

    public class ScheduleFragmentPagerAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 4;
        private String tabTitles[] = new String[] { "Dia 14", "Dia 16", "Dia 17", "Dia 20" };
        private DaySchedule[] schedule_days;
        private Context context;

        public ScheduleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;

            schedule_days = new DaySchedule[4];

            /*Day 14*/
            schedule_days[0] = new DaySchedule(14);
            schedule_days[0].insertEvent("16:00", "17:30", "Cocktail Network",
                    "Existência de uma Cocktail network com a participação de ambos estudantes e colaboradores de empresas. Irá " +
                            "decorrer no Complexo Pedagógico.");
            schedule_days[0].sortEvents();


            /*Day 17*/
            schedule_days[1] = new DaySchedule(16);
            schedule_days[1].insertEvent("09:30", "10:30", "Check-in",
                    "Check-in dos participantes, entrega de Welcome Packs e Nametags");
            schedule_days[1].insertEvent("10:30", "11:30", "Opening Session",
                    "Sessão de abertura que contará com a presença de alguns representantes de empresas e entidades " +
                            "públicas. Irá decorrer no auditório do Departamento de Ambiente e Ordenamento");
            schedule_days[1].insertEvent("11:30", "12:30", "Apresentação das provas",
                    "Apresentação das provas." +
                            " Irá decorrer no DAO, auditório do Departamento de Ambiente e Ordenamento");
            schedule_days[1].insertEvent("12:30", "13:00", "Foto Oficial",
                    "Foto oficial do evento na meia-lua");
            schedule_days[1].insertEvent("13:00", "14:30", "Almoço",
                    "Almoço Cantina do crasto");
            schedule_days[1].insertEvent("14:30", "00:00", "Prova",
                    "Provas de Case Study e Team Design, com a duração de 24 horas. Irá decorrer na" +
                            " ESSUA.");
            schedule_days[1].sortEvents();

            /*Day 18*/
            schedule_days[2] = new DaySchedule(17);
            schedule_days[2].insertEvent("00:00", "14:30", "Prova",
                    "Provas de Case Study e Team Design, com a duração de 24 horas. Irá decorrer na" +
                            " ESSUA.");
            schedule_days[2].sortEvents();

            /*Day 21*/
            schedule_days[3] = new DaySchedule(20);
            schedule_days[3].insertEvent("13:30", "18:00", "Apresentação das Provas",
                    "As apresentações das provas de Case Study e Team Design decorrerão em paralelo. " +
                            "Irão decorrer em salas a informar posteriormente.");
            schedule_days[3].insertEvent("18:00", "19:00", "Closing Session",
                    "Sessão de encerramento, onde serão anunciados os vencedores de ambas as provas, serão entregues os prémios e serão feitas " +
                            "algumas considerações relativamente ao evento. Irá decorrer no auditório no Departamento de Ambiente" +
                            " e Ordenamento.");
            schedule_days[3].sortEvents();


        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return SchedulePageFragment.newInstance(schedule_days[position]);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}
