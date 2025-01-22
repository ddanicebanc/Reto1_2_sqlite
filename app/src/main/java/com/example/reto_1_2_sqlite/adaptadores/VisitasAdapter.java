package com.example.reto_1_2_sqlite.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.reto_1_2_sqlite.R;
import com.example.reto_1_2_sqlite.modelos.Visita;

import java.util.ArrayList;

public class VisitasAdapter extends RecyclerView.Adapter<VisitasAdapter.ViewHolder> {
    //Interface para que MainActivity o cualquier otra actividad implemente el listener y poder
    // capturar el evento fuera del adaptador
    public interface ItemClickListener {
        public void onClick(View view, int position);
    }

    //Aquí podemos pasar cualquier tipo de colección de objetos
    private ArrayList<Visita> dataSet;
    //Lo utilizamos para poder capturar los clics sobre los elementos fuera del adaptador
    private ItemClickListener clicListener;
    //Referencia al contexto para poder obtener las imagenes que vamos a pintar
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txvPartnerName;
        private final TextView txvVisitDate;
        private final TextView txvDireccion;

        public ViewHolder (View view) {
            super(view);
            view.setOnClickListener(this);

            txvPartnerName = view.findViewById(R.id.txvPartnerName);
            txvVisitDate = view.findViewById(R.id.txvVisitDate);
            txvDireccion = view.findViewById(R.id.txvDireccion);
        }

        public TextView getTxvPartnerName () {return txvPartnerName;}
        public TextView getTxvVisitDate () {return txvVisitDate;}
        public TextView getTxvDireccion () {return txvDireccion;}

         @Override
        public void onClick (View view) {
            clicListener.onClick(view, getAdapterPosition());
        }
    }

    //CONSTRUCTOR
    public VisitasAdapter (ArrayList<Visita> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    //Este metodo se utiliza desde la actividad que captura el evento de clic de los items
    public void setOnClickListener(ItemClickListener clicListener){
        this.clicListener = clicListener;
    }

    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //Creamos la vista de cada item a partir de nuestro layout
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_visits_row, viewGroup, false);

        return new ViewHolder(view);
    }

    //Se llama cada vez que se hace scroll en la pantalla y los elementos desaparecen y aparecen
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String partnerName = dataSet.get(position).getPartnerName();
        String fechaVisita = dataSet.get(position).getFechaVisita();
        String direccion = dataSet.get(position).getDireccion();

        viewHolder.getTxvPartnerName().setText(partnerName);
        viewHolder.getTxvVisitDate().setText(fechaVisita);
        viewHolder.getTxvDireccion().setText(direccion);
    }

    // Devolvemos el numero de items de nuestro arraylist, lo invoca automaticamente el layout manager
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
