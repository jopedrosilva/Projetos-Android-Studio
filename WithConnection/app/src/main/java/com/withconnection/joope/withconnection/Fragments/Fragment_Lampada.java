package com.withconnection.joope.withconnection.Fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.withconnection.joope.withconnection.Adapters.lampadaAdapter;
import com.withconnection.joope.withconnection.MainActivity;
import com.withconnection.joope.withconnection.Model.lampada;
import com.withconnection.joope.withconnection.R;

import java.util.List;

public class Fragment_Lampada extends Fragment {

    private RecyclerView mRecycleView;
    private List<lampada> mList;

    @Override
    public  View onCreateView(LayoutInflater inflater,
                            ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment__lampada, container, false);

        mRecycleView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecycleView.getLayoutManager();
                lampadaAdapter adapter = (lampadaAdapter)mRecycleView.getAdapter();

                if(mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1){
                    List<lampada> listAux = ((MainActivity)getActivity()).pegarLampadas(10);

                    for (int i = 0; i < listAux.size(); i++){
                        adapter.addListItem( listAux.get(i), mList.size());
                    }
                }
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(llm);

        mList = ((MainActivity) getActivity()).pegarLampadas(10);
        lampadaAdapter adapter = new lampadaAdapter(getActivity(), mList);
        mRecycleView.setAdapter(adapter);

        return view;
    }
}
