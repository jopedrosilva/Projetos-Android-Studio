package com.withconnection.joope.withconnection.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.withconnection.joope.withconnection.Model.lampada;
import com.withconnection.joope.withconnection.R;

import java.util.List;

public class lampadaAdapter extends RecyclerView.Adapter<lampadaAdapter.MyViewHolder> {
    private List<lampada> mList;
    private LayoutInflater mLayoutInflater;

    public lampadaAdapter(Context c, List<lampada> l){
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.item_lampada, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Log.i("Passou Aqui:", "verdade");
        myViewHolder.mSwitch.setText(mList.get(position).getLampada());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addListItem(lampada l, int position){
        mList.add(l);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Switch mSwitch;

        public MyViewHolder(View itemView){
            super(itemView);

           mSwitch = (Switch) itemView.findViewById(R.id.switchLampada);
        }
    }
}
