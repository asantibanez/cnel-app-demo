package com.andressantibanez.android.cnelapp.endpoints.GetMeterConsumptions;

import java.util.ArrayList;

public class GetMeterConsumptionsResponse {

    public ArrayList<Consumo> listaconsumo;

    public static class Consumo {
        public String codigo_cuenta;
        public String nombre;
        public String consumo;
        public String fecha;
    }

}
