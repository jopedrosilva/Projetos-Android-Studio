package com.withconnection.joope.projetoone.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.withconnection.joope.projetoone.MainActivity;
import com.withconnection.joope.projetoone.Model.Car;
import com.withconnection.joope.projetoone.R;
import com.withconnection.joope.projetoone.adapters.CarAdapter;

import java.util.List;

public class CarFragment extends Fragment {

    private RecyclerView mRecycleView;
    private List<Car> mList;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_car, container, false);

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
                CarAdapter adapter = (CarAdapter)mRecycleView.getAdapter();

                if(mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1){
                    List<Car> listAux = ((MainActivity)getActivity()).getSetCarList(10);

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }
                }
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(llm);

        mList = ((MainActivity)getActivity()).getSetCarList(10);
        CarAdapter adapter = new CarAdapter(getActivity(), mList);
        mRecycleView.setAdapter(adapter);

        return view;
    }
}
