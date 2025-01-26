package com.example.reto_1_2_sqlite.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.modelos.Partner;

import java.util.ArrayList;

public class PartnersAdapter extends RecyclerView.Adapter<PartnersAdapter.ViewHolder> {
    //Interface para que MainActivity o cualquier otra actividad implemente el listener y poder
    // capturar el evento fuera del adaptador
    public interface ItemClickListener {
        public void onClick(View view, int position);
    }

    //Aquí podemos pasar cualquier tipo de colección de objetos
    private ArrayList<Partner> dataSet;
    //Lo utilizamos para poder capturar los clics sobre los elementos fuera del adaptador
    private ItemClickListener clicListener;
    //Referencia al contexto para poder obtener las imagenes que vamos a pintar
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txvPartnerName;
        private final TextView txvPartnerAddress;
        private final TextView txvPartnerTel;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            txvPartnerName = view.findViewById(R.id.txvPartnerName);
            txvPartnerAddress = view.findViewById(R.id.txvPartnerAddress);
            txvPartnerTel = view.findViewById(R.id.txvPartnerTel);
        }

        public TextView getTxvPartnerName() {
            return txvPartnerName;
        }

        public TextView getTxvDireccion() {
            return txvPartnerAddress;
        }

        public TextView getTxvTel() {
            return txvPartnerTel;
        }

        @Override
        public void onClick(View view) {
            clicListener.onClick(view, getAdapterPosition());
        }
    }
        public PartnersAdapter (ArrayList<Partner> dataSet, Context context) {
            this.dataSet = dataSet;
            this.context = context;
        }

        //Este metodo se utiliza desde la actividad que captura el evento de clic de los items
//        public void setOnClickListener(ItemClickListener clicListener){
//            this.clicListener = clicListener;
//        }

        //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            //Creamos la vista de cada item a partir de nuestro layout
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_partners, viewGroup, false);

            return new ViewHolder(view);
        }

        //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            String partnerName = dataSet.get(position).getNombre();
            String partnerAddress = dataSet.get(position).getDireccion();
            int partnerTel = dataSet.get(position).getTelefono();

            viewHolder.getTxvPartnerName().setText(partnerName);
            viewHolder.getTxvDireccion().setText(partnerAddress);
            viewHolder.getTxvTel().setText(String.valueOf(partnerTel));
        }

        // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
        @Override
        public int getItemCount() {
            return dataSet.size();
        }
}
