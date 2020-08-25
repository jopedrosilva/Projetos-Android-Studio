package com.withconnection.joope.projetoone.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.withconnection.joope.projetoone.Model.Car;
import com.withconnection.joope.projetoone.R;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {
    private List<Car> mList;
    private LayoutInflater mLayoutInflater;

    public CarAdapter(Context c, List<Car> l){
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.item_car, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CarAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.ivCar.setImageResource(mList.get(position).getPhotos());
        myViewHolder.ivModel.setText(mList.get(position).getModels());
        myViewHolder.ivBrand.setText(mList.get(position).getBrands());
        //myViewHolder.ivBrand.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addListItem(Car c, int position){
        mList.add(c);
        notifyItemInserted(position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivCar;
        public TextView ivModel;
        public TextView ivBrand;
        public MyViewHolder(View itemView){
            super(itemView);

            ivCar = (ImageView) itemView.findViewById(R.id.iv_car);
            ivModel = (TextView) itemView.findViewById(R.id.tv_model);
            ivBrand = (TextView) itemView.findViewById(R.id.tv_brand);
        }
    }
}
